/*
 * Copyright 2016-2018 original author or authors.
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
package org.springframework.data.rest.tests.neo4j.groovy;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;

import org.neo4j.springframework.data.core.schema.Id;
import org.neo4j.springframework.data.core.schema.Node;

/**
 * Simulates a Groovy domain object by extending {@link GroovyObject}.
 *
 * @author Greg Turnquist
 * @author Oliver Gierke
 * @author Michael J. Simons
 */
@Node
public class SimulatedGroovyDomainClass implements GroovyObject {

	private @Id String id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//
	// The following fields don't actually have to be implemented since the test cases don't
	// make any Groovy calls. This just simulates the structure of a Groovy object to
	// verify proper handling.
	//

	@Override
	public Object invokeMethod(String s, Object o) {
		return null;
	}

	@Override
	public Object getProperty(String s) {
		return null;
	}

	@Override
	public void setProperty(String s, Object o) {}

	@Override
	public MetaClass getMetaClass() {
		return null;
	}

	@Override
	public void setMetaClass(MetaClass metaClass) {}
}