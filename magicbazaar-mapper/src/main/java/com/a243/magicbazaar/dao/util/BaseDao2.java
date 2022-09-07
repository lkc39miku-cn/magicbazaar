package com.a243.magicbazaar.dao.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao2 {
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/magicbazaar?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8";
	private static String username = "root";
	private static String pwd = "123456";

	// 1.获取数据库链接
	private static Connection getConnection() {
		Connection conn = null;
		try {
			// 创建当前类的对象
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 增删改
	 * 
	 * @param sql
	 *            执行sql语句
	 * @return
	 */
	public static int update(String sql, Object... objs) {
		Connection conn = null;
		PreparedStatement ptm = null;
		int rs = 0;
		try {
			conn = getConnection();
			ptm = conn.prepareStatement(sql);
			setParams(ptm, objs);
			rs = ptm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(null, ptm, conn);
		}
		return rs;
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 *            执行的sql语句
	 * @param objs
	 *            sql中的参数 {author=北京大学出版社, price=89.0, id=1, describe=null,
	 *            title=JAVA}
	 * @return
	 */
	public static List<Map<String, Object>> query(String sql, Object... objs) {

		Connection conn = null;
		PreparedStatement ptm = null;
		ResultSet rs = null;
		List<Map<String, Object>> rslist = new ArrayList<Map<String, Object>>();// 存放所有查询到数据的结果集
		try {
			conn = getConnection();
			ptm = conn.prepareStatement(sql);
			setParams(ptm, objs);
			// 获取查询结果
			rs = ptm.executeQuery();
			// 获取结果集结构(列的结构)
			ResultSetMetaData md = rs.getMetaData();
			// 获取结果集列数
			int columns = md.getColumnCount();// 获取所有列的个数，即所有字段的总个数
			// 循环获取每一行
			while (rs.next()) {
				// rs.getString("title");rs.getString("1")
				// 封装每条数据对象
				Map<String, Object> map = new HashMap<String, Object>();
				// 获取当前行的每一列的值
				for (int i = 1; i <= columns; i++) {
					// 当前列的值,即每个字段的值
					Object value = rs.getObject(i);
					// 当前列的列名
					String columnName = md.getColumnName(i);// 得到本列的字段名称的
					// 每一行中的某一列
					map.put(columnName, value);
				}
				rslist.add(map);// 将上边封装好的每条数据对象添加到最后的结果集里
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, ptm, conn);
		}
		return rslist;
	}

	// 4.关闭所有对象
	private static void closeAll(ResultSet rs, PreparedStatement ptm, Connection conn) {
		try {
			if (null != rs)
				rs.close();
			if (null != ptm)
				ptm.close();
			if (null != conn)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 5.参数设置
	private static void setParams(PreparedStatement ptm, Object... objs) {
		if (null != objs) {
			for (int i = 0; i < objs.length; i++) {
				try {
					ptm.setObject(i + 1, objs[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
