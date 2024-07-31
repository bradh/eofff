package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public abstract class AbstractUUIDProperty extends ItemFullProperty {

    public AbstractUUIDProperty() {
        super(new FourCC("uuid"));
    }

    protected void writeUUIDHeaderTo(OutputStreamWriter writer) throws IOException {
        long bodySize = getBodySize();
        if (needLargeSize(bodySize)) {
            long boxSize = BYTES_IN_LARGE_FULL_BOX_HEADER + bodySize;
            writer.writeUnsignedInt32(LARGE_SIZE_FLAG);
            writer.writeFourCC(this.getFourCC());
            writer.writeLong(boxSize);
        } else {
            long boxSize = BYTES_IN_FULL_BOX_HEADER + bodySize;
            writer.writeUnsignedInt32(boxSize);
            writer.writeFourCC(this.getFourCC());
        }
        writer.writeUUID(getUUID());
        writer.write(getVersionAndFlagsAsBytes());
    }

    protected abstract UUID getUUID();
}
