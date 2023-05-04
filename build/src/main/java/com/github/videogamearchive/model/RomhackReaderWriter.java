package com.github.videogamearchive.model;

import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RomhackReaderWriter extends ReaderWriter {

    public Romhack read(Path path) throws IOException, ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(Files.newInputStream(path), charset));
        return read(jsonParser);
    }

    public Romhack read(String string) throws ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(new ByteArrayInputStream(string.getBytes(charset)), charset));
        return read(jsonParser);
    }

    private Romhack read(JsonParser jsonParser) throws ReflectiveOperationException {
        jsonParser.next();
        JsonObject object = jsonParser.getObject();
        Map<String, Serializable> jsonMap = getMap(object);
        return buildRomhack(jsonMap);
    }

    //
    // Read : Data binding to Romhack type
    //

    private Romhack buildRomhack(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        Romhack romhack = new Romhack(
                buildObject(Info.class, (Map<String, Serializable>) romhackAsMap.get("info")),
                buildObject(Provenance.class, (Map<String, Serializable>) romhackAsMap.get("provenance")),
                buildObject(Rom.class, (Map<String, Serializable>) romhackAsMap.get("rom")),
                buildList(Patch.class, (List<Map<String, Serializable>>) romhackAsMap.get("patches"))
            );

        // Due to the missing type information the labels list contains String instead of Enum, needs to be replaced
        for (Patch patch:romhack.patches()) {
            List<Label> labels = new ArrayList<>();
            for (Object labelAsString:patch.labels()) {
                Label label = Enum.valueOf(Label.class, labelAsString.toString().replace(" ", "").replace("-", "") );
                labels.add(label);
            }
            patch.labels().clear();
            patch.labels().addAll(labels);
        }

        return romhack;
    }

}
