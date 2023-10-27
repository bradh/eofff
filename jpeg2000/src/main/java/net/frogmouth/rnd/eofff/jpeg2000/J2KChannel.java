package net.frogmouth.rnd.eofff.jpeg2000;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class J2KChannel {
    private final int channelNumber;
    private final int channelType;
    private final int association;

    public J2KChannel(int channelNumber, int channelType, int association) {
        this.channelNumber = channelNumber;
        this.channelType = channelType;
        this.association = association;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public int getChannelType() {
        return channelType;
    }

    public int getAssociation() {
        return association;
    }

    void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt16(channelNumber);
        writer.writeUnsignedInt16(channelType);
        writer.writeUnsignedInt16(association);
    }
}
