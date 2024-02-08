package net.frogmouth.rnd.eofff.jpeg2000;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;
import net.frogmouth.rnd.eofff.jpeg2000.fileformat.cmap.ComponentMappingBox;

// TODO: turn into container
public class J2KHeaderItemProperty extends ItemProperty {
    public static final FourCC J2KH_ATOM = new FourCC("j2kH");

    private J2KChannelDefinition channels;
    private ComponentMappingBox components;

    // private J2KPalette palette;
    // private J2KLayers layers;

    public J2KHeaderItemProperty() {
        super(J2KH_ATOM);
    }

    public J2KChannelDefinition getChannels() {
        return channels;
    }

    public void setChannels(J2KChannelDefinition channels) {
        this.channels = channels;
    }

    public ComponentMappingBox getComponents() {
        return components;
    }

    public void setComponents(ComponentMappingBox components) {
        this.components = components;
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += channels.getSize();
        if (components != null) {
            size += components.getSize();
        }
        // size += palette.getSize();
        // size += layers.getSize();
        return size;
    }

    @Override
    public String getFullName() {
        return "J2KHeaderItemProperty";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        channels.writeTo(writer);
        if (components != null) {
            components.writeTo(writer);
        }
        // palette.writeTo(writer);
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
