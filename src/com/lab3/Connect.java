package com.lab3;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import java.sql.*;


public class Connect {
    private final String USERNAME = "root";
    private final String PASSWORD = "llddvvcc5678";
    private final String URL = "jdbc:mysql://localhost:3306/logistic?useSSL=false";

    public Connection getConnect() {
        Connection connection = null; //для соединения с БД
        Driver driver = null;
        try { //еслинету драйвера
            driver = new FabricMySQLDriver();
        } catch (SQLException ex) {
            System.out.println("Ошибка при создании драйвера!");
            System.exit(0);
        }

        try { //регистрируем драйвер
            DriverManager.registerDriver(driver);
        } catch (SQLException ex) {
            System.out.println("Не удалось зарегистрировать драйвер!");
            System.exit(0);
        }

        try {// пробуем соед
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            DatabaseMetaData metaData =  connection.getMetaData(); //проверяем, есть ли таблицы, если нет, создать
            Statement statement = connection.createStatement();


        } catch (SQLException ex) {
            System.out.println("Не удалось создать соединение :(");
            System.exit(0);
        }

        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
}

/*    public void existTables () {
        //проверить!!

        ResultSet rsTableTransport = metaData.getTables(null, "logistic", "transport", null);
        if (!rsTableTransport.next()) { //если нет такой таблицы
            String creatTransport = "CREATE TABLE addresses ( id int(11) NOT NULL, name text NOT NULL, PRIMARY KEY (id)) ";
            try {
                statement.executeUpdate(creatTransport);
            } catch (SQLException e) {
                System.out.println("не удалось создать таблицу");
            }
        }


        ResultSet rsTableAddress = metaData.getTables(null, "logistic", "addresses", null);
        if (!rsTableAddress.next()) { //если нет такой таблицы
            String creatrAddresses = "CREATE TABLE addresses ( id int(11) NOT NULL, name text NOT NULL, PRIMARY KEY (id)) ";
            try {
                statement.executeUpdate(creatrAddresses);
            } catch (SQLException e) {
                System.out.println("не удалось создать таблицу");
            }
        }

        ResultSet rsTableClients = metaData.getTables(null, "logistic", "clients", null);
        if (!rsTableClients.next()) { //если нет такой таблицы
            String creatClients = "CREATE TABLE addresses ( id int(11) NOT NULL, name text NOT NULL, PRIMARY KEY (id)) ";
            try {
                statement.executeUpdate(creatClients);
            } catch (SQLException e) {
                System.out.println("не удалось создать таблицу");
            }
        }

        ResultSet rsTableOrders = metaData.getTables(null, "logistic", "orders", null);
        if (!rsTableOrders.next())

        { //если нет такой таблицы
            String creatOrders = "CREATE TABLE orders (id int(11) NOT NULL, cargo text NOT NULL,town text NOT NULL,address int(11) NOT NULL,store text NOT NULL,representation text NOT NULL,client int(11) NOT NULL,route text NOT NULL,transport int(11) NOT NULL , PRIMARY KEY (id))";
            try {
                statement.executeUpdate(creatOrders);
            } catch (SQLException e) {
                System.out.println("не удалось создать таблицу");
            }
        }

    }
  */
}
