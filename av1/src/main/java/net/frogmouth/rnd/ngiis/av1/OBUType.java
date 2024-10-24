package net.frogmouth.rnd.ngiis.av1;

public enum OBUType {
    Unknown(-1),
    RESERVED(0),
    OBU_SEQUENCE_HEADER(1),
    OBU_TEMPORAL_DELIMITER(2),
    OBU_FRAME_HEADER(3),
    OBU_TILE_GROUP(4),
    OBU_METADATA(5),
    OBU_FRAME(6),
    OBU_REDUNDANT_FRAME_HEADER(7),
    OBU_TILE_LIST(8),
    RESERVED9(9),
    RESERVED10(10),
    RESERVED11(11),
    RESERVED12(12),
    RESERVED13(13),
    RESERVED14(14),
    OBU_PADDING(15);

    private OBUType(int val) {
        this.val = val;
    }

    private final int val;

    public int getValue() {
        return val;
    }

    public static OBUType lookupEntry(int v) {
        for (var entry : OBUType.values()) {
            if (entry.getValue() == v) {
                return entry;
            }
        }
        return Unknown;
    }
}
