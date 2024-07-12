package hu.bono.bigbank.dragons.common.infrastructure;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class ResourceFactory {

    public BufferedWriter createBufferedWriter(
        final File file
    ) throws IOException {
        return new BufferedWriter(new FileWriter(file, true));
    }

    public CSVPrinter createCSVPrinter(
        final BufferedWriter writer
    ) throws IOException {
        return new CSVPrinter(
            writer,
            CSVFormat.DEFAULT.builder()
                .setQuoteMode(QuoteMode.ALL_NON_NULL)
                .build()
        );
    }
}
