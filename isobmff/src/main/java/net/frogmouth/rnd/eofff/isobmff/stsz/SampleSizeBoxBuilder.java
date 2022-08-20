package net.frogmouth.rnd.eofff.isobmff.stsz;

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

        SampleSizeBox box = new SampleSizeBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.setSampleSize(sampleSize);
        box.setSampleCount(sampleCount);
        return box;
    }
}
