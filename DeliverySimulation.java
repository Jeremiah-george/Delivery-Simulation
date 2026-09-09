import java.util.Scanner;

public class DeliverySimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DeliverySystem system = new DeliverySystem();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) continue;

            // Fallback for mashed strings like "ADD_PLATFORMDoorDash"
            if (command.startsWith("ADD_PLATFORM") && command.length() > 12) {
                String newPlatformName = command.substring(12).trim();
                system.ADD_PLATFORM(newPlatformName);
                continue;
            }

            switch (command) {
                case "ADD_CUSTOMER":
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    String platformName = scanner.nextLine().trim();
                    int x = Integer.parseInt(scanner.nextLine().trim());
                    int y = Integer.parseInt(scanner.nextLine().trim());
                    system.ADD_CUSTOMER(id, platformName, x, y);
                    break;

                case "REMOVE_CUSTOMER":
                    int removeId = Integer.parseInt(scanner.nextLine().trim());
                    system.REMOVE_CUSTOMER(removeId);
                    break;

                case "MOVE_CUSTOMER":
                    int moveId = Integer.parseInt(scanner.nextLine().trim());
                    int newX = Integer.parseInt(scanner.nextLine().trim());
                    int newY = Integer.parseInt(scanner.nextLine().trim());
                    system.MOVE_CUSTOMER(moveId, newX, newY);
                    break;

                case "ADD_RESTAURANT":
                    String restaurantId = scanner.nextLine().trim();
                    int restaurantX = Integer.parseInt(scanner.nextLine().trim());
                    int restaurantY = Integer.parseInt(scanner.nextLine().trim());
                    int radius = Integer.parseInt(scanner.nextLine().trim());
                    system.ADD_RESTAURANT(restaurantId, restaurantX, restaurantY, radius);
                    break;

                case "REMOVE_RESTAURANT":
                    String removeRestaurantId = scanner.nextLine().trim();
                    system.REMOVE_RESTAURANT(removeRestaurantId);
                    break;

                case "RESTAURANT_ORDER_COUNT":
                    String countRestaurantId = scanner.nextLine().trim();
                    System.out.println(system.RESTAURANT_ORDER_COUNT(countRestaurantId));
                    break;

                case "ADD_PLATFORM":
                    String nextPlatformName = scanner.nextLine().trim();
                    system.ADD_PLATFORM(nextPlatformName);
                    break;

                case "PLATFORM_CUSTOMER_COUNT":
                    String searchPlatformName = scanner.nextLine().trim();
                    System.out.println(system.PLATFORM_CUSTOMER_COUNT(searchPlatformName));
                    break;

                case "REGISTER_PLATFORM_RESTAURANT":
                    String regPlatformName = scanner.nextLine().trim();
                    String regRestaurantId = scanner.nextLine().trim();
                    system.REGISTER_PLATFORM_RESTAURANT(regPlatformName, regRestaurantId);
                    break;

                case "CHANGE_PLATFORM":
                    int changeId = Integer.parseInt(scanner.nextLine().trim());
                    String changePlatformName = scanner.nextLine().trim();
                    system.CHANGE_PLATFORM(changeId, changePlatformName);
                    break;

                case "NO_COVERAGE_COUNT":
                    system.NO_COVERAGE_COUNT();
                    break;

                default:
                    // Ignore unknown commands
                    break;
            }
        }
        scanner.close();
    }
}