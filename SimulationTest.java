import java.util.*;

public class SimulationTest {
    static String[] plats = {"A", "B", "C"};

    public static void main(String[] args) {
        Random rnd = new Random(42);
        DeliverySystem s = new DeliverySystem();
        for (String p : plats) s.ADD_PLATFORM(p);
        int rid = 0, cid = 0, violations = 0, events = 10000;
        long total = 0, max = 0;

        for (int i = 0; i < events; i++) {
            int t = rnd.nextInt(6);
            long t0 = System.nanoTime();
            if (t == 0 || s.restaurants.size() < 20) {
                String id = "R" + rid++;
                s.ADD_RESTAURANT(id, rnd.nextInt(100), rnd.nextInt(100), 5 + rnd.nextInt(30));
                s.REGISTER_PLATFORM_RESTAURANT(plats[rnd.nextInt(3)], id);
            } else if (t == 1 || s.customers.size() < 50) {
                s.ADD_CUSTOMER(cid++, plats[rnd.nextInt(3)], rnd.nextInt(100), rnd.nextInt(100));
            } else if (t == 2) {
                s.MOVE_CUSTOMER(s.customers.get(rnd.nextInt(s.customers.size())).id, rnd.nextInt(100), rnd.nextInt(100));
            } else if (t == 3) {
                s.CHANGE_PLATFORM(s.customers.get(rnd.nextInt(s.customers.size())).id, plats[rnd.nextInt(3)]);
            } else if (t == 4) {
                s.REMOVE_RESTAURANT(s.restaurants.get(rnd.nextInt(s.restaurants.size())).id);
            } else {
                s.REMOVE_CUSTOMER(s.customers.get(rnd.nextInt(s.customers.size())).id);
            }
            long dt = System.nanoTime() - t0;
            total += dt;
            max = Math.max(max, dt);
            violations += check(s);
        }
        System.out.println(violations + " violations across " + events + " events");
        System.out.printf("avg %.4f ms per event, max %.4f ms%n", total / 1e6 / events, max / 1e6);
    }

    // Checks every customer's assignment against the matching rules from scratch.
    static int check(DeliverySystem s) {
        int bad = 0;
        for (Customer c : s.customers) {
            Platform p = null;
            for (Platform x : s.platforms) if (x.name.equals(c.platformName)) p = x;
            Restaurant best = null;
            double bestDist = Double.MAX_VALUE;
            if (p != null) {
                for (Restaurant r : p.restaurants) {
                    double d = Math.hypot(c.x - r.x, c.y - r.y);
                    if (d <= r.deliveryRad && d < bestDist) { bestDist = d; best = r; }
                }
            }
            if (best == null) {
                if (c.assignedRestaurant != null) bad++;
            } else {
                if (c.assignedRestaurant == null) { bad++; continue; }
                double d = Math.hypot(c.x - c.assignedRestaurant.x, c.y - c.assignedRestaurant.y);
                if (!p.restaurants.contains(c.assignedRestaurant)
                        || d > c.assignedRestaurant.deliveryRad
                        || d > bestDist + 0.0001) bad++;
            }
        }
        return bad;
    }
}