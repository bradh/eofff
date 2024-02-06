package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Plain Text Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.5.3
 */
public abstract class PlainTextSampleEntry extends BaseSampleEntry implements SampleEntry {

    public PlainTextSampleEntry(FourCC format) {
        super(format);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }
}
