# Delivery Dispatch Simulation

A Java project that simulates a food delivery platform matching customers to the nearest restaurant that can actually deliver to them.

You feed it commands through stdin (add a restaurant, add a customer, move someone, etc.) and it keeps everything matched up automatically. Customers only get matched within their own platform (like DoorDash), and only to restaurants whose delivery radius covers them. If two restaurants are tied on distance, whichever one has fewer orders right now gets picked.

When something changes, only the customers it can affect are re-matched. For example, a newly registered restaurant only triggers re-matching for customers inside its delivery radius.

## Running it

```
javac *.java
java DeliverySimulation
```

You can also just pipe a file of commands in:

```
java DeliverySimulation < commands.txt
```

## Testing

```
java SimulationTest
```

`SimulationTest` replays 10,000 random events with a fixed seed (adding, moving and removing customers and restaurants, and switching platforms). After every event it recomputes each customer's correct assignment from scratch using the plain rules (right platform, in range, nearest wins) and compares it to what the engine assigned. It prints the number of mismatches and the average and maximum time per event.

Latest run: 0 violations across 10000 events
            avg 0.0028 ms per event, max 7.9817 ms

## Commands

Each command is its own line, with any arguments on the lines right after it.

- `ADD_CUSTOMER` – id, platform name, x, y
- `REMOVE_CUSTOMER` – id
- `MOVE_CUSTOMER` – id, new x, new y
- `ADD_RESTAURANT` – id, x, y, delivery radius
- `REMOVE_RESTAURANT` – id
- `RESTAURANT_ORDER_COUNT` – id (prints number of orders)
- `ADD_PLATFORM` – name
- `PLATFORM_CUSTOMER_COUNT` – name (prints count)
- `REGISTER_PLATFORM_RESTAURANT` – platform name, restaurant id
- `CHANGE_PLATFORM` – customer id, new platform name
- `NO_COVERAGE_COUNT` – no args, prints how many customers have nothing in range

## Files

- `Restaurant.java` / `Customer.java` / `Platform.java` – the basic entities
- `DeliverySystem.java` – where all the matching logic lives
- `DeliverySimulation.java` – main(), reads the commands and calls into DeliverySystem
- `SimulationTest.java` – randomised consistency and timing test