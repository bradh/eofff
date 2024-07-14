package net.frogmouth.rnd.ngiis.png;

import java.util.Arrays;
import java.util.HexFormat;

public enum PngChunkType {
    IHDR(new byte[] {0x49, 0x48, 0x44, 0x52}),
    PLTE(new byte[] {0x50, 0x4c, 0x54, 0x45}),
    IDAT(new byte[] {0x49, 0x44, 0x41, 0x54}),
    IEND(new byte[] {0x49, 0x45, 0x4E, 0x44}),
    UNKNOWN(new byte[] {0x00, 0x00, 0x00, 0x00});

    private final byte[] code;

    private PngChunkType(byte[] code) {
        this.code = code;
    }

    public static PngChunkType lookup(byte[] chunkBytes) {
        for (PngChunkType chunkType : values()) {
            if (Arrays.equals(chunkType.code, (chunkBytes))) {
                return chunkType;
            }
        }
        System.out.println("failed to lookup: " + HexFormat.of().formatHex(chunkBytes));
        return UNKNOWN;
    }
}
