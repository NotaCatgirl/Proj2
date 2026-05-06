package database;

import model.CartItem; // For now, remove later!

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:ecommerce.db";
    public static String currentUser;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createUsers =
                "CREATE TABLE IF NOT EXISTS users ("
                        + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username TEXT UNIQUE NOT NULL, "
                        + "password TEXT NOT NULL, "
                        + "role TEXT NOT NULL)";

        String createProducts =
                "CREATE TABLE IF NOT EXISTS products ("
                        + "product_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "description TEXT, "
                        + "price REAL NOT NULL, "
                        + "stock INTEGER NOT NULL, "
                        + "image_path TEXT)";

        String createOrders =
                "CREATE TABLE IF NOT EXISTS orders ("
                        + "order_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER NOT NULL, "
                        + "product_id INTEGER NOT NULL, "
                        + "quantity INTEGER NOT NULL, "
                        + "status TEXT NOT NULL, "
                        + "order_date TEXT, "
                        + "price_at_purchase REAL, "
                        + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                        + "FOREIGN KEY (product_id) REFERENCES products(product_id))";

        String createCartItems =
                "CREATE TABLE IF NOT EXISTS cart_item ("
                        + "cart_item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER NOT NULL, "
                        + "product_id INTEGER NOT NULL, "
                        + "quantity INTEGER NOT NULL, "
                        + "date_added TEXT, "
                        + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                        + "FOREIGN KEY (product_id) REFERENCES products(product_id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            stmt.execute(createCartItems);

            insertSampleProducts();
            insertDefaultAdmin();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String validLogin(String user, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void signUp(String user, String password) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user);
            ps.setString(2, password);
            ps.setString(3, "user");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExist(String user) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // USED BY PRODUCT BROWSING
    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();

        String query = "SELECT product_id, name, description, price, stock, image_path FROM products";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("image_path")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // ADMIN FEATURES
    public static List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();

        String sql = "SELECT o.order_id, u.username, o.product_id, o.quantity, o.price_at_purchase " +
                "FROM orders o JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(
                        "Order #" + rs.getInt("order_id") +
                                " | User: " + rs.getString("username") +
                                " | Product ID: " + rs.getInt("product_id") +
                                " | Qty: " + rs.getInt("quantity") +
                                " | Price: $" + rs.getDouble("price_at_purchase")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        String sql = "SELECT user_id, username, role FROM users ORDER BY user_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(
                        "User ID: " + rs.getInt("user_id") +
                                " | Username: " + rs.getString("username") +
                                " | Role: " + rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void addProduct(String name, String desc, double price, int stock) {
        String sql = "INSERT INTO products (name, description, price, stock, image_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, null);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleProducts() {
        String query = """
                INSERT OR IGNORE INTO products (product_id, name, description, price, stock, image_path)
                VALUES
                (1, 'Pajama Guy', 'Pajama man with a teddy bear', 9.99, 10, '/images/pajamafigure.png'),
                (2, 'Astronaut Guy', 'Astronaut man', 24.99, 25, '/images/astronautfigure.png'),
                (3, 'Construction Guy', 'Construction figure', 29.99, 15, '/images/constructionfigure.png'),
                (4, 'Average Guy', 'Average figure', 19.99, 12, '/images/averageguyfigure.png'),
                (5, 'Shark Guy', 'Shark figure', 19.99, 30, '/images/sharkfigure.png')
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDefaultAdmin() {
        String query = """
            INSERT OR IGNORE INTO users (username, password, role)
            VALUES ('admin', 'admin123', 'admin')
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        if (currentUser == null) {
            return 1;
        }
        String query = "SELECT user_id FROM users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, currentUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT cart_item.cart_item_id, products.product_id, "
                + "products.name, products.price, cart_item.quantity "
                + "FROM cart_item "
                + "JOIN products ON cart_item.product_id = products.product_id "
                + "WHERE cart_item.user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CartItem cartItem = new CartItem(
                        resultSet.getInt("cart_item_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                );
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public static void updateCartQuantity(int cartItemId, int newQuantity) {
        String query = "UPDATE cart_item SET quantity = ? WHERE cart_item_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, cartItemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCartItem(int cartItemId) {
        String query = "DELETE FROM cart_item WHERE cart_item_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cartItemId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToCart(int userId, int productId) {
        String checkQuery = "SELECT cart_item_id, quantity FROM cart_item "
                + "WHERE user_id = ? AND product_id = ?";
        String updateQuery = "UPDATE cart_item SET quantity = ? WHERE cart_item_id = ?";
        String insertQuery = "INSERT INTO cart_item (user_id, product_id, quantity, date_added) "
                + "VALUES (?, ?, 1, datetime('now'))";

        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, productId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int existingCartItemId = resultSet.getInt("cart_item_id");
                int existingQuantity = resultSet.getInt("quantity");
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, existingQuantity + 1);
                    updateStatement.setInt(2, existingCartItemId);
                    updateStatement.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, userId);
                    insertStatement.setInt(2, productId);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}