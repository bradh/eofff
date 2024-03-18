package net.frogmouth.rnd.eofff.isobmff.sampleentry.hint;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SubtitleSampleEntry;

/**
 * File Delivery Hint Sample Entry (fdp ).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.2.3
 */
public class FDHintSampleEntry extends SubtitleSampleEntry implements SampleEntry {

    public static final FourCC FDP_ATOM = new FourCC("fdp ");

    private int hintTrackVersion = 1;
    private int highestCompatibleVersion = 1;
    private int partitionEntryID;
    private int FEC_overhead;

    public FDHintSampleEntry() {
        super(FDP_ATOM);
    }

    @Override
    public String getFullName() {
        return "FDHintSampleEntry";
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

    public int getPartitionEntryID() {
        return partitionEntryID;
    }

    public void setPartitionEntryID(int partitionEntryID) {
        this.partitionEntryID = partitionEntryID;
    }

    public int getFEC_overhead() {
        return FEC_overhead;
    }

    public void setFEC_overhead(int FEC_overhead) {
        this.FEC_overhead = FEC_overhead;
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
        sb.append(", partition_entry_ID=");
        sb.append(partitionEntryID);
        sb.append(", FEC_overhead=");
        sb.append(FEC_overhead);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += (4 * Short.BYTES);
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
        stream.writeUnsignedInt16(partitionEntryID);
        stream.writeUnsignedInt16(FEC_overhead);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }
}
