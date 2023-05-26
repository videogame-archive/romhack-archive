package com.github.videogamearchive.model.json;

import com.github.videogamearchive.model.Game;

import java.io.Serializable;
import java.util.Map;

public class GameMapper extends JSONMapper<Game> {

    protected Game build(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        return new Game((String) romhackAsMap.get("name"), (Long) romhackAsMap.get("id"));
    }

}
