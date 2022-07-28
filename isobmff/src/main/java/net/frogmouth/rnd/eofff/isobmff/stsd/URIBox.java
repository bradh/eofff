package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * URI Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 12.3.3.
 */
public class URIBox extends FullBox {

    private String theURI;

    public URIBox(long size, FourCC name) {
        super(size, name);
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(theURI.getBytes(StandardCharsets.UTF_8));
        stream.write(0); // null terminator
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
