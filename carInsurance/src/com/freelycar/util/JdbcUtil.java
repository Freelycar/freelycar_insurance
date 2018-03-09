package com.freelycar.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {

    private static Properties prop = new Properties();
    static {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();  
        if (cl == null)
            cl = JdbcUtil.class.getClassLoader(); 
        InputStream in = cl.getResourceAsStream("jdbc.properties");
        try {
            prop.load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {

            Class.forName(prop.getProperty("jdbc.driverClass"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
            		prop.getProperty("jdbc.Url"),
            		prop.getProperty("jdbc.user"), 
            		prop.getProperty("jdbc.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }
}
