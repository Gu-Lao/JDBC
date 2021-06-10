package com.company5.dbutils;

import com.company.transaction.Customer;
import com.company4.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PseudoColumnUsage;
import java.sql.SQLException;
import java.util.List;

public class QueryRunnerTest {

    @Test
    public void testInsert(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?);";
            int i = runner.update(conn, sql, "cheglong", "123@qq.com", "1997-09-08");
            System.out.println("添加了" + i + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        com.company.util.JDBCUtils.closeResource(conn,null);
        }
    }

    //查询测试
    @Test
    public void testQuery1() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id = ?;";
        BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);

        Customer customer = runner.query(conn, sql, handler, 23);
        System.out.println(customer);
    }


    //查询测试
    @Test
    public void testQuery2() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id < ?;";
        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);

        List<Customer> query = runner.query(conn, sql, handler, 23);
        query.forEach(System.out::println);
    }
}
