package com.jdbc.day1;

import java.sql.*;

public class InsertAndDeleteTest {

    static String URL="jdbc:oracle:thin:@54.224.165.80:1521:xe";
    static String username="hr";
    static String password="hr";


    public static void main(String[] args) throws SQLException {

        Connection connection= DriverManager.getConnection(URL,username,password);
        Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String INSERT_QUERY="INSERT INTO employees VALUES (224, 'Betul', 'Cakir', 'bc@cybertek.com', '222-222-5567','02-JUN-20', 'SDET',15000,0,NULL,NULL)";
        String DELETE_QUERY="DELETE FROM employees WHERE employee_id=224";
        ResultSet resultSet=statement.executeQuery(DELETE_QUERY);

        resultSet.close();
        statement.close();
        connection.close();
    }
}
