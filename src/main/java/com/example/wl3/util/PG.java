package com.example.wl3.util;

import com.example.wl3.beans.Point;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PG {
    private static final String URL = "jdbc:postgresql://localhost:5432/studs";
    private static final String USER = "s335080";
    private static final String PASSWORD = "1qmidR6jeExAcPq8";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection to PostgreSQL has been established :" + connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }


    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection to PostgreSQL has been closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addPoint(Point point) {
//        if points table does not exist, create it
        try {
            java.sql.Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS points (id serial PRIMARY KEY, x double precision, y double precision, r double precision, duration bigint, time timestamp, hit boolean)");
            statement.close();

            java.sql.PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO points (x, y, r, duration, time, hit) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setDouble(1, point.getX());
            preparedStatement.setDouble(2, point.getY());
            preparedStatement.setDouble(3, point.getR());
            point.setExecutionTime(System.nanoTime() - point.getStartTime());
            preparedStatement.setLong(4, point.getExecutionTime());
            preparedStatement.setTimestamp(5, Timestamp.from(point.getCurrentTime().toInstant()));
            preparedStatement.setBoolean(6, point.isHit());
            int n = preparedStatement.executeUpdate();
            System.out.println("n addPoint: " + n);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        try {
            java.sql.PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM points");
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Point point = new Point();
                point.setX(resultSet.getDouble("x"));
                point.setY(resultSet.getDouble("y"));
                point.setR(resultSet.getDouble("r"));
                point.setExecutionTime(resultSet.getLong("duration"));
                point.setCurrentTime(Date.from(resultSet.getTimestamp("time").toInstant()));
                point.setHit(resultSet.getBoolean("hit"));
                points.add(point);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return points;
    }

    public static void deletePoints() {
        try {
            java.sql.PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM points");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
