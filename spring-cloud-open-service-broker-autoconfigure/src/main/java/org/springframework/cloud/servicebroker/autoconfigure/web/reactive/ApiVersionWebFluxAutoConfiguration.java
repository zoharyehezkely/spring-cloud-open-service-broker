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

package org.springframework.cloud.servicebroker.autoconfigure.web.reactive;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.servicebroker.autoconfigure.web.ServiceBrokerProperties;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the service broker API validation. Configures support for any
 * service broker API version if a version is not specifically configured.
 *
 * <p>
 * API validation may be disabled completely by setting the following configuration property:
 *
 * <pre>
 * spring.cloud.openservicebroker.api-version-check-enabled = false
 * </pre>
 *
 * @author Roy Clarkson
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnBean(ServiceInstanceService.class)
@ConditionalOnProperty(prefix = "spring.cloud.openservicebroker", name = "api-version-check-enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(WebFluxAutoConfiguration.class)
@EnableConfigurationProperties(ServiceBrokerProperties.class)
public class ApiVersionWebFluxAutoConfiguration {

	private final ServiceBrokerProperties serviceBrokerProperties;

	/**
	 * Construct a new {@link ApiVersionWebFluxAutoConfiguration}
	 *
	 * @param serviceBrokerProperties the service broker properties
	 */
	public ApiVersionWebFluxAutoConfiguration(ServiceBrokerProperties serviceBrokerProperties) {
		this.serviceBrokerProperties = serviceBrokerProperties;
	}

	/**
	 * Provides a {@link BrokerApiVersion} bean if the 'api-version' property is available in external configuration
	 *
	 * @return the bean
	 */
	@Bean
	@ConditionalOnMissingBean(BrokerApiVersion.class)
	@ConditionalOnProperty(prefix = "spring.cloud.openservicebroker", name = "api-version")
	public BrokerApiVersion serviceBrokerApiVersionProperty() {
		return new BrokerApiVersion(this.serviceBrokerProperties.getApiVersion());
	}

	/**
	 * Conditionally provides a {@link BrokerApiVersion} bean
	 *
	 * @return the bean
	 */
	@Bean
	@ConditionalOnMissingBean(BrokerApiVersion.class)
	public BrokerApiVersion serviceBrokerApiVersion() {
		return new BrokerApiVersion();
	}

	/**
	 * Conditionally provides a {@link ApiVersionWebFilter} bean
	 *
	 * @param brokerApiVersion the api version
	 * @return the bean
	 */
	@Bean
	public ApiVersionWebFilter apiVersionWebFilter(BrokerApiVersion brokerApiVersion) {
		return new ApiVersionWebFilter(brokerApiVersion);
	}

}
