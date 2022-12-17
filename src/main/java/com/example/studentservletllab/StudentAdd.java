package com.example.studentservletllab;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "Default", urlPatterns = "/", loadOnStartup = 1)
public class StudentAdd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception exception) {
            exception.printStackTrace(writer);
            writer.print(exception.getMessage());
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3311/university", "root", "root")) {
            if (request.getParameter("name") != null && request.getParameter("surname") != null) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student (name_, surname, age, email, group_, faculty) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");

                preparedStatement.setString(1, request.getParameter("name"));
                preparedStatement.setString(2, request.getParameter("surname"));
                preparedStatement.setString(3, request.getParameter("age"));
                preparedStatement.setString(4, request.getParameter("email"));
                preparedStatement.setString(5, request.getParameter("group"));
                preparedStatement.setString(6, request.getParameter("faculty"));

                preparedStatement.executeUpdate();
                response.sendRedirect("./");
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
            List<Student> students = new LinkedList<>();

            while(resultSet.next()) {
                Student student = new Student(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
                students.add(student);
            }

            request.setAttribute("students", students);
            request.getRequestDispatcher("view.jsp").forward(request, response);
        } catch (SQLException exception) {
            writer.print(exception.getMessage());
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
