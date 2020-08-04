/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rest.tests.neo4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.neo4j.springframework.data.core.schema.GeneratedValue;
import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;
import org.neo4j.springframework.data.core.schema.Relationship;
import org.springframework.data.annotation.ReadOnlyProperty;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Oliver Gierke
 * @author Michael J. Simons
 */
@Node
public class User {

	public enum Gender {
		MALE, FEMALE;
	}

	@Id @GeneratedValue
	public UUID id;
	public String firstname, lastname;
	public Address address;
	public Set<Address> shippingAddresses;
	public List<String> nicknames;
	public Gender gender;
	public @ReadOnlyProperty EmailAddress email;
	public LocalDateTime java8DateTime;
	public TypeWithPattern pattern;
	@Relationship("HAS_COLLEAGUE")
	public List<User> colleagues;
	@Relationship("HAS_MANAGER")
	public User manager;
	public Map<String, Nested> colleaguesMap = new HashMap<>();

	public static class EmailAddress {

		private final String value;

		/**
		 * @param value
		 */
		public EmailAddress(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return value;
		}
	}

	public static class TypeWithPattern {}

	@Node
	public static class Nested {

		@Id @GeneratedValue
		public UUID id;
		public User user;
		public String foo = "foo";

		public Nested(User user) {
			this.user = user;
		}
	}
}
