package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), lastName VARCHAR(255), age TINYINT)";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String DISABLE_FOREIGN_KEY_CHECKS_SQL = "SET FOREIGN_KEY_CHECKS = 0";
    private static final String ENABLE_FOREIGN_KEY_CHECKS_SQL = "SET FOREIGN_KEY_CHECKS = 1";
    private static final String CLEAN_TABLE_SQL = "TRUNCATE TABLE users";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DISABLE_FOREIGN_KEY_CHECKS_SQL);
            statement.executeUpdate(DROP_TABLE_SQL);
            statement.executeUpdate(ENABLE_FOREIGN_KEY_CHECKS_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_SQL)) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");
                users.add(new User(id, name, lastName, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
