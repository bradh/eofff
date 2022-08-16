package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/*
 * TODO: This need to have the MetadataSampleEntry and SampleEntry stuff factored out
 */
public class URIMetaSampleEntry extends AbstractContainerBox {

    private int dataReferenceIndex;

    public URIMetaSampleEntry(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "URIMetadataSampleEntry";
    }

    public int getDataReferenceIndex() {
        return dataReferenceIndex;
    }

    public void setDataReferenceIndex(int dataReferenceIndex) {
        this.dataReferenceIndex = dataReferenceIndex;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        for (int i = 0; i < 6; i++) {
            stream.writeByte(0);
        }
        stream.writeShort((short) dataReferenceIndex);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    // TODO: add toString() override
}
