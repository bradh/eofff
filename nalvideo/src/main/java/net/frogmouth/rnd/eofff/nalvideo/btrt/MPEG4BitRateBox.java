package net.frogmouth.rnd.eofff.nalvideo.btrt;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class MPEG4BitRateBox extends BaseBox {

    private long bufferSizeDB;
    private long maxBitrate;
    private long avgBitrate;

    public MPEG4BitRateBox(FourCC name) {
        super(name);
    }

    /**
     * The size of the decoding buffer for the elementary stream in bytes.
     *
     * @return the size of the decoding buffer for the elementary stream in bytes.
     */
    public long getBufferSizeDB() {
        return bufferSizeDB;
    }

    /**
     * Set the size of the decoding buffer for the elementary stream in bytes.
     *
     * @param bufferSizeDB the size of the decoding buffer for the elementary stream in bytes.
     */
    public void setBufferSizeDB(long bufferSizeDB) {
        this.bufferSizeDB = bufferSizeDB;
    }

    /**
     * The maximum rate in bits/second over any window of one second.
     *
     * @return the maximum rate in bits/second over any window of one second.
     */
    public long getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * Set the maximum rate in bits/second over any window of one second.
     *
     * @param maxBitrate the maximum rate in bits/second over any window of one second.
     */
    public void setMaxBitrate(long maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    /**
     * The average rate in bits/second over the entire presentation.
     *
     * @return the average rate in bits/second over the entire presentation.
     */
    public long getAvgBitrate() {
        return avgBitrate;
    }

    /**
     * Set the average rate in bits/second over the entire presentation.
     *
     * @param avgBitrate the average rate in bits/second over the entire presentation.
     */
    public void setAvgBitrate(long avgBitrate) {
        this.avgBitrate = avgBitrate;
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES;
        size += (3 * Integer.BYTES);
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += (3 * Integer.BYTES);
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.writeUnsignedInt32((int) bufferSizeDB);
        stream.writeUnsignedInt32((int) maxBitrate);
        stream.writeUnsignedInt32((int) avgBitrate);
    }
}
