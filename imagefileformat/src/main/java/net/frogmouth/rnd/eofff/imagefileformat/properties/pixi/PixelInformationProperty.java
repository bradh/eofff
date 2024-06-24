package net.frogmouth.rnd.eofff.imagefileformat.properties.pixi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Pixel Information Property.
 *
 * <p>The PixelInformationProperty descriptive item property indicates the number and bit depth of
 * colour and alpha/depth components, if present, in the reconstructed image of the associated image
 * item.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.5.6.1, plus Amendment 3 (in draft) which provides the
 * extended version (version = 1).
 */
public class PixelInformationProperty extends ItemFullProperty {

    public static final FourCC PIXI_ATOM = new FourCC("pixi");

    private final List<Integer> channels = new ArrayList<>();
    private final List<SupplementalChannelInfo> extraInfo = new ArrayList<>();

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

    public List<SupplementalChannelInfo> getExtraInfo() {
        return extraInfo;
    }

    public void addExtraInfo(SupplementalChannelInfo info) {
        this.extraInfo.add(info);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(channels.size());
        for (Integer channel : channels) {
            writer.writeByte(channel);
        }
        if (getVersion() == 1) {
            for (SupplementalChannelInfo supplementalChannelInfo : this.extraInfo) {
                supplementalChannelInfo.writeTo(writer);
            }
        }
    }

    @Override
    public long getBodySize() {
        long size = Byte.BYTES + channels.size() * Byte.BYTES;
        if (getVersion() == 1) {
            for (SupplementalChannelInfo supplementalChannelInfo : this.extraInfo) {
                size += supplementalChannelInfo.size();
            }
        }
        return size;
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
        if (getVersion() == 1) {
            sb.append(", has_alpha=");
            sb.append(hasAlpha());
            sb.append(", alpha_premultiplied=");
            sb.append(hasAlphaPremultiplied());
            sb.append(", has_subsampling=");
            sb.append(hasSubsampling());
            sb.append(", channels:");
            for (SupplementalChannelInfo supplementalChannelInfo : this.extraInfo) {
                supplementalChannelInfo.addToStringBuilder(sb, nestingLevel + 1);
            }
        }
        return sb.toString();
    }

    public boolean hasSubsampling() {
        return (getFlags() & 0x04) == 0x04;
    }

    public boolean hasAlphaPremultiplied() {
        return (getFlags() & 0x02) == 0x02;
    }

    public boolean hasAlpha() {
        return (getFlags() & 0x01) == 0x01;
    }
}
