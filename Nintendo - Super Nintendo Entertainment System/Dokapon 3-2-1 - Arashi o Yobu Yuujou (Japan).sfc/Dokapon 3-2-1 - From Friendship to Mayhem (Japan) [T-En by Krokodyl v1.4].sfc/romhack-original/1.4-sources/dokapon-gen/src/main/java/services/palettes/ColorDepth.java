package services.palettes;

public enum ColorDepth {
    
    _1BPP(8),
    _2BPP(16),
    _4BPP(32)
    ;
    
    private int bytesPerTile;

    ColorDepth(int bytesPerTile) {
        this.bytesPerTile = bytesPerTile;
    }

    public int getBytesPerTile() {
        return bytesPerTile;
    }
}
