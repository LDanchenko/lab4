package com.lab3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class App {
    private JTable table1;
    private  JButton button1;
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private JTable table2;
    private JTable table3;
    private JTable table4;


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
//выводим заказы
        table3.setModel(showData(statement, "SELECT o.id, o.cargo, o.town ,o.store, o.representation, o.route,  a.name AS address,c.name AS client, t.name AS transport, s.name AS status  FROM orders o INNER JOIN addresses a ON o.address = a.id INNER JOIN clients c ON o.client = c.id  INNER JOIN transport t ON o.transport = t.id INNER JOIN status s ON o.status = s.id"));
       // table3.setModel(showData(statement, "SELECT a.name AS address FROM orders o INNER JOIN addresses a ON o.address = a.id"));

//закрываем подключение
        con.close();


//обработчик нажатия на кнопку
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Hello!");
            }
        });
    }

    //тут выводим данные в таблицы

    public DefaultTableModel showData(Statement statement, String Query) throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel(0,0);
        ResultSet resultSet = statement.executeQuery(Query);
        // из метаданных узнаем колличество столбцов строк и названия столбцов
        ResultSetMetaData metaData =  resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        //добавляем столбы в модель таблицы
        for (int i = 1; i<=columnCount; i++){
            tableModel.addColumn(metaData.getColumnName(i));
        }
        //добавляем строки в модель таблицы
        String[] row = new String[columnCount];
        while (resultSet.next()){
            for (int i = 1; i<=columnCount; i++){
                row [i-1] = resultSet.getString(i);
            }
            tableModel.addRow(row);
        }
        resultSet.close();
        return tableModel;
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
