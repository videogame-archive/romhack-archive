package com.github.videogamearchive.community.rhdn;

import com.github.videogamearchive.util.CSV;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {

    private static final Map<String, CSVRecord> cache = new HashMap<>();

    static {
        try {
            for (CSVRecord record:getTranslations()) {
                cache.put(record.get("Url"), record);
            }
            for (CSVRecord record:getHacks()) {
                cache.put(record.get("Url"), record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<CSVRecord> getHacks() throws IOException {
        InputStream input = Resource.class.getResourceAsStream("hacks.csv");
        return CSV.read(input);
    }

    private static List<CSVRecord> getTranslations() throws IOException {
        InputStream input = Resource.class.getResourceAsStream("translations.csv");
        return CSV.read(input);
    }

    public static CSVRecord getInfo(String url) {
        return cache.get(url);
    }
}
