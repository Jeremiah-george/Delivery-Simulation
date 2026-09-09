import java.util.List;
import java.util.ArrayList;
 
public class Platform {
    String name;
    List<Customer> customers = new ArrayList<>();
    List<Restaurant> restaurants = new ArrayList<>();
 
    Platform(String name) {
        this.name = name;
    }
}