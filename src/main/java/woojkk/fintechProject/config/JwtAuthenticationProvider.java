package woojkk.fintechProject.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;
import woojkk.fintechProject.domain.UserVo;

public class JwtAuthenticationProvider {

  private final String secretKey = "secretKey";

  private final long tokenValidTime = 1000L*60*30;

  public String createToken(String userPk, Long id) {
    Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk))
        .setId(Aes256Util.encrypt(id.toString()));
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime()+ tokenValidTime))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public boolean validateToken(String jwtToken) {
    try {

    Jws<Claims> claimsJws = Jwts.parser()
        .setSigningKey(secretKey).parseClaimsJws(jwtToken);

    return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public UserVo getUserVo(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token).getBody();

    return new UserVo(Long.valueOf(Objects.requireNonNull(
        Aes256Util.decrypt(claims.getId()))),
        Aes256Util.decrypt(claims.getSubject()));
  }

}
