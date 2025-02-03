package web6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web6.model.User;
import web6.service.RoleService;
import web6.service.UserService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAdminPage(User user, Model model) {
        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "/adminPage";
    }

    @GetMapping("/add")
    public String newUserPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "/newUser";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user) {
        getUserRoles(user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    private void getUserRoles(User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getRole(role.getUserRole()))
                .collect(Collectors.toSet()));
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "/edit";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user) {
        getUserRoles(user);
        userService.updateUser(user);
        return "redirect:/admin";
    }


}