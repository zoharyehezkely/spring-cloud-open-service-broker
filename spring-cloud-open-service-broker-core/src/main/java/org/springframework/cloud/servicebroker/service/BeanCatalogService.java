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

package org.springframework.cloud.servicebroker.service;

import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Mono;

import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;

/**
 * An implementation of the {@link CatalogService} that allows the {@link Catalog} to be specified as a Spring Bean.
 *
 * @author sgreenberg@pivotal.io
 */
public class BeanCatalogService implements CatalogService {

	private final Catalog catalog;

	private final Map<String, ServiceDefinition> serviceDefs = new HashMap<>();

	/**
	 * Construct a service with the provided {@link Catalog bean}.
	 *
	 * @param catalog the {@link Catalog} bean
	 */
	public BeanCatalogService(Catalog catalog) {
		this.catalog = catalog;
		initializeMap();
	}

	private void initializeMap() {
		catalog.getServiceDefinitions().forEach(def -> serviceDefs.put(def.getId(), def));
	}

	@Override
	public Mono<Catalog> getCatalog() {
		return Mono.just(catalog);
	}

	@Override
	public Mono<ServiceDefinition> getServiceDefinition(final String serviceId) {
		return Mono.justOrEmpty(serviceDefs.get(serviceId));
	}

}
