package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class PixelInformationProperty extends ItemFullProperty {

    private final List<Integer> channels = new ArrayList<>();

    public PixelInformationProperty(long size, FourCC name) {
        super(size, name);
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
