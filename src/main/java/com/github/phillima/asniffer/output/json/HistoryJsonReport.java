package com.github.phillima.asniffer.output.json;

import com.github.phillima.asniffer.git.models.AMReportGitModel;
import com.github.phillima.asniffer.output.IHistoryReport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * The Class HistoryJsonReport
 *
 * @author Pedro Junho Silveira
 * @since 07/08/2026
 */

public class HistoryJsonReport implements IHistoryReport {
    private static final Logger logger = LogManager.getLogger(HistoryJsonReport.class);

    // Gson has no built-in adapter for java.time types; without this, it falls back to
    // reflecting over Instant's private fields, which fails under the Java module system.
    private static final DateTimeFormatter COMMIT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneOffset.UTC);

    private JsonWriter jsonWriter;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>)
                    (instant, type, context) -> new JsonPrimitive(COMMIT_DATE_FORMAT.format(instant)))
            .create();

    @Override
    public void start(String reportPath, String projectName) {
        try {
            Path outputPath = Path.of(reportPath + File.separator + projectName + "-history.json");

            Writer writer = Files.newBufferedWriter(outputPath);

            jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

        } catch (IOException ex) {
            throw new RuntimeException("Error starting history report", ex);
        }
    }

    @Override
    public void append(AMReportGitModel analysis) {
        try {
            gson.toJson(analysis, AMReportGitModel.class, jsonWriter);

            jsonWriter.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Error writing history report", ex);
        }
    }

    @Override
    public void finish() {
        try {
            if (jsonWriter != null) {
                jsonWriter.endArray();
                jsonWriter.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error finishing history report", ex);
        }
    }

}
