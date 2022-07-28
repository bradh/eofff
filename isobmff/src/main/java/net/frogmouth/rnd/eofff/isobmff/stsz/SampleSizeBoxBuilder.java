package net.frogmouth.rnd.eofff.isobmff.stsz;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleSizeBoxBuilder {

    private int version;
    private int flags;
    private long sampleSize;
    private long sampleCount;

    public SampleSizeBoxBuilder() {}

    public SampleSizeBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleSizeBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleSizeBoxBuilder withSampleSize(long size) {
        this.sampleSize = size;
        return this;
    }

    public SampleSizeBoxBuilder withSampleCount(long count) {
        this.sampleCount = count;
        return this;
    }

    public SampleSizeBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        size += Integer.BYTES;
        SampleSizeBox box = new SampleSizeBox(size, new FourCC("stsz"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setSampleSize(sampleSize);
        box.setSampleCount(sampleCount);
        return box;
    }
}
