package net.frogmouth.rnd.eofff.uncompressed.cmpc;

/** Compression Range Type. */
public enum CompressionRangeType {
    /** The entity is the full item or sample. */
    FULL_ITEM(0),
    /**
     * The entity is the full image for a given component (component-based interleave and mixed
     * interleave).
     *
     * <p>Only applicable to image-related types.
     */
    COMPONENT(1),
    /**
     * The entity is a tile.
     *
     * <p>Only applicable to image-related types.
     */
    TILE(2),
    /**
     * The entity is a row.
     *
     * <p>Only applicable to image-related types.
     */
    ROW(3),
    /**
     * The entity is a pixel.
     *
     * <p>Only applicable to image-related types.
     */
    PIXEL(4),
    /**
     * The entity is not known.
     *
     * <p>This is not a valid value, and should not be created.
     */
    UNKNOWN(-1);

    private CompressionRangeType(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static CompressionRangeType getTypeForValue(int value) {
        for (CompressionRangeType type : CompressionRangeType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return CompressionRangeType.UNKNOWN;
    }
}
