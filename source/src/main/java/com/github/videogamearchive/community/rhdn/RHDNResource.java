package com.github.videogamearchive.community.rhdn;

import com.github.videogamearchive.model.Label;
import com.github.videogamearchive.model.Hack;
import com.github.videogamearchive.util.CSV;
import com.github.videogamearchive.util.PatchResource;
import com.github.videogamearchive.util.StringUtil;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static com.github.videogamearchive.hack2release.Romhack2Release.archiveFormat;

public class RHDNResource implements PatchResource {
    private static DateTimeFormatter rhdnFormat = DateTimeFormatter.ofPattern ("d MMMM yyyy").withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault());
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
        InputStream input = RHDNResource.class.getResourceAsStream("hacks.csv");
        if (input == null) {
            return Collections.emptyList();
        }
        return CSV.read(input);
    }

    private static List<CSVRecord> getTranslations() throws IOException {
        InputStream input = RHDNResource.class.getResourceAsStream("translations.csv");
        if (input == null) {
            return Collections.emptyList();
        }
        return CSV.read(input);
    }

    public static CSVRecord getInfo(String url) {
        return cache.get(url);
    }

    public Hack getPatch(String url) {
        CSVRecord info = cache.get(url);
        if (info == null) {
            return null;
        }

        Long id = null;
        String name = null;
        if (info.isMapped("Name")) { // hacks have names
            name = info.get("Name");
        } else if (info.isMapped("Language")) { // translations have a language
            name = StringUtil.substring(info.get("Language"), "[", "]", false) + " translation for " + StringUtil.substring(info.get("Game"), "[", "]", false);
        }
        List<String> authors = new ArrayList<>();
        String shortAuthors = null;
        String releasedByAsString = info.get("Released By");
        if (releasedByAsString != null && !releasedByAsString.isBlank()) {
            List<String> releasedBy = StringUtil.substrings(releasedByAsString, "[", "]", true);
            authors.addAll(releasedBy);
            shortAuthors = CSV.toString(releasedBy);
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

        // String url = url;
        List<String> otherUrls = List.of();
        String version = info.get("Patch Version");
        String releaseDate = info.get("Release Date");
        if (releaseDate != null && !releaseDate.isBlank()) {
            //releaseDate = StringUtil.substring(releaseDate, "[", "]", true);
            TemporalAccessor releaseDateAsDate = rhdnFormat.parse(releaseDate);
            releaseDate = archiveFormat.format(releaseDateAsDate);
        }

        List<String> options = null;
        String shortOptions = null;
        List<Label> labels = new ArrayList<>();

        return new Hack(id, name, authors, shortAuthors, url, otherUrls, version, releaseDate, options, shortOptions, labels, List.of());
    }
}
