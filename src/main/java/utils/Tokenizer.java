package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.nimbusds.jose.JOSEException;
import security.SharedSecret;

import java.util.Date;
import java.util.List;

public class Tokenizer {
 public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min

 static public String createToken(String userName, List<String> roles) throws JOSEException {

  StringBuilder res = new StringBuilder();
  for (String string : roles) {
   res.append(string);
   res.append(",");
  }
  String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
  String token ="";
  Date date = new Date();
  try {
   Algorithm algorithm = Algorithm.HMAC256(SharedSecret.getSharedKey());
   token = JWT.create()
           .withIssuer("master_schwencke")
           .withClaim("username", userName)
           .withClaim("roles", rolesAsString)
           .withIssuedAt(date)
           .withExpiresAt(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
           .sign(algorithm);

  } catch (JWTCreationException exception){
   throw new JWTCreationException(exception.getMessage(),exception.getCause());
  }
  return token;

 }
}
