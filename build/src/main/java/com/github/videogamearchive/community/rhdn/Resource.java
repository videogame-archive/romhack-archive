package com.github.videogamearchive.community.rhdn;

import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.util.CSV;
import com.github.videogamearchive.util.StringUtil;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.videogamearchive.hack2arch.Romhack2Archive.archiveFormat;

public class Resource {

    private static final SimpleDateFormat rhdnFormat = new SimpleDateFormat("dd MMMMM YYYY");
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

    public static Patch getPatch(String url) throws ParseException {
        CSVRecord info = cache.get(url);
        if (info == null) {
            return null;
        }

        List<String> authors = new ArrayList<>();
        String releasedByAsString = info.get("Released By");
        if (releasedByAsString != null && !releasedByAsString.isBlank()) {
            List<String> releasedBy = StringUtil.substrings(releasedByAsString, "[", "]", true);
            authors.addAll(releasedBy);
        }
        String creditsAsString = info.get("Credits");
        if (creditsAsString != null && !creditsAsString.isBlank()) {
            List<String> credits = StringUtil.substrings(creditsAsString, "[", "]", true);
            for (String credit : credits) {
                if (!authors.contains(credit)) {
                    authors.add(credit);
                }
            }
        }

        String shortAuthors = null;
        // String url = url;
        String version = info.get("Patch Version");
        String releaseDate = info.get("Last Modified");
        if (releaseDate != null && !releaseDate.isBlank()) {
            releaseDate = StringUtil.substring(releaseDate, "[", "]", true);
            Date releaseDateAsDate = rhdnFormat.parse(releaseDate);
            releaseDate = archiveFormat.format(releaseDateAsDate);
        }

        String alternative = null;
        List<String> labels = new ArrayList<>();

        return new Patch(authors, shortAuthors, url, version, releaseDate, alternative, labels);
    }
}
