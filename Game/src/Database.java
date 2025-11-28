import java.sql.*;

/**
 * Database: 负责打开/关闭 Connection 并提供基础获取 Connection 的方法。
 * 注意：URL / username / password 与原代码保持一致（如需修改请直接编辑这里）。
 */
public class Database {
    private Connection connection;

    // 与原始代码一致的连接信息（请按需修改）
    private final String url = "jdbc:mysql://localhost:3306/gamedate?serverTimezone=Asia/Shanghai";
    private final String username = "root";
    private final String password = "050093";

    public void connectDatabase() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) connectDatabase();
        return connection;
    }

    public void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }
}
