package com.lms.util;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully");

            System.out.println("DB_URL configured: " + URL);
            System.out.println("DB_USERNAME configured: " + USERNAME);

            if (URL != null) {
                String host = URL.replaceFirst(
                        "jdbc:mysql://([^:/]+).*",
                        "$1"
                );

                System.out.println("DB Host: " + host);

                InetAddress[] addresses = InetAddress.getAllByName(host);

                for (InetAddress address : addresses) {
                    System.out.println(
                        "DB Resolved IP: " + address.getHostAddress()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {

        System.out.println("Attempting MySQL connection...");

        return DriverManager.getConnection(
            URL,
            USERNAME,
            PASSWORD
        );
    }
}