package ga.tumit.sbtoh.controller;

import ga.tumit.sbtoh.dto.LoginRequest;
import ga.tumit.sbtoh.dto.LoginResponse;
import ga.tumit.sbtoh.session.InMemorySessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final InMemorySessionRegistry sessionRegistry;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // auth
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        // register new session
        var sessionId = sessionRegistry.registersNewSession(request.username());

        // return response with sessionId
        return ResponseEntity.ok(new LoginResponse(sessionId));
    }
}
