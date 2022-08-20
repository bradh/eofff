package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * URI Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 12.3.3.
 */
public class URIBox extends FullBox {

    private String theURI;

    public URIBox(FourCC name) {
        super(name);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += theURI.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        return size;
    }

    @Override
    public String getFullName() {
        return "SampleSizeBox";
    }

    public String getTheURI() {
        return theURI;
    }

    public void setTheURI(String theURI) {
        this.theURI = theURI;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeNullTerminatedString(theURI);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': theURI=");
        sb.append(theURI);
        return sb.toString();
    }
}
