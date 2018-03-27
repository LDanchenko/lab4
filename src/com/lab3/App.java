package com.lab3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.UIManager.getString;

public class App {
    private JTable table1;
    private JButton button1;
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private JTable table2;
    private JTable table3;
    private JTable table4;
    private JTable table5;
    private JTable table6;
    private JButton button2;
    private JTable table7;
    private JComboBox comboBox1;
    private JLabel label1;
    private JComboBox comboBox2;
    private JButton button3;
    private JComboBox comboBox3;
    private JButton v_Button1;
    private JComboBox comboBox4;
    private JButton clButton;
    private JButton allButton;
    private JButton alButton;
    private JTextField textField1;
    private JTextField textField2;
    private JButton добавитьButton;


    public App() throws SQLException {
//Выводим данные из таблиц
        Connect connect = new Connect();
        Connection con = connect.getConnect(); //подключились
        Statement statement = con.createStatement();
//выводим клиентов
        table1.setModel(showData(statement, "SELECT * FROM clients"));
        //вывод таблицы адресов
        table2.setModel(showData(statement, "SELECT * FROM addresses"));
//выводим транспорт
        table4.setModel(showData(statement, "SELECT * FROM transport"));
//выводим товары
        table5.setModel(showData(statement, "SELECT * FROM goods"));
        //выводим магазины
        table6.setModel(showData(statement, "SELECT * FROM stores"));
        //выводим колличество товаров в магазине


        table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id "));
//выводим заказы
        table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id"));
        // table3.setModel(showData(statement, "SELECT a.name AS address FROM orders o INNER JOIN addresses a ON o.address = a.id"));

//закрываем подключение



        comboBoxFill(statement, "SELECT  s.name AS status FROM orders o  INNER JOIN status s ON o.status = s.id", comboBox3);
        comboBoxFill(statement, "SELECT  s.name AS store FROM count c  INNER JOIN stores s ON c.store = s.id", comboBox2);
        comboBoxFill(statement, "SELECT  g.name AS goods FROM count c  INNER JOIN goods g ON c.goods = g.id", comboBox1);
        comboBoxFill(statement, "SELECT  c.name AS client FROM orders o  INNER JOIN clients c ON o.client = c.id", comboBox4);

        //кнопка на вкладке склад - посмотреть где товары и
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String good = comboBox1.getSelectedItem().toString();

                String parametr = comboBox1.getSelectedItem().toString();
                try {
                    String s = null;
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM goods WHERE goods.name = '"+parametr +"'");
                    if(resultSet.next()) {
                       s = resultSet.getString(1);
                    }
                    //JOptionPane.showMessageDialog(null,  resultSet.getString(1));
                     table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores " +
                            "s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id WHERE c.goods = "+ s + " "));
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        //когда закрыли приложение закрыли и коннект!!
      //  con.close();
//кнопка выберите склад
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String good = comboBox2.getSelectedItem().toString();

                String parametr = comboBox2.getSelectedItem().toString();
                try {
                    String s = null;
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM stores WHERE stores.name = '"+parametr +"'");
                    if(resultSet.next()) {
                        s = resultSet.getString(1);
                      //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores " +
                            "s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id WHERE c.store = "+ s + " "));
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        //кнопка фильтр по статусу заказа
        v_Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String good = comboBox3.getSelectedItem().toString();

                String parametr = comboBox3.getSelectedItem().toString();
                try {
                    String s = null;
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM status WHERE status.name = '"+parametr +"'");
                    if(resultSet.next()) {
                        s = resultSet.getString(1);
                        //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                                    " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id WHERE o.status = " + s +" "));
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        //вывести все склад
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id "));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//выводим заказы
            }
        });

        //по статусу заказа
        alButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                            " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        //вывести по клиентам
        clButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String good = comboBox4.getSelectedItem().toString();

                String parametr = comboBox4.getSelectedItem().toString();
                try {
                    String s = null;
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM clients WHERE clients.name = '"+parametr +"'");
                    if(resultSet.next()) {
                        s = resultSet.getString(1);
                        //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                            " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id WHERE o.client = " + s +" "));
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        //кнопка добавить клиента
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text =  textField1.getText();
                addFiled(text, "INSERT INTO clients VALUES (null, '" + text + " ')", statement, table1, "SELECT * FROM clients");
            }
        });
    }

    //тут выводим данные в таблицы

    public DefaultTableModel showData(Statement statement, String Query) throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel(0, 0);
        ResultSet resultSet = statement.executeQuery(Query);
        // из метаданных узнаем колличество столбцов строк и названия столбцов
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        //добавляем столбы в модель таблицы
        for (int i = 1; i <= columnCount; i++) {
            tableModel.addColumn(metaData.getColumnName(i));
        }
        //добавляем строки в модель таблицы
        String[] row = new String[columnCount];
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = resultSet.getString(i);
            }
            tableModel.addRow(row);
        }
        resultSet.close();
        return tableModel;
    }

    //для заполнения комбобокс
    public void comboBoxFill(Statement statement, String query, JComboBox comboBox1) throws SQLException {
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
//из резалт сет значения передаем в список
        ArrayList<String> al = new ArrayList<String>();
        while (resultSet.next()) {
            ArrayList<String> record = new ArrayList<String>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String value = resultSet.getString(i);
                al.add(value);

            }

        }
//удаляем повторы
        List<String> deduped = al.stream().distinct().collect(Collectors.toList());
        for (int i = 1; i<=deduped.size(); i++){
            comboBox1.addItem(deduped.get(i-1));
        }
    }

    public void addFiled(String text,  String query, Statement statement, JTable jTable, String selectQuery) {

        if (!text.equals("")) {
            try {
                statement.executeUpdate(query);
                int rowCount = statement.getUpdateCount();
                if (rowCount > 0)
                {
                    textField1.setText("");
                    jTable.setModel(showData(statement, selectQuery));
                }
                if (rowCount <= 0)
                {
                    System.out.println("Что то пошло не так");

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else {
            JOptionPane.showMessageDialog(null, "Заполните поле");
        }

    }



    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("App"); //работа с формой
        frame.setContentPane(new App().panelMain); // отобразить контент элемента панель
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        App app = new App();


    }



}
