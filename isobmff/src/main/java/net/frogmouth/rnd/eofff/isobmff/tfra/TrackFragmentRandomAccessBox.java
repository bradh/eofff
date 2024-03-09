package net.frogmouth.rnd.eofff.isobmff.tfra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Fragment Random Access Box (tfra).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.10.
 */
public class TrackFragmentRandomAccessBox extends FullBox {

    public static final FourCC TFRA_ATOM = new FourCC("tfra");

    private long trackID;
    private int lengthSizeOfTrafNum;
    private int lengthSizeOfTrunNum;
    private int lengthSizeOfSampleNum;
    private List<TrackFragmentEntry> entries = new ArrayList<>();

    public TrackFragmentRandomAccessBox() {
        super(TFRA_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackFragmentRandomAccessBox";
    }

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public List<TrackFragmentEntry> getEntries() {
        return entries;
    }

    public void addEntry(TrackFragmentEntry entry) {
        this.entries.add(entry);
    }

    public int getLengthSizeOfTrafNum() {
        return lengthSizeOfTrafNum;
    }

    public void setLengthSizeOfTrafNum(int lengthSizeOfTrafNum) {
        this.lengthSizeOfTrafNum = lengthSizeOfTrafNum;
    }

    public int getLengthSizeOfTrunNum() {
        return lengthSizeOfTrunNum;
    }

    public void setLengthSizeOfTrunNum(int lengthSizeOfTrunNum) {
        this.lengthSizeOfTrunNum = lengthSizeOfTrunNum;
    }

    public int getLengthSizeOfSampleNum() {
        return lengthSizeOfSampleNum;
    }

    public void setLengthSizeOfSampleNum(int lengthSizeOfSampleNum) {
        this.lengthSizeOfSampleNum = lengthSizeOfSampleNum;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES; // track_ID;
        size += Integer.BYTES; // reserved + sizes
        size += Integer.BYTES; // number_of_entry
        deriveVersionAndSizes();
        int bytesPerEntry = getBytesPerEntry();
        size += (bytesPerEntry * entries.size());
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        deriveVersionAndSizes();
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(trackID);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("track_ID=");
        sb.append(getTrackID());
        sb.append(", length_size_of_traf_num=");
        sb.append(lengthSizeOfTrafNum);
        sb.append(", length_size_of_trun_num=");
        sb.append(lengthSizeOfTrunNum);
        sb.append(", length_size_of_sample_num=");
        sb.append(lengthSizeOfSampleNum);
        entries.forEach(
                entry -> {
                    sb.append("\n");
                    addIndent(nestingLevel + 1, sb);
                    sb.append(entry.toString());
                });
        return sb.toString();
    }

    private void deriveVersionAndSizes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private int getBytesPerEntry() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
