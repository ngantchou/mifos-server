/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.jobs.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.businessdate.domain.BusinessDateType;
import org.apache.fineract.infrastructure.businessdate.service.BusinessDateReadPlatformService;
import org.apache.fineract.infrastructure.core.domain.ActionContext;
import org.apache.fineract.infrastructure.core.domain.FineractPlatformTenant;
import org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.core.service.tenant.TenantDetailsService;
import org.apache.fineract.infrastructure.event.external.service.JdbcTemplateFactory;
import org.apache.fineract.infrastructure.jobs.domain.JobExecutionRepository;
import org.apache.fineract.useradministration.domain.AppUser;
import org.apache.fineract.useradministration.domain.AppUserRepositoryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "fineract.mode.batch-manager-enabled", havingValue = "true")
public class StuckJobListener implements ApplicationListener<ContextRefreshedEvent> {

    private final JobExecutionRepository jobExecutionRepository;
    private final JdbcTemplateFactory jdbcTemplateFactory;
    private final TenantDetailsService tenantDetailsService;
    private final JobRegistry jobRegistry;
    private final BusinessDateReadPlatformService businessDateReadPlatformService;
    private final StuckJobExecutorService stuckJobExecutorService;
    private final AppUserRepositoryWrapper userRepository;
    private static final Logger logger = LoggerFactory.getLogger(StuckJobListener.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!jobRegistry.getJobNames().isEmpty()) {
            List<FineractPlatformTenant> allTenants = tenantDetailsService.findAllTenants();
            allTenants.forEach(tenant -> {
                NamedParameterJdbcTemplate namedParameterJdbcTemplate = jdbcTemplateFactory.createNamedParameterJdbcTemplate(tenant);
                List<String> stuckJobNames = jobExecutionRepository.getStuckJobNames(namedParameterJdbcTemplate);
                if (stuckJobNames != null && !stuckJobNames.isEmpty()) {
                    try {
                        ThreadLocalContextUtil.setTenant(tenant);
                        HashMap<BusinessDateType, LocalDate> businessDates = businessDateReadPlatformService.getBusinessDates();
                        ThreadLocalContextUtil.setActionContext(ActionContext.DEFAULT);
                        ThreadLocalContextUtil.setBusinessDates(businessDates);
                        AppUser user = userRepository.fetchSystemUser();
                        
                        if (user != null && user.getPassword() != null) {
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth);
                            
                            stuckJobNames.forEach(stuckJobName -> {
                                if (stuckJobName != null && !stuckJobName.isEmpty()) {
                                    stuckJobExecutorService.resumeStuckJob(stuckJobName);
                                } else {
                                    // Log the null or empty stuck job name
                                    logger.warn("Encountered null or empty stuck job name for tenant: {}", tenant.getId());
                                }
                            });
                        } else {
                            // Log if user or password is null
                            logger.warn("System user or password is null for tenant: {}", tenant.getId());
                        }
                    } catch (Exception e) {
                        logger.error("Error while trying to restart stuck jobs for tenant: {}", tenant.getId(), e);
                        throw new RuntimeException("Error while trying to restart stuck jobs", e);
                    } finally {
                        ThreadLocalContextUtil.reset();
                    }
                } else {
                    // Log if no stuck job names are found
                    logger.info("No stuck job names found for tenant: {}", tenant.getId());
                }
            });
        } else {
            // Log if no job names are found in the job registry
            logger.info("No job names found in the job registry");
        }
    }
    
}
