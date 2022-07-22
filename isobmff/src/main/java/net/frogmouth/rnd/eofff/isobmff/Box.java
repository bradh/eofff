package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.io.OutputStream;

public interface Box {

    public String getFullName();

    FourCC getFourCC();

    long getSize();

    byte[] getSizeAsBytes();

    public void writeTo(OutputStream writer) throws IOException;
}
