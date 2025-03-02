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

package org.springframework.cloud.servicebroker.controller;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import org.springframework.cloud.servicebroker.JsonUtils;
import org.springframework.cloud.servicebroker.model.Context;
import org.springframework.cloud.servicebroker.model.PlatformContext;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.cloud.servicebroker.service.CatalogService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public abstract class ControllerRequestTest {

	@Mock
	protected CatalogService catalogService;

	protected ServiceDefinition serviceDefinition;

	protected Plan plan;

	Context identityContext;

	Context requestContext;

	Map<String, String> pathVariables = Collections
			.singletonMap("platformInstanceId", "platform-instance-id");

	@BeforeEach
	public void setUpControllerRequestTest() {
		openMocks(this);

		plan = Plan.builder()
				.id("plan-id")
				.build();

		serviceDefinition = ServiceDefinition.builder()
				.id("service-definition-id")
				.plans(plan)
				.build();

		lenient().when(catalogService.getServiceDefinition(anyString()))
				.thenReturn(Mono.empty());

		lenient().when(catalogService.getServiceDefinition("service-definition-id"))
				.thenReturn(Mono.just(serviceDefinition));

		identityContext = PlatformContext.builder()
				.platform("test-platform")
				.property("user", "user-id")
				.build();

		requestContext = PlatformContext.builder()
				.platform("test-platform")
				.property("request-property", "value")
				.build();
	}

	String encodeOriginatingIdentity(Context context) {
		Map<String, Object> properties = context.getProperties();
		String propertiesJson = JsonUtils.toJson(properties);

		return context.getPlatform() +
				" " +
				Base64.getEncoder().encodeToString(propertiesJson.getBytes());
	}

}
