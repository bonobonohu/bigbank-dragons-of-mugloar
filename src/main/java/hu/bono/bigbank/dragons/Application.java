package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.application.Runner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final Runner runner;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        runner.run();
    }
}
