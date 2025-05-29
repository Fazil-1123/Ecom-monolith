package com.ecom.monolith.service;

import com.ecom.monolith.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CloudFrontUrlSignerService {

    private final CloudFrontUrlService cloudFrontUrlService;
    private final AwsProperties awsProperties;

    public String generateSignedUrl(String resourcePath, Date expirationDate) {
        try {
            String distributionDomain = awsProperties.getCloudfront().getDomain();
            String keyPairId = awsProperties.getCloudfront().getKeyPairId();
            long epochSeconds = expirationDate.toInstant().getEpochSecond();

            String policy = String.format(
                    "{\"Statement\":[{\"Resource\":\"https://%s%s\",\"Condition\":{\"DateLessThan\":{\"AWS:EpochTime\":%d}}}]}",
                    distributionDomain,
                    resourcePath,
                    epochSeconds
            );

            // Sign the policy
            byte[] signatureBytes = sign(policy, cloudFrontUrlService.getCloudFrontPrivateKey());
            String encodedSignature = urlSafeBase64(signatureBytes);

            // Build the signed URL
            return String.format("https://%s%s?Expires=%d&Signature=%s&Key-Pair-Id=%s",
                    distributionDomain,
                    resourcePath,
                    epochSeconds,
                    encodedSignature,
                    keyPairId);

        } catch (Exception e) {
            throw new RuntimeException("Error generating signed CloudFront URL", e);
        }
    }

    private byte[] sign(String data, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("SHA1withRSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        return signer.sign();
    }

    private String urlSafeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data)
                .replace("+", "-")
                .replace("=", "_")
                .replace("/", "~");
    }
}
