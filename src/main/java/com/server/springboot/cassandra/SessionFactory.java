/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * @author Henry Hottelet
 */

package com.server.springboot.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.FactoryBean;

/**
 * Create a session for a keyspace from cluster object
 */
public class SessionFactory implements FactoryBean<Session> {
	String keyspace;
	Cluster cluster;
	
	public SessionFactory(Cluster cluster, String keyspace) {
		this.cluster = cluster;
		this.keyspace = keyspace;
	}

	@Override
	public Session getObject() throws Exception {
		if(cluster == null) {
			return null;
		}
		return cluster.connect(keyspace);
	}

	@Override
	public Class<?> getObjectType() {
		return Session.class;

	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
}
