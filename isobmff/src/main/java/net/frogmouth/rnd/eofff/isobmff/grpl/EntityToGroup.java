package net.frogmouth.rnd.eofff.isobmff.grpl;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public interface EntityToGroup {
    public abstract void writeTo(OutputStreamWriter writer) throws IOException;

    public abstract long getSize();

    public String getFullName();

    FourCC getFourCC();

    public String toString(int nestingLevel);
}
