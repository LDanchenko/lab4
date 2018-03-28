package com.lab3;

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
                addFiled(text, textField1,"INSERT INTO clients VALUES (null, '" + text + " ')", statement, table1, "SELECT * FROM clients");
            }
        });
        addGoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text =  textField2.getText();
                addFiled(text, textField2,"INSERT INTO goods VALUES (null, '" + text + " ')", statement, table5, "SELECT * FROM goods");
            }
        });
        addAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text =  textField3.getText();
                addFiled(text, textField3,"INSERT INTO addresses VALUES (null, '" + text + " ')", statement, table2, "SELECT * FROM addresses");
            }
        });
        addTransportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text =  textField4.getText();
                addFiled(text, textField4,"INSERT INTO transport VALUES (null, '" + text + " ')", statement, table4, "SELECT * FROM transport");
            }
        });
        addStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text =  textField5.getText();
                addFiled(text, textField5,"INSERT INTO stores VALUES (null, '" + text + " ')", statement, table6, "SELECT * FROM stores");
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
                           " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id   ") ;

                   //добавили строку
                    model.addRow(new Object[]{ "","","","","","", "", "", "", ""});


                    table3.setModel(model);
//в строке нужны выпадающие поля
                    JComboBox comboBoxAddress = new JComboBox();
                    comboBoxFill(statement, "SELECT addresses.name  FROM addresses" ,comboBoxAddress);
                    table3.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBoxAddress));


                    JComboBox comboBoxGoods = new JComboBox();
                    comboBoxFill(statement, "SELECT goods.name FROM goods " ,comboBoxGoods);
                    table3.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBoxGoods));

                    JComboBox comboBoxClient = new JComboBox();
                    comboBoxFill(statement, "SELECT clients.name FROM clients" ,comboBoxClient);
                    table3.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBoxClient));

                    JComboBox comboBoxTransport = new JComboBox();
                    comboBoxFill(statement, "SELECT transport.name FROM transport" ,comboBoxTransport);
                    table3.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboBoxTransport));

                    JComboBox comboBoxStore = new JComboBox();
                    comboBoxFill(statement, "SELECT stores.name FROM stores" ,comboBoxStore);
                    table3.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(comboBoxStore));

                    JComboBox comboBoxStatus = new JComboBox();
                    comboBoxFill(statement, "SELECT status.name FROM status" ,comboBoxStatus);
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
                List <String> ArrayValuesFromTable = new ArrayList<>();
               // String[][] values = new String[lastRow][lastColumn];
                for (int i=0; i<table3.getColumnCount(); i++){
                    ArrayValuesFromTable.add(table3.getValueAt(lastRow-1, i).toString());
                }

                if ((!(ArrayValuesFromTable.get(0)).isEmpty())|| (!(ArrayValuesFromTable.get(1)).isEmpty())||(!(ArrayValuesFromTable.get(2)).isEmpty())||(!(ArrayValuesFromTable.get(3)).isEmpty())||(!(ArrayValuesFromTable.get(4)).isEmpty())||(!(ArrayValuesFromTable.get(5)).isEmpty())||(!(ArrayValuesFromTable.get(6)).isEmpty())||(!(ArrayValuesFromTable.get(7)).isEmpty())||(!(ArrayValuesFromTable.get(8)).isEmpty())||(!(ArrayValuesFromTable.get(9)).isEmpty())){


                    //находим  id в его таблицы по имени
                    int address = findId(statement, "SELECT addresses.id from addresses where addresses.name = '" + ArrayValuesFromTable.get(4).toString() + "'");
                    System.out.println("адрес " + address);

                    int good = findId(statement, "SELECT goods.id from goods where goods.name = '" + ArrayValuesFromTable.get(5).toString() + "'");
                    System.out.println("товар " + good);

                    int client = findId(statement, "SELECT clients.id from clients where clients.name = '" + ArrayValuesFromTable.get(6).toString() + "'");
                    System.out.println("клиент " + client);

                    int transport = findId(statement, "SELECT transport.id from transport where transport.name = '" + ArrayValuesFromTable.get(7).toString() + "'");
                    System.out.println("транспорт " + transport);

                    int stores = findId(statement, "SELECT stores.id from stores where stores.name = '" + ArrayValuesFromTable.get(8).toString() + "'");
                    System.out.println("магазин" + stores);

                    int status = findId(statement, "SELECT status.id from status where status.name = '" + ArrayValuesFromTable.get(9).toString() + "'");
                    System.out.println("статус" + status);


                    String query = "INSERT INTO orders VALUES (null, '" + good + "', '" + ArrayValuesFromTable.get(1).toString()+"' , '" + address+"', " + "'" + stores+ "', '" +ArrayValuesFromTable.get(2).toString()+"', '"
                   + client +  "',' " + ArrayValuesFromTable.get(3).toString()+ "', '" + transport + "', '" + status + "')" ;
                    try {
                        statement.executeUpdate(query);
                        int rowCount = statement.getUpdateCount();
                        if (rowCount > 0)
                        {
                            DefaultTableModel model = new DefaultTableModel();
                            model = showData(statement, "SELECT o.id, o.town,  o.representation, o.route,  a.name AS address, g.name AS goods, c.name AS client, t.name AS transport, st.name AS store, s.name AS status  " +
                                    "FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN goods g ON o.goods = g.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id  " +
                                    " INNER JOIN stores st ON o.store = st.id INNER JOIN status s ON o.status = s.id   ") ;

                            //добавили строку
                            model.addRow(new Object[]{ "","","","","","", "", "", "", ""});


                            table3.setModel(model);
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
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                }

            }
        });
    }


    //метод поиска id в таблице по имене
    public int findId(Statement statement, String query){
        String result = null;
        try {
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
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
                if (column!=0){
                    return true;
                }
                else return false;
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
        for (int i = 1; i<=deduped.size(); i++){
            comboBox1.addItem(deduped.get(i-1));
        }
    }

    public void addFiled(String text, JTextField textField, String query, Statement statement, JTable jTable, String selectQuery) {

        if (!text.equals("")) {
            try {
                statement.executeUpdate(query);
                int rowCount = statement.getUpdateCount();
                if (rowCount > 0)
                {
                    textField.setText("");
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
