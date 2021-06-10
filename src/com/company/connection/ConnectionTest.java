package com.company.connection;



import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionTest {
    //方式一
    @Test
    public void testConnection() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();
        String URL = "JDBC:mysql://localhost:3306/test";

        Properties Info = new Properties();
        Info.setProperty("user","root");
        Info.setProperty("password","123456");
        Connection conn = driver.connect(URL, Info);
        System.out.println(conn);
    }

    //方式二
    @Test
    public void testConnection2() throws Exception {

        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String URL = "JDBC:mysql://localhost:3306/test";

        Properties Info = new Properties();
        Info.setProperty("user","root");
        Info.setProperty("password","123456");
        Connection conn = driver.connect(URL, Info);
        System.out.println(conn);
    }
    @Test
    //方式三
    public void testConnection3() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        DriverManager.registerDriver(driver);

        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

    //方式四
    public void testConnection4() throws Exception {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        Class.forName("com.mysql.jdbc.Driver");
//      Driver driver = (Driver) clazz.newInstance();

//        DriverManager.registerDriver(driver);

        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

    //方式五
    @Test
    public void testConnection5() throws Exception {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
}
