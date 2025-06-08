package dev.anandbose.todo.web.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import dev.anandbose.todo.web.data.PagedModel;
import dev.anandbose.todo.web.data.TodoDto;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final OAuth2AuthorizedClientService clientService;
    private final RestTemplate restTemplate;

    @Value("${todo.resource-server}")
    private String resourceServerUrl;

    public TodoController(OAuth2AuthorizedClientService clientService, RestTemplateBuilder restTemplateBuilder) {
        this.clientService = clientService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping
    public String getAllTodos(
            Model model,
            @RequestParam(defaultValue = "0", required = false) long page,
            @RequestParam(defaultValue = "20", required = false) long size,
            OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(),
                token.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        String url = UriComponentsBuilder.fromUriString(resourceServerUrl)
                .path("/todo")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Accept", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<PagedModel<TodoDto>> type = new ParameterizedTypeReference<PagedModel<TodoDto>>() {};
        ResponseEntity<PagedModel<TodoDto>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, type);
        model.addAttribute("todoList", response.getBody());
        return "todo-list";
    }

}
