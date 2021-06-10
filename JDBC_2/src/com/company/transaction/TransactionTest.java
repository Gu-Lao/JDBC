package com.company.transaction;

import com.company.util.JDBCUtils;
import org.junit.Test;

import javax.swing.text.Utilities;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionTest {


    @Test
    public void testUpdate(){
        //未考虑数据库事务的写法
        String sql1="update user_table set balance = balance - 100 where user = ?";
        update(sql1,"AA");

        String sql2="update user_table set balance = balance + 100 where user = ?";
        update(sql2,"BB");
    }





    //通用的增删改操作*************未考虑数据库事务的写法
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
    public void testUpdateWithTx()  {

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            System.out.println(conn.getAutoCommit());
            String sql1="update user_table set balance = balance - 100 where user = ?";
            update(conn,sql1,"AA");

            String sql2="update user_table set balance = balance + 100 where user = ?";
            update(conn,sql2,"BB");
            System.out.println("转账成功");
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }


    //通用的增删改操作*************考虑数据库事务的写法
    public void update(Connection conn,String sql,Object ... args){
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回prepareStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            //3.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null,ps);
        }
    }


    //通用的查询操作-----修改版
    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMataData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();

                //获取结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //给cust对象指定的某个属性，columValue
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }

        return null;
    }
}
