package ga.tumit.sbtoh.config;

import ga.tumit.sbtoh.session.SessionFilter;
import ga.tumit.sbtoh.user.CurrentUserService;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AppConfig {

    private final CurrentUserService currentUserService;
    private final SessionFilter sessionFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable cors & scrf
        http = http.cors().and().csrf().disable();

        // if found exception should return 401 UNAUTHORIZED
        http = http.exceptionHandling()
                .authenticationEntryPoint(((request, response, ex) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())))
                .and();

        var permitAllEndpoints = Arrays.asList("/login", "/**/api-docs/**", "/swagger**/**");

        // /api/login => not need auth
        // other => need auth
        http.authorizeRequests()
                .antMatchers(permitAllEndpoints.toArray(new String[0]))
                .permitAll()
                .anyRequest()
                .authenticated();

        // add sessionFilter for init currentUser in request before call authFilter
        http.addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
