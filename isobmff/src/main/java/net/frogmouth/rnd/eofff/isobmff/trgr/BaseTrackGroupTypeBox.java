package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class BaseTrackGroupTypeBox extends FullBox implements TrackGroupTypeBox {

    private long trackGroupId;

    public BaseTrackGroupTypeBox(FourCC format) {
        super(format);
    }

    public long getTrackGroupId() {
        return trackGroupId;
    }

    public void setTrackGroupId(long trackGroupId) {
        this.trackGroupId = trackGroupId;
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        return size;
    }

    protected long getBaseBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        writeBaseTrackGroupTypeBoxContent(stream);
    }

    protected void writeBaseTrackGroupTypeBoxContent(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32(trackGroupId);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("track_group_id=");
        sb.append(trackGroupId);
        return sb.toString();
    }
}
