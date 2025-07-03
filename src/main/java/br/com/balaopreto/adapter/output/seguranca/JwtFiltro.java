//package br.com.balaopreto.adapter.output.seguranca;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class JwtFiltro implements Filter {
//
//    private final JwtUtils jwtUtils;
//
//    public JwtFiltro(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest http = (HttpServletRequest) request;
//        String token = http.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // remove "Bearer "
//            if (jwtUtils.validarToken(token)) {
//                // Token válido → pode continuar
//                Claims claims = jwtUtils.extrairClaims(token);
//                request.setAttribute("idUsuario", claims.get("id"));
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//
//        // Bloqueia se não for válido
//        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//}
