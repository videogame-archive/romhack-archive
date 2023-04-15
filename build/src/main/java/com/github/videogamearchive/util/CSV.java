package com.github.videogamearchive.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class CSV {
    private CSV() {
    }

    public static List<CSVRecord> read(InputStream in) throws IOException {
        Reader reader = new InputStreamReader(in);
        CSVParser parse = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        List<CSVRecord> records = parse.getRecords();
        parse.close();
        return records;
    }

    public static void write(Path file, String[] headers, List<String[]> rows) throws IOException {
        final var csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(headers).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(file.toFile(), StandardCharsets.UTF_8), csvFormat)) {
            csvPrinter.printRecords(rows);
        }
    }

    private static final String EMPTY_STRING = "";

    public static String toString(List records) {
        if (records == null) {
            return EMPTY_STRING;
        }
        StringBuilder builder = new StringBuilder();
        for (Object record:records) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(record.toString());
        }
        return builder.toString();
    }
}
