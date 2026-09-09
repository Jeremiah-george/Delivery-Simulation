public class Restaurant {
    String id;
    int x;
    int y;
    String platform;
    int deliveryRad;

    public Restaurant(String id, int x, int y, int deliveryRad) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.deliveryRad = deliveryRad;
        this.platform = null;
    }
}