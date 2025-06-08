package dev.anandbose.todo.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final OAuth2AuthorizedClientService clientService;

    public HomeController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String home(Model model) {
        String userName = "Guest";
        boolean isLoggedIn = false;
        try {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
            System.out.println("Expecting Bearer Token: " + client.getAccessToken().getTokenValue());
            userName = authToken.getName();
            isLoggedIn = true;
        } catch (Exception e) {

        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userName", userName);
        return "index";
    }
}
