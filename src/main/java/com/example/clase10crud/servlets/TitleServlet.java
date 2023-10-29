package com.example.clase10crud.servlets;

import com.example.clase10crud.beans.Title;
import com.example.clase10crud.daos.TitleDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "TitleServlet", value = "/TitleServlet")
public class TitleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        TitleDao titleDao = new TitleDao();

        switch (action) {
            case "lista":
                //saca del modelo
                ArrayList<Title> list = titleDao.list();

                //mandar la lista a la vista -> title/lista.jsp
                request.setAttribute("lista", list);
                RequestDispatcher rd = request.getRequestDispatcher("title/lista.jsp");
                rd.forward(request, response);
                break;
            case "new":
                request.getRequestDispatcher("title/form_new.jsp").forward(request,response);
                break;
            case "edit":
                String id = request.getParameter("id");
                Title title = titleDao.buscarPorId(id);

                if(title != null){
                    request.setAttribute("title",title);
                    request.getRequestDispatcher("title/form_edit.jsp").forward(request,response);
                }else{
                    response.sendRedirect(request.getContextPath() + "/TitleServlet");
                }
                break;
        }

        //TODO: complete
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        TitleDao titleDao = new TitleDao();

        String action = req.getParameter("action") == null ? "crear" : req.getParameter("action");

        switch (action) {
            case "crear"://voy a crear un nuevo trabajo
                String titleId = req.getParameter("titleId");  // Recibo los inputs del usuario
                String titleEmpNo = req.getParameter("titleEmpNo");
                String titleTtitle = req.getParameter("titleTtitle");
                String titleFromDate = req.getParameter("titleFromDate");
                String titleToDate = req.getParameter("titleToDate");

                boolean isAllValid = true;

                if (titleTtitle.length() > 35) {
                    isAllValid = false;
                }

                if (isAllValid) {

                    Title title = titleDao.buscarPorId(titleId);

                    if (title == null) {
                        titleDao.crear(Integer.parseInt(titleId),Integer.parseInt(titleEmpNo), titleTtitle, titleFromDate, titleToDate);
                        resp.sendRedirect(req.getContextPath() + "/TitleServlet");
                    } else {
                        req.getRequestDispatcher("title/form_new.jsp").forward(req, resp);
                    }
                } else {
                    req.getRequestDispatcher("title/form_new.jsp").forward(req, resp);
                }
                break;
            case "e": //voy a actualizar
                String titleId2 = req.getParameter("titleId");
                String titleEmpNo2 = req.getParameter("titleEmpNo");
                String titleTtitle2 = req.getParameter("titleTtitle");
                String titleFromDate2 = req.getParameter("titleFromDate");
                String titleToDate2 = req.getParameter("titleToDate");

                boolean isAllValid2 = true;

                if(titleTtitle2.length() > 35){
                    isAllValid2 = false;
                }


                if(isAllValid2){
                    Title title = new Title();
                    title.setId(Integer.parseInt(titleId2));
                    title.setEmpNo(Integer.parseInt(titleEmpNo2));
                    title.setTitle(titleTtitle2);
                    title.setFromDate(titleFromDate2);
                    title.setToDate(titleToDate2);

                    titleDao.actualizar(title);
                    resp.sendRedirect(req.getContextPath() + "/TitleServlet");
                }else{
                    req.setAttribute("title",titleDao.buscarPorId(titleId2));
                    req.getRequestDispatcher("job/form_edit.jsp").forward(req,resp);
                }
                break;
        }
    }
}
