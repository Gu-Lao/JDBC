package com.company5.blob;

import com.company.bean.Customer;
import com.company3.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

public class BlobTest {

    @Test
    public void testInsert() throws Exception {
        Connection coon = JDBCUtils.getConnection();
        String Sql = "INSERT into customers(name,email,birth,photo)values(?,?,?,?);";
        PreparedStatement ps = coon.prepareStatement(Sql);
        ps.setObject(1,"孤老");
        ps.setObject(2,"qq@qq.com");
        ps.setObject(3,"2000-02-01");
        FileInputStream is = new FileInputStream(new File("1598955004458.jpeg.jpeg"));
        ps.setObject(4,is);

        ps.execute();

        JDBCUtils.closeResource(coon,ps);
    }

    @Test
    public void testQuery() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?;";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,21);
            rs = ps.executeQuery();

            is = null;
            fos = null;
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);

                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("zhaopian.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }
}
