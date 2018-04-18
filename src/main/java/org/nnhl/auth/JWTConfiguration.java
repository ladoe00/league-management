package org.nnhl.auth;

import java.io.IOException;
import java.io.StringWriter;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

public final class JWTConfiguration
{
    private static final String ISSUER = "League Management Authentication Server";

    private static final String AUDIENCE = "League Management Client";

    private static JWTConfiguration instance;

    public static synchronized JWTConfiguration getInstance()
    {
        if (instance == null)
        {
            synchronized (JWTConfiguration.class)
            {
                if (instance == null)
                {
                    try
                    {
                        instance = new JWTConfiguration();
                    }
                    catch (JoseException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    private final RsaJsonWebKey rsaJsonWebKey;

    private JWTConfiguration() throws JoseException
    {
        this.rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        this.rsaJsonWebKey.setKeyId("authKey");
    }

    public String getPublicKey()
    {
        PemObject pemObject = new PemObject("PUBLIC KEY", rsaJsonWebKey.getRsaPublicKey().getEncoded());

        StringWriter sr = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(sr))
        {
            pemWriter.writeObject(pemObject);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return sr.toString();
    }

    public void configureClaims(JwtClaims claims)
    {
        claims.setAudience(AUDIENCE);
        claims.setIssuer(ISSUER);
        claims.setIssuedAtToNow();
        claims.setGeneratedJwtId();
        claims.setExpirationTimeMinutesInTheFuture(20);
        claims.setNotBeforeMinutesInThePast(2);
    }

    public JsonWebSignature createJsonWebSignature(JwtClaims claims)
    {
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        return jws;
    }

    public JwtConsumer createJwtConsumer()
    {
        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(300)
                .setRequireSubject()
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setExpectedIssuer(ISSUER)
                .setExpectedAudience(AUDIENCE)
                .setJwsAlgorithmConstraints(
                        new AlgorithmConstraints(ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256))
                .build();
        return consumer;
    }
}
