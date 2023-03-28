package com.github.openretrogamingarchive.dat;
import com.github.openretrogamingarchive.model.Romhack;
import com.github.openretrogamingarchive.model.RomhackReaderWriter;
import com.github.openretrogamingarchive.model.RomhackValidator;
import com.github.openretrogamingarchive.util.PathUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatCreator {
    private static final String ROMHACK_JSON = "romhack.json";

    private static final String ROMHACK_BPS = "romhack.bps";

    private static final String ROMHACK_ORIGINAL = "romhack-original";
    private static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String NOW = TIMESTAMP_FORMAT.format(new Date());
    private static Map<String, Document> documents = new HashMap<>();

    private static RomhackReaderWriter romhackReader = new RomhackReaderWriter();
    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            help();
        } else {
            boolean validate = false;
            if (args.length == 2) {
                validate = args[1].equals("validate");
            }
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                for (File systemFolder:root.listFiles()) {
                    processSystem(systemFolder, validate);
                }
                for (String name:documents.keySet()) {
                    output(documents.get(name), Files.newOutputStream(Path.of(name + ".xml")), true);
                }
            } else {
                help();
            }
        }
    }
    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar dat-creator.jar \"pathToArchiveRoot\" [\"validate\"]");
    }

    private static void processSystem(File system, boolean validate) throws ParserConfigurationException, IOException, ReflectiveOperationException {
        if (!system.isDirectory()) {
            ignored(system);
            return;
        } else {
            processing(system);
        }
        for (File parent:system.listFiles()) {
            if (!parent.isDirectory()) {
                ignored(parent);
                continue;
            } else {
                processing(parent);
            }
            for (File clone:parent.listFiles()) {
                if (!clone.isDirectory()) {
                    ignored(clone);
                    continue;
                }
                File romhackJSON = null;
                File romhackBPS = null;
                File romhackOriginal = null;
                for (File file:clone.listFiles()) {
                    if (file.getName().equals(ROMHACK_JSON) && file.isFile()) {
                        romhackJSON = file;
                    } else if (file.getName().equals(ROMHACK_BPS) && file.isFile()) {
                        romhackBPS = file;
                    } else if(file.getName().equals(ROMHACK_ORIGINAL) && file.isDirectory() && file.listFiles().length > 0) {
                        romhackOriginal = file;
                    }
                }
                if (romhackJSON == null || romhackBPS == null || romhackOriginal == null) {
                    ignored(clone);
                    continue;
                } else {
                    processing(clone);
                }

                Romhack romhack = romhackReader.read(romhackJSON.toPath());
                String folderName = clone.getName();

                if (validate) {
                    RomhackValidator.validateRomHashLength(romhack);
                    RomhackValidator.validateFolder(romhack, clone.toPath());
                    RomhackValidator.validateBPS(romhack, clone.toPath().resolve("romhack.bps"));
                }

                String romhackPrimaryCollection = romhack.patches().get(0).labels().get(0);
                for (String collection:romhack.patches().get(0).labels()) {
                    Document systemCollectionDocument = getSystemCollectioDocument(system.getName(), collection.strip());
                    addGame(systemCollectionDocument, folderName.replace("[" + romhackPrimaryCollection, "[" + collection), romhack);
                }
            }
        }
    }
    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        return documentBuilderFactory.newDocumentBuilder();
    }
    private static void processing(File file) {
        System.out.println("Processing folder: " + file.getPath());
    }
    private static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }

    private static void addGame(Document doc, String fileName, Romhack romhack) {
        Element game = doc.createElement("game");
        game.setAttribute("name", PathUtil.getNameWithoutExtension(fileName));

        Element description = doc.createElement("description");
        description.setTextContent(romhack.info().name());

        Element rom = doc.createElement("rom");
        rom.setAttribute("name", fileName);
        rom.setAttribute("size", Long.toString(romhack.rom().size()));
        rom.setAttribute("crc", romhack.rom().crc32());
        rom.setAttribute("md5", romhack.rom().md5());
        rom.setAttribute("sha1", romhack.rom().sha1());

        doc.getElementsByTagName("datafile").item(0).appendChild(game);
        game.appendChild(description);
        game.appendChild(rom);
    }
    private static Document getSystemCollectioDocument(String system, String collection) throws ParserConfigurationException {
        String name = system + " ("+ collection + ")";
        Document document = documents.get(name);
        if (document == null) {
            document = getDocument();
            Element datafile = datafile(document);
            datafile.appendChild(header(document, name));
            documents.put(name, document);
        }
        return document;
    }
    public static Document getDocument() throws ParserConfigurationException {
        Document doc = getDocumentBuilder().newDocument();
        doc.setXmlStandalone(true);
        return doc;
    }
    private static Element datafile(Document doc) {
        Element data = doc.createElement("datafile");
        doc.appendChild(data);
        return data;
    }
    private static Element header(Document doc, String name) {
        Element header = doc.createElement("header");
        header.appendChild(element(doc, "name", name)); // system (collection)
        header.appendChild(element(doc, "description", name + " (" + NOW + ")")); // name (date)
        header.appendChild(element(doc, "version", NOW)); // date
        header.appendChild(element(doc, "date", NOW)); //YYYY-MM-DD HH-MM-SS
        header.appendChild(element(doc, "author", "videogame-archive"));
        header.appendChild(element(doc, "homepage", "romhack-archive"));
        header.appendChild(element(doc, "url", "https://github.com/videogame-archive/romhack-archive"));
        return header;
    }
    private static Element element(Document doc, String key, String value) {
        Element element = doc.createElement(key);
        element.setTextContent(value);
        return element;
    }
    public static void output(Document doc, OutputStream output, boolean includeDocType) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        if (includeDocType) {
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Logiqx//DTD ROM Management Datafile//EN");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.logiqx.com/Dats/datafile.dtd");
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}
