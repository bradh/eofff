package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/** A single region with the Region Item. */
public interface Region {
    GeometryType getGeometryType();

    public boolean needsLongFormat();

    public void writeTo(OutputStreamWriter stream, boolean useLongForm) throws IOException;
}
