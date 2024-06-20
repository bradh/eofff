package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class PixelInformationProperty extends ItemFullProperty {

    public static final FourCC PIXI_ATOM = new FourCC("pixi");

    private final List<Integer> channels = new ArrayList<>();

    public PixelInformationProperty() {
        super(PIXI_ATOM);
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("'[");
        for (Integer bitsPerChannel : channels) {
            sb.append(bitsPerChannel);
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
