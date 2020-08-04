/*
 * Copyright 2012-2020 the original author or authors.
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

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * @author Jon Brisbin
 * @author Oliver Gierke
 */
@Configuration
@EnableMongoRepositories
@EnableMongoAuditing
public class MongoDbRepositoryConfig extends AbstractMongoClientConfiguration {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		return "spring-data-rest-sample";
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getMappingBasePackage()
	 */
	@Override
	protected String getMappingBasePackage() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo()
	 */
	@Override
	public MongoClient mongoClient() {
		return MongoClients.create();
	}
}