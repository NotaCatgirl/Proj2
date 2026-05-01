package database;

import model.Product;
import java.util.ArrayList;
import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:ecommerce.db";

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

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);

            // Noemhi added insert sample products
            insertSampleProducts();

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

    // Noemhi added sample products for browsing
    private static void insertSampleProducts() {
        String query = """
                INSERT OR IGNORE INTO products (product_id, name, description, price, stock, image_path)
                VALUES
                (1, 'Pajama Guy', 'Pajama man with a teddy bear', 9.99, 10, '/images/pajamafigure.png'),
                (2, 'Astronaut Guy', 'Astronaut man, who is an astronaut', 24.99, 25, '/images/astronautfigure.png'),
                (3, 'Construction Guy', 'A Construction figure', 29.99, 15, '/images/constructionfigure.png'),
                (4, 'Average Guy', 'Just your average Joe, nothing special about this one', 1000000.99, 12, '/images/averageguyfigure.png'),
                (5, 'Shark Guy', 'A shark man figure', 19.99, 30, '/images/sharkfigure.png')
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}