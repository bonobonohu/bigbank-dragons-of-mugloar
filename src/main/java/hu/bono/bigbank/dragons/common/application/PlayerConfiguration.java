package hu.bono.bigbank.dragons.common.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
    prefix = "dragons.player"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerConfiguration {

    private Integer purchaseLivesThreshold;
    private Integer extraLives;
}
