//package br.com.balaopreto.adapter.output.seguranca;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FiltroConfig {
//
//    @Bean
//    public FilterRegistrationBean<JwtFiltro> jwtFilter(JwtUtils jwtUtils) {
//        FilterRegistrationBean<JwtFiltro> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new JwtFiltro(jwtUtils));
//        registration.addUrlPatterns("/v1/mensagem/*"); // aqui você protege rotas como envio de mensagem
//        return registration;
//    }
//}
