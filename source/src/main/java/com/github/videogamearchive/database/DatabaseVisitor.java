package com.github.videogamearchive.database;

import com.github.videogamearchive.model.Identifiable;

import java.io.File;

public interface DatabaseVisitor {

    boolean validate();
    void walk(File identifiableFolder, Identifiable identifiable);

}
