package net.frogmouth.rnd.eofff.isobmff.sampleentry.hint;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SubtitleSampleEntry;

/**
 * Received RTP Hint Sample Entry (rrtp).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.4.1
 */
public class ReceivedRtpHintSampleEntry extends SubtitleSampleEntry implements SampleEntry {

    public static final FourCC RRTP_ATOM = new FourCC("rrtp");

    private int hintTrackVersion = 1;
    private int highestCompatibleVersion = 1;
    private long maxPacketSize;

    public ReceivedRtpHintSampleEntry() {
        super(RRTP_ATOM);
    }

    @Override
    public String getFullName() {
        return "ReceivedRtpHintSampleEntry";
    }

    public int getHintTrackVersion() {
        return hintTrackVersion;
    }

    public void setHintTrackVersion(int hintTrackVersion) {
        this.hintTrackVersion = hintTrackVersion;
    }

    public int getHighestCompatibleVersion() {
        return highestCompatibleVersion;
    }

    public void setHighestCompatibleVersion(int highestCompatibleVersion) {
        this.highestCompatibleVersion = highestCompatibleVersion;
    }

    public long getMaxPacketSize() {
        return maxPacketSize;
    }

    public void setMaxPacketSize(long maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", hinttrackversion=");
        sb.append(hintTrackVersion);
        sb.append(", highestcompatibleversion=");
        sb.append(highestCompatibleVersion);
        sb.append(", maxpacketsize=");
        sb.append(maxPacketSize);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += (2 * Short.BYTES);
        size += Integer.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeUnsignedInt16(hintTrackVersion);
        stream.writeUnsignedInt16(highestCompatibleVersion);
        stream.writeUnsignedInt32(maxPacketSize);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }
}
