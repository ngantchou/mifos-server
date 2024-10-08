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
package org.apache.fineract.infrastructure.jobs.api;

import static org.apache.fineract.infrastructure.jobs.api.SchedulerJobApiConstants.JOB_DETAIL_RESPONSE_DATA_PARAMETERS;
import static org.apache.fineract.infrastructure.jobs.api.SchedulerJobApiConstants.JOB_HISTORY_RESPONSE_DATA_PARAMETERS;
import static org.apache.fineract.infrastructure.jobs.api.SchedulerJobApiConstants.SCHEDULER_RESOURCE_NAME;
import static org.apache.fineract.infrastructure.jobs.api.SchedulerJobApiConstants.SHORT_NAME_PARAM;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.api.IdTypeResolver;
import org.apache.fineract.infrastructure.core.config.FineractProperties;
import org.apache.fineract.infrastructure.core.data.ApiGlobalErrorResponse;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.exception.UnrecognizedQueryParamException;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.infrastructure.core.service.Page;
import org.apache.fineract.infrastructure.core.service.SearchParameters;
import org.apache.fineract.infrastructure.jobs.data.JobDetailData;
import org.apache.fineract.infrastructure.jobs.data.JobDetailHistoryData;
import org.apache.fineract.infrastructure.jobs.service.JobRegisterService;
import org.apache.fineract.infrastructure.jobs.service.SchedulerJobRunnerReadService;
import org.apache.fineract.infrastructure.security.exception.NoAuthorizationException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.infrastructure.security.service.SqlValidator;
import org.springframework.stereotype.Component;

@Path("/v1/jobs")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Component
@RequiredArgsConstructor
@Tag(name = "SCHEDULER JOB", description = "Batch jobs (also known as cron jobs on Unix-based systems) are a series of back-end jobs executed on a computer at a particular time defined in job's cron expression.\n\n At any point, you can view the list of batch jobs scheduled to run along with other details specific to each job. Manually you can execute the jobs at any point of time.\n\n The scheduler status can be either \"Active\" or \"Standby\". If the scheduler status is Active, it indicates that all batch jobs are running/ will run as per the specified schedule.If the scheduler status is Standby, it will ensure all scheduled batch runs are suspended.")
public class SchedulerJobApiResource {

    private final SchedulerJobRunnerReadService schedulerJobRunnerReadService;
    private final JobRegisterService jobRegisterService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final ToApiJsonSerializer<JobDetailData> toApiJsonSerializer;
    private final ToApiJsonSerializer<JobDetailHistoryData> jobHistoryToApiJsonSerializer;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final PlatformSecurityContext context;
    private final FineractProperties fineractProperties;
    private final SqlValidator sqlValidator;

    @GET
    @Operation(summary = "Retrieve Scheduler Jobs", description = "Returns the list of jobs.\n" + "\n" + "Example Requests:\n" + "\n"
            + "jobs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.GetJobsResponse.class)))) })
    public String retrieveAll(@Context final UriInfo uriInfo) {
        this.context.authenticatedUser().validateHasReadPermission(SCHEDULER_RESOURCE_NAME);
        final List<JobDetailData> jobDetailDatas = this.schedulerJobRunnerReadService.findAllJobDetails();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, jobDetailDatas, JOB_DETAIL_RESPONSE_DATA_PARAMETERS);
    }

    @GET
    @Path("{" + SchedulerJobApiConstants.JOB_ID + "}")
    @Operation(summary = "Retrieve a Job", description = "Returns the details of a Job.\n" + "\n" + "Example Requests:\n" + "\n" + "jobs/5")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.GetJobsResponse.class))) })
    public String retrieveOne(@PathParam(SchedulerJobApiConstants.JOB_ID) @Parameter(description = "jobId") final Long jobId,
            @Context final UriInfo uriInfo) {
        return retrieveOne(IdTypeResolver.resolveDefault(), Objects.toString(jobId, null), uriInfo);
    }

    @GET
    @Path(SHORT_NAME_PARAM + "/{shortName}")
    @Operation(summary = "Retrieve a Job", description = "Returns the details of a Job bu shortName.\n" + "\n" + "Example Requests:\n"
            + "\n" + "jobs/short-name/SA_PINT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.GetJobsResponse.class))) })
    public String retrieveByShortName(
            @PathParam("shortName") @Parameter(required = true, description = SchedulerJobApiConstants.SHORT_NAME_PARAM) final String shortName,
            @Context final UriInfo uriInfo) {
        return retrieveOne(IdTypeResolver.resolve(SHORT_NAME_PARAM), shortName, uriInfo);
    }

    @GET
    @Path("{" + SchedulerJobApiConstants.JOB_ID + "}/" + SchedulerJobApiConstants.JOB_RUN_HISTORY)
    @Operation(summary = "Retrieve Job Run History", description = "Example Requests:\n" + "\n" + "jobs/5/runhistory?offset=0&limit=200")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.GetJobsJobIDJobRunHistoryResponse.class))) })
    public String retrieveHistory(@Context final UriInfo uriInfo,
            @PathParam(SchedulerJobApiConstants.JOB_ID) @Parameter(description = "jobId") final Long jobId,
            @QueryParam("offset") @Parameter(description = "offset") final Integer offset,
            @QueryParam("limit") @Parameter(description = "limit") final Integer limit,
            @QueryParam("orderBy") @Parameter(description = "orderBy") final String orderBy,
            @QueryParam("sortOrder") @Parameter(description = "sortOrder") final String sortOrder) {
        return retrieveHistory(IdTypeResolver.resolveDefault(), Objects.toString(jobId, null), offset, limit, orderBy, sortOrder, uriInfo);
    }

    @GET
    @Path(SHORT_NAME_PARAM + "/{shortName}/" + SchedulerJobApiConstants.JOB_RUN_HISTORY)
    @Operation(summary = "Retrieve Job Run History", description = "Example Requests:\n" + "\n"
            + "jobs/short-name/SA_PINT/runhistory?offset=0&limit=200")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.GetJobsJobIDJobRunHistoryResponse.class))) })
    public String retrieveHistoryByShortName(@Context final UriInfo uriInfo,
            @PathParam("shortName") @Parameter(required = true, description = SchedulerJobApiConstants.SHORT_NAME_PARAM) final String shortName,
            @QueryParam("offset") @Parameter(description = "offset") final Integer offset,
            @QueryParam("limit") @Parameter(description = "limit") final Integer limit,
            @QueryParam("orderBy") @Parameter(description = "orderBy") final String orderBy,
            @QueryParam("sortOrder") @Parameter(description = "sortOrder") final String sortOrder) {
        return retrieveHistory(IdTypeResolver.resolve(SHORT_NAME_PARAM), shortName, offset, limit, orderBy, sortOrder, uriInfo);
    }

    @POST
    @Path("{" + SchedulerJobApiConstants.JOB_ID + "}")
    @Operation(summary = "Run a Job", description = "Manually Execute Specific Job.")
    @RequestBody(content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.ExecuteJobRequest.class)))
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "POST: jobs/1?command=executeJob") })
    public Response executeJob(@PathParam(SchedulerJobApiConstants.JOB_ID) @Parameter(description = "jobId") final Long jobId,
            @QueryParam(SchedulerJobApiConstants.COMMAND) @Parameter(description = "command") final String commandParam,
            @Parameter(hidden = true) final String jsonRequestBody) {
        return executeJob(IdTypeResolver.resolveDefault(), Objects.toString(jobId, null), commandParam, jsonRequestBody);
    }

    @POST
    @Path(SHORT_NAME_PARAM + "/{shortName}")
    @Operation(summary = "Run a Job", description = "Manually Execute Specific Job.")
    @RequestBody(content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.ExecuteJobRequest.class)))
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "POST: jobs/short-name/SA_PINT?command=executeJob") })
    public Response executeJobByShortName(
            @PathParam("shortName") @Parameter(required = true, description = SchedulerJobApiConstants.SHORT_NAME_PARAM) final String shortName,
            @QueryParam(SchedulerJobApiConstants.COMMAND) @Parameter(description = "command") final String commandParam,
            @Parameter(hidden = true) final String jsonRequestBody) {
        return executeJob(IdTypeResolver.resolve(SHORT_NAME_PARAM), shortName, commandParam, jsonRequestBody);
    }

    @PUT
    @Path("{" + SchedulerJobApiConstants.JOB_ID + "}")
    @Operation(summary = "Update a Job", description = "Updates the details of a job.")
    @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.PutJobsJobIDRequest.class)))
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OK") })
    public String updateJobDetail(@PathParam(SchedulerJobApiConstants.JOB_ID) @Parameter(description = "jobId") final Long jobId,
            @Parameter(hidden = true) final String jsonRequestBody) {
        return updateJobDetail(IdTypeResolver.resolveDefault(), Objects.toString(jobId, null), jsonRequestBody);
    }

    @PUT
    @Path(SHORT_NAME_PARAM + "/{shortName}")
    @Operation(summary = "Update a Job", description = "Updates the details of a job.")
    @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SchedulerJobApiResourceSwagger.PutJobsJobIDRequest.class)))
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OK") })
    public String updateJobDetailByShortName(
            @PathParam("shortName") @Parameter(required = true, description = SchedulerJobApiConstants.SHORT_NAME_PARAM) final String shortName,
            @Parameter(hidden = true) final String jsonRequestBody) {
        return updateJobDetail(IdTypeResolver.resolve(SHORT_NAME_PARAM), shortName, jsonRequestBody);
    }

    private boolean is(final String commandParam, final String commandValue) {
        return StringUtils.isNotBlank(commandParam) && commandParam.trim().equalsIgnoreCase(commandValue);
    }

    private String retrieveOne(@NotNull IdTypeResolver.IdType idType, String identifier, UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(SCHEDULER_RESOURCE_NAME);
        final JobDetailData jobDetailData = schedulerJobRunnerReadService.retrieveOne(idType, identifier);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return toApiJsonSerializer.serialize(settings, jobDetailData, JOB_DETAIL_RESPONSE_DATA_PARAMETERS);
    }

    private String retrieveHistory(@NotNull IdTypeResolver.IdType idType, String identifier, Integer offset, Integer limit, String orderBy,
            String sortOrder, UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(SCHEDULER_RESOURCE_NAME);
        sqlValidator.validate(orderBy);
        sqlValidator.validate(sortOrder);
        final SearchParameters searchParameters = SearchParameters.builder().limit(limit).offset(offset).orderBy(orderBy)
                .sortOrder(sortOrder).build();
        final Page<JobDetailHistoryData> jobHistoryData = schedulerJobRunnerReadService.retrieveJobHistory(idType, identifier,
                searchParameters);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return jobHistoryToApiJsonSerializer.serialize(settings, jobHistoryData, JOB_HISTORY_RESPONSE_DATA_PARAMETERS);
    }

    private Response executeJob(@NotNull IdTypeResolver.IdType idType, String identifier, String commandParam, String jsonRequestBody) {
        // check the logged-in user have permissions to execute scheduler jobs
        Response response;
        if (fineractProperties.getMode().isBatchManagerEnabled()) {
            final boolean hasNotPermission = context.authenticatedUser().hasNotPermissionForAnyOf("ALL_FUNCTIONS", "EXECUTEJOB_SCHEDULER");
            if (hasNotPermission) {
                final String authorizationMessage = "User has no authority to execute scheduler jobs";
                throw new NoAuthorizationException(authorizationMessage);
            }
            response = Response.status(400).build();
            if (is(commandParam, SchedulerJobApiConstants.COMMAND_EXECUTE_JOB)) {
                Long jobId = schedulerJobRunnerReadService.retrieveId(idType, identifier);
                jobRegisterService.executeJobWithParameters(jobId, jsonRequestBody);
                response = Response.status(202).build();
            } else {
                throw new UnrecognizedQueryParamException(SchedulerJobApiConstants.COMMAND, commandParam);
            }
        } else {
            ApiGlobalErrorResponse errorResponse = ApiGlobalErrorResponse.invalidInstanceTypeMethod("Batch");
            response = Response.status(Status.METHOD_NOT_ALLOWED).entity(errorResponse).build();
        }
        return response;
    }

    private String updateJobDetail(@NotNull IdTypeResolver.IdType idType, String identifier, String jsonRequestBody) {
        Long jobId = schedulerJobRunnerReadService.retrieveId(idType, identifier);
        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
                .updateJobDetail(jobId) //
                .withJson(jsonRequestBody) //
                .build(); //
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        if (result.getChanges() != null && (result.getChanges().containsKey(SchedulerJobApiConstants.jobActiveStatusParamName)
                || result.getChanges().containsKey(SchedulerJobApiConstants.cronExpressionParamName))) {
            this.jobRegisterService.rescheduleJob(jobId);
        }
        return this.toApiJsonSerializer.serialize(result);
    }
}
