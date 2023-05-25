package com.github.videogamearchive.model.json;

import com.github.videogamearchive.model.*;

import java.io.Serializable;
import java.util.Map;

public class SystemMapper extends JSONMapper<System_> {

    protected System_ build(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        return new System_((Long) romhackAsMap.get("id"));
    }

}
