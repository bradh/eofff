package net.frogmouth.rnd.eofff.isobmff.tfra;

public class TrackFragmentEntry {
    private long time;
    private long moofOffset;

    private long trafNumber;
    private long trunNumber;
    private long sampleDelta;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMoofOffset() {
        return moofOffset;
    }

    public void setMoofOffset(long moofOffset) {
        this.moofOffset = moofOffset;
    }

    public long getTrafNumber() {
        return trafNumber;
    }

    public void setTrafNumber(long trafNumber) {
        this.trafNumber = trafNumber;
    }

    public long getTrunNumber() {
        return trunNumber;
    }

    public void setTrunNumber(long trunNumber) {
        this.trunNumber = trunNumber;
    }

    public long getSampleDelta() {
        return sampleDelta;
    }

    public void setSampleDelta(long sampleDelta) {
        this.sampleDelta = sampleDelta;
    }

    @Override
    public String toString() {
        return "time="
                + time
                + ", moof_offset="
                + moofOffset
                + ", traf_number="
                + trafNumber
                + ", trun_number="
                + trunNumber
                + ", sample_delta="
                + sampleDelta;
    }
}
