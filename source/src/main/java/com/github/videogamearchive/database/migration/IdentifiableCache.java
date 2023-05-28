package com.github.videogamearchive.database.migration;

import com.github.videogamearchive.model.Identifiable;

import java.util.HashMap;
import java.util.Map;

public class IdentifiableCache<RECORD extends Identifiable<RECORD>> {
    private Map<String, Long> assignedIds = new HashMap<>();
    private Long lastId = null;

    public RECORD updateLastId(boolean dryRun, String signature, RECORD identifiable) {
        if (identifiable.id() == null && !dryRun) { // Update
            Long assignedValue = assignedIds.get(signature);
            if (assignedValue == null) {
                if (lastId == null) {
                    assignedValue = 1L; // First value
                } else {
                    assignedValue = lastId + 1;
                }
            }
            identifiable = identifiable.withId(assignedValue);
        }

        // Update stored id
        if (identifiable.id() != null) {
            if (lastId == null || lastId < identifiable.id()) {
                lastId = identifiable.id();
            }
        }
        // Update assigned ids
        if (!assignedIds.containsKey(signature) && identifiable.id() != null) {
            assignedIds.put(signature, identifiable.id());
        }

        return identifiable;
    }
}
