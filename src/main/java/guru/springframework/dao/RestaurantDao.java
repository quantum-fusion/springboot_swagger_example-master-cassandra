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

package guru.springframework.dao;

import com.datastax.driver.core.*;
import guru.springframework.domain.Product;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import guru.springframework.util.CustomException;

import java.sql.SQLException;
import java.util.ArrayList;

// import com.codahale.metrics.*;

import guru.springframework.domain.Restaurant;
import org.springframework.stereotype.Repository;

/**
 * Created by Henry Hottelet
 */

@RepositoryRestResource
public interface RestaurantDao {

//    public Restaurant saveRestaurant(Restaurant restaurant);

    public void createTable(String keySpace) throws SQLException, CustomException;

    public String setRestaurants(String keySpace, String restaurantId, String cuisine, String seating) throws SQLException, Exception, CustomException;

    public ArrayList<String> getRestaurants(ResultSet results) throws SQLException, Exception, CustomException;

}
