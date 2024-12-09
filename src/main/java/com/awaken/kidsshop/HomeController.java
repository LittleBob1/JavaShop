package com.awaken.kidsshop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "redirect:/register";
    }

    @GetMapping("/home")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Домашняя страница");
        model.addAttribute("pageContent", "home");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            List<String> roles = authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            model.addAttribute("roles", roles);
        }
        return "index";
    }
}
