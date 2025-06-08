package dev.anandbose.todo.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import dev.anandbose.todo.web.data.CategoryCreateRequest;
import dev.anandbose.todo.web.data.CategoryDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final OAuth2AuthorizedClientService clientService;

    private final RestTemplate restTemplate;

    private final String resourceServerUrl;

    public CategoryController(OAuth2AuthorizedClientService clientService, RestTemplateBuilder restTemplateBuilder,
            @Value("${todo.resource-server}") String resourceServerUrl) {
        this.clientService = clientService;
        this.restTemplate = restTemplateBuilder.build();
        this.resourceServerUrl = resourceServerUrl;
    }

    @GetMapping
    public String getCategories(Model model, OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(),
                token.getName());
        String bearerToken = client.getAccessToken().getTokenValue();
        URI url = UriComponentsBuilder.fromUriString(resourceServerUrl)
                .path("category")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.add("Accept", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<CategoryDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        model.addAttribute("categories", response.getBody());
        return "category-list";
    }

    @GetMapping("/create")
    public String createCategory() {
        return "category-create";
    }

    @PostMapping("/create")
    public String postCreateCategory(OAuth2AuthenticationToken token, @RequestParam String name) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(),
                token.getName());
        String bearerToken = client.getAccessToken().getTokenValue();
        URI url = UriComponentsBuilder.fromUriString(resourceServerUrl)
                .path("category")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.add("Accept", "application/json");
        HttpEntity<CategoryCreateRequest> entity = new HttpEntity<>(new CategoryCreateRequest(name), headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        return "redirect:/category";
    }

}
