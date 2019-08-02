package com.alextim.controller;


import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.service.ServiceDB;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.util.security.Credential;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class UserController extends HttpServlet {

    private final ServiceDB serviceDB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("<h3> Security info page: " + request.getUserPrincipal().getName() + " </h3>");
        builder.append("<p> Users: </p>");

        List<User> users = serviceDB.loadAll(0, Integer.MAX_VALUE);
        users.forEach(user -> builder.append("<p>" + user + "</p>"));

        builder.append("<a href=\"/\">home</a>");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(builder.toString());
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User newUser = User.builder()
                .name(request.getParameter("name"))
                .cryptPassword(Credential.Crypt.crypt(request.getParameter("name"), request.getParameter("password")))
                .gender(User.Gender.valueOf(request.getParameter("gender")))
                .address(new Address(request.getParameter("address")))
                .phone(new Phone(request.getParameter("phones")))
                .role("guest")
                .build();

        serviceDB.save(newUser);

        StringBuilder builder = new StringBuilder();
        builder.append("<p>User added</p>");
        builder.append("<a href=\"/\">home</a>");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(builder.toString());
        printWriter.flush();
    }
}