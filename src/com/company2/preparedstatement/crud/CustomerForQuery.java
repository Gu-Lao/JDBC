package com.company2.preparedstatement.crud;

import com.company.bean.Customer;
import com.company3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class CustomerForQuery {

    @Test
    public void testQueryForCustomers() {
        String sql = "Select id,name,birth,email from customers where id = ?";

        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);
    }




    public Customer queryForCustomers(String sql,Object...args) {
        Connection conn = null;
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

            if (rs.next()){
                Customer cust = new Customer();

                //获取结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //给cust对象指定的某个属性，columValue
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust,columValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

        return null;
    }








    @Test
    public void testQuery1() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers whrer id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        //执行并返回结果集
        ResultSet resultSet = ps.executeQuery();

        //处理结果集
        if(resultSet.next()){

            //获取当前这条数据的各个字段值
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date brith = resultSet.getDate(4);

            //方式一
//            Object[] data = {id, name, email, brith};
            //方式二 将数据封装为一个对象（推荐）
            Customer customer = new Customer(id, name, email, brith);
            System.out.println(customer);
        }

        //关闭资源

    }
}
