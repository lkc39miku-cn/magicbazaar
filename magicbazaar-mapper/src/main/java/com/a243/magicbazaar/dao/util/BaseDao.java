package com.a243.magicbazaar.dao.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao {
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://127.0.0.1:3306/magicbazaar?useSSL=false";
    private static String userName = "root";
    private static String pwd = "123456";

    //创建数据库连接对象的公共方法
    private static Connection getConn() {
        Connection conn = null;
        try {
            //加载驱动
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, pwd);//得到数据库连接对象
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /***
     * 增删改公共方法
     * @param sql   要执行的sql语句
     * @param objs   要赋值给sql占位符的参数
     * @return 影响的数据条数
     */
    public static int update(String sql, Object... objs) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        conn = getConn();//得到数据库连接对象
        int result = 0;//存储结果数据
        try {
            pstmt = conn.prepareStatement(sql);//创建PreparedStatement对象
            //第三步：给sql的占位符赋值
            if (objs != null && objs.length > 0) {
                //说明传递的有参数
                for (int i = 0; i < objs.length; i++) {
                    pstmt.setObject(i + 1, objs[i]);
                }
            }
            result = pstmt.executeUpdate();//第四步：执行sql
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(null, pstmt, conn);//关闭对象
        }
        return result;
    }

    /***
     * 可以获取新添加数据主键id的公共方法
     * @param sql   要执行的sql语句
     * @param objs   要赋值给sql占位符的参数
     * @return 刚生成数据的主键id
     */
    public static int updateTwo(String sql, Object... objs) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        conn = getConn();//得到数据库连接对象
        int result = 0;//存储结果数据
        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//创建PreparedStatement对象
            //第三步：给sql的占位符赋值
            if (objs != null && objs.length > 0) {
                //说明传递的有参数
                for (int i = 0; i < objs.length; i++) {
                    pstmt.setObject(i + 1, objs[i]);
                }
            }
            pstmt.executeUpdate();//第四步：执行sql
            //获取刚返回过来的生成的主键id
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                result = Integer.parseInt(rs.getObject(1) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(null, pstmt, conn);//关闭对象
        }
        return result;
    }

    /**
     * 查询方法的封装
     *
     * @param sql  执行的sql语句
     * @param objs 给占位符赋值的参数
     * @return 查询到的数据结果集
     */
    public static List<Map<String, Object>> find(String sql, Object... objs) {
        List<Map<String, Object>> resultList = new ArrayList<>();//存储封装的数据结果集的集合
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        conn = getConn();//第一步：得到数据库连接
        try {
            //第二步：创建PreparedStatement对象
            pstmt = conn.prepareStatement(sql);
            //第三步：给占位符赋值
            if (objs != null && objs.length > 0) {
                //有查询参数
                for (int i = 0; i < objs.length; i++) {
                    pstmt.setObject(i + 1, objs[i]);
                }
            }
            //第四步：执行sql语句
            rs = pstmt.executeQuery();
            //第五步：处理结果集
            //首先得到原数据对象
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnNum = rsmd.getColumnCount();//得到查询到的列的个数，即字段的个数
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();//存储每条数据对象的集合
                for (int j = 1; j <= columnNum; j++) {
                    //遍历每一列，得到每一列的字段名和该字段对应的数据
                    String columnName = rsmd.getColumnName(j);//得到指定列的字段名
                    Object v = rs.getObject(j);//得到指定列的数据值
                    m.put(columnName, v);//将每个字段名与该字段的数据设置到map集合
                }
                resultList.add(m);//将本条数据对象存档到ArrayList集合里
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, conn);//关闭所有对象
        }
        return resultList;
    }


    /**
     * 关闭打开的对象的方法
     *
     * @param rs    结果集对象
     * @param pstmt 执行的PreparedStatement对象
     * @param conn  数据库连接对象
     */
    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
