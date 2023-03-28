package com.github.openretrogamingarchive.model;

import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class RomhackReaderWriter extends ReaderWriter {

    public Romhack read(String string) throws IOException, ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(new ByteArrayInputStream(string.getBytes(charset)), charset));
        return read(jsonParser);
    }

    public Romhack read(Path path) throws IOException, ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(Files.newInputStream(path), charset));
        return read(jsonParser);
    }

    private Romhack read(JsonParser jsonParser) throws IOException, ReflectiveOperationException {
        jsonParser.next();
        JsonObject object = jsonParser.getObject();
        Map<String, Serializable> jsonMap = getMap(object);
        return buildRomhack(jsonMap);
    }

    //
    // Read : Data binding to Romhack type
    //

    private Romhack buildRomhack(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        return new Romhack(
                buildObject(Info.class, (Map<String, Serializable>) romhackAsMap.get("info")),
                buildObject(Provenance.class, (Map<String, Serializable>) romhackAsMap.get("provenance")),
                buildObject(Rom.class, (Map<String, Serializable>) romhackAsMap.get("rom")),
                buildList(Patch.class, (List<Map<String, Serializable>>) romhackAsMap.get("patches"))
            );
    }

}
