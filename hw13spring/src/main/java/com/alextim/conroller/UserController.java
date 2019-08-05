package com.alextim.conroller;

import com.alextim.domain.User;
import com.alextim.service.ServiceDB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor @Slf4j
public class UserController {

    private final ServiceDB service;

    //http://localhost:8080/DIhello/
    @GetMapping("/")
    public String userList() {
        return "index.html";
    }

    @GetMapping("user/new")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    //http://localhost:8080/DIhello/user
    @GetMapping("/user")
    public String userCreate(Model model) {
        List<User> users = service.loadAll(0, Integer.MAX_VALUE);
        model.addAttribute("users", users);
        return "userList.html";
    }

    @PostMapping("/user")
    public RedirectView userSave(@ModelAttribute User user) {
        service.save(user);
        return new RedirectView("/user", true);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }
}
