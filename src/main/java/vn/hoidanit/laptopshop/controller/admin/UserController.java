package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.model.Role;
import vn.hoidanit.laptopshop.model.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.uploadService = uploadService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/")
    // public String getHomePage(Model model) {
    // // Nếu ở file application.property không có xác định file view là jsp
    // // return "hello.jsp";

    // // Nếu ở file application.property có xác định file view là jsp hoặc trong
    // // folder config
    // // String mess = this.userService.handleHello();
    // List<User> users =
    // this.userService.getAllUsersByEmail("hai0944345963@gmail.com");
    // model.addAttribute("test", "mess");
    // return "hello";
    // }

    // Get User
    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    // Update User
    @RequestMapping("/admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/update";
    }

    @RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
    public String updateUser(Model model, @ModelAttribute("user") User user) {
        this.userService.updateUserById(user);
        return "redirect:/admin/user";
    }

    // Delete User
    @RequestMapping("/admin/user/delete/{id}") // GET
    public String DeleteAUserPage(Model model, @PathVariable long id) {
        User userDelete = new User();
        userDelete.setId(id);
        model.addAttribute("id", id);
        model.addAttribute("user", userDelete);
        return "admin/user/delete";
    }

    @RequestMapping("/admin/user/delete")
    public String DeleteAUser(Model model, @ModelAttribute("user") User user) {

        this.userService.deleteAUser(user.getId());
        return "redirect:/admin/user";
    }

    // Create User
    @RequestMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUser(Model model, @ModelAttribute("newUser") @Valid User newUser,
            BindingResult bindingResult,
            @RequestParam("fileUpload") MultipartFile file) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        // Không dùng redirect vì nó không lưu giá trị cũ ở trang đó nếu ta điền vào rồi
        if(bindingResult.hasErrors()) {
            return "/admin/user/create";
        }

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setAvatar(avatar);
        newUser.setPassword(hashPassword);

        // Nếu không setRole thì khi này role trong newUser chỉ có name là ADMIN còn lại
        // đều là null kể cả ID của Role
        newUser.setRole(this.userService.getRoleByName(newUser.getRole().getName()));
        this.userService.handleSaveUser(newUser);
        return "redirect:/admin/user";
    }

}
