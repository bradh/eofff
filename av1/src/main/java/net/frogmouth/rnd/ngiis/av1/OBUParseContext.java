package net.frogmouth.rnd.ngiis.av1;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OBUParseContext {
    private static final Logger LOG = LoggerFactory.getLogger(OBUParseContext.class);
    private final MemorySegment memorySegment;
    private long cursor;
    private int bitsLeftInByte = Byte.SIZE;

    static final ValueLayout.OfByte BYTE_LITTLE_ENDIAN =
            ValueLayout.JAVA_BYTE.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfShort SHORT_LITLE_ENDIAN =
            ValueLayout.JAVA_SHORT.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfInt INT_LITTLE_ENDIAN =
            ValueLayout.JAVA_INT.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfLong LONG_LITTLE_ENDIAN =
            ValueLayout.JAVA_LONG.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);

    public OBUParseContext(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.cursor = 0;
    }

    /**
     * Get bits, starting with the most significant bits.
     *
     * @param numBits the number of bits
     * @return the bits as an integer
     */
    public int readBits(final int numBits) {
        int bitsStillRequired = numBits;
        int res = 0;

        while ((hasRemaining()) && (bitsStillRequired > 0)) {
            int numBitsToExtract = Math.min(bitsStillRequired, bitsLeftInByte);
            int byteMask = (1 << numBitsToExtract) - 1;
            int lowBitOffset = (bitsLeftInByte - numBitsToExtract);
            int newBits =
                    (memorySegment.get(BYTE_LITTLE_ENDIAN, cursor) & (byteMask << lowBitOffset))
                            >> lowBitOffset;
            res = (res << numBitsToExtract) | newBits;
            bitsLeftInByte -= numBitsToExtract;
            if (bitsLeftInByte == 0) {
                bitsLeftInByte = Byte.SIZE;
                cursor++;
            }
            bitsStillRequired -= numBitsToExtract;
        }
        return res;
    }

    public boolean readFlag() {
        int flag = readBits(1);
        return (flag == 0x01);
    }

    public long getCursorPosition() {
        return cursor;
    }

    public boolean hasRemaining() {
        return cursor < memorySegment.byteSize();
    }

    public int readUnsignedInt8() {
        int i = memorySegment.get(BYTE_LITTLE_ENDIAN, cursor) & 0x00FF;
        cursor += Byte.BYTES;
        return i;
    }

    public long readUnsignedInt32() {
        long i = memorySegment.get(INT_LITTLE_ENDIAN, cursor) & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
        return i;
    }

    public void skipBytes(long l) {
        cursor += l;
    }

    public byte[] getBytes(long numBytes) {
        MemorySegment slice = this.memorySegment.asSlice(this.cursor, numBytes);
        cursor += numBytes;
        return slice.toArray(ValueLayout.JAVA_BYTE);
    }

    OBU readOBU() {
        // temporary hack
        int OperatingPointIdc = 0;
        OBU obu = new OBU();
        OBUHeader header = readOBUHeader();
        long obuSize;
        if (header.isHasSizeField()) {
            obuSize = readOBUSize();
        } else {
            throw new UnsupportedOperationException("Assumed always have header size");
        }

        long startPosition = this.getCursorPosition();
        if (header.getType() != OBUType.OBU_SEQUENCE_HEADER
                && header.getType() != OBUType.OBU_TEMPORAL_DELIMITER
                && OperatingPointIdc != 0
                && header.isExtensionFlag()) {
            int inTemporalLayer = (OperatingPointIdc >> header.getTemporalId()) & 1;
            int inSpatialLayer = (OperatingPointIdc >> (header.getSpatialId() + 8)) & 1;
            if ((inTemporalLayer != 0) || (inSpatialLayer != 0)) {
                drop_obu();
                return null;
            }
        }
        switch (header.getType()) {
            case OBU_SEQUENCE_HEADER -> sequence_header_obu();
            case OBU_TEMPORAL_DELIMITER -> temporal_delimiter_obu();
            case OBU_FRAME_HEADER -> frame_header_obu();
            case OBU_REDUNDANT_FRAME_HEADER -> frame_header_obu();
            case OBU_TILE_GROUP -> tile_group_obu(obuSize);
            case OBU_METADATA -> metadata_obu();
            case OBU_FRAME -> frame_obu(obuSize);
            case OBU_TILE_LIST -> tile_list_obu();
            case OBU_PADDING -> padding_obu();
            default -> reserved_obu();
        }
        long payloadBits = this.getCursorPosition() - startPosition;
        if (obuSize > 0
                && header.getType() != OBUType.OBU_TILE_GROUP
                && header.getType() != OBUType.OBU_TILE_LIST
                && header.getType() != OBUType.OBU_FRAME) {
            trailing_bits(obuSize - payloadBits);
        }
        return obu;
    }

    private long readOBUSize() {
        return readLeb128();
    }

    private long readLeb128() {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int leb128_byte = readUnsignedInt8();
            value |= ((leb128_byte & 0x7f) << (i * 7));
            if ((leb128_byte & 0x80) == 0x00) {
                break;
            }
        }
        return value;
    }

    private OBUHeader readOBUHeader() {
        OBUHeader header = new OBUHeader();
        header.setForbiddenBit(readBits(1));
        header.setType(OBUType.lookupEntry(readBits(4)));
        header.setExtensionFlag(readFlag());
        header.setHasSizeField(readFlag());
        readBits(1); // obu_reserved_1bit
        if (header.isExtensionFlag()) {
            header.setTemporalId(readBits(3));
            header.setSpatialId(readBits(2));
            readBits(3); // extension_header_reserved_3bits
        }
        return header;
    }

    private void sequence_header_obu() {
        // TODO
    }

    private void temporal_delimiter_obu() {
        // TODO
    }

    private void frame_header_obu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void tile_group_obu(long obuSize) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void metadata_obu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void frame_obu(long obuSize) {
        // TODO
    }

    private void tile_list_obu() {
        // TODO
    }

    private void padding_obu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void reserved_obu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void trailing_bits(long l) {
        this.skipBytes(l);
        this.bitsLeftInByte = Byte.BYTES;
    }

    private void drop_obu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
