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

package org.springframework.cloud.servicebroker.model.binding;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import org.springframework.cloud.servicebroker.JsonUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteServiceInstanceBindingResponseTest {

	@Test
	void responseWithDefaultsIsBuilt() {
		DeleteServiceInstanceBindingResponse response = DeleteServiceInstanceBindingResponse.builder()
				.build();

		assertThat(response.isAsync()).isEqualTo(false);
		assertThat(response.getOperation()).isNull();
	}

	@Test
	void responseWithAllValuesIsBuilt() {
		DeleteServiceInstanceBindingResponse response = DeleteServiceInstanceBindingResponse.builder()
				.async(true)
				.operation("in progress")
				.build();

		assertThat(response.isAsync()).isEqualTo(true);
		assertThat(response.getOperation()).isEqualTo("in progress");
	}

	@Test
	void responseWithAllValuesIsDeserialized() {
		DeleteServiceInstanceBindingResponse response = JsonUtils.readTestDataFile(
				"deleteResponse.json", DeleteServiceInstanceBindingResponse.class);

		assertThat(response.getOperation()).isEqualTo("in progress");
	}

	@Test
	void withinOperationCharacterLimit() {
		DeleteServiceInstanceBindingResponse.builder()
				.operation(RandomString.make(9_999))
				.build();
	}

	@Test
	void exceedsOperationCharacterLimit() {
		assertThrows(IllegalArgumentException.class, () ->
				DeleteServiceInstanceBindingResponse.builder()
						.operation(RandomString.make(10_001))
						.build());
	}

	@Test
	void exactlyOperationCharacterLimit() {
		DeleteServiceInstanceBindingResponse.builder()
				.operation(RandomString.make(10_000))
				.build();
	}

}
