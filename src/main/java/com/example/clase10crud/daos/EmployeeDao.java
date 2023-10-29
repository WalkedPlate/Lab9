package com.example.clase10crud.daos;

import com.example.clase10crud.beans.Employee;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeDao {
    private static final String username = "root";
    private static final String password = "root";

    public ArrayList<Employee> list(){

        ArrayList<Employee> lista = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";

        String sql = "select * from employees limit 100";


        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpNo(rs.getInt(1));
                employee.setBirthDate(rs.getString(2));
                employee.setFirstName(rs.getString(3));
                employee.setLastName(rs.getString(4));
                employee.setGender(rs.getString(5));
                employee.setHireDate(rs.getString(6));

                lista.add(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public void create(Employee employee){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "root";

        String sql = "insert into employees (emp_no, birth_date, first_name,last_name,gender,hire_date) values (?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement pstmt = connection.prepareStatement(sql)){


            pstmt.setInt(1,employee.getEmpNo());
            pstmt.setString(2, employee.getBirthDate());
            pstmt.setString(3, employee.getFirstName());
            pstmt.setString(4,employee.getLastName());
            pstmt.setString(5,employee.getGender());
            pstmt.setString(6,employee.getHireDate());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Employee buscarPorId(String id){

        Employee employee = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";

        String sql = "select * from employees where emp_no = ?";


        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,id);

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    employee = new Employee();
                    employee.setEmpNo(rs.getInt(1));
                    employee.setBirthDate(rs.getString(2));
                    employee.setFirstName(rs.getString(3));
                    employee.setLastName(rs.getString(4));
                    employee.setGender(rs.getString(5));
                    employee.setHireDate(rs.getString(6));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employee;
    }

    public void actualizar(Employee employee){
        // TODO
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "root";

        String sql = "update employees set birth_date = ?, first_name = ?, last_name = ?, gender = ?, hire_date = ? where emp_no = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,employee.getBirthDate());
            pstmt.setString(2,employee.getFirstName());
            pstmt.setString(3,employee.getLastName());
            pstmt.setString(4,employee.getGender());
            pstmt.setString(5, employee.getHireDate());;
            pstmt.setInt(6,employee.getEmpNo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void borrar(String employeeNo) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";

        String sql = "delete from employees where emp_no = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,employeeNo);
            pstmt.executeUpdate();

        }
    }

    public ArrayList<Employee> searchByName(String name) {
        // TODO
        ArrayList<Employee> lista = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "root";

        String sql = "select * from employees where lower(first_name) like lower(?) or lower(last_name) like lower(?);";


        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,"%" + name + "%");
            pstmt.setString(2,"%" + name + "%");


            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    while (rs.next()) {
                        Employee employee = new Employee();
                        employee.setEmpNo(rs.getInt(1));
                        employee.setBirthDate(rs.getString(2));
                        employee.setFirstName(rs.getString(3));
                        employee.setLastName(rs.getString(4));
                        employee.setGender(rs.getString(5));
                        employee.setHireDate(rs.getString(6));

                        lista.add(employee);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public int searchLastId() {

        int lastEmpNo = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:mysql://localhost:3306/employees";

        String sql = "select max(employees.emp_no) from employees";


        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lastEmpNo = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        return lastEmpNo;
    }
}
