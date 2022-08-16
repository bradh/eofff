package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;

public interface Box {

    public String getFullName();

    FourCC getFourCC();

    long getSize();

    long getBodySize();

    public void writeTo(OutputStreamWriter writer) throws IOException;
}
