package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public interface AbstractItemProperty {
    public abstract void writeTo(OutputStreamWriter writer) throws IOException;

    public abstract long getSize();
}
