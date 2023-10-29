package com.example.clase10crud.servlets;

import com.example.clase10crud.beans.Employee;
import com.example.clase10crud.daos.EmployeeDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

//  http://localhost:8080/EmployeeServlet
@WebServlet(name = "EmployeeServlet", value = "/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        EmployeeDao employeeDao = new EmployeeDao();

        switch (action){
            case "lista":
                //saca del modelo
                ArrayList<Employee> list = employeeDao.list();

                //mandar la lista a la vista -> job/lista.jsp
                request.setAttribute("lista",list);
                RequestDispatcher rd = request.getRequestDispatcher("employee/lista.jsp");
                rd.forward(request,response);
                break;
            case "new":
                request.getRequestDispatcher("employee/form_new.jsp").forward(request,response);
                break;
            case "edit":
                String id = request.getParameter("id");
                Employee employee = employeeDao.buscarPorId(id);

                if(employee != null){
                    request.setAttribute("employee", employee);
                    request.getRequestDispatcher("employee/form_edit.jsp").forward(request,response);
                }else{
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                }
                break;
            case "del":
                String idd = request.getParameter("id");
                Employee employee1 = employeeDao.buscarPorId(idd);

                if(employee1 != null){
                    try {
                        employeeDao.borrar(idd);
                    } catch (SQLException e) {
                        System.out.println("Log: excepcion: " + e.getMessage());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        EmployeeDao employeeDao = new EmployeeDao();

        String action = request.getParameter("action") == null ? "crear" : request.getParameter("action");

        switch (action){
            case "crear":

                int empNo= employeeDao.searchLastId() + 1;
                String birthDate = request.getParameter("birthDate");
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String gender = request.getParameter("gender");
                String hireDate = request.getParameter("hireDate");

                Employee e = new Employee();
                e.setEmpNo(empNo);
                e.setBirthDate(birthDate);
                e.setFirstName(firstName);
                e.setLastName(lastName);
                e.setGender(gender);
                e.setHireDate(hireDate);

                boolean isAllValid = true;

                if(firstName.length() > 14){
                    isAllValid = false;
                }

                if(lastName.length() > 16){
                    isAllValid = false;
                }

                if(isAllValid){

                    Employee eAlt = employeeDao.buscarPorId(String.valueOf(empNo));

                    if(eAlt== null){
                        employeeDao.create(e);
                        response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                    }else{
                        request.getRequestDispatcher("employee/form_new.jsp").forward(request,response);
                    }
                }else{
                    request.getRequestDispatcher("employee/form_new.jsp").forward(request,response);
                }
                break;

            case "e":
                // TODO

                int empNo2 = Integer.parseInt(request.getParameter("empNo"));
                String birthDate2 = request.getParameter("birthDate");
                String firstName2 = request.getParameter("firstName");
                String lastName2 = request.getParameter("lastName");
                String gender2 = request.getParameter("gender");
                String hireDate2 = request.getParameter("hireDate");

                Employee e2 = new Employee();


                boolean isAllValid2 = true;

                if(firstName2.length() > 14){
                    isAllValid2 = false;
                }

                if(lastName2.length() > 16){
                    isAllValid2 = false;
                }
                if(isAllValid2){

                    e2.setEmpNo(empNo2);
                    e2.setBirthDate(birthDate2);
                    e2.setFirstName(firstName2);
                    e2.setLastName(lastName2);
                    e2.setGender(gender2);
                    e2.setHireDate(hireDate2);
                    employeeDao.actualizar(e2);
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                }else{
                    request.setAttribute("employee",employeeDao.buscarPorId(String.valueOf(empNo2)));
                    request.getRequestDispatcher("employee/form_edit.jsp").forward(request,response);
                }
                break;

            case "s":
                String textBuscar = request.getParameter("textoBuscar");
                ArrayList<Employee> lista = employeeDao.searchByName(textBuscar);

                request.setAttribute("lista",lista);
                request.setAttribute("busqueda",textBuscar);
                request.getRequestDispatcher("employee/lista.jsp").forward(request,response);

                break;
        }
    }
}