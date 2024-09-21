package net.frogmouth.rnd.eofff.imagefileformat.properties.pixi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/** Supplementary pixel channel information. */
public class SupplementalChannelInfo {

    private int channelIDC; // TODO: enum?
    private int channelFormat; // TODO: enum?
    // TODO: subsampling type
    private String channelLabel;

    public int getChannelIDC() {
        return channelIDC;
    }

    public void setChannelIDC(int channelIDC) {
        this.channelIDC = channelIDC;
    }

    public int getChannelFormat() {
        return channelFormat;
    }

    public void setChannelFormat(int channelFormat) {
        this.channelFormat = channelFormat;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public long size() {
        long size = 0;
        size += 1;
        // TODO: subsampling
        if (isChannelLabelPresent()) {
            size += channelLabel.getBytes(StandardCharsets.UTF_8).length;
            size += 1; // null terminator
        }
        return size;
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        boolean channelLabelPresent = isChannelLabelPresent();
        int b = ((channelIDC << 5) | (channelFormat << 3) | (channelLabelPresent ? (1 << 2) : 0));
        stream.writeByte(b);
        // TODO: subsampling
        if (channelLabelPresent) {
            stream.writeNullTerminatedString(channelLabel);
        }
    }

    protected boolean isChannelLabelPresent() {
        return (channelLabel != null) && (channelLabel.length() > 0);
    }

    public void addToStringBuilder(StringBuilder sb, int nestingLevel) {
        sb.append("\n");
        BaseBox.addIndent(nestingLevel, sb);
        sb.append("channel_idc=");
        sb.append(channelIDC);
        sb.append(" (");
        sb.append(lookupChannelIDC());
        sb.append("), channel_format=");
        sb.append(channelFormat);
        sb.append(" (");
        sb.append(lookupChannelFormat());
        sb.append("), channel_label_present=");
        sb.append(this.isChannelLabelPresent());
        // TODO: subsampling
        if (this.isChannelLabelPresent()) {
            sb.append(", channel_label=");
            sb.append(channelLabel);
        }
    }

    private String lookupChannelIDC() {
        return switch (this.channelIDC) {
            case 0 -> "colour/grayscale";
            case 1 -> "alpha";
            case 2 -> "depth";
            default -> "unknown";
        };
    }

    private String lookupChannelFormat() {
        return switch (this.channelFormat) {
            case 0 -> "unsigned integer";
            case 1 -> "signed integer";
            case 2 -> "floating point";
            default -> "unknown";
        };
    }
}
