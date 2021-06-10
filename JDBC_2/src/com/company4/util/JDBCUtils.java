package com.company4.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class JDBCUtils {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("hellc3p0");
    public static Connection testGetConnection1() throws Exception {
        Connection conn = cpds.getConnection();
        return conn;
    }



    private static DataSource source;
    static{
        try {
            Properties pros = new Properties();
            //方式一
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式二
            //FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Connection testGetConnection2() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }

    private static DataSource source1;
    static{
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection3() throws Exception {
        Connection conn = source1.getConnection();
        return conn;
    }
}
