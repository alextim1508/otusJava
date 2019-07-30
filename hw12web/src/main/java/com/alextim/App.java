package com.alextim;

import com.alextim.cache.MyCache;
import com.alextim.controller.UserController;
import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import com.alextim.service.MyLoginService;
import com.alextim.service.ServiceDB;
import com.alextim.service.ServiceDbImpl;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class App {

    private final static int PORT = 8080;

    private final static UserRepository userRepository = new UserRepositoryImpl();
    private final static MyCache<User> myCache
            = new MyCache<>(10, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    private final static ServiceDbImpl serviceDb = new ServiceDbImpl(userRepository, myCache);

    public static void main(String[] args) throws Exception {
        new App().init().start();
    }

    public App init() {
        User ivan = User.builder()
                .name("Ivan")
                .gender(User.Gender.MALE)
                .address(new Address("street1"))
                .phone(new Phone("number1"))
                .phone(new Phone("number2"))
                .role("guest")
                .cryptPassword(Credential.Crypt.crypt("Ivan", "Ivan"))
                .build();

        userRepository.insert(ivan);

        User alex = User.builder()
                .name("Alex")
                .gender(User.Gender.MALE)
                .address(new Address("street2"))
                .phone(new Phone("number3"))
                .phone(new Phone("number4"))
                .role("moderator")
                .role("admin")
                .cryptPassword(Credential.Crypt.crypt("Alex", "Alex"))
                .build();

        userRepository.insert(alex);

        User kirill = User.builder()
                .name("Kirill")
                .gender(User.Gender.MALE)
                .address(new Address("street3"))
                .phone(new Phone("number5"))
                .phone(new Phone("number6"))
                .role("moderator")
                .cryptPassword(Credential.Crypt.crypt("Kirill", "Kirill"))
                .build();

        userRepository.insert(kirill);

        return this;
    }

    public void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();

        serviceDb.close();
    }



    private Server createServer(int port) {
        LoginService loginService = getMyLoginService(serviceDb);

        Server server = new Server(port);
        server.addBean(loginService);
        server.addBean(new ErrorHandler() {
            @Override
            protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {

                writer.write("<!DOCTYPE<html><head><title>Error</title></head><html><body>"
                        + code + " - " + HttpStatus.getMessage(code) + message + "</body></html>");
            }
        });

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UserController(serviceDb)), "/user");

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(createResourceHandler());
        handlerList.addHandler(createSessionHandler());
        handlerList.addHandler(createSecurityHandler(context, loginService));

        server.setHandler(handlerList);
        return server;
    }

    private LoginService getMyLoginService(ServiceDB serviceDB) {
        return new MyLoginService(serviceDB);
    }

    private Handler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();

        URL fileDir = App.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }

    private Handler createSessionHandler() {
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setMaxInactiveInterval(30);
        return sessionHandler;
    }

    private Handler createSecurityHandler(ServletContextHandler context, LoginService loginService) {
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping adminPath = new ConstraintMapping();
        adminPath.setPathSpec("/user");
        adminPath.setConstraint(constraint);


        List<ConstraintMapping> mappings = new ArrayList<>();
        mappings.add(adminPath);

        FormAuthenticator formAuthenticator = new FormAuthenticator("/login.html", "/login.html?error=true", false);

        securityHandler.setConstraintMappings(mappings);
        securityHandler.setAuthenticator(formAuthenticator);
        securityHandler.setLoginService(loginService);
        securityHandler.setHandler(context);

        return securityHandler;
    }


}
