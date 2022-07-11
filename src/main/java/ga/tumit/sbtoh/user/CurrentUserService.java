package ga.tumit.sbtoh.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CurrentUserService implements UserDetailsService {

    private final UserInMemoryRepository userInMemoryRepository;

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        final var currentUser = userInMemoryRepository.findUserByUsername(username);
        if (currentUser == null) {
            throw new UsernameNotFoundException("not found: username=%s".formatted(username));
        }
        return currentUser;
    }
}
