package database;

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


}