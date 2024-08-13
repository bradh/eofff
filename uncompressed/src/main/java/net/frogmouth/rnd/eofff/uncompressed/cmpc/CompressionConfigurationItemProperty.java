package net.frogmouth.rnd.eofff.uncompressed.cmpc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Compression configuration information.
 *
 * <p>See ISO/IEC 23001-17 Amd. 2 (CDAM) Section 8.2
 */
public class CompressionConfigurationItemProperty extends ItemFullProperty {

    public static final FourCC CMPC_ATOM = new FourCC("cmpC");

    private FourCC compressionType;
    private CompressionRangeType compressedRangeType;

    public CompressionConfigurationItemProperty() {
        super(CMPC_ATOM);
    }

    @Override
    public String getFullName() {
        return "CompressionConfigurationItemProperty";
    }

    public FourCC getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(FourCC compressionType) {
        this.compressionType = compressionType;
    }

    public CompressionRangeType getCompressedRangeType() {
        return compressedRangeType;
    }

    public void setCompressedRangeType(CompressionRangeType compressedRangeType) {
        this.compressedRangeType = compressedRangeType;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("compression_type=");
        sb.append(compressionType);
        sb.append(", compressed_range_type=");
        sb.append(compressedRangeType);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += FourCC.BYTES;
        count += Byte.BYTES; // compressed range type
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeFourCC(compressionType);
        stream.writeUnsignedInt8(this.compressedRangeType.getValue());
    }
}
