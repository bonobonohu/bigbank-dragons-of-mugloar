package hu.bono.bigbank.dragons.common.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(
    prefix = "dragons.runner"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunnerConfiguration {

    private Integer noOfCharacters;
    private Set<String> characterNames;
    private Integer maxRuns;
}
