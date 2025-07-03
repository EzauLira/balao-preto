//package br.com.balaopreto.adapter.output.seguranca;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
//
//
//import java.util.Date;
//@Component
//public class JwtUtils {
//    private static final String SECRET_KEY = "chaveSecretaDoSeuApp"; // você pode guardar isso num lugar seguro
//    private static final long EXPIRATION_TIME_MS = 86400000; // 1 dia
//
//    public String gerarToken(String email, int idUsuario) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("id", idUsuario)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }
//
//    public boolean validarToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public Claims extrairClaims(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//    }
//
//    public String getEmail(String token) {
//        return extrairClaims(token).getSubject();
//    }
//
//    public int getIdUsuario(String token) {
//        return (Integer) extrairClaims(token).get("id");
//    }
//}
