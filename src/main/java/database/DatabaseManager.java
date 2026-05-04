package database;

import model.CartItem;

import java.sql.*;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String validLogin(String user, String password){
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1,user);
            ps.setString(2,password);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("role");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void signUp(String user, String password){
        String query = "INSERT INTO users (username,password,role) VALUES (?,?,?)";
        try (Connection con =getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1,user);
            ps.setString(2,password);
            ps.setString(3, "user");
            ps.executeUpdate () ;
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean userExist(String user){
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1,user);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void setUser(String username){
        currentUser = username;
    }

    public static String getCurrentUser(){
        return currentUser;
    }

    public static java.util.List<CartItem> getCartItems(int userId) {
        java.util.List<CartItem> cartItems = new java.util.ArrayList<>();
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
                updateCartQuantity(existingCartItemId, existingQuantity + 1);
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, userId);
                    insertStatement.setInt(2, productId);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}