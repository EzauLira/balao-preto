package br.com.balaopreto.adapter.output.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/login/logar",
                                "/v1/login/verificar-codigo",
                                "/v1/verificacao/enviar-dados",
                                "/v1/verificacao/verificar-codigo",
                                "/v1/recuperar/verificar-codigo",
                                "/v1/recuperar/email-por-telefone",

                                // Liberar todas as páginas HTML na raiz
                                "/*.html",

                                // Liberar todos os recursos estáticos
                                "/assets/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/manifest.json",
                                "/sw.js"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
