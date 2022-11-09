package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory =  () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);;
    }
    public <P> P commandForResult(String command, DataBaseFunction<PreparedStatement, P> function){
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            return function.apply(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public void commmand(String command, DataBaseConsumer<PreparedStatement> consumer){
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            consumer.accept(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
