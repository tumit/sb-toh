package ga.tumit.sbtoh.user;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class UserInMemoryRepository {

    private static final HashMap<String, CurrentUser> REGISTERED_USERS = new HashMap<>();

    @PostConstruct
    public void setupUser() {
        REGISTERED_USERS.put(
                "user1",
                CurrentUser.builder()
                        .username("user1")
                        .password("$2a$10$Eex7MQWWczCIdkb47IJ.RundU8v7l92PjqwrseDQLv8.rRPy6l3c6")
                        .build());
        REGISTERED_USERS.put(
                "user2",
                CurrentUser.builder()
                        .username("user2")
                        .password("$2a$10$.uvY0rdXbrl5mCRASiqc.OvB7hPMa0OiCqq.mGH.L31/nZiIbDxrW")
                        .build());
    }

    public CurrentUser findUserByUsername(final String username) {
        return REGISTERED_USERS.get(username);
    }
}
