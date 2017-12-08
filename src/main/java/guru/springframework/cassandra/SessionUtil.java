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

package guru.springframework.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import guru.springframework.controllers.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import guru.springframework.util.CustomException;

import java.sql.SQLException;
import java.util.ArrayList;

// import com.codahale.metrics.*;


/**
 * Created by Henry Hottelet
 */
@Component
public class SessionUtil {

    //  private static final Logger logger = Logger.getLogger(Orm.class);
    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    public Cluster cluster;
    // private Metadata metadata;
    //   private ArrayList<String> queryResults = new ArrayList<String>();

    public Singleton controllerProperties;


    public void customException(String text, Exception e) throws CustomException {

        logger.error(text);
        throw new CustomException(text, e);
    }

    public void setuplocalDatabase(String keyspace) {
        this.controllerProperties.setCassandraIpAddress("127.0.0.1");
        this.controllerProperties.setCassandraPort("9042");
    }

    public void setupRestaurantDatabaseTables(String keySpace) throws CustomException {

        try {

            this.setupPooling(this.controllerProperties.getCassandraIpAddress());
            Session session = cluster.connect(keySpace);

            this.createRestaurantTable(keySpace);
            this.createTablesTable(keySpace);
            this.createIndex(keySpace);

    } catch (Exception e) {
        logger.error("SessionUtil::setupDatabaseTables(): Here is some ERROR: " + e);
    }

    }

    public void setupDatabase(String ipaddress, String port) {

        this.controllerProperties.setCassandraIpAddress(ipaddress);
        this.controllerProperties.setCassandraPort(port);
    }

    public void connect(String ipAddress) throws CustomException {

        // https://stackoverflow.com/questions/16783725/error-while-connecting-to-cassandra-using-java-driver-for-apache-cassandra-1-0-f
        // http://docs.datastax.com/en/developer/java-driver/3.1/manual/native_protocol/
        try {
            cluster = Cluster.builder()
                    .addContactPoint(ipAddress)
                    .withProtocolVersion(ProtocolVersion.V2)
                    .build();

            // metadata = cluster.getMetadata();
            //     logger.info("Connected to cluster:" + metadata.getClusterName() + "\n");
//            for (Host host : metadata.getAllHosts()) {
//                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
//                        host.getDatacenter(), host.getAddress(), host.getRack());
//            }

            System.out.printf("Connected to cluster: %s\n", cluster.getClusterName());

        } catch (Exception e) {

            customException("Failed to connect", e);
        }
    }

    public void close() {
        cluster.close();

        logger.info("Disconnected from cluster: %s\n" +
                cluster.getClusterName());
    }

    public void createIndex(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            // Index for roomlocation Table
            //    session.execute("CREATE INDEX list_roomname ON accounts.roomlocation (roomname);");


            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        }
        catch (Exception e) {
            customException("Orm::createTable exception: ", e);
        }
    }

    public void setupPooling(String ipAddress) throws CustomException {
        //TODO need to determine these configurable settings for connection pooling and read/write performance latency
        //cassandra.read.consistency.level=ONE
        //cassandra.write.consistency.level=ONE
        //cassandra.retryDownedHostsDelayInSeconds=10
        //cassandra.retryDownedHosts=true
        //cassandra.autoDiscoverHosts=true

        try {
            PoolingOptions pools = new PoolingOptions();
            pools.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
            pools.setMaxConnectionsPerHost(HostDistance.LOCAL, 8);

            pools.setCoreConnectionsPerHost(HostDistance.REMOTE, 1);
            pools.setMaxConnectionsPerHost(HostDistance.REMOTE, 2);

            String cassandraNodes[] = new String[]{
                    ipAddress
            };

            // https://stackoverflow.com/questions/16783725/error-while-connecting-to-cassandra-using-java-driver-for-apache-cassandra-1-0-f
            // http://docs.datastax.com/en/developer/java-driver/3.1/manual/native_protocol/
            final Cluster.Builder builder = new Cluster.Builder().addContactPoints(cassandraNodes).withProtocolVersion(ProtocolVersion.V2).withPoolingOptions(pools);

            cluster = builder.build();
            // metadata = cluster.getMetadata();

            System.out.println("Connected to cluster:" + cluster.getClusterName());

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {

            customException("Orm::setupPooling exception: ", e);
        }
    }

    public void createTable(String keySpace, String tableName) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("CREATE TABLE " + tableName + "(" +
                    "message text," +
                    "testId text PRIMARY KEY," +
                    ")WITH COMPACT STORAGE;");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::createTable exception: ", e);
        }
    }

    public void deleteTable(String keySpace, String tableName) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("DROP TABLE " + keySpace + "." + tableName);

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::createTable exception: ", e);
        }
    }



    public void createSchema(String keySpace) throws SQLException, CustomException {

        try {

            this.setupPooling(this.controllerProperties.getCassandraIpAddress());

            Session session = cluster.connect();

            session.execute("CREATE KEYSPACE " + keySpace + " WITH replication" +
                    "= {'class':'SimpleStrategy', 'replication_factor':3};");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {

            customException("Orm::createSchema exception: ", e);
        }
    }

    public void deleteSchema(String keySpace) throws SQLException, CustomException {

        try {
            Session session = cluster.connect();

            session.execute("DROP KEYSPACE " + keySpace);

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::deleteSchema exception: ", e);
        }
    }

    public ResultSet querySchema(String keySpace, String cql) throws SQLException, CustomException {
        ResultSet r = null;

        try {

            this.setupPooling(this.controllerProperties.getCassandraIpAddress());

            Session session = cluster.connect(keySpace);
            r = session.execute(cql);

            // cluster.close();
        }
        catch (Exception e)
        {
            logger.error("querySchema exception: ", e);
        }

        logger.debug("queryschema results:" + r);
        return r;
    }

    public void createRestaurantTable(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("CREATE TABLE restaurants (" +
                    "cuisine text," +
                    "seating text," +
                    "restaurantId text PRIMARY KEY," +
                    ")WITH COMPACT STORAGE;");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            logger.error("Orm::createTable exception: ", e);
        }
    }



    public String setRestaurants(String keySpace, String restaurantId, String cuisine, String seating) throws SQLException, Exception, CustomException {

        try {
            this.setupPooling(this.controllerProperties.getCassandraIpAddress());
            Session session = cluster.connect(keySpace);

            //String cql = "SELECT * FROM restaurants.restaurantId WHERE restaurantId =\" + \"'\" + restaurantId + \"'\" +  \"allow filtering;";
            //ArrayList<String> queryList = new ArrayList<String>();
            //queryList = getRestaurants(querySchema(keySpace,cql));

            //   logger.debug("" + queryList);
            //  logger.debug("" + queryList.size());

            Insert insert = QueryBuilder
                    .insertInto("restaurants")
                    .value("cuisine", cuisine)
                    .value("seating", seating)
                    .value("restaurantId", restaurantId);

            logger.debug(insert.getQueryString());

            ResultSet results = session.execute(insert);

            return "200/Success";

        } catch (Exception e) {
            logger.error("Orm::setRestaurants exception: ", e);
        }

        return "500/Error: Orm::setRestaurants Exception";
    }

    public ArrayList<String> getRestaurants(ResultSet results, String keySpace) throws SQLException, Exception, CustomException {
        ArrayList<String> queryResults = new ArrayList<String>();

        try {

            this.setupPooling(this.controllerProperties.getCassandraIpAddress());
            Session session = cluster.connect(keySpace);

            for (Row row : results) {

                queryResults.add(String.format("%-20s\t%-20s\t%-20s\n",
                        row.getString("cuisine"),
                        row.getString("seating"),
                        row.getString("restaurantId")));
            }

            return queryResults;

        }
        catch (Exception e) {
            customException("Orm::getRestaurants exception: ", e);
        }

        return queryResults;

    }

    public void createTablesTable(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("CREATE TABLE tables (" +
                    "restaurantId text," +
                    "seatNumber text," +
                    "tableId text PRIMARY KEY," +
                    ")WITH COMPACT STORAGE;");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::createTable exception: ", e);
        }
    }

    public String setTables(String keySpace, String tableId, String restaurantId, String seatNumber) throws SQLException, Exception, CustomException {

        try {
            Session session = cluster.connect(keySpace);

            Insert insert = QueryBuilder
                    .insertInto("tables")
                    .value("restaurantId", restaurantId)
                    .value("seatNumber", seatNumber)
                    .value("tableId", tableId);

            logger.debug(insert.getQueryString());

            ResultSet results = session.execute(insert);

            return "200/Success";

        } catch (Exception e) {
            customException("Orm::setTables exception: ", e);
        }

        return "500/Error: Orm::setTables Exception";
    }


    public ArrayList<String> getTables(ResultSet results, String keySpace) {

        ArrayList<String> queryResults = new ArrayList<String>();

        try {
            Session session = cluster.connect(keySpace);

            for (Row row : results) {

                queryResults.add(String.format("%-20s\t%-20s\t%-20s\n",
                        row.getString("restaurantId"),
                        row.getString("seatNumber"),
                        row.getString("tableId")));
            }

            return queryResults;

        }
        catch (Exception e) {
         customException("Orm::getRestaurants exception: ", e);

        }

        return queryResults;

    }


    public String getTables(String keySpace, String tableId) throws SQLException, Exception, CustomException {

        try {
            Session session = cluster.connect(keySpace);

            Delete delete = QueryBuilder
                    .delete()
                    .from("tables", "tableId");
            // .where(eq("tableId", tableId));

            logger.debug(delete.getQueryString());

            ResultSet results = session.execute(delete);

            return "200/Success";

        } catch (Exception e) {
            customException("Orm::getTables exception: ", e);
        }

        return "500/Error: Orm::getTables Exception";
    }


}
