package net.frogmouth.rnd.eofff.uncompressed.icef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class GenericallyCompressedUnitsItemInfo extends ItemFullProperty {

    public static final FourCC ICEF_ATOM = new FourCC("icef");

    private final List<CompressedUnitInfo> units = new ArrayList<>();
    private boolean forcedOffsets = false;

    public GenericallyCompressedUnitsItemInfo() {
        super(ICEF_ATOM);
    }

    @Override
    public String getFullName() {
        return "GenericallyCompressedUnitsItemInfoBox";
    }

    public List<CompressedUnitInfo> getCompressedUnitInfos() {
        return new ArrayList<>(units);
    }

    public void addCompressedUnitInfo(CompressedUnitInfo unit) {
        units.add(unit);
    }

    public boolean isForcedOffsets() {
        return forcedOffsets;
    }

    /**
     * Force use of offsets, even if not required.
     *
     * <p>Compressed units can have "length 0" for the offsets, which means the offset is implied by
     * the sum of the lengths. This forces use of explicit offsets even in cases where the length
     * could be 0.
     *
     * @param forcedOffsets if true, force inclusion of offsets
     */
    public void setForcedOffsets(boolean forcedOffsets) {
        this.forcedOffsets = forcedOffsets;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Byte.BYTES;
        size += Integer.BYTES;
        IcefEncoding encoding = new IcefEncoding(forcedOffsets);
        for (CompressedUnitInfo unit : units) {
            encoding.update(unit.unitOffset(), unit.unitSize());
        }
        size += (encoding.getNumberOfBytesPerEntry() * units.size());
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        IcefEncoding encoding = new IcefEncoding(forcedOffsets);
        for (CompressedUnitInfo unit : units) {
            encoding.update(unit.unitOffset(), unit.unitSize());
        }
        stream.writeUnsignedInt8(encoding.getEncoding());
        stream.writeUnsignedInt32(units.size());
        for (CompressedUnitInfo unit : units) {
            encoding.writeToStream(unit.unitOffset(), unit.unitSize(), stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("num_compressed_units=");
        sb.append(units.size());
        for (CompressedUnitInfo unit : units) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append(unit.toString());
        }
        return sb.toString();
    }
}
