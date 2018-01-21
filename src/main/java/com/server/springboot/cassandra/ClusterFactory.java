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

import com.datastax.driver.core.*;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.policies.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created with IntelliJ IDEA.
 */
public class ClusterFactory implements FactoryBean<Cluster> {

    private static final Logger logger = LoggerFactory.getLogger(ClusterFactory.class);

    int coreConnectionPerLocalHost;
    int maxConnectionPerLocalHost;
    int coreConnectionPerRemoteHost;
    int maxConnectionPerRemoteHost;
    int connectTimeoutMillis;
    int readTimeOutMillis;
    boolean keepAlive;
    String cassandraNodes;
    boolean isDisabled ;
	String dataCenterName;
	long connectionRetryMs;

    @Override
    public Cluster getObject() throws Exception {

        if (isDisabled) {
            return null;
        }

        String[] cassandraPoints = cassandraNodes.split(";");
        logger.error("ClusterFactory:cassandraPoints:" + cassandraPoints);

        PoolingOptions pools = new PoolingOptions();
        pools.setCoreConnectionsPerHost(HostDistance.LOCAL, coreConnectionPerLocalHost);
        pools.setMaxConnectionsPerHost(HostDistance.LOCAL, maxConnectionPerLocalHost);
        pools.setCoreConnectionsPerHost(HostDistance.REMOTE, coreConnectionPerRemoteHost);
        pools.setMaxConnectionsPerHost(HostDistance.REMOTE, maxConnectionPerRemoteHost);
        
        SocketOptions socketOptions = new SocketOptions();
        socketOptions.setConnectTimeoutMillis(connectTimeoutMillis);
        socketOptions.setKeepAlive(keepAlive);
        socketOptions.setReadTimeoutMillis(readTimeOutMillis);

        final Cluster.Builder builder =
                new Cluster.Builder().addContactPoints("127.0.0.1") // cassandraPoints)
                .withPoolingOptions(pools)
                .withSocketOptions(socketOptions)
                // .withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy(dataCenterName)))
                // by default, statements will be considered non-idempotent
                .withQueryOptions(new QueryOptions().setDefaultIdempotence(false))
                // make your retry policy idempotence-aware
                .withRetryPolicy(new IdempotenceAwareRetryPolicy(DefaultRetryPolicy.INSTANCE))
                .withReconnectionPolicy(new ConstantReconnectionPolicy(connectionRetryMs))
                .withCompression(Compression.LZ4);

        return builder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return Cluster.class;

    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCoreConnectionPerLocalHost(int coreConnectionPerLocalHost) {
        this.coreConnectionPerLocalHost = coreConnectionPerLocalHost;
    }

    public void setMaxConnectionPerLocalHost(int maxConnectionPerLocalHost) {
        this.maxConnectionPerLocalHost = maxConnectionPerLocalHost;
    }

    public void setCoreConnectionPerRemoteHost(int coreConnectionPerRemoteHost) {
        this.coreConnectionPerRemoteHost = coreConnectionPerRemoteHost;
    }

    public void setMaxConnectionPerRemoteHost(int maxConnectionPerRemoteHost) {
        this.maxConnectionPerRemoteHost = maxConnectionPerRemoteHost;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setReadTimeOutMillis(int readTimeOutMillis) {
        this.readTimeOutMillis = readTimeOutMillis;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setCassandraNodes(String cassandraNodes) {
        this.cassandraNodes = cassandraNodes;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}

	public void setConnectionRetryMs(long connectionRetryMs) {
		this.connectionRetryMs = connectionRetryMs;
	}
}
