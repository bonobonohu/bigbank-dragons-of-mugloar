package hu.bono.bigbank.dragons.common.application;

import hu.bono.bigbank.dragons.common.infrastructure.DungeonMaster;
import hu.bono.bigbank.dragons.common.infrastructure.Player;
import hu.bono.bigbank.dragons.common.infrastructure.PlayerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class Runner {

    private static final int NO_OF_CHARACTERS_TO_PLAY_WITH = 3;
    private static final Set<String> CHARACTER_NAMES = Set.of(
        "Joe Hallenbeck", "John McClane", "Butch Coolidge",
        "Malcolm Crowe", "Hartigan", "General Joe Colton",
        "Lt. Muldoon", "David Dunn", "Jimmy Tudeski",
        "Harry S. Stamper", "The Jackal", "Korben Dallas",
        "John Smith", "Hudson Hawk", "James Cole",
        "Lieutenant A.K. Waters", "Old Joe", "Paul Kersey",
        "Valmora", "Leonard", "Steve Ford"
    );
    private static final int MAX_RUNS = 255;

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private final DungeonMaster dungeonMaster;
    private final PlayerFactory playerFactory;

    public void run() {
        final Instant start = Instant.now();
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        final int noOfThreads = NO_OF_CHARACTERS_TO_PLAY_WITH + 1;
        try (ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors)) {
            final Set<String> usedNames = new HashSet<>();
            final List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < NO_OF_CHARACTERS_TO_PLAY_WITH; i++) {
                final Future<?> future = executorService.submit(() -> {
                    final Player player = playerFactory.createPlayer(dungeonMaster);
                    final String characterName = getCharacterName(usedNames);
                    usedNames.add(characterName);
                    LOG.info("Starting to play with: {}", characterName);
                    player.play(characterName, MAX_RUNS);
                });
                futures.add(future);
            }
            executorService.shutdown();

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException executionException) {
                    LOG.error("Exception in submitted task", executionException.getCause());
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    LOG.error("Task was interrupted", interruptedException);
                }
            }
        } catch (Exception exception) {
            LOG.error("Exception caught in Runner class", exception);
        }
        final Instant finish = Instant.now();
        LOG.info(
            "Application ran {} seconds with {} players, {} processors and {} threads",
            Duration.between(start, finish).getSeconds(),
            NO_OF_CHARACTERS_TO_PLAY_WITH,
            availableProcessors,
            noOfThreads
        );
    }

    private String getCharacterName(
        final Set<String> usedNames
    ) {
        final List<String> availableNames = new ArrayList<>(CHARACTER_NAMES);
        availableNames.removeAll(usedNames);
        if (availableNames.isEmpty()) {
            throw new RuntimeException("No available character names left");
        }
        return availableNames.get(new Random().nextInt(availableNames.size()));
    }
}
