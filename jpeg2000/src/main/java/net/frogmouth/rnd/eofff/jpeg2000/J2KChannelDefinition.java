package net.frogmouth.rnd.eofff.jpeg2000;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class J2KChannelDefinition extends BaseBox {
    public static final FourCC CDEF_ATOM = new FourCC("cdef");

    private List<J2KChannel> channels = new ArrayList<>();

    public J2KChannelDefinition() {
        super(CDEF_ATOM);
    }

    public List<J2KChannel> getChannels() {
        return channels;
    }

    public void addChannel(J2KChannel channel) {
        channels.add(channel);
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += Short.BYTES;
        size += (channels.size() * (Short.BYTES + Short.BYTES + Short.BYTES));
        return size;
    }

    @Override
    public String getFullName() {
        return "J2KChannelDefinition";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt16(channels.size());
        for (J2KChannel channel : channels) {
            channel.writeTo(writer);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        // TODO: implement
        return sb.toString();
    }
}
