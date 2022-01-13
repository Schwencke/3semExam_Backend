package errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    ExceptionDTO err;

    @Override
    public Response toResponse(Throwable ex) {
        Logger.getLogger(CustomExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        Response.StatusType type = getStatusType(ex);
        int statusCode = type.getStatusCode();

        if (ex instanceof CustomException) {
            statusCode = ((CustomException) ex).getStatusCode();
            err = new ExceptionDTO(statusCode, ex.getMessage());
        } else {
            err = new ExceptionDTO(statusCode, type.getReasonPhrase());
        }

        return Response.status(statusCode)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON).
                build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse().getStatusInfo();
        }
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    public static Response makeErrRes(String msg, int status) {
        ExceptionDTO error = new ExceptionDTO(status, msg);
        String errJson = gson.toJson(error);
        return Response.status(error.getCode())
                .entity(errJson)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
