package dev.anandbose.todo.authorization;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebAuthenticationController {

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService consentService;

    public WebAuthenticationController(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationConsentService consentService) {
        this.registeredClientRepository = registeredClientRepository;
        this.consentService = consentService;
    }

    @GetMapping("/login")
    public String login(
            Model model,
            @RequestParam(value = "error") Optional<String> error) {
        if (error.isPresent()) {
            model.addAttribute("error", "The last login has failed :(");
        }
        return "login";
    }

    @GetMapping("/oauth2/consent")
    public String consent(
        Model model,
        Principal principal,
        @RequestParam("client_id") String clientId,
        @RequestParam("scope") String scopes,
        @RequestParam("state") String state
    ) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            throw new IllegalStateException("Client is not registered");
        }
        OAuth2AuthorizationConsent consent = consentService.findById(clientId, principal.getName());
        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        Set<String> requestedScopes = Arrays.asList(scopes.split(" ")).stream().collect(Collectors.toSet());
        Set<String> grantedScopes = consent != null ? consent.getScopes() : new HashSet<>();
        requestedScopes.removeAll(grantedScopes);
        model.addAttribute("requestedScopes", requestedScopes);
        if (!grantedScopes.isEmpty()) {
            model.addAttribute("grantedScopes", grantedScopes);
        }
        return "consent";
    }
    
}
