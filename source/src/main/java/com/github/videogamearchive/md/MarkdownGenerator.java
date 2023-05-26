package com.github.videogamearchive.md;

import com.github.videogamearchive.index.IndexCreator;
import com.github.videogamearchive.index.IndexRomhack;
import com.github.videogamearchive.model.*;
import com.github.videogamearchive.model.json.RomhackMapper;
import fun.mingshan.markdown4j.Markdown;
import fun.mingshan.markdown4j.type.block.CodeBlock;
import fun.mingshan.markdown4j.type.block.StringBlock;
import fun.mingshan.markdown4j.type.block.TableBlock;
import fun.mingshan.markdown4j.type.block.TitleBlock;
import fun.mingshan.markdown4j.type.element.ImageElement;
import fun.mingshan.markdown4j.type.element.UrlElement;
import fun.mingshan.markdown4j.writer.MdWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MarkdownGenerator {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            help();
        } else {
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                CacheDatabase cacheDB = new CacheDatabase();
                IndexCreator.processDatabase(root, cacheDB);
                generatePages(cacheDB);
            } else {
                help();
            }
        }
    }

    private static void generatePages(CacheDatabase cacheDB) throws IOException, ReflectiveOperationException {
        generateMainIndexPage(cacheDB);
        generateSystemPage(cacheDB);
        generateGamePage(cacheDB);
        generatePatchPage(cacheDB);
        generateFilePage(cacheDB);
    }
    private static void generateFilePage(CacheDatabase cacheDB) throws IOException, ReflectiveOperationException {
        for (long id:cacheDB.filesById.keySet()) {
            IndexRomhack indexRomhack = cacheDB.filesById.get(id);
            Path path = Path.of("../docs/database/file/" + id + "/index");
            Files.createDirectories(path.getParent());
            Markdown markdown = Markdown.builder()
                    .name(path.toString())
                    .block(getBrand(3))
                    .block(StringBlock.builder().content("\n").build())
                    .block(getTitle("File: " + id))
                    .block(StringBlock.builder().content("\n").build())
                    .block(CodeBlock.builder().language("json").content(new RomhackMapper().write(indexRomhack.romhack())).build())
                    .build();
            MdWriter.write(markdown);
        }
    }
    private static void generatePatchPage(CacheDatabase cacheDB) throws IOException {
        for (long id:cacheDB.patchById.keySet()) {
            TableBlock table = getRomhacksTable(cacheDB.filesByPatch.get(id));

            Path path = Path.of("../docs/database/patch/" + id + "/index");
            Files.createDirectories(path.getParent());

            Markdown markdown = Markdown.builder()
                    .name(path.toString())
                    .block(getBrand(3))
                    .block(StringBlock.builder().content("\n").build())
                    .block(getTitle("Patch: " + id))
                    .block(StringBlock.builder().content("\n").build())
                    .block(table)
                    .build();
            MdWriter.write(markdown);
        }
    }

    private static void generateGamePage(CacheDatabase cacheDB) throws IOException {
        for (long id:cacheDB.gameById.keySet()) {
            TableBlock table = getRomhacksTable(cacheDB.filesByGame.get(id));

            Path path = Path.of("../docs/database/game/" + id + "/index");
            Files.createDirectories(path.getParent());

            Markdown markdown = Markdown.builder()
                    .name(path.toString())
                    .block(getBrand(3))
                    .block(StringBlock.builder().content("\n").build())
                    .block(getTitle("Game: " + id))
                    .block(StringBlock.builder().content("\n").build())
                    .block(table)
                    .build();
            MdWriter.write(markdown);
        }
    }

    private static void generateSystemPage(CacheDatabase cacheDB) throws IOException {
        for (long id:cacheDB.systemById.keySet()) {
            TableBlock table = getRomhacksTable(cacheDB.filesBySystem.get(id));

            Path path = Path.of("../docs/database/system/" + id + "/index");
            Files.createDirectories(path.getParent());

            Markdown markdown = Markdown.builder()
                    .name(path.toString())
                    .block(getBrand(3))
                    .block(StringBlock.builder().content("\n").build())
                    .block(getTitle("System: " + id))
                    .block(StringBlock.builder().content("\n").build())
                    .block(table)
                    .build();
            MdWriter.write(markdown);
        }
    }

    private static TableBlock getRomhacksTable(List<IndexRomhack> romhacks) {
        List<TableBlock.TableRow> rows = new ArrayList<>();
        if (romhacks != null) {
            for (IndexRomhack romhack : romhacks) {
                List<String> rowValue = new ArrayList<>();
                UrlElement element1 = UrlElement.builder().tips(romhack.parent()).url("../../game/" + romhack.gameId() + "/index.md").build();
                rowValue.add(element1.toMd());
                UrlElement element2 = UrlElement.builder().tips(romhack.name()).url("../../file/" + romhack.romhack().id() + "/index.md").build();
                rowValue.add(element2.toMd());
                TableBlock.TableRow row = new TableBlock.TableRow();
                row.setRows(rowValue);
                rows.add(row);
            }
        }
        TableBlock table = TableBlock.builder().titles(List.of("Game", "Romhack")).rows(rows).build();
        return table;
    }

    private static void generateMainIndexPage(CacheDatabase cacheDB) throws IOException {
        List<TableBlock.TableRow> rows = new ArrayList<>();

        for (Long id:cacheDB.systemNames.keySet()) {
            String name = cacheDB.systemNames.get(id);
            List<String> rowValue = new ArrayList<>();
            UrlElement element = UrlElement.builder().tips(name).url("system/" + id + "/index.md").build();
            rowValue.add(element.toMd());
            TableBlock.TableRow row = new TableBlock.TableRow();
            row.setRows(rowValue);
            rows.add(row);
        }

        TableBlock table = TableBlock.builder().titles(List.of("System")).rows(rows).build();

        Markdown markdown = Markdown.builder()
                .name("../docs/database/index")
                .block(getBrand(1))
                .block(StringBlock.builder().content("\n").build())
                .block(getTitle("Systems:"))
                .block(StringBlock.builder().content("\n").build())
                .block(table)
                .build();
        MdWriter.write(markdown);
    }

    private static TitleBlock getTitle(String title) {
        return TitleBlock.builder().level(TitleBlock.Level.FIRST).content(title).build();
    }

    private static StringBlock getBrand(int level) {
        String path = "brand/videogame-archive-(alt).png";
        while (level > 0) {
            path = "../" + path;
            level--;
        }
        return ImageElement.builder()
                .imageUrl(path)
                .build().toBlock();
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar page-creator.jar \"pathToDatabaseRoot\"");
    }

    private static class CacheDatabase implements IndexCreator.IdentifiableVisitor {
        // Systems Page - List Systems [System Name]
        // docs/database/index.html
        private final Map<Long, String> systemNames = new HashMap<>();

        // System Page - List - Games / Files [Parent Name, File Name]
        // docs/database/system/id/index.html
        private final Map<Long, List<IndexRomhack>> filesBySystem = new HashMap<>();
        private final Map<Long, System_> systemById = new HashMap<>();

        // Game Page - List - Games / Files [Parent Name, File Name]
        // docs/database/game/id/index.html
        private final Map<Long, List<IndexRomhack>> filesByGame = new HashMap<>();
        private final Map<Long, Game> gameById = new HashMap<>();

        // Patch Page - List - Games / Files [Parent Name, File Name]
        // docs/database/patch/id/index.html
        private final Map<Long, List<IndexRomhack>> filesByPatch = new HashMap<>();
        private final Map<Long, Patch> patchById = new HashMap<>();

        // File Page [All Fields]
        // docs/database/file/id/index.html
        private final Map<Long, IndexRomhack> filesById = new HashMap<>();

        @Override
        public void walk(File identifiableFolder, Identifiable identifiable) {
            if (identifiable instanceof System_) {
                System_ system = (System_) identifiable;
                systemNames.put(system.id(), identifiableFolder.getName());
                systemById.put(system.id(), system);
            } else if (identifiable instanceof Game) {
                Game game = (Game) identifiable;
                gameById.put(game.id(), game);
            } else if (identifiable instanceof IndexRomhack) {
                IndexRomhack indexRomhack = (IndexRomhack) identifiable;
                filesById.put(indexRomhack.romhack().id(), indexRomhack);

                List<IndexRomhack> filesBySystemList = filesBySystem.get(indexRomhack.systemId());
                if (filesBySystemList == null) {
                    filesBySystemList = new ArrayList<>();
                    filesBySystem.put(indexRomhack.systemId(), filesBySystemList);
                }
                filesBySystemList.add(indexRomhack);

                List<IndexRomhack> filesByGameList = filesByGame.get(indexRomhack.gameId());
                if (filesByGameList == null) {
                    filesByGameList = new ArrayList<>();
                    filesByGame.put(indexRomhack.gameId(), filesByGameList);
                }
                filesByGameList.add(indexRomhack);

                for (Patch patch:indexRomhack.romhack().patches()) {
                    patchById.put(patch.id(), patch);

                    List<IndexRomhack> filesByPatchList = filesByPatch.get(patch.id());
                    if (filesByPatchList == null) {
                        filesByPatchList = new ArrayList<>();
                        filesByPatch.put(patch.id(), filesByPatchList);
                    }
                    filesByPatchList.add(indexRomhack);
                }
            }
        }

        public Map<Long, String> getSystemNames() {
            return systemNames;
        }

        public Map<Long, List<IndexRomhack>> getFilesBySystem() {
            return filesBySystem;
        }

        public Map<Long, System_> getSystemById() {
            return systemById;
        }

        public Map<Long, List<IndexRomhack>> getFilesByGame() {
            return filesByGame;
        }

        public Map<Long, Game> getGameById() {
            return gameById;
        }

        public Map<Long, List<IndexRomhack>> getFilesByPatch() {
            return filesByPatch;
        }

        public Map<Long, Patch> getPatchById() {
            return patchById;
        }

        public Map<Long, IndexRomhack> getFilesById() {
            return filesById;
        }
    }
}
