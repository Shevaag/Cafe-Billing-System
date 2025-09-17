import java.util.ArrayList; //resizable array implementation
import java.util.List; //represents an ordered collection
import java.util.Scanner; //to read input from various input sources
import java.io.File; //represents a file
import java.io.FileNotFoundException; //an exception that is thrown when an attempt to open a file denoted by a specified pathname has failed
import java.io.BufferedWriter; //writing to files to improve performance
import java.io.FileWriter; // to write text files more efficiently
import java.io.IOException; //thrown when dealing with file

class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class Order {
    private int orderNumber;
    private String name;
    private String contact;
    private String mail;
    private String address;
    private String items;

    public Order(int orderNumber, String name, String contact, String mail, String address, String items) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.contact = contact;
        this.mail = mail;
        this.address = address;
        this.items = items;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public String getItems() {
        return items;
    }
}

public class CafeMocha {

    private static int orderCounter = 0; // Counter to generate unique order numbers
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        CafeMocha cafe = new CafeMocha();
        cafe.loadMenuItems(); // Load menu items from file
        cafe.loadLastOrderNumber(); // Load the last order number

        while (true) {
            if (cafe.login()) {
                System.out.println("Login successful!");
                cafe.mainMenu();
            } else {
                System.out.println("Login failed! Try again.");
            }
        }
    }

    // Load menu items from an external file (menu.txt)
    void loadMenuItems() {
        try (Scanner scanner = new Scanner(new File("menu.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String itemName = parts[0].trim(); //removes any leading spaces
                    double itemPrice = Double.parseDouble(parts[1].trim()); //price convertion string->double 
                    menuItems.add(new MenuItem(itemName, itemPrice));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Menu file not found.");
        }
    }

    // Load the last order number from a file
    void loadLastOrderNumber() {
        try (Scanner scanner = new Scanner(new File("order_number.txt"))) {
            if (scanner.hasNextLine()) {
                orderCounter = Integer.parseInt(scanner.nextLine().trim()); //string->int
            }
        } catch (FileNotFoundException e) {
            // If the file does not exist, it means no orders have been placed yet, so we start from 0
            System.out.println("Order number file not found. Starting with order number 0.");
        }
    }

    // Save the current order number to the file
    void saveOrderNumber() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order_number.txt"))) {
            writer.write(String.valueOf(orderCounter)); //int->string
        } catch (IOException e) {
            System.out.println("Error saving the order number.");
            e.printStackTrace();
        }
    }

    // User login function
    boolean login() {
        try (Scanner scanner = new Scanner(new File("login.txt"))) {
            Scanner inputScanner = new Scanner(System.in);

            System.out.println("Enter Username: ");
            String username = inputScanner.nextLine();
            System.out.println("Enter Password: ");
            String password = inputScanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            System.out.println("User file not found.");
            return false;
        }
    }

    // Main menu section after logged in successfully
    void mainMenu() {
        int choice;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.println("\n=== Café Mocha System ===");
            System.out.println("1. Add New Customer Order");
            System.out.println("2. Display Order Details");
            System.out.println("3. Calculate and Print Bill");
            System.out.println("4. Add New Item to Menu");
            System.out.println("5. Help");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            scan.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNewOrder();
                    break;
                case 2:
                    displayOrderDetails();
                    break;
                case 3:
                    calculateAndPrintBill();
                    break;
                case 4:
                    addNewMenuItem();
                    break;
                case 5:
                    help();
                    break;
                case 6:
                    logout();
                    return; // Exit the mainMenu loop to return to login
                case 7:
                    System.out.println("Exiting the system...");
                    System.exit(0); // Terminate the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    void addNewOrder() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Customer Name");
        String name = scan.nextLine();
        System.out.println("Enter Customer Mobile Number");
        String contact = scan.nextLine();
        System.out.println("Enter Customer Email Address");
        String mail = scan.nextLine();
        System.out.println("Enter Customer Address");
        String address = scan.nextLine();

        displayMenuItems();
        System.out.print("Enter item numbers and the quantity to add to the order (e.g., 1x2, 4x1): ");
        String items = scan.nextLine();

        // Create a new order with a unique order number
        int orderNumber = ++orderCounter;
        Order newOrder = new Order(orderNumber, name, contact, mail, address, items);
        orders.add(newOrder);
        saveOrder(newOrder);
        saveOrderNumber(); // Save the updated order number
        System.out.println("Order placed successfully! Your order number is: " + orderNumber);
    }

    void displayOrderDetails() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the order number to display details: ");
        int orderNumber = scan.nextInt();
        scan.nextLine(); // Consume newline

        Order order = orders.stream().filter(o -> o.getOrderNumber() == orderNumber).findFirst().orElse(null); //o-?each order
        if (order != null) {
            System.out.println("Order Number: " + order.getOrderNumber());
            System.out.println("Customer Name: " + order.getName());
            System.out.println("Contact: " + order.getContact());
            System.out.println("Email: " + order.getMail());
            System.out.println("Address: " + order.getAddress());
            System.out.println("Ordered Items: " + getFormattedItems(order.getItems()));
        } else {
            System.out.println("Order not found.");
        }
    }

    String getFormattedItems(String items) {
        StringBuilder formattedItems = new StringBuilder();
        String[] itemEntries = items.split(", ");
        for (String entry : itemEntries) {
            String[] parts = entry.split("x");
            int itemNumber = Integer.parseInt(parts[0]); //string->int
            int quantity = Integer.parseInt(parts[1]);
            MenuItem item = menuItems.get(itemNumber - 1);
            if (item != null) {
                formattedItems.append(item.getName()).append(" x ").append(quantity).append(", ");
            } else {
                formattedItems.append("Unknown item x ").append(quantity).append(", ");
            }
        }
        return formattedItems.length() > 0 ? formattedItems.substring(0, formattedItems.length() - 2) : "";
    }

    double calculateTotal(Order order) {
        double total = 0.0; //initialization
        String[] itemEntries = order.getItems().split(", ");
        for (String entry : itemEntries) {
            String[] parts = entry.split("x");
            int itemNumber = Integer.parseInt(parts[0]); //string->int
            int quantity = Integer.parseInt(parts[1]);
            MenuItem item = menuItems.get(itemNumber - 1);
            if (item != null) {
                total += item.getPrice() * quantity;
            } else {
                System.out.println("Item not found: " + itemNumber);
            }
        }
        return total;
    }

    void calculateAndPrintBill() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the order number to calculate and print the bill: ");
        int orderNumber = scan.nextInt();
        scan.nextLine(); // Consume newline

        Order order = orders.stream().filter(o -> o.getOrderNumber() == orderNumber).findFirst().orElse(null);
        if (order != null) {
            double total = calculateTotal(order);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("bills.txt", true))) {
                bw.write("Order Number: " + order.getOrderNumber() + ", Total: Rs." + String.format("%.2f", total)); //with 2 decimal places
                bw.newLine();
            } catch (IOException e) {
                System.out.println("Error saving the bill.");
                e.printStackTrace();
            }

            System.out.println("\n=== Bill ===");
            System.out.println("Order Number: " + order.getOrderNumber());
            System.out.println("Customer Name: " + order.getName());
            System.out.println("Contact: " + order.getContact());
            System.out.println("Email: " + order.getMail());
            System.out.println("Address: " + order.getAddress());
            System.out.println("Ordered Items: " + getFormattedItems(order.getItems()));
            System.out.println("Total: Rs." + String.format("%.2f", total));
        } else {
            System.out.println("Order not found.");
        }
    }

    void addNewMenuItem() {
        Scanner scan = new Scanner(System.in);

        // Get new item details from the user
        System.out.print("Enter the name of the new menu item: ");
        String itemName = scan.nextLine();
        System.out.print("Enter the price of the new menu item: ");
        double itemPrice = scan.nextDouble();
        scan.nextLine(); // Consume newline

        // Add the new item to the menu
        MenuItem newItem = new MenuItem(itemName, itemPrice);
        menuItems.add(newItem);

        // Save the new item to the menu.txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu.txt", true))) {
            writer.write(itemName + ", " + itemPrice);
            writer.newLine();
            System.out.println("New item added to the menu successfully.");
        } catch (IOException e) {
            System.out.println("Error saving new menu item.");
            e.printStackTrace();
        }
    }

    void displayMenuItems() {
        System.out.println("\n=== Menu Items ===");
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            System.out.println((i + 1) + ": " + item.getName() + " - Rs." + item.getPrice());
        }
    }

    void help() {
        System.out.println("\n=== Help ===");
        System.out.println("1. Add New Customer Order: Record a new customer order.");
        System.out.println("2. Display Order Details: Show details of a specific order.");
        System.out.println("3. Calculate and Print Bill: Calculate the total for an order and print the bill.");
        System.out.println("4. Add New Item to Menu: Add a new item to the menu.");
        System.out.println("5. Help: Display this help information.");
        System.out.println("6. Logout: Log out of the system.");
        System.out.println("7. Exit: Exit the system.");
    }

    void saveOrder(Order order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("orders.txt", true))) {
            bw.write(order.getOrderNumber() + "," + order.getName() + "," + order.getContact() + "," + order.getMail() + "," + order.getAddress() + "," + order.getItems());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving order.");
            e.printStackTrace();
        }
    }

    //logout functionality
    void logout() {
        System.out.println("Logging out...");
    
    }
}
