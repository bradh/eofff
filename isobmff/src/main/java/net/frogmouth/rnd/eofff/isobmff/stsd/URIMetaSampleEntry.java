package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/*
 * TODO: This need to have the MetadataSampleEntry and SampleEntry stuff factored out
 */
public class URIMetaSampleEntry extends AbstractContainerBox {

    private int dataReferenceIndex;

    public URIMetaSampleEntry(long size, FourCC name) {
        super(size, name);
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        for (int i = 0; i < 6; i++) {
            stream.write(0);
        }
        stream.write(shortToBytes((short) dataReferenceIndex));
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    // TODO: add toString() override
}
