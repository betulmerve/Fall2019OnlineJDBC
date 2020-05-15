package com.jdbc.day2;

import com.github.javafaker.Faker;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class DatabaseTests {

    final String DB_URL="jdbc:oracle:thin:@54.224.165.80:1521:xe";
    final String DB_USER="hr";
    final String DB_PASSWORD="hr";

    @Test
    public void getEmployeesData() throws SQLException {
        Connection connection= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        //ResultSet.TYPE_SCROLL_INSENSITIVE - make ResultSet scrollable
        //ResultSet.CONCUR_READ_ONLY - creates ResultSet object that cannot be updated but can be read
        String QUERY="SELECT * FROM employees";
        ResultSet resultSet=statement.executeQuery(QUERY);

        List<Integer> employeeIDs=new ArrayList<>();
        List<String> names=new ArrayList<>();
        List<Map<String,Integer>> employeeIDsMap=new ArrayList<>();
        List<Map<String,String>> namesMap=new ArrayList<>();

        while (resultSet.next()) {
           Map<String,Integer> map=new HashMap<>();
           map.put("employee_id",resultSet.getInt("employee_id"));
           employeeIDsMap.add(map);

           Map<String,String> name=new HashMap<>();
           name.put("full_name",resultSet.getString("first_name")+" "+resultSet.getString("last_name"));
           namesMap.add(name);
            employeeIDs.add(resultSet.getInt("employee_id"));
            names.add(resultSet.getString("first_name")+" "+resultSet.getString("last_name"));

        }
        System.out.println(employeeIDs);
        System.out.println(names);
        System.out.println(employeeIDsMap);
        System.out.println(namesMap);

        //get 5th employee
        String fifthEmployee=namesMap.get(4).get("full_name");
        System.out.println("Fifth employee: "+fifthEmployee);

        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void insertTest() throws SQLException {
        Connection connection= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

        String QUERY_GET_LAST_EMPLOYEE_ID = "SELECT MAX(employee_id) FROM employees"; // returns last employee id
        ResultSet resultSet=statement.executeQuery(QUERY_GET_LAST_EMPLOYEE_ID);//this object contains result set data
        //since employee_id is an integer, we use getInt("column index"), and + 1 to increment
        resultSet.next();//to jump to the first row. Initially, pointer is outside of the table
        int employeeId=resultSet.getInt(1)+1;

        //to check if email exist
        boolean emailExist=false;
        String randomEmail=null;
        Faker faker=new Faker();

        do{

            randomEmail=faker.internet().emailAddress();
            String QUERY_TO_CHECK_IF_EMAIL_EXISTS = "SELECT COUNT(*) FROM employees WHERE email = '"+randomEmail+"'";
            ResultSet resultSet2=statement.executeQuery(QUERY_TO_CHECK_IF_EMAIL_EXISTS);
            resultSet2.next();
            emailExist=resultSet2.getInt(1)>0; //if count is positive, it will be true, means email exists
        }while (emailExist && randomEmail.length()>24);

        String QUERY = "INSERT INTO employees VALUES(" + employeeId + ", '" + faker.name().firstName() + "', '" + faker.name().firstName() + "', '" + randomEmail + "', '508-598-6987', SYSDATE, 'IT_PROG', 15000, 0, NULL, NULL)";
        System.out.println("Query: "+ QUERY);
        ResultSet resultSet3=statement.executeQuery(QUERY);



        resultSet.close();
        statement.close();
        connection.close();

    }
}
