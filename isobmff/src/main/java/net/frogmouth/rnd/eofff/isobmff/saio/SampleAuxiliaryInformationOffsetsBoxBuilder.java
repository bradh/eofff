package net.frogmouth.rnd.eofff.isobmff.saio;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.saiz.*;

public class SampleAuxiliaryInformationOffsetsBoxBuilder {

    private int version;
    private int flags;
    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private List<Long> offsets = new ArrayList<>();

    public SampleAuxiliaryInformationOffsetsBoxBuilder() {}

    public SampleAuxiliaryInformationOffsetsBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleAuxiliaryInformationOffsetsBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleAuxiliaryInformationOffsetsBoxBuilder withAuxInfoType(FourCC fourCC) {
        this.auxInfoType = fourCC;
        return this;
    }

    public SampleAuxiliaryInformationOffsetsBoxBuilder withAuxInfoTypeParameter(long parameter) {
        this.auxInfoTypeParameter = parameter;
        return this;
    }

    public SampleAuxiliaryInformationOffsetsBoxBuilder addOffset(long offset) {
        this.offsets.add(offset);
        return this;
    }

    public SampleAuxiliaryInformationOffsetsBox build() {
        SampleAuxiliaryInformationOffsetsBox box = new SampleAuxiliaryInformationOffsetsBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.setAuxInfoType(auxInfoType);
        box.setAuxInfoTypeParameter(auxInfoTypeParameter);
        box.setEntryCount(offsets.size());
        long[] offsetValues = new long[offsets.size()];
        for (int i = 0; i < offsets.size(); i++) {
            offsetValues[i] = offsets.get(i);
        }
        box.setOffsets(offsetValues);
        return box;
    }
}
