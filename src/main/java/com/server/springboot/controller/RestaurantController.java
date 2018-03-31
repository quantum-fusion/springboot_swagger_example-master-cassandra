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

package com.server.springboot.controller;

// import guru.springframework.cassandra.RestaurantDaoImpl;
import com.server.springboot.cassandra.SessionUtil;
import com.server.springboot.domain.Restaurant;
import com.server.springboot.domain.Table;
import com.server.springboot.util.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.server.springboot.encryption.*;

import java.io.IOException;
import java.util.ArrayList;

// import org.springframework.ui.Model;

@RestController
@RequestMapping("/restaurant")
@Api(value="onlinestore", description="Operations pertaining to Rest API Server")
@ComponentScan({"com.server.springboot.controller","com.server.springboot.cassandra"})
public class RestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private byte[] bobpublickeyEnc;
    private byte[] privateKey;

    private void createServerSystemProperties() {
        System.setProperty("keystore","src/test/resources/server-aes-keystore.jck");
        System.setProperty("storepass","mystorepass");
        System.setProperty("alias","jceksaes");
        System.setProperty("keypass", "mykeypass");
    }

    @Autowired
    private SessionUtil p;

    public RestaurantController() {

        createServerSystemProperties();

    }

    @Autowired
    RestaurantController(SessionUtil s) throws CustomException {

        this.p = s;

        this.p.controllerProperties = Singleton.getInstance( );


        try {

            if(this.p.controllerProperties.getCassandraIpAddress().equals("127.0.0.1")) {
                //p.connect(this.p.controllerProperties.getCassandraIpAddress());
                p.setupPooling(this.p.controllerProperties.getCassandraIpAddress());
                logger.info("RestaurantController::RestaurantController: cassandraIPaddress" + this.p.controllerProperties.getCassandraIpAddress());
            }
            else if(this.p.controllerProperties.getCassandraIpAddress().equals(""))
            {
                //p.connect(this.p.controllerProperties.getCassandraIpAddress());
                p.setupPooling(this.p.controllerProperties.getCassandraIpAddress());
                logger.info("RestaurantController::RestaurantController: cassandraIPaddress" + this.p.controllerProperties.getCassandraIpAddress());
            }
            else
            {
                //p.connect(this.p.controllerProperties.getCassandraIpAddress());
                p.setupPooling(this.p.controllerProperties.getCassandraIpAddress());
                logger.info("RestaurantController::RestaurantController: cassandraIPaddress" + this.p.controllerProperties.getCassandraIpAddress());
            }

            if(this.p.controllerProperties.getCassandraPort().equals("9042")) {
                logger.info("RestaurantController::RestaurantController: cassandraPort" + this.p.controllerProperties.getCassandraPort());
            }
            else if(this.p.controllerProperties.getCassandraPort().equals("")) {
                logger.info("RestaurantController::RestaurantController: cassandraPort" + this.p.controllerProperties.getCassandraPort());
            }
            else
            {
                logger.info("RestaurantController::RestaurantController: cassandraPort" + this.p.controllerProperties.getCassandraPort());
            }


            this.p.createSchema("accounts");
            logger.info("RestaurantController::RestaurantController:createSchema executed");
            this.p.createTable("accounts", "test");
            this.p.deleteTable("accounts", "test");

        } catch (Exception e) {
            logger.error("ServletController::ServletController(): Here is some ERROR: " + e);
        }

    }

    @ApiOperation(value = "Local Database Server Initialization")
    @RequestMapping(value = "/localarguments", method= RequestMethod.POST, produces = "text/plain")
    public String localarguments() {

        this.p.setuplocalDatabase("accounts");

        logger.info("Local Rest Server arguments!:" + this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort());

        return this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort() + "\n";
    }

    @ApiOperation(value = "Rest Server command arguments")
    @RequestMapping(value = "/testarguments", method= RequestMethod.POST, produces = "text/plain")
    public String testarguments(@RequestBody Singleton s ) {

        try {

            this.p.setupDatabase(s.getCassandraIpAddress(), s.getCassandraPort());
            this.p.setupLogin(s.getLogin(), s.getPassword());

            logger.info("Configure Rest Server arguments!:" + this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort());
            logger.info("Login: " + this.p.controllerProperties.getLogin() + " Password: " + this.p.controllerProperties.getPassword());

            this.p.setupPooling(s.getCassandraIpAddress());
            this.p.createSchema("accounts");
            logger.info("RestaurantController::RestaurantController:createSchema executed");
            this.p.createTable("accounts", "test");
            this.p.deleteTable("accounts", "test");

        }
        catch (Exception e)
        {

            logger.error("testarguments: " + e);
        }



        return this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort() + "\n";
    }

    @ApiOperation(value = "Report Database Server Initialization")
    @RequestMapping(value = "/reportarguments", method= RequestMethod.POST, produces = "text/plain")
    public String reportarguments() {

        logger.info("Report Rest Server arguments!:" + this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort());

        return this.p.controllerProperties.getCassandraIpAddress() + ":" + this.p.controllerProperties.getCassandraPort() + "\n";
    }

    @ApiOperation(value = "Setup Database Tables Initialization")
    @RequestMapping(value = "/setupRestaurantDatabaseTables", method= RequestMethod.POST, produces = "text/plain")
    public String setupRestaurantDatabaseTables () {

        try {
            this.p.setupRestaurantDatabaseTables("accounts");
        }
        catch (Exception e)
        {
          return "500/Error: RestaurantController::setupRestaurantDatabaseTables";
        }

        return "200/OK";
    }

    @ApiOperation(value = "Greetings from Rest Server")
    @RequestMapping(value = "/", method= {RequestMethod.GET,RequestMethod.POST}, produces = "text/plain")
    public String index() {

        logger.info("Greetings from Rest Server!");

        return "Greetings from Rest Server!";
    }

    @ApiOperation(value = "Greetings from Rest Server")
    @RequestMapping(value = "/helloworld", method= {RequestMethod.GET,RequestMethod.POST}, produces = "text/plain")
    public String helloworld() {

        logger.info("helloworld");

        return "helloworld!";
    }

    @ApiOperation(value = "Post a message to Rest Server")
    @RequestMapping(value = "/postMessage", method= RequestMethod.POST, produces = "text/plain")
    public String postMessage(@RequestBody String jsonRequest) {

        logger.info("jsonRequest:" + jsonRequest);

        return "200/OK";

    }

    @ApiOperation(value = "greeting from Rest Server")
    @RequestMapping(value = "/greeting", method= {RequestMethod.GET,RequestMethod.POST}, produces = "text/plain")
    public String greeting(@RequestBody String json) {

        logger.info("greeting info message" + json);

        return "greeting info message" + json;
    }

    @ApiOperation(value = "post Alice Public Key")
    @RequestMapping(value = "/postAlicePublicKey", method= {RequestMethod.POST}, produces = "text/plain")
    public String postAlicePublicKey(@RequestBody String json) {

        PublicKeyEnc mypub = new PublicKeyEnc();

        try {
        String mode = "USE_SKIP_DH_PARAMS";

        // read in Bob's public key certificates
        createServerSystemProperties();

        DHKeyAgreement2 keyAgree = new DHKeyAgreement2();

        mode = "GENERATE_DH_PARAMS";

        keyAgree.setup(mode);

        logger.info("greeting diffieHellman json: " + json);

        ObjectMapper mapper = new ObjectMapper();

        PublicKeyEnc alicepublickeyEnc = new PublicKeyEnc();

        alicepublickeyEnc = mapper.readValue(json, PublicKeyEnc.class);

        //     System.out.println("alicepublickeyEnc:" + alicepublickeyEnc.getPublicKeyEnc().toString());

        byte[] bobpublickeyEnc = keyAgree.BobKeyGenerate(alicepublickeyEnc.getPublicKeyEnc());

        this.bobpublickeyEnc = bobpublickeyEnc;

        //   System.out.println("bobpublickeyEnc:" + bobpublickeyEnc.toString());
        byte[] bobsecretkey = keyAgree.generateBobSecretKey(alicepublickeyEnc.getPublicKeyEnc());

        this.privateKey = bobsecretkey;

        System.out.println("Bob's public key from Alice's post: " + DHKeyAgreement2.toHexString(bobpublickeyEnc));

        return DHKeyAgreement2.toHexString(bobpublickeyEnc);

            // returning Bob's public key over to Alice so that Alice can generate Alice's shared secret


                } catch (Exception e) {
                    e.printStackTrace();
                }

        return "";

    }

    @ApiOperation(value = "get Bob Public Key")
    @RequestMapping(value = "/getBobPublicKey", method= {RequestMethod.GET}, produces = "text/plain")
    public byte[] getBobPublicKey() {

        logger.info("bob public key" + DHKeyAgreement2.toHexString(bobpublickeyEnc));

        return this.bobpublickeyEnc;
    }

    @ApiOperation(value = "get private Key")
    @RequestMapping(value = "/getPrivateKey", method= {RequestMethod.GET}, produces = "text/plain")
    public byte[] getPrivateKey() {

        logger.info("private key: " + DHKeyAgreement2.toHexString(this.privateKey));

        return this.privateKey;
    }


    @ApiOperation(value = "Add a new restaurant to inventory list")
    @RequestMapping(value = "/addRestaurant", method = RequestMethod.POST, produces = "text/plain")
    public String addRestaurant(@RequestBody Restaurant restaurant) {



        try {

//            ObjectMapper mapper = new ObjectMapper();
//            Restaurant value = mapper.readValue(restaurant.toString(), Restaurant.class);
//
//            logger.info("json:" + value);


            p.setRestaurants("accounts",restaurant.getRestaurantId(), restaurant.getCuisine(), restaurant.getSeating());

            logger.info("addRestaurant 200/OK");

            return "200/OK";

        }
        catch (Exception e)
        {

            logger.error("ServletController::addRestaurant" + e);
        }

        return "500/error";

    }



    // @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a Table")
    @RequestMapping(value = "/addTable", method = RequestMethod.POST)
    public String addTable(@RequestBody String json) {

        logger.info("json:" + json);

        try {

    //        ObjectMapper mapper = new ObjectMapper();
    //        Table value = mapper.readValue(json, Table.class);

 //ToDo           p.setTables("accounts", value.getTableId(),value.getRestaurantId(),value.getSeatNumber());

            return "200/OK";

        }
        catch (Exception e)
        {

            logger.error("ServletController::addTable" + e);
        }

        logger.error("500/Error");
        return "500/Error";

    }

    @ApiOperation(value = "Get a restaurant from list")
    @RequestMapping(value = "/getRestaurants", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<String> getRestaurants(Restaurant restaurant, Model model) {

        ArrayList<String> restaurantList = new ArrayList<String>();

        logger.error("Henry getRestaurants");

        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.error("Henry cql: " + cql);
        try {
            restaurantList = p.getRestaurants(p.querySchema("accounts", cql), "accounts");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("restaurantList: " + restaurantList);
        logger.error("restaurantList: " + restaurantList);

        return restaurantList;
    }

    /*

    Check availability for an optimal table, for a given customer group size and a choice of cuisine.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get a Table from list")
    @RequestMapping(value = "/getTables", method = RequestMethod.POST)
    public ArrayList<String> getTables(@RequestBody String json) {

        logger.info("json:" + json);

        ArrayList<String> tableList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants WHERE cuisine =\" + \"'\" + cuisine + \"'\" +  \" AND seats=\" + \"'\" + seats + \"'\" + allow filtering;";
        logger.debug("cql: " + cql);

        try {
 //ToDo            tableList = p.getTables(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("tableList: " + tableList);

        return tableList;
    }

    /*

    Feature A. Reserve a table for a group of customers, by looking up the table by TableId.

     suggesting NapSack algorithm

    Added feature B, is to lookup by group size, if a group size is between 7 and 10, multiple tables need to be joined.
     It should reserve the most optimal table (e.g. a group of 3 should be seated in a table of 4, if available

     */
    @ApiOperation(value = "Reserve a Table from list")
    @RequestMapping(value = "/reserveTable", method = RequestMethod.POST)
    public String reserveTable(@RequestBody String json) {

        logger.info("json:" + json);

        ArrayList<String> tableList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants WHERE cuisine =\" + \"'\" + cuisine + \"'\" +  \" AND seats=\" + \"'\" + seats + \"'\" + allow filtering;";
        logger.debug("cql: " + cql);


        try {

       //     ObjectMapper mapper = new ObjectMapper();
       //     Table value = mapper.readValue(json, Table.class);

            // split order of 10 seats into two orders with 7 and 3.

            // tableList = p.getTables(("accounts", cql));
//            if(value.getSeatNumber())
//            {
//                Integer quantityremainder = 0;
//                // first order for 7, returns remainder.
//                quantityremainder = p.getTables("accounts", value.getTableId(), value.getSeatNumber());
//
//                // second order for 3.
//                p.getTables("accounts", value.getTableId(), quantityremainder);
//            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        return "200/OK";
    }


    /*

    Free-up (unreserve a table after the customers leave the restaurant, by adding back the TableId to the Table inventory.

     */
    // @ResponseStatus(HttpStatus.CREATED)

    @ApiOperation(value = "Leave a Table")
    @RequestMapping(value = "/leaveTable", method = RequestMethod.POST)
    public String leaveTable(@RequestBody String json) {

        logger.info("json:" + json);

        try {

        //    ObjectMapper mapper = new ObjectMapper();
        //    Table value = mapper.readValue(json, Table.class);

//ToDo            p.setTables("accounts", value.getTableId(),value.getRestaurantId(),value.getSeatNumber());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "200/OK";
    }

}