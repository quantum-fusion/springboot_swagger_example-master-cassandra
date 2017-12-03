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

package guru.springframework.controllers;

import guru.springframework.dao.RestaurantDao;
import guru.springframework.dao.TableDao;
import guru.springframework.domain.Product;
import guru.springframework.domain.Restaurant;
import guru.springframework.domain.Table;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import guru.springframework.cassandra.SessionUtil;

import java.util.ArrayList;

// import org.springframework.ui.Model;

@RestController
@RequestMapping("/restaurant")
@Api(value="onlinestore", description="Operations pertaining to Rest API Server")
@ComponentScan("persistence")
public class RestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private RestaurantDao RestaurantService;
    private TableDao TableService;

    @Autowired
    private guru.springframework.cassandra.SessionUtil p;

    public RestaurantController() {
    }

    @Autowired
    RestaurantController(SessionUtil s) {

        this.p = s;

        try {

            s.connect("127.0.0.1");
          //  s.setupPooling("127.0.0.1");

     //       s.createSchema("accounts");
     //       s.createTable("accounts");
     //       s.createIndex("accounts");

        } catch (Exception e) {
            logger.error("ServletController::ServletController(): Here is some ERROR: " + e);
        }

    }

    @ApiOperation(value = "Greetings from Rest Server")
    @RequestMapping(value = "/", method= RequestMethod.POST, produces = "text/plain")
    public String index() {

        logger.info("Greetings from Rest Server!");

        return "Greetings from Rest Server!";
    }

    @ApiOperation(value = "Greetings from Rest Server")
    @RequestMapping(value = "/helloworld", method= RequestMethod.POST, produces = "text/plain")
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
    @RequestMapping(value = "/greeting", method= RequestMethod.POST, produces = "text/plain")
    public String greeting(@RequestBody String json) {

        logger.info("greeting info message" + json);

        return "greeting info message" + json;
    }

    @ApiOperation(value = "Add a new restaurant to inventory list")
    @RequestMapping(value = "/addRestaurant", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addRestaurant(@RequestBody Restaurant restaurant) {

        // logger.info("json:" + restaurant);

        // RestaurantService.addRestaurant(restaurant);

        try {

            //   ObjectMapper mapper = new ObjectMapper();
            //  Restaurant value = mapper.readValue(restaurant.toString(), Restaurant.class);

            //ToDo         p.setRestaurants("accounts",value.getRestaurantId(), value.getCuisine(), value.getSeating());

            return new ResponseEntity("restaurant saved successfully", HttpStatus.OK);

        }
        catch (Exception e)
        {

            logger.error("ServletController::addRestaurant" + e);
        }

        return new ResponseEntity("restaurant saved successfully", HttpStatus.EXPECTATION_FAILED);

    }



    // @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a Table")
    @RequestMapping(value = "/addTable", method = RequestMethod.POST)
    public String addTable(@RequestBody String json) {

        logger.info("json:" + json);

        try {

            ObjectMapper mapper = new ObjectMapper();
            Table value = mapper.readValue(json, Table.class);

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
    @RequestMapping(value = "/getRestaurants", method = RequestMethod.POST, produces = "application/json")
    public ArrayList<String> getRestaurants() {

        ArrayList<String> restaurantList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.debug("cql: " + cql);
        try {
//ToDo            restaurantList = p.getRestaurants(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("restaurantList: " + restaurantList);

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

            ObjectMapper mapper = new ObjectMapper();
            Table value = mapper.readValue(json, Table.class);

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

            ObjectMapper mapper = new ObjectMapper();
            Table value = mapper.readValue(json, Table.class);

//ToDo            p.setTables("accounts", value.getTableId(),value.getRestaurantId(),value.getSeatNumber());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "200/OK";
    }

}