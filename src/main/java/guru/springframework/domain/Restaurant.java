package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by hottelet on 10/23/17.
 */

public class Restaurant implements Serializable {

    static final long serialVersionUID = 1L; //assign a long value

    private String restaurantId;
    private String cuisine;
    private String seating;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }
}
