package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Description Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.5.2
 */
public class SampleDescriptionBox extends FullBox {

    public static final FourCC STSD_ATOM = new FourCC("stsd");

    private final List<SampleEntry> nestedBoxes = new ArrayList<>();

    public SampleDescriptionBox() {
        super(STSD_ATOM);
    }

    @Override
    public String getFullName() {
        return "SampleDescriptionBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (SampleEntry box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addSampleEntries(List<SampleEntry> boxes) {
        nestedBoxes.addAll(boxes);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeInt(nestedBoxes.size());
        for (Box entry : nestedBoxes) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (SampleEntry item : nestedBoxes) {
            sb.append("\n");
            sb.append(item.toString(nestingLevel + 1));
        }
        return sb.toString();
    }
}
