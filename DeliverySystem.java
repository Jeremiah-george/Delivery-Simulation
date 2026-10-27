import java.util.List;
import java.util.ArrayList;

public class DeliverySystem {
    public List<Restaurant> restaurants;
    public List<Customer> customers;
    public List<Platform> platforms;

    public DeliverySystem() {
        this.restaurants = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.platforms = new ArrayList<>();
    }

    public void ADD_CUSTOMER(int id, String platformName, int x, int y) {
        Customer newCustomer = new Customer(id, x, y, platformName);
        this.customers.add(newCustomer);
        newCustomer.assignedRestaurant = findNearestRestaurantForCustomer(newCustomer);
    }

    public void REMOVE_CUSTOMER(int searchId) {
        customers.removeIf(customer -> customer.id == searchId);
    }

    public void MOVE_CUSTOMER(int id, int newX, int newY) {
        for (Customer currCustomer : this.customers) {
            if (currCustomer.id == id) {
                currCustomer.move(newX, newY, this);
                break;
            }
        }
    }

    public void ADD_RESTAURANT(String restaurantId, int x, int y, int deliveryRad) {
        this.restaurants.add(new Restaurant(restaurantId, x, y, deliveryRad));
    }

    public void REMOVE_RESTAURANT(String restaurantId) {
        Restaurant removedRestaurant = null;

        // Find and remove from main system list
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).id.equals(restaurantId)) {
                removedRestaurant = restaurants.remove(i);
                break;
            }
        }

        if (removedRestaurant != null) {
            // Remove from platforms' internal lists
            for (Platform p : platforms) {
                p.restaurants.remove(removedRestaurant);
            }

            // Reassign affected customers to the next best restaurant
            for (Customer customer : customers) {
                if (customer.assignedRestaurant == removedRestaurant) {
                    customer.assignedRestaurant = findNearestRestaurantForCustomer(customer);
                }
            }
        }
    }

    public int RESTAURANT_ORDER_COUNT(String restaurantId) {
        int total = 0;
        for (Customer currCustomer : this.customers) {
            if (currCustomer.assignedRestaurant != null && currCustomer.assignedRestaurant.id.equals(restaurantId)) {
                total++;
            }
        }
        return total;
    }

    public void ADD_PLATFORM(String newPlatformName) {
        this.platforms.add(new Platform(newPlatformName));
    }

    public int PLATFORM_CUSTOMER_COUNT(String searchPlatformName) {
        int total = 0;
        for (Customer currCustomer : this.customers) {
            if (currCustomer.platformName.equals(searchPlatformName)) {
                total++;
            }
        }
        return total;
    }

    Restaurant findNearestRestaurantForCustomer(Customer customer) {
        Platform customerPlatform = null;
        for (Platform p : platforms) {
            if (p.name.equals(customer.platformName)) {
                customerPlatform = p;
                break;
            }
        }

        if (customerPlatform == null || customerPlatform.restaurants.isEmpty()) {
            return null;
        }

        Restaurant nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Restaurant restaurant : customerPlatform.restaurants) {
            double dist = Math.hypot(customer.x - restaurant.x, customer.y - restaurant.y);
            if (dist > restaurant.deliveryRad) continue;
            if (nearest == null || dist < minDistance - 0.0001) {
                minDistance = dist;
                nearest = restaurant;
            } else if (Math.abs(dist - minDistance) < 0.0001
                    && getCustomerCountForRestaurant(restaurant) < getCustomerCountForRestaurant(nearest)) {
                nearest = restaurant;
            }
        }

        return nearest;
    }

    public int getCustomerCountForRestaurant(Restaurant restaurant) {
        int count = 0;
        for (Customer customer : customers) {
            if (customer.assignedRestaurant == restaurant) {
                count++;
            }
        }
        return count;
    }

    public void REGISTER_PLATFORM_RESTAURANT(String platformName, String restaurantId) {
        Platform targetPlatform = null;
        Restaurant targetRestaurant = null;

        for (Platform p : platforms) {
            if (p.name.equals(platformName)) {
                targetPlatform = p;
                break;
            }
        }

        for (Restaurant restaurant : restaurants) {
            if (restaurant.id.equals(restaurantId)) {
                targetRestaurant = restaurant;
                break;
            }
        }

        if (targetPlatform != null && targetRestaurant != null && !targetPlatform.restaurants.contains(targetRestaurant)) {
            targetPlatform.restaurants.add(targetRestaurant);
            targetRestaurant.platform = platformName;

            // Re-evaluate customers for this platform as they might find this new restaurant closer
            for (Customer customer : customers) {
                if (customer.platformName.equals(platformName)
                        && Math.hypot(customer.x - targetRestaurant.x, customer.y - targetRestaurant.y) <= targetRestaurant.deliveryRad) {
                    customer.assignedRestaurant = findNearestRestaurantForCustomer(customer);
                }
            }
        }
    }

    public void CHANGE_PLATFORM(int customerId, String newPlatformName) {
        for (Customer customer : customers) {
            if (customer.id == customerId) {
                customer.platformName = newPlatformName;
                customer.assignedRestaurant = findNearestRestaurantForCustomer(customer);
                break;
            }
        }
    }

    public void NO_COVERAGE_COUNT() {
        for (Platform p : platforms) {
            int noCoverageCount = 0;
            for (Customer customer : customers) {
                if (customer.platformName.equals(p.name) && customer.assignedRestaurant == null) {
                    noCoverageCount++;
                }
            }
            System.out.println(p.name + ": " + noCoverageCount + " customers with no restaurant in range.");
        }
    }
}