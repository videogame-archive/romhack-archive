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

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
*
* The palette pane contains the following components:
* - Foreground color box
* - Background color box
* - Palette vizualiser (see TMPaletteVizualiser)
* - FG/BG swap button
* - Arrow up/down for switching palette index
*
**/

public class TMPalettePane extends JPanel implements MouseInputListener {

    private TMView view;
    private TMUI ui;
    private TMPaletteVizualiser vizualiser = new TMPaletteVizualiser();
    private ColorBox fgColorBox = new ColorBox();
    private ColorBox bgColorBox = new ColorBox();

    ClassLoader cl = getClass().getClassLoader();
    private Cursor pickupCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(cl.getResource("icons/Dropper24.gif")).getImage(), new Point(8,23), "Dropper");
    private JButton decButton = new JButton(new ImageIcon(cl.getResource("icons/DecPalIndex24.gif")));
    private JButton incButton = new JButton(new ImageIcon(cl.getResource("icons/IncPalIndex24.gif")));
    private JButton swapButton = new JButton(new ImageIcon(cl.getResource("icons/Swap24.gif")));

/**
*
* Creates a palette pane.
*
**/

    public TMPalettePane(TMUI ui) {
        this.ui = ui;
        vizualiser = new TMPaletteVizualiser();
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        swapButton.setBorder(null);
        // set up UI
        setLayout(null);    // no layout manager, want to place and size components pixel-perfect

        // add components
        add(fgColorBox);
        add(bgColorBox);
        add(swapButton);
        add(vizualiser);
        add(decButton);
        add(incButton);

        // set sizes
        fgColorBox.setSize(32, 32);
        bgColorBox.setSize(32, 32);
        swapButton.setSize(32, 32);
        vizualiser.setSize(256, 64);
        decButton.setSize(32, 64);
        incButton.setSize(32, 64);

        // set positions
        fgColorBox.setLocation(8, 8);
        bgColorBox.setLocation(40, 40);
        swapButton.setLocation(8, 40);
        decButton.setLocation(80, 8);
        vizualiser.setLocation(112, 8);
        incButton.setLocation(368, 8);

        vizualiser.setCursor(pickupCursor);
        vizualiser.addMouseListener(this);

        swapButton.setFocusable(false);
        swapButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    swapColors();
                }
            }
        );
        decButton.setFocusable(false);
        decButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setPreviousPalIndex();
                }
            }
        );
        incButton.setFocusable(false);
        incButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setNextPalIndex();
                }
            }
        );

        setPreferredSize(new Dimension(2048, 80));
    }

/**
*
* Sets the palette to be rendered.
*
**/

    public void setPalette(TMPalette palette) {
        vizualiser.setPalette(palette);
    }

/**
*
* Sets the palette index from which to start displaying colors.
*
**/

    public void setPalIndex(int palIndex) {
        vizualiser.setPalIndex(palIndex);
    }

/**
*
* Sets the bitdepth, i.e. how many colors to display.
*
**/

    public void setBitDepth(int bitDepth) {
        vizualiser.setBitDepth(bitDepth);
    }

/**
*
* Sets the foreground color.
*
**/

    public void setFGColor(int fgColor) {
        fgColorBox.setColor(fgColor);
    }

/**
*
* Sets the background color.
*
**/

    public void setBGColor(int bgColor) {
        bgColorBox.setColor(bgColor);
    }

/**
*
* Called when a view has been selected.
* Loads and displays the view's palette according to current settings.
*
**/

    public void viewSelected(TMView view) {
        this.view = view;
        // get view palette,color settings
        setPalette(view.getPalette());
        setPalIndex(view.getPalIndex());
        setBitDepth(view.getTileCodec().getBitsPerPixel());
        setFGColor(view.getFGColor());
        setBGColor(view.getBGColor());
        repaint();
    }

/**
*
* Called when user clicked on a color.
* Set the color as foreground or background color depending on which button was pressed.
*
**/

    public void mousePressed(MouseEvent e) {
        // get the color
        int color = vizualiser.getColorAt(e.getX(), e.getY());
        // set it
        if (e.getButton() == MouseEvent.BUTTON1) {
            // set as foreground color
            ui.setFGColor(color);
        }
        else {
            // set as background color
            ui.setBGColor(color);
        }
    }

// Other mouse events, not used yet...

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            // let user edit the color
            Color newColor = JColorChooser.showDialog(ui, "Edit Color", new Color(fgColorBox.getColor()));
            if (newColor != null) {
                boolean equal = view.getFGColor() == view.getBGColor();
                int rgb = newColor.getRGB();
                int colorIndex = vizualiser.getIndexOfColorAt(e.getX(), e.getY());
                TMPalette palette = view.getPalette();

                /* view.addReversibleAction(
                    new ReversiblePaletteEditAction(
                        view,
                        palette,
                        colorIndex,
                        palette.getEntryRGB(colorIndex),
                        rgb
                    )
                ;*/

                // set the new color(s)
                palette.setEntryRGB(colorIndex, rgb);
                ui.setFGColor(palette.getEntryRGB(colorIndex));
                if (equal) {
                    ui.setBGColor(palette.getEntryRGB(colorIndex));
                }

                // PS: If palette is NOT direct then this means fileimage.modified!!
                if (!palette.isDirect()) {
                    byte[] src = palette.getEntryBytes(colorIndex);
                    byte[] dest = view.getFileImage().getContents();
                    System.arraycopy(src, 0, dest, palette.getOffset()+(colorIndex*src.length), src.length);
                    ui.fileImageModified(view.getFileImage());
                }

                // redraw stuff
                view.getEditorCanvas().unpackPixels();
                view.getEditorCanvas().redraw();
                repaint();
            }
        }
    }

    public void mouseDragged(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

/**
*
* A "color box" is merely a label that is painted with a color.
*
**/

    private class ColorBox extends JLabel {

        private int color;

        public void setColor(int color) {
            this.color = color;
            repaint();
        }

        public int getColor() {
            return color;
        }

        public void paintComponent(Graphics g) {
            g.setColor(new Color(color));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

    }

/**
*
* Switches to the previous palette index.
* Wrap-around is employed if the current index is the first one (0).
*
**/

    public void setPreviousPalIndex() {
        view.setKeysEnabled(false);
        //
        int pi = view.getPalIndex();
        pi = (pi == 0) ? view.getPalIndexMaximum() : pi-1;
        view.mapDrawColorsToPalIndex(pi);
        view.setPalIndex(pi);
        viewSelected(view);
        //
        view.setKeysEnabled(true);
    }

/**
*
* Switches to the next palette index.
* Wrap-around is employed if the current index is the last one (maximum).
*
**/

    public void setNextPalIndex() {
        view.setKeysEnabled(false);
        //
        int pi = view.getPalIndex();
        pi = (pi == view.getPalIndexMaximum()) ? 0 : pi+1;
        view.mapDrawColorsToPalIndex(pi);
        view.setPalIndex(pi);
        viewSelected(view);
        //
        view.setKeysEnabled(true);
    }

/**
*
* Swaps the foreground and background colors.
*
**/

    public void swapColors() {
        int fg = fgColorBox.getColor();
        ui.setFGColor(bgColorBox.getColor());
        ui.setBGColor(fg);
    }

}