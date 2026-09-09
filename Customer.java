public class Customer {
    int id;
    int x;
    int y;
    String platformName;
    Restaurant assignedRestaurant;

    Customer(int id, int x, int y, String platformName) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.platformName = platformName;
    }

    void move(int newX, int newY, DeliverySystem system) {
        this.x = newX;
        this.y = newY;
        Restaurant nearest = system.findNearestRestaurantForCustomer(this);
        this.assignedRestaurant = nearest;
    }
}