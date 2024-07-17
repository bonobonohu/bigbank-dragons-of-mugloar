package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.domain.LogWriterException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogWriter {

    static final String USER_DIR = System.getProperty("user.dir");
    static final String LOG_DIR = "game_logs";
    static final String LOG_FILE_EXTENSION = ".csv";
    static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));
    static final DateTimeFormatter TIMESTAMP_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
    static final List<String> CSV_FIELDS = List.of(
        "Timestamp",
        "Character name",
        "GameId",
        "Event",
        "Input",
        "Output",
        "Character Sheet"
    );

    private final ResourceFactory resourceFactory;

    @Builder
    private record Resources(
        BufferedWriter writer,
        CSVPrinter csvPrinter
    ) {

    }

    public void log(
        final GameSession gameSession,
        final String event,
        final Object input,
        final Object output
    ) {
        try {
            final Resources resources = open(gameSession);

            final String timestamp = TIMESTAMP_FORMATTER.format(Instant.now());
            resources.csvPrinter.printRecord(
                timestamp,
                gameSession.getCharacterSheet().getName(),
                gameSession.getGameId(),
                event,
                (input == null ? "null" : input.toString()),
                output.toString(),
                gameSession.getCharacterSheet().toString()
            );
            resources.csvPrinter.flush();

            close(resources);
        } catch (IOException ioException) {
            throw new LogWriterException(ioException);
        }
    }

    private Resources open(
        final GameSession gameSession
    ) throws IOException {
        final File file = getFileInSessionDir(gameSession);
        final boolean fileExists = file.exists();

        final BufferedWriter writer = resourceFactory.createBufferedWriter(file);
        final CSVPrinter csvPrinter = resourceFactory.createCSVPrinter(writer);

        if (!fileExists) {
            csvPrinter.printRecord(CSV_FIELDS);
            csvPrinter.flush();
        }

        return Resources.builder()
            .writer(writer)
            .csvPrinter(csvPrinter)
            .build();
    }

    private File getFileInSessionDir(
        final GameSession gameSession
    ) throws IOException {
        final String sessionDate = DATE_FORMATTER.format(gameSession.getCreationTimestamp());
        final File directory = Paths.get(USER_DIR, LOG_DIR, sessionDate).toFile();

        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }

        return new File(directory, gameSession.getLogFileName() + LOG_FILE_EXTENSION);
    }

    private void close(
        final Resources resources
    ) throws IOException {
        if (resources.csvPrinter != null) {
            resources.csvPrinter.close();
        }
        if (resources.writer != null) {
            resources.writer.close();
        }
    }
}
