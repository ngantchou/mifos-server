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
package org.apache.fineract.portfolio.loanproduct.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.fineract.infrastructure.core.api.JsonCommand;

/**
 * LoanProductMinMaxConstraints encapsulates all the Min and Max details of a {@link LoanProduct}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LoanProductMinMaxConstraints {

    @Column(name = "min_principal_amount", scale = 6, precision = 19)
    private BigDecimal minPrincipal;

    @Column(name = "max_principal_amount", scale = 6, precision = 19)
    private BigDecimal maxPrincipal;

    @Column(name = "min_nominal_interest_rate_per_period", scale = 6, precision = 19)
    private BigDecimal minNominalInterestRatePerPeriod;

    @Column(name = "max_nominal_interest_rate_per_period", scale = 6, precision = 19)
    private BigDecimal maxNominalInterestRatePerPeriod;

    @Column(name = "min_number_of_repayments")
    private Integer minNumberOfRepayments;

    @Column(name = "max_number_of_repayments")
    private Integer maxNumberOfRepayments;

    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(20);

        final String localeAsInput = command.locale();

        final String minPrincipalParamName = "minPrincipal";
        if (command.isChangeInBigDecimalParameterNamedWithNullCheck(minPrincipalParamName, this.minPrincipal)) {
            final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(minPrincipalParamName);
            actualChanges.put(minPrincipalParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.minPrincipal = newValue;
        }

        final String maxPrincipalParamName = "maxPrincipal";
        if (command.isChangeInBigDecimalParameterNamedWithNullCheck(maxPrincipalParamName, this.maxPrincipal)) {
            final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(maxPrincipalParamName);
            actualChanges.put(maxPrincipalParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.maxPrincipal = newValue;
        }

        final String minNumberOfRepaymentsParamName = "minNumberOfRepayments";
        if (command.isChangeInIntegerParameterNamed(minNumberOfRepaymentsParamName, this.minNumberOfRepayments)) {
            final Integer newValue = command.integerValueOfParameterNamed(minNumberOfRepaymentsParamName);
            actualChanges.put(minNumberOfRepaymentsParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.minNumberOfRepayments = newValue;
        }

        final String maxNumberOfRepaymentsParamName = "maxNumberOfRepayments";
        if (command.isChangeInIntegerParameterNamed(maxNumberOfRepaymentsParamName, this.maxNumberOfRepayments)) {
            final Integer newValue = command.integerValueOfParameterNamed(maxNumberOfRepaymentsParamName);
            actualChanges.put(maxNumberOfRepaymentsParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.maxNumberOfRepayments = newValue;
        }

        final String minInterestRatePerPeriodParamName = "minInterestRatePerPeriod";
        if (command.isChangeInBigDecimalParameterNamedWithNullCheck(minInterestRatePerPeriodParamName,
                this.minNominalInterestRatePerPeriod)) {
            final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(minInterestRatePerPeriodParamName);
            actualChanges.put(minInterestRatePerPeriodParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.minNominalInterestRatePerPeriod = newValue;
        }

        final String maxInterestRatePerPeriodParamName = "maxInterestRatePerPeriod";
        if (command.isChangeInBigDecimalParameterNamedWithNullCheck(maxInterestRatePerPeriodParamName,
                this.maxNominalInterestRatePerPeriod)) {
            final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(maxInterestRatePerPeriodParamName);
            actualChanges.put(maxInterestRatePerPeriodParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.maxNominalInterestRatePerPeriod = newValue;
        }

        return actualChanges;
    }

    public BigDecimal getMinNominalInterestRatePerPeriod() {
        return this.minNominalInterestRatePerPeriod == null ? null
                : BigDecimal.valueOf(Double.parseDouble(this.minNominalInterestRatePerPeriod.stripTrailingZeros().toString()));
    }

    public BigDecimal getMaxNominalInterestRatePerPeriod() {
        return this.maxNominalInterestRatePerPeriod == null ? null
                : BigDecimal.valueOf(Double.parseDouble(this.maxNominalInterestRatePerPeriod.stripTrailingZeros().toString()));
    }

    public void updateForFloatingInterestRates() {
        this.minNominalInterestRatePerPeriod = null;
        this.maxNominalInterestRatePerPeriod = null;
    }
}
