package com.lab3;

import com.sun.org.apache.bcel.internal.generic.Select;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.UIManager.getString;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.removeAuxiliaryLookAndFeel;

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
    private JButton addGoodButton;
    private JTextField textField3;
    private JButton addAddressButton;
    private JTextField textField4;
    private JButton addTransportButton;
    private JTextField textField5;
    private JButton addStoreButton;
    private JButton addRowButton;
    private JButton addOrder;
    private JButton addRowSkladButton;
    private JButton saveRowButton;
    private JComboBox comboBox5;
    private JButton deleteclientButton;
    private JComboBox comboBox6;
    private JButton deletegoodButton;
    private JComboBox comboBox7;
    private JButton deleteAdressButton;
    private JComboBox comboBox8;
    private JButton deleteTransportButton;
    private JComboBox comboBox9;
    private JButton deleteOrderButton;
    private JComboBox comboBox10;
    private JButton deleteStoreButton;
    private JComboBox comboBox11;
    private JButton seleteSkladButton;


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
        comboBoxFill(statement, "SELECT id FROM clients", comboBox5);
        comboBoxFill(statement, "SELECT id FROM goods", comboBox6);
        comboBoxFill(statement, "SELECT id FROM addresses", comboBox7);
        comboBoxFill(statement, "SELECT id FROM transport", comboBox8);
        comboBoxFill(statement, "SELECT id FROM orders", comboBox9);
        comboBoxFill(statement, "SELECT id FROM stores", comboBox10);
        comboBoxFill(statement, "SELECT id FROM count", comboBox11 );



        //кнопка на вкладке склад - посмотреть где товары и
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String good = comboBox1.getSelectedItem().toString();

                String parametr = comboBox1.getSelectedItem().toString();
                try {
                    String s = null;
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM goods WHERE goods.name = '" + parametr + "'");
                    if (resultSet.next()) {
                        s = resultSet.getString(1);
                    }
                    //JOptionPane.showMessageDialog(null,  resultSet.getString(1));
                    table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores " +
                            "s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id WHERE c.goods = " + s + " "));
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
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM stores WHERE stores.name = '" + parametr + "'");
                    if (resultSet.next()) {
                        s = resultSet.getString(1);
                        //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table7.setModel(showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores " +
                            "s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id WHERE c.store = " + s + " "));
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
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM status WHERE status.name = '" + parametr + "'");
                    if (resultSet.next()) {
                        s = resultSet.getString(1);
                        //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                            " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id WHERE o.status = " + s + " "));
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
                    ResultSet resultSet = statement.executeQuery("SELECT id FROM clients WHERE clients.name = '" + parametr + "'");
                    if (resultSet.next()) {
                        s = resultSet.getString(1);
                        //  JOptionPane.showMessageDialog(null, resultSet.getString(1));
                    }
                    table3.setModel(showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                            " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id WHERE o.client = " + s + " "));
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
                String text = textField1.getText();
                addFiled(text, textField1, "INSERT INTO clients VALUES (null, '" + text + " ')", statement, table1, "SELECT * FROM clients", comboBox5);
            }
        });
        addGoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = textField2.getText();
                addFiled(text, textField2, "INSERT INTO goods VALUES (null, '" + text + " ')", statement, table5, "SELECT * FROM goods", comboBox6);
            }
        });
        addAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = textField3.getText();
                addFiled(text, textField3, "INSERT INTO addresses VALUES (null, '" + text + " ')", statement, table2, "SELECT * FROM addresses", comboBox7);
            }
        });
        addTransportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = textField4.getText();
                addFiled(text, textField4, "INSERT INTO transport VALUES (null, '" + text + " ')", statement, table4, "SELECT * FROM transport", comboBox8);
            }
        });
        addStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = textField5.getText();
                addFiled(text, textField5, "INSERT INTO stores VALUES (null, '" + text + " ')", statement, table6, "SELECT * FROM stores", comboBox10);
            }
        });
        //кнопка добавить заказ
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //нужно добавить строку к тейбл модел
                try {

                    DefaultTableModel model = new DefaultTableModel();
                    model = showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                            "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                            " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id   ");

                    //добавили строку
                    model.addRow(new Object[]{"", "", "", "", "", "", "", "", "", ""});


                    table3.setModel(model);
//в строке нужны выпадающие поля
                    JComboBox comboBoxAddress = new JComboBox();
                    comboBoxFill(statement, "SELECT addresses.name  FROM addresses", comboBoxAddress);
                    table3.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBoxAddress));


                    JComboBox comboBoxGoods = new JComboBox();
                    comboBoxFill(statement, "SELECT goods.name FROM goods ", comboBoxGoods);
                    table3.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBoxGoods));

                    JComboBox comboBoxClient = new JComboBox();
                    comboBoxFill(statement, "SELECT clients.name FROM clients", comboBoxClient);
                    table3.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBoxClient));

                    JComboBox comboBoxTransport = new JComboBox();
                    comboBoxFill(statement, "SELECT transport.name FROM transport", comboBoxTransport);
                    table3.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboBoxTransport));

                    JComboBox comboBoxStore = new JComboBox();
                    comboBoxFill(statement, "SELECT stores.name FROM stores", comboBoxStore);
                    table3.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(comboBoxStore));

                    JComboBox comboBoxStatus = new JComboBox();
                    comboBoxFill(statement, "SELECT status.name FROM status", comboBoxStatus);
                    table3.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(comboBoxStatus));

                    //Set up tool tips for the sport cells.
                    //    DefaultTableCellRenderer renderer =
                    //          new DefaultTableCellRenderer();
                    //table3.getColumnModel().getColumn(4).setCellRenderer(renderer);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        //когда в строку записали данные добавляем в базу
        addOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //System.out.println(table3.getColumnCount());
                //System.out.println(table3.getRowCount());
                int lastColumn = table3.getColumnCount();
                int lastRow = table3.getRowCount();
                List<String> ArrayValuesFromTable = new ArrayList<>();
                // String[][] values = new String[lastRow][lastColumn];
                for (int i = 0; i < table3.getColumnCount(); i++) {
                    ArrayValuesFromTable.add(table3.getValueAt(lastRow - 1, i).toString());
                }

                if ((!(ArrayValuesFromTable.get(0)).isEmpty()) || (!(ArrayValuesFromTable.get(1)).isEmpty()) || (!(ArrayValuesFromTable.get(2)).isEmpty()) || (!(ArrayValuesFromTable.get(3)).isEmpty()) || (!(ArrayValuesFromTable.get(4)).isEmpty()) || (!(ArrayValuesFromTable.get(5)).isEmpty()) || (!(ArrayValuesFromTable.get(6)).isEmpty()) || (!(ArrayValuesFromTable.get(7)).isEmpty()) || (!(ArrayValuesFromTable.get(8)).isEmpty()) || (!(ArrayValuesFromTable.get(9)).isEmpty())) {


                    //находим  id в его таблицы по имени
                    int address = findId(statement, "SELECT addresses.id from addresses where addresses.name = '" + ArrayValuesFromTable.get(4).toString() + "'");

                    int good = findId(statement, "SELECT goods.id from goods where goods.name = '" + ArrayValuesFromTable.get(5).toString() + "'");

                    int client = findId(statement, "SELECT clients.id from clients where clients.name = '" + ArrayValuesFromTable.get(6).toString() + "'");

                    int transport = findId(statement, "SELECT transport.id from transport where transport.name = '" + ArrayValuesFromTable.get(7).toString() + "'");

                    int stores = findId(statement, "SELECT stores.id from stores where stores.name = '" + ArrayValuesFromTable.get(8).toString() + "'");

                    int status = findId(statement, "SELECT status.id from status where status.name = '" + ArrayValuesFromTable.get(9).toString() + "'");


                    String query = "INSERT INTO orders VALUES (null, '" + good + "', '" + ArrayValuesFromTable.get(1).toString() + "' , '" + address + "', " + "'" + stores + "', '" + ArrayValuesFromTable.get(2).toString() + "', '"
                            + client + "',' " + ArrayValuesFromTable.get(3).toString() + "', '" + transport + "', '" + status + "')";
                    try {
                        statement.executeUpdate(query);
                        int rowCount = statement.getUpdateCount();
                        if (rowCount > 0) {
                            DefaultTableModel model = new DefaultTableModel();
                            model = showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                                    "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                                    " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id   ");

                            //добавили строку
                            model.addRow(new Object[]{"", "", "", "", "", "", "", "", "", ""});


                            table3.setModel(model);
                            //добавить в комбобокс для удаления новый эелемент

                            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM orders;");
                            ResultSetMetaData metaData = resultSet.getMetaData();
        if (resultSet.next()) {
                    comboBox9.addItem(resultSet.getString(1));

        }
                        }
                        if (rowCount <= 0) {
                            System.out.println("Что то пошло не так");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                } else {
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                }

            }
        });
        addRowSkladButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {

                    DefaultTableModel model = new DefaultTableModel();
                    model = showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id");

                    //добавили строку
                    model.addRow(new Object[]{"", "", "", "", "", "", "", "", "", ""});


                    table7.setModel(model);
//в строке нужны выпадающие поля
                    JComboBox comboBoxAddress = new JComboBox();
                    comboBoxFill(statement, "SELECT stores.name  FROM stores", comboBoxAddress);
                    table7.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxAddress));


                    JComboBox comboBoxGoods = new JComboBox();
                    comboBoxFill(statement, "SELECT goods.name FROM goods ", comboBoxGoods);
                    table7.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBoxGoods));


                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        });
        saveRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int lastColumn = table7.getColumnCount();
                int lastRow = table7.getRowCount();
                List<String> ArrayValuesFromTable = new ArrayList<>();
                // String[][] values = new String[lastRow][lastColumn];
                for (int i = 0; i < table7.getColumnCount(); i++) {
                    ArrayValuesFromTable.add(table7.getValueAt(lastRow - 1, i).toString());
                }

                String count = ArrayValuesFromTable.get(1).toString();


                if ((!(ArrayValuesFromTable.get(0)).isEmpty()) || (!(ArrayValuesFromTable.get(2)).isEmpty()) || (!(ArrayValuesFromTable.get(3)).isEmpty()) || !ArrayValuesFromTable.get(1).isEmpty()) {

                    if (isNumeric(count) == false) {
                        JOptionPane.showMessageDialog(null, "Колличество должно быть числом");

                    } else {
                        int Count = Integer.parseInt(count);


                        //находим  id в его таблицы по имени
                        int store = findId(statement, "SELECT stores.id from stores where stores.name = '" + ArrayValuesFromTable.get(2).toString() + "'");

                        int good = findId(statement, "SELECT goods.id from goods where goods.name = '" + ArrayValuesFromTable.get(3).toString() + "'");


                        String query = "INSERT INTO count VALUES (null, '" + store + "', '" + good + "','" + Count + "')";
                        try {
                            statement.executeUpdate(query);
                            int rowCount = statement.getUpdateCount();
                            if (rowCount > 0) {
                                DefaultTableModel model = new DefaultTableModel();
                                model = showData(statement, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id");

                                //добавили строку
                                model.addRow(new Object[]{"", "", "", ""});


                                table7.setModel(model);

                                table3.setModel(model);
                                //добавить в комбобокс для удаления новый эелемент

                                ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM count;");
                                ResultSetMetaData metaData = resultSet.getMetaData();
                                if (resultSet.next()) {
                                    comboBox11.addItem(resultSet.getString(1));
                                }


                            }
                            if (rowCount <= 0) {
                                System.out.println("Что то пошло не так");

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                }


            }
        });
        deleteclientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                    Object o = comboBox5.getSelectedItem();
                  deleteRow(comboBox5, "clients", statement, table1, "SELECT * FROM clients" );
                comboBox5.removeItem(o);


            }

        });
        deletegoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
             Object o = comboBox6.getSelectedItem();
                deleteRow(comboBox6, "goods", statement, table5, "SELECT * FROM goods" );
                comboBox6.removeItem(o);

            }
        });
        deleteAdressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object o = comboBox7.getSelectedItem();
                deleteRow(comboBox7, "addresses", statement, table2, "SELECT * FROM addresses" );
                comboBox7.removeItem(o);
            }
        });
        deleteTransportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object o = comboBox8.getSelectedItem();
                deleteRow(comboBox8, "transport", statement, table4, "SELECT * FROM transport" );
                comboBox8.removeItem(o);
            }
        });
        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object o = comboBox9.getSelectedItem();
                deleteRow(comboBox9, "orders", statement, table3, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                        "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                        " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id" );
                comboBox9.removeItem(o);
            }
        });
        deleteStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    Object o = comboBox10.getSelectedItem();
                    deleteRow(comboBox10, "stores", statement, table6, "SELECT * FROM stores" );
                    comboBox10.removeItem(o);

            }
        });
        seleteSkladButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object o = comboBox11.getSelectedItem();
                deleteRow(comboBox11, "count", statement, table7, "SELECT c.id, c.count, s.name AS store, g.name AS goods FROM count c INNER JOIN stores s ON c.store = s.id INNER JOIN goods g ON c.goods = g.id" );
                comboBox11.removeItem(o);
            }
        });
    }

    public  void deleteRow(JComboBox comboBox, String table ,Statement statement, JTable table1, String model) {
        int id = Integer.parseInt(comboBox.getSelectedItem().toString());
        try {
            statement.executeUpdate("DELETE FROM " + table+ " WHERE  " + table +".id = '" + id +"'");
            int rowCount = statement.getUpdateCount();
            if (rowCount > 0) {
                table1.setModel(showData(statement, model));
            }
            if (rowCount <= 0) {
                System.out.println("Что то пошло не так");



            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static boolean isNumeric(String str) {
        try {
            double d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    //метод поиска id в таблице по имене
    public int findId(Statement statement, String query) {
        String result = null;
        try {
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                result = resultSet.getString(1).toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        int id = Integer.parseInt(result);
        return id;
    }

    //тут выводим данные в таблицы

    public DefaultTableModel showData(Statement statement, String Query) throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel() {
            //чтобы нельзя было редактировать первую колонку с id
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != 0) {
                    return true;
                } else return false;
            }
        };
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
        for (int i = 1; i <= deduped.size(); i++) {
            comboBox1.addItem(deduped.get(i - 1));
        }
    }

    public void addFiled(String text, JTextField textField, String query, Statement statement, JTable jTable, String selectQuery, JComboBox jComboBox) {

        if (!text.equals("")) {
            try {
                statement.executeUpdate(query);
                int rowCount = statement.getUpdateCount();
                if (rowCount > 0) {
                    textField.setText("");
                    jTable.setModel(showData(statement, selectQuery));
                    jComboBox.addItem(jTable.getValueAt(jTable.getRowCount()-1, 0 ));
                }
                if (rowCount <= 0) {
                    System.out.println("Что то пошло не так");

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
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
