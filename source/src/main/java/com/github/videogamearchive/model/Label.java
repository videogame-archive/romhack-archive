package com.github.videogamearchive.model;

public enum Label {
    /*
     * Category Labels, Translations
     */
    TEn("T-En"),
    TEs("T-Es"),
    TFr("T-fr"),
    TDe("T-de"),
    TIt("T-it"),
    TJa("T-ja"),
    /*
     * Category Labels
     */
    Game("Game"),
    Overhaul("Overhaul"),
    Themed("Themed"),
    Colorization("Colorization"),
    Polish("Polish"),
    Fix("Fix"),
    Cheat("Cheat"),
    EasyType("EasyType"),
    HardType("HardType"),
    Alternate("Alternate"),
    Performance("Performance"),
    Tweak("Tweak"),
    Reskin("Reskin"),
    Recolor("Recolor"),
    Retouch("Retouch"),
    /*
     * Other Labels
     */
    Restoration("Restoration"),
    Port("Port"),
    Music("Music");

    private final String displayName;

    Label(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
