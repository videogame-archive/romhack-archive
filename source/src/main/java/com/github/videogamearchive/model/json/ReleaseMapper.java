package com.github.videogamearchive.model.json;

import com.github.videogamearchive.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReleaseMapper extends JSONMapper<Release> {

    protected Release build(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        Release romhack = new Release(
                (Long) romhackAsMap.get("id"),
                buildObject(Info.class, (Map<String, Serializable>) romhackAsMap.get("info")),
                buildObject(Provenance.class, (Map<String, Serializable>) romhackAsMap.get("provenance")),
                buildObject(Rom.class, (Map<String, Serializable>) romhackAsMap.get("rom")),
                buildList(Hack.class, (List<Map<String, Serializable>>) romhackAsMap.get("hacks"))
            );

        // Due to the missing type information the labels list contains String instead of Enum, needs to be replaced
        for (Hack patch:romhack.hacks()) {
            List<Label> labels = new ArrayList<>();
            for (Object labelAsString:patch.labels()) {
                Label label = Enum.valueOf(Label.class, labelAsString.toString().replace(" ", "").replace("-", "") );
                labels.add(label);
            }
            patch.labels().clear();
            patch.labels().addAll(labels);
        }

        //TODO - Due to the missing type information the medias list contains Map instead of Media, needs to be replaced
        for (Hack patch:romhack.hacks()) {
            List<Media> medias = new ArrayList<>();
            if (patch.medias() != null) {
                for (Object mediaAsObject : patch.medias()) {
                    Map<String, String> mediaAsMap = (Map<String, String>) mediaAsObject;
                    String url = mediaAsMap.get("url");
                    String filename = mediaAsMap.get("filename");
                    MediaType mediaType = Enum.valueOf(MediaType.class, mediaAsMap.get("mediaType").toString().replace(" ", "").replace("-", ""));
                    ;
                    Media media = new Media(url, filename, mediaType);
                    medias.add(media);
                }
                patch.medias().clear();
                patch.medias().addAll(medias);
            }
        }

        return romhack;
    }

}
