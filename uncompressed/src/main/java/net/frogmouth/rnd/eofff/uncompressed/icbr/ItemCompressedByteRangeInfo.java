package net.frogmouth.rnd.eofff.uncompressed.icbr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ItemCompressedByteRangeInfo extends ItemFullProperty {

    public static final FourCC ICBR_ATOM = new FourCC("icbr");

    private final List<ByteRange> ranges = new ArrayList<>();

    public ItemCompressedByteRangeInfo() {
        super(ICBR_ATOM);
    }

    @Override
    public String getFullName() {
        return "ItemCompressionByteRangeInfo";
    }

    public List<ByteRange> getRanges() {
        return new ArrayList<>(ranges);
    }

    public void addRange(ByteRange range) {
        ranges.add(range);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        if (getVersion() == 1) {
            size += ranges.size() * (2 * Long.BYTES);
        } else {
            size += ranges.size() * (2 * Integer.BYTES);
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(ranges.size());
        for (ByteRange range : ranges) {
            if (getVersion() == 1) {
                stream.writeLong(range.rangeOffset());
                stream.writeLong(range.rangeSize());
            } else {
                stream.writeUnsignedInt32(range.rangeOffset());
                stream.writeUnsignedInt32(range.rangeSize());
            }
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("num_ranges=");
        sb.append(ranges.size());
        for (ByteRange range : ranges) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append(range.toString());
        }
        return sb.toString();
    }
}
