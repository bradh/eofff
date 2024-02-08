package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class PixelInformationProperty extends ItemFullProperty {

    private final List<Integer> channels = new ArrayList<>();

    public PixelInformationProperty(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "PixelInformationProperty";
    }

    public List<Integer> getChannels() {
        return channels;
    }

    public void addChannel(int bitsPerChannel) {
        this.channels.add(bitsPerChannel);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(channels.size());
        for (Integer channel : channels) {
            writer.writeByte(channel);
        }
    }

    @Override
    public long getBodySize() {
        return Byte.BYTES + channels.size() * Byte.BYTES;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': [");
        for (Integer bitsPerChannel : channels) {
            sb.append(bitsPerChannel);
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
