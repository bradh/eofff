package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Abstract Sample Entry implementation.
 *
 * <p>This is the mostly-common part of all Sample Entry implementations. There are some weird cases
 * (like `mebx`) where using SampleEntry will be cleaner.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.5.2.
 */
public abstract class BaseSampleEntry extends AbstractContainerBox implements SampleEntry {

    private int dataReferenceIndex;

    public BaseSampleEntry(FourCC format) {
        super(format);
    }

    public int getDataReferenceIndex() {
        return dataReferenceIndex;
    }

    public void setDataReferenceIndex(int dataReferenceIndex) {
        this.dataReferenceIndex = dataReferenceIndex;
    }

    @Override
    public long getSize() {
        long bodySize = getBodySize();
        if (needLargeSize(bodySize)) {
            return bodySize + BYTES_IN_LARGE_BOX_HEADER;
        } else {
            return bodySize + BYTES_IN_BOX_HEADER;
        }
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    protected long getBaseBodySize() {
        long size = 0;
        size += 6 * Byte.BYTES;
        size += Short.BYTES;
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    protected void writeBaseSampleEntryContent(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        for (int i = 0; i < 6; i++) {
            stream.writeByte(0);
        }
        stream.writeShort((short) dataReferenceIndex);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(dataReferenceIndex);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }
}
