package com.github.videogamearchive.model.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JSONMapper<RECORD extends Record> {
    protected static final JsonProvider jsonProvider = JsonProvider.provider();

    //
    // Charset
    //

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected Charset charset = DEFAULT_CHARSET;

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    //
    // Time formats
    //

    public static DateTimeFormatter ISO_OFFSET_DATE_TIME_UTC = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);
    public static DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    public static DateTimeFormatter ISO_LOCAL_TIME = DateTimeFormatter.ISO_LOCAL_TIME;

    public void write(Record record, Path pathToFile) throws ReflectiveOperationException, IOException {
        String json = write(record);
        Files.write(pathToFile, json.getBytes(charset));
    }
    public String write(Record record) throws ReflectiveOperationException {
        JsonObject json = buildJson(record);

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(json);
        jsonWriter.close();
        String jsonAsString = stringListsFormatter(sw.toString());
        int indexOfStart = jsonAsString.indexOf("{");
        return jsonAsString.substring(indexOfStart);
    }

    private String stringListsFormatter(String input) {
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        int start = -1;
        int end = -1;
        boolean objectFound = false;
        while(offset < input.length()) {
            char character = input.charAt(offset);
            builder.append(character);
            if (character == '[') {
                objectFound = false; // Resets
                start = builder.length() - 1;
            }
            if (character == ']') {
                end = builder.length();
            }
            if (character == '{') {
                objectFound = true;
            }
            if (start != -1 && end != -1 && !objectFound) {
                String match = builder.substring(start, end);
                String removeNewLine = match.replace("\n", "").replaceAll(" +", " ");
                builder.replace(start, end, removeNewLine);
            }
            if (start != -1 && end != -1) { // reset
                start = -1;
                end = -1;
            }
            offset++;
        }
        return builder.toString();
    }

    //
    // Write : Data binding to Record type
    //

    private JsonArray buildJson(List<?> values) throws ReflectiveOperationException {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Object value: values) {
            if(value == null) {
                builder.addNull();
            } else if (value instanceof Record) {
                builder.add(buildJson((Record) value));
            } else if (value instanceof List) {
                builder.add(buildJson((List<?>) value));
            } else if(value instanceof Enum) {
                builder.add(value.toString());
            } else if (value instanceof String) {
                builder.add((String) value);
            } else if (value instanceof Long) {
                builder.add((Long) value);
            } else if (value instanceof Boolean) {
                builder.add((Boolean) value);
            }
        }
        return builder.build();
    }

    private <E extends Record> JsonObject buildJson(E record) throws ReflectiveOperationException {
        Class<?> clazz = record.getClass();
        Map<String, Method> methodsByName = new HashMap<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method:methods) {
            methodsByName.put(method.getName(), method);
        }

        Constructor<?> constructor = clazz.getConstructors()[0];
        Parameter[] constructorParameters = constructor.getParameters();

        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Parameter constructorParameter : constructorParameters) {
            String parameterNameIsMethodName = constructorParameter.getName();
            Method method = methodsByName.get(parameterNameIsMethodName);
            Object value = method.invoke(record);

            if(value == null) {
                builder.addNull(parameterNameIsMethodName);
            } else if (value instanceof Record) {
                builder.add(parameterNameIsMethodName, buildJson((Record) value));
            } else if (value instanceof List) {
                builder.add(parameterNameIsMethodName, buildJson((List<?>) value));
            } else if(value instanceof Enum) {
                builder.add(parameterNameIsMethodName, value.toString());
            } else if (value instanceof String) {
                builder.add(parameterNameIsMethodName, (String) value);
            } else if (value instanceof Long) {
                builder.add(parameterNameIsMethodName, (Long) value);
            } else if (value instanceof Boolean) {
                builder.add(parameterNameIsMethodName, (Boolean) value);
            }
        }

        return builder.build();
    }

    //
    // Read
    //

    public RECORD read(Path path) throws IOException, ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(Files.newInputStream(path), charset));
        return read(jsonParser);
    }

    public RECORD read(String string) throws ReflectiveOperationException {
        JsonParser jsonParser = jsonProvider.createParser(new InputStreamReader(new ByteArrayInputStream(string.getBytes(charset)), charset));
        return read(jsonParser);
    }

    private RECORD read(JsonParser jsonParser) throws ReflectiveOperationException {
        jsonParser.next();
        JsonObject object = jsonParser.getObject();
        Map<String, Serializable> jsonMap = getMap(object);
        return build(jsonMap);
    }

    protected abstract RECORD build(Map<String, Serializable> jsonMap) throws ReflectiveOperationException;

    protected <E> List<E> buildList(Class<E> clazz, List<Map<String, Serializable>> patchesAsListOfMaps) throws ReflectiveOperationException {
        List<E> patches = new ArrayList<>(patchesAsListOfMaps.size());
        for (Map<String, Serializable> patchAsMap:patchesAsListOfMaps) {
            patches.add(buildObject(clazz, patchAsMap));
        }
        return patches;
    }


    protected <E> E buildObject(Class<E> clazz, Map<String, Serializable> parameters) throws ReflectiveOperationException {
        Constructor<E> constructor = (Constructor<E>) clazz.getConstructors()[0];
        Parameter[] constructorParameters = constructor.getParameters();
        Object[] initParameters = new Object[constructorParameters.length];
        for (int i = 0;i < constructorParameters.length; i++) {
            Parameter parameter = constructorParameters[i];
            Class<?> type = parameter.getType();
            String parameterName = parameter.getName();
            if (type == String.class ||
                type == Long.class ||
                    type == Boolean.class ||
                    type == List.class ||
                type == Map.class ||
                    parameters.get(parameterName) == null) {
                initParameters[i] = parameters.get(parameterName);
            } else if (type.isEnum()) {
                initParameters[i] = Enum.valueOf((Class<Enum>) type, ((String) parameters.get(parameterName)).replace(" ", "").replace("-", ""));
            } else {
                initParameters[i] = buildObject(type, (Map<String, Serializable>) parameters.get(parameterName));
            }
        }
        return constructor.newInstance(initParameters);
    }

    private String toStringClass(Object[] objects) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            if (objects[i] == null) {
                builder.append("null");
            } else {
                builder.append(objects[i].getClass().getSimpleName());
            }
        }
        builder.append("]");
        return builder.toString();
    }

    //
    // Section: Json deserialization without Data Binding
    //

    protected Serializable getValue(JsonValue jsonValue) {
        if (jsonValue == null) {
            return null;
        }

        switch (jsonValue.getValueType()) {
            case ARRAY:
                return getList((JsonArray) jsonValue);
            case OBJECT:
                return getMap((JsonObject) jsonValue);
            case STRING:
                return ((JsonString) jsonValue).getString();
            case NUMBER:
                return ((JsonNumber) jsonValue).longValueExact();
            case TRUE:
                return Boolean.TRUE;
            case FALSE:
                return Boolean.FALSE;
            case NULL:
                return null;
        }

        throw new RuntimeException("json object could not be parsed.");
    }

    protected ArrayList<Serializable> getList(JsonArray jsonArray) {
        ArrayList<Serializable> list = new ArrayList<>(jsonArray.size());
        for (JsonValue value:jsonArray) {
            list.add(getValue(value));
        }
        return list;
    }

    protected HashMap<String, Serializable> getMap(JsonObject jsonObject) {
        HashMap<String, Serializable> map = new HashMap<>(jsonObject.size());
        for (String key:jsonObject.keySet()) {
            map.put(key, getValue(jsonObject.get(key)));
        }
        return map;
    }
}
