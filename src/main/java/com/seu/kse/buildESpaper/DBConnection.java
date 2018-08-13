package com.seu.kse.buildESpaper;

import com.mysql.jdbc.Connection;
import com.seu.kse.bean.Paper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBConnection {

    String driver = "com.mysql.jdbc.Driver";
    String url= "jdbc:mysql://120.78.165.80:3306/reasearch_kg";
    String user = "root";
    String password = "ksedb2017";

    public Connection connection;

    public DBConnection() {

        try {
            Class.forName(driver);// 加载驱动程序
            connection = (Connection) DriverManager.getConnection(url, user, password);// 连续数据库

            if(!connection.isClosed())
                System.out.println("Succeeded connecting to the Database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
