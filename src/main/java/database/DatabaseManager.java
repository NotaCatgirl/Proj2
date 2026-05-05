package database;

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
                        + "stock INTEGER NOT NULL)";

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

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
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
        String query = "INSERT INTO users (username,password,role) VALUES (?,?,?)";
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
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();

        String sql = "SELECT o.order_id, o.user_id, u.username, o.product_id, o.quantity, o.price_at_purchase, o.order_date FROM orders o JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC ";

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

    public static List<String> getAllProducts() {
        List<String> products = new ArrayList<>();

        String sql = "SELECT product_id, name, price, stock FROM products ORDER BY product_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(
                        "ID: " + rs.getInt("product_id") +
                                " | " + rs.getString("name") +
                                " | $" + rs.getDouble("price") +
                                " | Stock: " + rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void addProduct(String name, String desc, double price, int stock) {
        String sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setDouble(3, price);
            ps.setInt(4, stock);

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

    public static void setUser (String username){
        currentUser = username;
    }

    public static String getCurrentUser () {
        return currentUser;
    }
}