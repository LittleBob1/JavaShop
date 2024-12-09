package com.awaken.kidsshop.modules.productitem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/productsItems")
public class ProductItemPageController {
    @GetMapping
    public String sizesPage(Model model) {
        model.addAttribute("pageTitle", "Позиции товаров");
        model.addAttribute("pageContent", "productsItems");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            List<String> roles = authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            model.addAttribute("roles", roles);
        }
        return "index";
    }
}
