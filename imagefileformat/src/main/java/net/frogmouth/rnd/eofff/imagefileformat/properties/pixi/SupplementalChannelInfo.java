package net.frogmouth.rnd.eofff.imagefileformat.properties.pixi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/** Supplementary pixel channel information. */
public class SupplementalChannelInfo {

    private int channelIDC; // TODO: enum?
    private int channelDataType; // TODO: enum?
    // TODO: subsampling type
    private String channelLabel;

    public int getChannelIDC() {
        return channelIDC;
    }

    public void setChannelIDC(int channelIDC) {
        this.channelIDC = channelIDC;
    }

    public int getChannelDataType() {
        return channelDataType;
    }

    public void setChannelDataType(int channelDataType) {
        this.channelDataType = channelDataType;
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
        int b = ((channelIDC << 5) | (channelDataType << 3) | (channelLabelPresent ? (1 << 2) : 0));
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
        sb.append("), channel_data_type=");
        sb.append(channelDataType);
        sb.append(" (");
        sb.append(lookupChannelDataType());
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

    private String lookupChannelDataType() {
        return switch (this.channelDataType) {
            case 0 -> "unsigned integer";
            case 1 -> "signed integer";
            case 2 -> "floating point";
            default -> "unknown";
        };
    }
}
