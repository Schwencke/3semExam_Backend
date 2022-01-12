package security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import facades.UserFacade;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.User;
import errorhandling.API_Exception;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import security.errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;
import utils.Tokenizer;

@Path("login")
public class LoginEndpoint {

    //public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonString) throws AuthenticationException, API_Exception {
        String username;
        String password;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            username = json.get("username").getAsString();
            password = json.get("password").getAsString();
        } catch (Exception e) {
           throw new API_Exception("Malformed JSON Suplied",400,e);
        }

        try {
            User user = USER_FACADE.getVerifiedUser(username, password);
            String token = Tokenizer.createToken(username, user.getRolesAsStrings());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", username);
            responseJson.addProperty("token", token);
            return Response.ok(new Gson().toJson(responseJson)).build();

        } catch (JOSEException | AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                throw (AuthenticationException) ex;
            }
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new AuthenticationException("Invalid username or password! Please try again");
    }


//    private String createToken(String userName, List<String> roles) throws JOSEException {
//
//        StringBuilder res = new StringBuilder();
//        for (String string : roles) {
//            res.append(string);
//            res.append(",");
//        }
//        String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
//        String token ="";
//        Date date = new Date();
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(SharedSecret.getSharedKey());
//             token = JWT.create()
//                    .withIssuer("master_schwencke")
//                    .withClaim("username", userName)
//                    .withClaim("roles", rolesAsString)
//                    .withIssuedAt(date)
//                    .withExpiresAt(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
//                    .sign(algorithm);
//
//        } catch (JWTCreationException exception){
//            throw new JWTCreationException(exception.getMessage(),exception.getCause());
//        }
//        return token;
//
//    }

//    @POST
//    @Path("verify")
//    @Consumes(MediaType.TEXT_PLAIN)
//    public Response verifyToken(String token) throws Exception {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(SharedSecret.getSharedKey());
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer("master_schwencke")
//                    .build();
//            DecodedJWT jwt = verifier.verify(token);
//        } catch (JWTVerificationException exception){
//            throw new AuthenticationException(exception.getMessage());
//        }
//        return Response.ok().build();
//    }

}
