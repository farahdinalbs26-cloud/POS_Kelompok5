/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pos_kelompok5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String HOST = "localhost";
    private static final String DB = "db_pos_kelompok5";
    private static final String USER = "root";
    private static final String PASS = "";

    private static final String URL = "jdbc:mysql://" + HOST + "/" + DB;
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    public static void main(String[] args) {
        try {
            if (getConnection() != null) {
                System.out.println("SUKSES: Koneksi ke db_pos_kelompok5 berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("GAGAL: " + e.getMessage());
        }
    }
}


