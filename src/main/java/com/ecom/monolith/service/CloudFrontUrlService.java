package com.ecom.monolith.service;

import com.ecom.monolith.config.AwsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Map;

@Slf4j
@Service
public class CloudFrontUrlService {

    private final SecretsManagerClient secretsManagerClient;
    private final AwsProperties awsProperties;

    @Getter
    private PrivateKey cloudFrontPrivateKey;

    public CloudFrontUrlService(SecretsManagerClient secretsManagerClient, AwsProperties awsProperties) {
        this.secretsManagerClient = secretsManagerClient;
        this.awsProperties = awsProperties;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing CloudFrontUrlService...");

        try {
            String secretName = awsProperties.getCloudfront().getPrivateKeySecretName();
            String secretJson = secretsManagerClient.getSecretValue(
                    GetSecretValueRequest.builder().secretId(secretName).build()
            ).secretString();

            Map<String, String> secrets = new ObjectMapper().readValue(secretJson, Map.class);
            String rawPem = secrets.get("CLOUDFRONT_PRIVATE_KEY");

            if (rawPem == null || rawPem.isBlank()) {
                throw new IllegalArgumentException("Missing 'CLOUDFRONT_PRIVATE_KEY' in Secrets Manager");
            }

            String formattedPem = formatPemBlock(rawPem);
            log.debug("Normalized PEM content:\n{}", formattedPem);

            this.cloudFrontPrivateKey = loadPkcs1PrivateKey(formattedPem);

        } catch (Exception e) {
            log.error("Failed to load CloudFront private key", e);
            throw new RuntimeException("Could not load CloudFront private key", e);
        }
    }

    private PrivateKey loadPkcs1PrivateKey(String pem) throws Exception {
        try (PemReader pemReader = new PemReader(new StringReader(pem))) {
            PemObject pemObject = pemReader.readPemObject();
            if (pemObject == null) {
                throw new IllegalArgumentException("PEM format is invalid or empty");
            }

            byte[] content = pemObject.getContent();
            ASN1Sequence primitive = ASN1Sequence.getInstance(content);
            RSAPrivateKey rsa = RSAPrivateKey.getInstance(primitive);

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(
                    rsa.getModulus(),
                    rsa.getPublicExponent(),
                    rsa.getPrivateExponent(),
                    rsa.getPrime1(),
                    rsa.getPrime2(),
                    rsa.getExponent1(),
                    rsa.getExponent2(),
                    rsa.getCoefficient()
            );

            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    private String formatPemBlock(String raw) {
        raw = raw.replace("\\n", "")
                .replace("\\r", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", "")
                .replaceAll("^\"|\"$", "")
                .trim();

        StringBuilder formatted = new StringBuilder();
        formatted.append("-----BEGIN RSA PRIVATE KEY-----\n");

        int index = 0;
        while (index < raw.length()) {
            int end = Math.min(index + 64, raw.length());
            formatted.append(raw, index, end).append("\n");
            index = end;
        }

        formatted.append("-----END RSA PRIVATE KEY-----\n");
        return formatted.toString();
    }
}
