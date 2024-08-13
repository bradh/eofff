package net.frogmouth.rnd.eofff.uncompressed.icef;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

class IcefEncoding {
    private boolean canUseZeroLengthOffsets = true;
    private int minimumBitsOffset = 16;
    private int minimumBitsSize = 8;
    private long impliedOffset = 0;

    public IcefEncoding(boolean forceOffsets) {
        if (forceOffsets) {
            canUseZeroLengthOffsets = false;
        }
    }

    void update(long offset, long size) {
        while (offset >= (1L << minimumBitsOffset)) {
            minimumBitsOffset += Byte.SIZE;
        }
        while (size >= (1L << minimumBitsSize)) {
            minimumBitsSize += Byte.SIZE;
        }
        if (offset != impliedOffset) {
            canUseZeroLengthOffsets = false;
        }
        impliedOffset += size;
    }

    int getEncoding() {
        int unit_offset_code;
        if (canUseZeroLengthOffsets) {
            unit_offset_code = 0;
        } else if (minimumBitsOffset == 16) {
            unit_offset_code = 1;
        } else if (minimumBitsOffset == 24) {
            unit_offset_code = 2;
        } else if (minimumBitsOffset == 32) {
            unit_offset_code = 3;
        } else {
            unit_offset_code = 4;
        }
        int unit_size_code =
                switch (minimumBitsSize) {
                    case 8 -> 0;
                    case 16 -> 1;
                    case 24 -> 2;
                    case 32 -> 3;
                    default -> 4;
                };
        return (unit_offset_code << 5) | (unit_size_code << 2);
    }

    int getNumberOfBytesPerEntry() {
        if (minimumBitsOffset > Integer.SIZE) {
            minimumBitsOffset = Long.SIZE;
        }
        if (minimumBitsSize > Integer.SIZE) {
            minimumBitsSize = Long.SIZE;
        }
        if (canUseZeroLengthOffsets) {
            return (minimumBitsSize / Byte.SIZE);
        }
        return ((minimumBitsOffset + minimumBitsSize) / Byte.SIZE);
    }

    void writeToStream(long unitOffset, long unitSize, OutputStreamWriter stream)
            throws IOException {
        if (!canUseZeroLengthOffsets) {
            switch (minimumBitsOffset) {
                case 16 -> stream.writeUnsignedInt16(unitOffset);
                case 24 -> stream.writeUnsignedInt24(unitOffset);
                case 32 -> stream.writeUnsignedInt32(unitOffset);
                default -> stream.writeLong(unitOffset);
            }
        }
        switch (minimumBitsSize) {
            case 8 -> stream.writeUnsignedInt8((int) unitSize);
            case 16 -> stream.writeUnsignedInt16(unitSize);
            case 24 -> stream.writeUnsignedInt24(unitSize);
            case 32 -> stream.writeUnsignedInt32(unitSize);
            default -> stream.writeLong(unitSize);
        }
    }
}
