/*
 * Copyright 2014-2020 the original author or authors.
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
package org.springframework.data.rest.webmvc.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.JsonSchemaFormat;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.tests.TestMvcClient;
import org.springframework.data.rest.tests.neo4j.Neo4jRepositoryConfig;
import org.springframework.data.rest.tests.neo4j.Neo4jServerConfig;
import org.springframework.data.rest.tests.neo4j.Profile;
import org.springframework.data.rest.tests.neo4j.User.EmailAddress;
import org.springframework.data.rest.tests.neo4j.User.TypeWithPattern;
import org.springframework.data.rest.tests.neo4j.groovy.SimulatedGroovyDomainClass;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.json.PersistentEntityToJsonSchemaConverter.ValueTypeSchemaPropertyCustomizerFactory;
import org.springframework.data.rest.webmvc.json.PersistentEntityToJsonSchemaConverterUnitTests.TestConfiguration;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.hateoas.mediatype.MessageResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Oliver Gierke
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Neo4jServerConfig.class, Neo4jRepositoryConfig.class, TestConfiguration.class })
public class PersistentEntityToJsonSchemaConverterUnitTests {

	@Autowired MessageResolver resolver;
	@Autowired RepositoryRestConfiguration configuration;
	@Autowired PersistentEntities entities;
	@Autowired Associations associations;

	ObjectMapper objectMapper = new ObjectMapper();

	@Configuration
	@Import(RepositoryRestMvcConfiguration.class)
	static class TestConfiguration implements RepositoryRestConfigurer {

		@Override
		public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

			config.getMetadataConfiguration().registerJsonSchemaFormat(JsonSchemaFormat.EMAIL, EmailAddress.class);
			config.getMetadataConfiguration().registerFormattingPatternFor("[A-Z]+", TypeWithPattern.class);

			config.exposeIdsFor(Profile.class);
		}
	}

	PersistentEntityToJsonSchemaConverter converter;

	@Before
	public void setUp() {

		TestMvcClient.initWebTest();

		ValueTypeSchemaPropertyCustomizerFactory customizerFactory = mock(
			ValueTypeSchemaPropertyCustomizerFactory.class);

		converter = new PersistentEntityToJsonSchemaConverter(entities, associations, resolver, objectMapper,
			configuration,
			customizerFactory);
	}

	@Test // DATAREST-631, DATAREST-632
	public void fulfillsConstraintsForProfile() {

		List<Constraint> constraints = new ArrayList<>();
		constraints.add(new Constraint("$.properties.id", is(notNullValue()), "Has descriptor for id property"));
		constraints.add(new Constraint("$.description", is("Profile description"), "Adds description to schema root"));
		constraints
			.add(new Constraint("$.properties.renamed", is(notNullValue()), "Has descriptor for renamed property"));
		constraints.add(
			new Constraint("$.properties.aliased", is(nullValue()),
				"No descriptor for original name of renamed property"));

		assertConstraints(Profile.class, constraints);
	}

	@Test // DATAREST-754
	public void handlesGroovyDomainObjects() {

		List<Constraint> constraints = new ArrayList<>();
		constraints.add(new Constraint("$.properties.name", is(notNullValue()), "Has descriptor for name property"));

		assertConstraints(SimulatedGroovyDomainClass.class, constraints);
	}

	@SuppressWarnings("unchecked")
	private void assertConstraints(Class<?> type, Iterable<Constraint> constraints) {

		String writeSchemaFor = writeSchemaFor(type);

		for (Constraint constraint : constraints) {

			try {
				assertThat(constraint.description, JsonPath.read(writeSchemaFor, constraint.selector),
					constraint.matcher);
			} catch (PathNotFoundException e) {
				assertThat(constraint.matcher.matches(null)).isTrue();
			} catch (RuntimeException e) {
				assertThat(e, constraint.matcher);
			}
		}
	}

	private String writeSchemaFor(Class<?> type) {

		try {
			return objectMapper.writeValueAsString(converter.convert(type));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private static class Constraint {

		String selector;
		Matcher matcher;
		String description;

		public Constraint(String selector, Matcher matcher, String description) {
			this.selector = selector;
			this.matcher = matcher;
			this.description = description;
		}
	}
}
