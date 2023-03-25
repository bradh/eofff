package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

/** Region geometry type. */
public enum GeometryType {
    RESERVED(-1),
    POINT(0),
    RECTANGLE(1),
    ELLIPSE(2),
    POLYGON(3),
    REFERENCED_MASK(4),
    INLINE_MASK(5),
    POLYLINE(6);

    static GeometryType fromEncodedValue(byte value) {
        for (GeometryType type : values()) {
            if (type.getEncodedValue() == value) {
                return type;
            }
        }
        return RESERVED;
    }

    private GeometryType(int value) {
        this.encodedValue = value;
    }

    private final int encodedValue;

    public int getEncodedValue() {
        return encodedValue;
    }
}
