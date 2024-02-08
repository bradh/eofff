package net.frogmouth.rnd.eofff.isobmff.iprp;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class PropertyAssociation {
    private boolean essential;
    private int propertyIndex;

    public boolean isEssential() {
        return essential;
    }

    public void setEssential(boolean essential) {
        this.essential = essential;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(int propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    @Override
    public String toString() {
        return "association to "
                + propertyIndex
                + (essential ? " - (Essential)" : " - (Not Essential)");
    }

    int getSize(int flags) {
        if ((flags & 0x01) == 0x01) {
            return Short.BYTES;
        } else {
            return Byte.BYTES;
        }
    }

    void writeTo(OutputStreamWriter writer, int flags) throws IOException {
        int v = propertyIndex;
        if ((flags & 0x01) == 0x01) {
            if (essential) {
                v |= 0x8000;
            }
            writer.writeUnsignedInt16(v);
        } else {
            if (essential) {
                v |= 0x80;
            }
            writer.writeByte(v);
        }
    }
}
