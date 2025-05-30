package com.ecom.monolith.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "aws")
@Component
public class AwsProperties {

    private String region;
    private S3 s3;
    private CloudFront cloudfront;

    @Data
    public static class S3 {
        private String bucket;
        private String imageFolder;
    }

    @Data
    public static class CloudFront {
        private String domain;
        private String keyPairId;
        private String privateKeySecretName;
        private int signedUrlExpirationSeconds;
    }
}
