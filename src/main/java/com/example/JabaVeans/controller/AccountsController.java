package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.service.UserService;
import com.example.JabaVeans.viewhelper.PassProperties;
import com.example.JabaVeans.viewhelper.TablePage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AccountsController {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public AccountsController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/accounts")
    public ModelAndView accounts() {
        ModelAndView mv = new ModelAndView("accounts-table");
        Slice<User> users = userService.findAllWhereRoleNotLike(0, 10, "LVL99");
        TablePage tablePage = new TablePage(users);

            mv.addObject(users.getContent());

        mv.addObject(tablePage);

        return mv;
    }

    @PostMapping("/accounts")
    public ModelAndView accounts(@ModelAttribute("tablePage") TablePage tablePage) {
        ModelAndView mv = new ModelAndView("accounts-table");
        Slice<User> users;
        tablePage.updatePageNumber();
        if (tablePage.getSearchStr().isBlank()) {
            users = userService.findAllWhereRoleNotLike(tablePage.getCurrentPage(), tablePage.getSize(), "LVL99");
        } else {
            users = userService.searchAllWhereRoleNotLikeLVL99(tablePage.getCurrentPage(), tablePage.getSize(), tablePage.getSearchStr());
        }
        tablePage.update(users);
        mv.addObject(users.getContent());
        mv.addObject(tablePage);

        return mv;
    }

    @GetMapping("/accounts/state/{username}")
    public ModelAndView accountSwitchState(@PathVariable("username") String username) {
        ModelAndView mv = new ModelAndView("redirect:/accounts");
        User user = userService.findUser(username);
        if (user != null) {
            user.setEnabled(!user.isEnabled());
            userService.saveUser(user);
        }
        return mv;
    }

    @GetMapping("/accounts/add")
    public ModelAndView addUser() {
        ModelAndView mv = new ModelAndView("account-add");
        mv.addObject(new User());
        return mv;
    }

    @PostMapping("/accounts/add")
    public ModelAndView addUser(@Valid  User user, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("account-add");
        String msg;
        boolean success;
        if (bindingResult.hasErrors()) {
            mv.addObject(user);
            msg = "";
            success = false;
        } else if (userService.findUser(user.getUsername()) != null) {
            mv.addObject(user);
            msg = "Taka nazwa użytkownika już istnieje.";
            success = false;
        } else {
            user.setEnabled(true);
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            mv.addObject(new User());
            msg = "Udało się utworzyć konto.";
            success = true;
        }

        mv.addObject("msg", msg);
        mv.addObject("success", success);

        return mv;
    }

    @GetMapping("/accounts/change-password")
    public ModelAndView changePass(Principal principal) {
        ModelAndView mv = new ModelAndView("change-password");
        mv.addObject("passProperties", new PassProperties());

        return mv;
    }

    @PostMapping("/accounts/change-password")
    public ModelAndView changePass(@Valid PassProperties passProperties, BindingResult bindingResult, Principal principal) {
        ModelAndView mv = new ModelAndView("change-password");
        if (bindingResult.hasErrors() == false) {
            User user = userService.findUser(principal.getName());
            String oldPass = passProperties.getOldPass();

            if (encoder.matches(oldPass, user.getPassword())) {
                user.setPassword(encoder.encode(passProperties.getNewPass1()));
                userService.saveUser(user);
                mv.addObject("msg", "Hasło zostało zmienione");
            } else {
                mv.addObject("msg", "Stare hasło nie pasuje.");
            }

        }
        BeanUtils.copyProperties(new PassProperties(), passProperties);

        return mv;
    }

}
