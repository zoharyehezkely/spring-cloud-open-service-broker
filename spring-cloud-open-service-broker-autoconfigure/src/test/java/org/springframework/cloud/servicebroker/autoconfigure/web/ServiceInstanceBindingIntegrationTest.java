/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.servicebroker.autoconfigure.web;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.cloud.servicebroker.JsonUtils;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class ServiceInstanceBindingIntegrationTest extends ControllerIntegrationTest {

	protected static final String SERVICE_INSTANCE_BINDING_ID = "service-instance-binding-id";

	private static final String SERVICE_INSTANCES_ROOT_PATH = "/v2/service_instances/";

	protected String createRequestBody;

	@BeforeEach
	void setupBase() {
		CreateServiceInstanceBindingRequest request = CreateServiceInstanceBindingRequest.builder()
				.serviceDefinitionId(serviceDefinition.getId())
				.planId("plan-one-id")
				.build();

		createRequestBody = JsonUtils.toJson(request);
	}

	protected String buildCreateUrl() {
		return buildCreateUrl(null, false);
	}

	protected String buildCreateUrl(String platformInstanceId, boolean asyncAccepted) {
		return buildBaseUrl(platformInstanceId)
				.queryParam("accepts_incomplete", asyncAccepted)
				.toUriString();
	}

	protected String buildGetUrl(String platformInstanceId) {
		return buildGetUrl(platformInstanceId, null, null, false);
	}

	protected String buildGetUrl(String platformInstanceId, String serviceDefinitionId, String planId,
			boolean asyncAccepted) {
		UriComponentsBuilder builder = buildBaseUrl(platformInstanceId)
				.queryParam("accepts_incomplete", asyncAccepted);
		if (StringUtils.hasLength(serviceDefinitionId)) {
			builder.queryParam("service_id", serviceDefinitionId);
		}
		if (StringUtils.hasLength(planId)) {
			builder.queryParam("plan_id", planId);
		}
		return builder.toUriString();
	}

	protected String buildDeleteUrl() {
		return buildDeleteUrl(null, false);
	}

	protected String buildDeleteUrl(String platformInstanceId, boolean asyncAccepted) {
		return buildBaseUrl(platformInstanceId)
				.queryParam("service_id", serviceDefinition.getId())
				.queryParam("plan_id", serviceDefinition.getPlans().get(0).getId())
				.queryParam("accepts_incomplete", asyncAccepted)
				.toUriString();
	}

	protected String buildLastOperationUrl() {
		return buildLastOperationUrl(null);
	}

	protected String buildLastOperationUrl(String platformInstanceId) {
		return buildBaseUrl(platformInstanceId)
				.pathSegment("last_operation")
				.queryParam("operation", "working")
				.toUriString();
	}

	private UriComponentsBuilder buildBaseUrl(String platformInstanceId) {
		return UriComponentsBuilder.fromPath("/")
				.pathSegment(platformInstanceId)
				.path(SERVICE_INSTANCES_ROOT_PATH)
				.pathSegment(SERVICE_INSTANCE_ID, "service_bindings", SERVICE_INSTANCE_BINDING_ID);
	}

}
