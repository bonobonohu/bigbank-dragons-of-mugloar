package hu.bono.bigbank.dragons.common.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
    prefix = "dragons.api"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiConfiguration {

    private String baseUrl;
    private Endpoints endpoints;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Endpoints {

        private String gameStart;
        private String investigateReputation;
        private String messages;
        private String solveAd;
        private String shop;
        private String shopBuyItem;
    }
}
