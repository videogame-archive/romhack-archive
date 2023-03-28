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

/**
*
* Allows undo/redo of making a new selection.
*
**/

public class ReversibleNewSelectionAction extends ReversibleAction {

    private TMSelectionCanvas oldSelection;
    private TMSelectionCanvas newSelection;
    private TMEditorCanvas owner;
    private int oldX, oldY;
    private int newX, newY;

    public ReversibleNewSelectionAction(TMSelectionCanvas oldSelection, TMSelectionCanvas newSelection, TMEditorCanvas owner) {
        super("New Selection");   // i18n
        this.oldSelection = oldSelection;
        this.newSelection = newSelection;
        this.owner = owner;
        if (oldSelection != null) {
            oldX = oldSelection.getX() / oldSelection.getScaledTileDim();
            oldY = oldSelection.getY() / oldSelection.getScaledTileDim();
        }
        newX = newSelection.getX() / newSelection.getScaledTileDim();
        newY = newSelection.getY() / newSelection.getScaledTileDim();
    }

    public void undo() {
        owner.encodeSelection();    // newSelection
        if (oldSelection != null) {
            owner.showSelection(oldSelection, oldX, oldX);
        }
        owner.repaint();
    }

    public void redo() {
        owner.encodeSelection();    // oldSelection
        owner.showSelection(newSelection, newX, newX);
        owner.repaint();
    }

    public boolean canUndo() { return true; }
    public boolean canRedo() { return true; }

}