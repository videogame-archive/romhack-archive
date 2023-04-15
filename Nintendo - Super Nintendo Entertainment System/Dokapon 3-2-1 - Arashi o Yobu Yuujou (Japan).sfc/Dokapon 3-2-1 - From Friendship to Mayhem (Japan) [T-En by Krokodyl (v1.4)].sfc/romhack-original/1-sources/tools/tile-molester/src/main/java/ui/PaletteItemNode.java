package ui;/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Molester.
*
*    Tile Molester is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Molester is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

import treenodes.*;
import colorcodecs.ColorCodec;
import utils.HexStringConverter;

/**
*
* A palette node.
*
**/

public class PaletteItemNode extends TMTreeNode {

    private TMPalette palette;
    private String description;

    public PaletteItemNode(TMPalette palette, String description) {
        super();
        this.palette = palette;
        this.description = description;
    }

/**
*
* Gets the palette represented by this node.
*
**/

    public TMPalette getPalette() {
        return palette;
    }

/**
*
* Gets the palette description.
*
**/

    public String getDescription() {
        return description;
    }

/**
*
* Sets the palette description.
*
**/

    public void setDescription(String description) {
        this.description = description;
        setModified(true);
    }

/**
*
* Returns the XML-equivalent of this palette node.
*
**/

    public String toXML() {
        StringBuffer s = new StringBuffer();
        s.append(getIndent());
        s.append("<palette");
//        s.append(" id=\""+palette.getID()+"\"");
        s.append(" size=\""+palette.getSize()+"\"");
        s.append(" direct=\""+(palette.isDirect() ? "yes" : "no")+"\"");
        if (!palette.isDirect()) {
            s.append(" offset=\""+palette.getOffset()+"\"");
        }
        s.append(" codec=\""+palette.getCodec().getID()+"\"");
        s.append(" endianness=\""+(palette.getEndianness() == ColorCodec.LITTLE_ENDIAN ? "little" : "big")+"\"");
        s.append(">\n");
        s.append(getIndent()+"  ");
        s.append("<description>"+description+"</description>\n");
        if (palette.isDirect()) {
            s.append(getIndent()+"  ");
            s.append("<data>");
            s.append(HexStringConverter.bytesToHexString(palette.entriesToBytes()));
            s.append("</data>\n");
        }
        s.append(getIndent());
        s.append("</palette>\n");
        return s.toString();
    }

/**
*
* Returns the string representation of this node.
*
**/

    public String toString() {
        return description;
    }

/**
*
* Sets the text of this node.
*
**/

    public void setText(String text) {
        setDescription(text);
    }

/**
*
* Gets the tooltiptext of this node.
*
**/

    public String getToolTipText() {
        return "Size: "+palette.getSize()+" ... Format: "+palette.getCodec().getDescription();
    }

}