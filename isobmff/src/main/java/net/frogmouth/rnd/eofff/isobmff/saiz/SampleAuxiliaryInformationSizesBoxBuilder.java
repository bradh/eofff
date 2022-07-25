package net.frogmouth.rnd.eofff.isobmff.saiz;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleAuxiliaryInformationSizesBoxBuilder {

    private int version;
    private int flags;
    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private String theURI;
    private long defaultSampleInfoSize;
    private long sampleCount;

    public SampleAuxiliaryInformationSizesBoxBuilder() {}

    public SampleAuxiliaryInformationSizesBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withAuxInfoType(FourCC fourCC) {
        this.auxInfoType = fourCC;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withAuxInfoTypeParameter(long parameter) {
        this.auxInfoTypeParameter = parameter;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withURI(String uri) {
        this.theURI = uri;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withDefaultSampleInfoSize(long size) {
        this.defaultSampleInfoSize = size;
        return this;
    }

    public SampleAuxiliaryInformationSizesBoxBuilder withSampleCount(long count) {
        this.sampleCount = count;
        return this;
    }

    public SampleAuxiliaryInformationSizesBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((flags & 0x01) == 0x01) {
            size += FourCC.BYTES;
            size += Integer.BYTES;
        }
        if (auxInfoType.equals(new FourCC("misb"))) {
            size += theURI.getBytes(StandardCharsets.UTF_8).length;
            size += Byte.BYTES; // null terminator
            // TODO: per parameters
            size += Byte.BYTES;
            size += Integer.BYTES;
            // TODO: sample_info_size array
        }
        SampleAuxiliaryInformationSizesBox box =
                new SampleAuxiliaryInformationSizesBox(size, new FourCC("saiz"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setAuxInfoType(auxInfoType);
        box.setAuxInfoTypeParameter(auxInfoTypeParameter);
        box.setTheURI(theURI);
        box.setDefaultSampleInfoSize(defaultSampleInfoSize);
        box.setSampleCount(sampleCount);
        return box;
    }
}
