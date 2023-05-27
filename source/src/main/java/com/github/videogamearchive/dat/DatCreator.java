package com.github.videogamearchive.dat;
import com.github.videogamearchive.database.IdentifiableVisitor;
import com.github.videogamearchive.index.ExtendedRomhack;
import com.github.videogamearchive.model.Identifiable;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.util.PathUtil;
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
    private static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String NOW = TIMESTAMP_FORMAT.format(new Date());
    private static Map<String, Document> documents = new HashMap<>();
    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            help();
        } else {
            boolean validate;
            if (args.length == 2) {
                validate = args[1].equals("--validate");
            } else {
                validate = false;
            }
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                IdentifiableVisitor visitor = new IdentifiableVisitor() {
                    @Override
                    public boolean validate() {
                        return validate;
                    }

                    @Override
                    public void walk(File identifiableFolder, Identifiable identifiable) {
                        if (identifiable instanceof ExtendedRomhack) {
                            ExtendedRomhack xRomhack = (ExtendedRomhack) identifiable;
                            try {
                                Document systemCollectionDocument = getSystemCollectioDocument(xRomhack.system());
                                addGame(systemCollectionDocument, xRomhack.name(), xRomhack.romhack());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };

                IdentifiableVisitor.processDatabase(root, visitor);

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
        System.out.println("\t\t java -jar dat-creator.jar \"database\" [--validate]");
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        return documentBuilderFactory.newDocumentBuilder();
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
    private static Document getSystemCollectioDocument(String system) throws ParserConfigurationException {
        String name = system;
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
