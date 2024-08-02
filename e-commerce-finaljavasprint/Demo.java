import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ecommerce", "postgres", "hellorobot@123")) {
            UserDAO userDAO = new UserDAO(connection);
            UserService userService = new UserService(userDAO);
            ProductDAO productDAO = new ProductDAO(connection);
            ProductService productService = new ProductService(productDAO);

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter role (buyer/seller/admin): ");
                        String role = scanner.nextLine();

                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setRole(role);

                        userService.registerUser(user);
                        System.out.println("User registered successfully.");
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();

                        User authenticatedUser = userService.authenticateUser(username, password);
                        if (authenticatedUser != null) {
                            
                            System.out.println("Login successful. Role: " + authenticatedUser.getRole());
                            switch (authenticatedUser.getRole()) {
                                case "buyer":
                                    buyerMenu(productService, scanner);
                                    break;
                                case "seller":
                                    sellerMenu(authenticatedUser, productService, scanner);
                                    break;
                                case "admin":
                                    adminMenu(userService, productService, scanner);
                                    break;
                            }
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                        break;

                    case 3:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void buyerMenu(ProductService productService, Scanner scanner) throws SQLException {
        boolean exit = false;

        while (!exit) {
            System.out.println("Buyer Menu:");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Product");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    List<Product> products = productService.getAllProducts();
                    for (Product product : products) {
                        System.out.println("ID: " + product.getProductId() + ", Name: " + product.getName() +
                                ", Price: " + product.getPrice() + ", Quantity: " + product.getQuantity() +
                                ", Seller ID: " + product.getSellerId());
                    }
                    break;

                case 2:
                    System.out.print("Enter product name to search: ");
                    String name = scanner.nextLine();
                    products = productService.getAllProducts();
                    for (Product product : products) {
                        if (product.getName().equalsIgnoreCase(name)) {
                            System.out.println("ID: " + product.getProductId() + ", Name: " + product.getName() +
                                    ", Price: " + product.getPrice() + ", Quantity: " + product.getQuantity() +
                                    ", Seller ID: " + product.getSellerId());
                        }
                    }
                    break;

                case 3:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void sellerMenu(User authenticatedUser, ProductService productService, Scanner scanner) throws SQLException {
        boolean exit = false;

        while (!exit) {
            System.out.println("Seller Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View My Products");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter product quantity: ");
                    int quantity = scanner.nextInt();

                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setSellerId(authenticatedUser.getUserId());

                    productService.addProduct(product);
                    System.out.println("Product added successfully.");
                    break;

                case 2:
                    System.out.print("Enter product ID to update: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new product name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter new product price: ");
                    price = scanner.nextDouble();
                    System.out.print("Enter new product quantity: ");
                    quantity = scanner.nextInt();

                    product = new Product();
                    product.setProductId(productId);
                    product.setName(name);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setSellerId(authenticatedUser.getUserId());

                    productService.updateProduct(product);
                    System.out.println("Product updated successfully.");
                    break;

                case 3:
                    System.out.print("Enter product ID to delete: ");
                    productId = scanner.nextInt();

                    productService.deleteProduct(productId, authenticatedUser.getUserId());
                    System.out.println("Product deleted successfully.");
                    break;

                case 4:
                    List<Product> products = productService.getProductsBySellerId(authenticatedUser.getUserId());
                    for (Product p : products) {
                        System.out.println("ID: " + p.getProductId() + ", Name: " + p.getName() +
                                ", Price: " + p.getPrice() + ", Quantity: " + p.getQuantity());
                    }
                    break;

                case 5:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void adminMenu(UserService userService, ProductService productService, Scanner scanner) throws SQLException {
        boolean exit = false;

        while (!exit) {
            System.out.println("Admin Menu:");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. View All Products");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    List<User> users = userService.getAllUsers();
                    for (User user : users) {
                        System.out.println("ID: " + user.getUserId() + ", Username: " + user.getUsername() +
                                ", Email: " + user.getEmail() + ", Role: " + user.getRole());
                    }
                    break;

                case 2:
                    System.out.print("Enter user ID to delete: ");
                    int userId = scanner.nextInt();

                    userService.deleteUser(userId);
                    System.out.println("User deleted successfully.");
                    break;

                case 3:
                    List<Product> products = productService.getAllProducts();
                    for (Product product : products) {
                        System.out.println("ID: " + product.getProductId() + ", Name: " + product.getName() +
                                ", Price: " + product.getPrice() + ", Quantity: " + product.getQuantity() +
                                ", Seller ID: " + product.getSellerId());
                    }
                    break;

                case 4:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}

