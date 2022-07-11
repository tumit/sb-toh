package ga.tumit.sbtoh.session;

import ga.tumit.sbtoh.user.CurrentUserService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class SessionFilter extends OncePerRequestFilter {

    private final InMemorySessionRegistry sessionRegistry;
    private final CurrentUserService currentUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (ObjectUtils.isEmpty(sessionId)) {
            filterChain.doFilter(request, response);
            return;
        }

        final var username = sessionRegistry.getUsernameForSession(sessionId);
        if (ObjectUtils.isEmpty(username)) {
            filterChain.doFilter(request, response);
            return;
        }

        final var currentUser = currentUserService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(currentUser)) {
            filterChain.doFilter(request, response);
            return;
        }

        final var authToken = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
