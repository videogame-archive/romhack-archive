package com.github.videogamearchive.model.json;

import com.github.videogamearchive.model.Parent;

import java.io.Serializable;
import java.util.Map;

public class ParentMapper extends JSONMapper<Parent> {

    protected Parent build(Map<String, Serializable> romhackAsMap) throws ReflectiveOperationException {
        return new Parent((Long) romhackAsMap.get("id"));
    }

}
