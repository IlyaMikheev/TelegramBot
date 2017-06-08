package com.turlygazhy.connection_pool;

import com.turlygazhy.entity.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class is for working with connections
 *
 * @author Turlygazy
 */
public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    //data for settings of connection
    private static final int CONNECTION_NUMBER = 10;
    private static final String JDBC_URL = "jdbc:h2:~/pizza";
    private static final String DB_LOGIN = "user";
    private static final String DB_PASSWORD = "user";

    private static BlockingQueue<Connection> connections = new LinkedBlockingQueue<>();

    static {
        try {
            //loading of class
            Class.forName("org.h2.Driver");
            logger.info("�������� \"org.h2.Driver\" ���������.");
        } catch (ClassNotFoundException e) {
            logger.info("����� \"org.h2.Driver\" �� ������.");
            throw new ConnectionPoolException(e);
        }
        Connection connection;
        try {
            //getting connection from DriverManager and filling BlockingQueue<Connection> connections
            for (int i = 0; i < CONNECTION_NUMBER; i++) {
                connection = DriverManager.getConnection(JDBC_URL, DB_LOGIN, DB_PASSWORD);
                connections.add(connection);
                logger.debug(connection + "added.");
            }
        } catch (SQLException e) {
            logger.info("SQLException.");
            throw new ConnectionPoolException(e);
        }
        System.out.println("Этот кусок кода был выполнен");

    }

    private ConnectionPool() {
    }

    static Connection connection;

    public synchronized static Connection getConnection() throws ConnectionPoolException {
        try {
            //try get connection with 5 seconds waiting //��������� ������������
            connection = connections.poll(5, TimeUnit.SECONDS);
            logger.debug(connection + " �������.");
        } catch (InterruptedException e) {
            logger.info("InterruptedException.");
            throw new ConnectionPoolException(e);
        }

        //the case when there is no free connection
        if (connection == null) throw new ConnectionPoolException("Connection is null");

        initBase();

        return connection;
    }

    /**
     * This method returns connection to BlockingQueue<Connection> connections
     *
     * @param connection for releasing
     * @throws ConnectionPoolException
     */
    public synchronized static void releaseConnection(Connection connection) throws ConnectionPoolException {
        try {
            //try to put connection with 5 seconds waiting
            connections.offer(connection, 5, TimeUnit.SECONDS);
            logger.debug(connection + " released.");
        } catch (InterruptedException e) {
            logger.info("InterruptedException.");
            throw new ConnectionPoolException("cannot put connection in pool");
        }
    }

    public static void initBase()  {
        PreparedStatement ps = null;


        try {
            ps = connection.prepareStatement("CREATE TABLE if not exists PUBLIC.BUTTON(" +
                    "id INT," +
                    "text TEXT," +
                    "id_command INT," +
                    "any_text TEXT," +
                    "any_boolean boolean);");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        int idParameterIndex = 1;
        int commandTypeIdColumnIndex = 2;
        int messageToUserColumnIndex = 3;
        int messageIdForChangingColumnIndex = 4;
        int listNameColumnIndex = 5;

        try {
            ps = connection.prepareStatement("CREATE TABLE if not exists PUBLIC.COMMAND(" +
                    "id BIGINT," +
                    "commandTypeId BIGINT," +
                    "messageToUser BIGINT," +
                    "messageIdForChanging BIGINT," +
                    "listName BIGINT);");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement(
                    "INSERT INTO PUBLIC.COMMAND(id, commandTypeId, messageToUser, messageIdForChanging, listName)" +
                            "VALUES(?, ?, ?, ?, ?);");
            ps.setLong(1,28L);
            ps.setLong(2,28L);
            ps.setLong(3,28L);
            ps.setLong(4,28L);
            ps.setLong(5,28L);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement(
                    "INSERT INTO PUBLIC.BUTTON(id, text, id_command, any_text, any_boolean) VALUES(?,?,?,?,?);");

            ps.setInt(1,22);
            ps.setString(2,"any_text_1");
            ps.setInt(3,10);
            ps.setString(4,"any_text2");
            ps.setBoolean(5, false);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement(
                    "INSERT INTO PUBLIC.BUTTON(id, text, id_command, any_text, any_boolean) VALUES(?,?,?,?,?);");

            ps.setInt(1,23);
            ps.setString(2,"any_text_1");
            ps.setInt(3,10);
            ps.setString(4,"any_text2");
            ps.setBoolean(5, false);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement(
                    "INSERT INTO PUBLIC.BUTTON(id, text, id_command, any_text, any_boolean) VALUES(?,?,?,?,?);");

            ps.setInt(1,28);
            ps.setString(2,"Ввод персональных данных");
            ps.setInt(3,28);
            ps.setString(4,"any_text2");
            ps.setBoolean(5, false);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {
            ps = connection.prepareStatement("DROP TABLE MESSAGE;");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("CREATE TABLE if not exists MESSAGE(" +
                    "id BIGINT," +
                    "text TEXT," +
                    "photo TEXT," +
                    "keyboard BIGINT);");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String message7 = "Привет, я робот. Я могу выполнить следующие команды: \n" +
                "1. Ввод персональных данных";

        try {
            ps = connection.prepareStatement("INSERT INTO MESSAGE(id, text, photo, keyboard) VALUES(?, ?, ?, ?);");
            ps.setLong(1,7L);
            ps.setString(2,message7);
            ps.setString(3,"photo_id");
            ps.setLong(4,1L);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

/*
        try {
            ps = connection.prepareStatement("INSERT INTO MESSAGE(id, text, photo, keyboard) VALUES(?, ?, ?, ?);");
            ps.setLong(1,101L);
            ps.setString(2,"Ваша фамилия?");
            ps.setString(3,"photo_id");
            ps.setLong(4,1L);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("INSERT INTO MESSAGE(id, text, photo, keyboard) VALUES(?, ?, ?, ?);");
            ps.setLong(1,102L);
            ps.setString(2,"Ваше имя?");
            ps.setString(3,"photo_id");
            ps.setLong(4,1L);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("INSERT INTO MESSAGE(id, text, photo, keyboard) VALUES(?, ?, ?, ?);");
            ps.setLong(1,103L);
            ps.setString(2,"Ваше отчество?");
            ps.setString(3,"photo_id");
            ps.setLong(4,1L);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("INSERT INTO MESSAGE(id, text, photo, keyboard) VALUES(?, ?, ?, ?);");
            ps.setLong(1,104L);
            ps.setString(2,"Ваше номер телефона?");
            ps.setString(3,"photo_id");
            ps.setLong(4,1L);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("CREATE TABLE if not exists KEYBOARD(" +
                    "id BIGINT," +
                    "any_text TEXT," +
                    "any_bool BOOLEAN);");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = connection.prepareStatement("INSERT INTO KEYBOARD(id, any_text, any_bool) VALUES(?, ?, ?);");
            ps.setLong(1,1L);
            ps.setString(2,"keyboard");
            ps.setBoolean(3,false);
            ps.execute();

            ps = connection.prepareStatement("INSERT INTO KEYBOARD(id, any_text, any_bool) VALUES(?, ?, ?);");
            ps.setLong(1,2L);
            ps.setString(2,"keyboard");
            ps.setBoolean(3,false);
            ps.execute();

            ps = connection.prepareStatement("INSERT INTO KEYBOARD(id, any_text, any_bool) VALUES(?, ?, ?);");
            ps.setLong(1,3L);
            ps.setString(2,"keyboard");
            ps.setBoolean(3,false);
            ps.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
*/




    }
}