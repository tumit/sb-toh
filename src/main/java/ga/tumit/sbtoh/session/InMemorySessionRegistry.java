package ga.tumit.sbtoh.session;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class InMemorySessionRegistry {

    private static final HashMap<String, String> SESSIONS = new HashMap<>();

    public String registersNewSession(final String username) {
        if (username == null) {
            throw new RuntimeException("username should not be null");
        }

        final String sessionId = generateSession();
        SESSIONS.put(sessionId, username);
        return sessionId;
    }

    public String getUsernameForSession(final String sessionId) {
        return SESSIONS.get(sessionId);
    }

    private String generateSession() {
        return new String(
                Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
    }
}
