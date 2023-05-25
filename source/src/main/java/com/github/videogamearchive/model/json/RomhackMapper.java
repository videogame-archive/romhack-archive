package com.github.videogamearchive.model.json;

import com.github.videogamearchive.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RomhackMapper extends JSONMapper<Romhack> {

    protected Romhack build(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        Romhack romhack = new Romhack(
                (Long) romhackAsMap.get("id"),
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

        // Due to the missing type information the medias list contains Map instead of Media, needs to be replaced

        return romhack;
    }

}
