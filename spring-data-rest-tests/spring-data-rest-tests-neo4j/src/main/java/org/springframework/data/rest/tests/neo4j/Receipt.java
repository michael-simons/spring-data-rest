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

package org.springframework.data.rest.tests.neo4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.neo4j.springframework.data.core.schema.GeneratedValue;
import org.neo4j.springframework.data.core.schema.Node;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

/**
 * @author Pablo Lozano
 * @author Michael J. Simons
 */
// tag::code[]
@Node
public class Receipt {

	public @Id @GeneratedValue UUID id;
	public @Version Long version;
	public @LastModifiedDate Date date;  // <1>

	public String saleItem;
	public BigDecimal amount;

}
// end::code[]
