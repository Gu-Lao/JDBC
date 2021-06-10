package com.company5.blob;

import com.company3.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertTest {

    @Test
    public void testInsert1() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?);";
            ps = conn.prepareStatement(sql);

            for (int i = 1; i <= 20000; i++){
                ps.setObject(1,"name_"+i);

                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }

    @Test
    //时间优化
    public void testInsert2() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into goods(name)values(?);";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++){
                ps.setObject(1,"name_"+i);

                ps.addBatch();
                if (i % 500 == 0){
                    ps.executeBatch();

                    ps.clearBatch();
                }
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
}
