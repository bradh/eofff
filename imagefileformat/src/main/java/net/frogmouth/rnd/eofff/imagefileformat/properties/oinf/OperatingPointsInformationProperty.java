package net.frogmouth.rnd.eofff.imagefileformat.properties.oinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;
import net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf.OperatingPointsRecord;

public class OperatingPointsInformationProperty extends ItemFullProperty {
    public static final FourCC OINF_ATOM = new FourCC("oinf");

    private OperatingPointsRecord opInfo;

    public OperatingPointsInformationProperty() {
        super(OINF_ATOM);
    }

    @Override
    public long getBodySize() {
        return opInfo.getSize();
    }

    public OperatingPointsRecord getOpInfo() {
        return opInfo;
    }

    public void setOpInfo(OperatingPointsRecord opInfo) {
        this.opInfo = opInfo;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        opInfo.writeTo(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        opInfo.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "OperatingPointsInformationProperty";
    }
}
