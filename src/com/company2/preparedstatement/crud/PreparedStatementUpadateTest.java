package com.company2.preparedstatement.crud;

import com.company3.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PreparedStatementUpadateTest {

    @Test
    public void testUpdate()  {
        //1.获取数据库的连接
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
//      2.预编译sql语句，返回PrepareStstement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }

    }



    @Test
    public void testInsert(){

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");
            //2.加载驱动
            Class.forName(driverClass);
            //3获取连接
            conn = DriverManager.getConnection(url, user, password);

//        System.out.println(conn);
            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            ps = conn.prepareStatement(sql);

            //5填充占位符
            ps.setString(1,"nazha");
            ps.setString(2,"nazhe@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1000-01-10");
            ps.setDate(3,new java.sql.Date(date.getTime()));


            //6.执行操作
            ps.execute();
        } catch (ParseException | IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        finally {

            //7.资源的关闭
            try {
                if (ps != null)
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null)
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    //通用的增删改操作
    public void update(String sql,Object ... args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.建立数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回prepareStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }
    @Test
    public void testCommonUpdate(){
//        String sql = "delete from customers where id = ?";
//        update(sql,3);
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql,"DD","2");
    }
}
