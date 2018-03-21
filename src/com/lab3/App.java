package com.lab3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class App {
    public JTable table1;
    private  JButton button1;
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private DefaultTableModel tableModel;



    public App() throws SQLException {
//Выводим данные из таблиц
        Connect connect = new Connect();
        Connection con = connect.getConnect(); //подключились
        DefaultTableModel tableModel = new DefaultTableModel(0,0);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
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
        con.close();
        table1.setModel(tableModel);


        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Hello!");
            }
        });
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
