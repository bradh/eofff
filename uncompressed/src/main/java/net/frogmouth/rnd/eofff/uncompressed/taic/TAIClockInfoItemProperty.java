package net.frogmouth.rnd.eofff.uncompressed.taic;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * TAI Clock Info.
 *
 * <p>This is currently Technology under Consideration for 23001-17.
 */
public class TAIClockInfoItemProperty extends ItemFullProperty {

    public static final FourCC TAIC_ATOM = new FourCC("taic");

    private static final long TIME_UNCERTAINTY_UNKNOWN = 0xFFFFFFFFFFFFFFFFl;
    private static final long CORRECTION_OFFSET_UNKNOWN = 0x7FFFFFFFFFFFFFFFl;
    private static final float CLOCK_DRIFT_RATE_UNKNOWN = 0.0f;
    private static final int REFERENCE_SOURCE_TYPE_UNKNOWN = 0;

    private long time_uncertainty = TIME_UNCERTAINTY_UNKNOWN;
    private long correction_offset = CORRECTION_OFFSET_UNKNOWN;
    private float clock_drift_rate = CLOCK_DRIFT_RATE_UNKNOWN;
    private byte reference_source_type = REFERENCE_SOURCE_TYPE_UNKNOWN;

    public TAIClockInfoItemProperty() {
        super(TAIC_ATOM);
    }

    @Override
    public String getFullName() {
        return "TAIClockInfoItemProperty";
    }

    @Override
    public long getBodySize() {
        return 8 + 8 + 4 + 1;
    }

    public long getTimeUncertainty() {
        return time_uncertainty;
    }

    public void setTimeUncertainty(long time_uncertainty) {
        this.time_uncertainty = time_uncertainty;
    }

    public long getCorrectionOffset() {
        return correction_offset;
    }

    public void setCorrectionOffset(long correction_offset) {
        this.correction_offset = correction_offset;
    }

    public float getClockDriftRate() {
        return clock_drift_rate;
    }

    public void setClockDriftRate(float clock_drift_rate) {
        this.clock_drift_rate = clock_drift_rate;
    }

    public byte getReferenceSourceType() {
        return reference_source_type;
    }

    public void setReferenceSourceType(byte reference_source_type) {
        this.reference_source_type = reference_source_type;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeLong(time_uncertainty);
        stream.writeLong(correction_offset);
        stream.writeDouble32(clock_drift_rate);
        stream.writeByte(reference_source_type);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("time_uncertainty: ");
        if (time_uncertainty == TIME_UNCERTAINTY_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(time_uncertainty);
        }
        sb.append(", correction_offset: ");
        if (correction_offset == CORRECTION_OFFSET_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(correction_offset);
        }
        sb.append(", clock_drift_rate: ");
        if (clock_drift_rate == CLOCK_DRIFT_RATE_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(clock_drift_rate);
        }
        sb.append(", reference_source_type: ");
        if (reference_source_type == REFERENCE_SOURCE_TYPE_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(reference_source_type);
        }
        return sb.toString();
    }
}
