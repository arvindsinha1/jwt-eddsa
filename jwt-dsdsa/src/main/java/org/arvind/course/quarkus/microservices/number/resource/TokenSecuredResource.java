package org.arvind.course.quarkus.microservices.number.resource;





import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import io.vertx.core.json.JsonObject;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;


import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;


@Path("/api/secured")
public class TokenSecuredResource {


    @Context
    HttpHeaders httpHeaders;



    @GET
    @Path("token")
    @Produces(MediaType.TEXT_PLAIN)
    public String getData(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) throws Exception{

        String xlength = httpHeaders.getHeaderString("X-Length");




        String token = authorization.substring("Bearer".length() +1);


        Signature signatureAlgorithm = EdDSAEngine.getInstance("EdDSA");
        //signatureAlgorithm = Signature.getInstance("Ed25519");

        StringBuilder path = new StringBuilder().append("META-INF").append(File.separator).append("resources").append(File.separator).append("publicKey.pem");



        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        String publicKeyPem = new String(resourceAsStream.readAllBytes());



        publicKeyPem = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");


        System.out.println(publicKeyPem+ " = publicKeyPem removed ---");
        byte[] encodedPublicPemKey = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyPem);


        KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicPemKey);
        EdDSAPublicKey edDSAPublicKey = new EdDSAPublicKey(x509EncodedKeySpec);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        signatureAlgorithm.initVerify(publicKey);

        String delimiter = "\\.";

        String[] tokensParts = token.split(delimiter);
        String payloadToken = tokensParts[1];
        String signedToken = tokensParts[2];
        String messageToken = tokensParts[0];
        for(String tokenPart: tokensParts){
            System.out.println(tokenPart + " ::::");
        }
        System.out.println(signedToken + " = signedTokenPart");

        String payloadAndMessagePartOfToken = new StringBuilder().append(messageToken).append(".").append(payloadToken).toString();

        signatureAlgorithm.update(payloadAndMessagePartOfToken.getBytes(StandardCharsets.US_ASCII));




        boolean verification = signatureAlgorithm.verify(java.util.Base64.getUrlDecoder().decode(signedToken));



        String payload =  new String(java.util.Base64.getUrlDecoder().decode(payloadToken));
        String header =  new String(java.util.Base64.getUrlDecoder().decode(messageToken));
        JsonObject headersObject = new JsonObject(header);


        JsonObject claims = new JsonObject(payload);
        System.out.println(claims + " = claims");
        System.out.println(claims.getMap().get("iss"));
        System.out.println(claims.getMap().get("user_id"));
        System.out.println(claims.getMap().get("user"));
        System.out.println(claims.getString("user"));
        System.out.println(verification+ " = verification status of token");

        //JWT.parse(token).getMap().forEach(System.out::println);




        //EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(resourceAsStream.readAllBytes());
        return "data1";
    }


    @GET
    @Path("token/rsa")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDataRsaToken(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) throws Exception{

        System.out.println(authorization+ " = authorizationToken");
        //Signature signature = RSA.getInstance("RS256");


        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/resources/publicKeyRsa.pem");
        String publicKeyPem = new String(resourceAsStream.readAllBytes());

        System.out.println(publicKeyPem+ " = publicKeyPem");

        publicKeyPem = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        System.out.println(publicKeyPem+ " = RSA publicKeyPem removed ---");
        byte[] encodedPublicPemKey = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyPem);





        //signature.initVerify(publicKey);
        //boolean verification = signature.verify(authorization.getBytes(StandardCharsets.UTF_8));


        //System.out.println(verification+ " = verification of token RSA");


        //EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(resourceAsStream.readAllBytes());
        return "data rsa";
    }
}
