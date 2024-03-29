/**
 * <p>Описание:</p>
 * Конфигурационный класс для настройки безопасности веб-приложения на основе Spring Security.
 *
 * <p>Поля:</p>
 * <ul>
 *     <li>userDetailsService: Сервис для работы с данными пользователя.</li>
 *     <li>loginController: Контроллер, обрабатывающий вход в систему.</li>
 * </ul>
 *
 * <p>Методы:</p>
 * <ul>
 *     <li><b>filterChain</b> - Настраивает цепочку фильтров безопасности.</li>
 *     <li><b>daoAuthenticationProvider</b> - Создает и настраивает менеджер аутентификации.</li>
 *     <li><b>passwordEncoder</b> - Объявляет bean для кодирования паролей.</li>
 * </ul>
 */

package ru.mtuci.MindScape.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.mtuci.MindScape.auth.service.LoginService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final LoginService loginService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login/**", "/registration/**", "/forgot_password/**").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .successHandler(((request, response, authentication) -> {
                            loginService.authorizeUser(response, authentication, request);
                        }))
                       )
                .logout(form -> form
                        .logoutSuccessUrl("/login?logout=true")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")).permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN"));
        return http.build();
    }
    @Bean
    public AuthenticationManager daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
