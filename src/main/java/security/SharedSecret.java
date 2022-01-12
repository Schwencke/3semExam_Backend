package security;

public class SharedSecret {


    private static byte[] secret = null;

    public static byte[] getSharedKey() {
        boolean isDeployed = (System.getenv("DEPLOYED") != null);
        if (!isDeployed) {secret = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();}
        else {  secret = System.getenv("SHARED_SECRET").getBytes();}
        return secret;
    }


}