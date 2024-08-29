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

    public static final long TIME_UNCERTAINTY_UNKNOWN = 0xFFFFFFFFFFFFFFFFl;
    public static final long CLOCK_RESOLUTION_MICROSECOND = 0x1000;
    public static final int CLOCK_DRIFT_RATE_UNKNOWN = 0x7FFFFFFF;
    public static final byte CLOCK_TYPE_UNKNOWN = 0;

    private long time_uncertainty = TIME_UNCERTAINTY_UNKNOWN;
    private long clock_resolution = CLOCK_RESOLUTION_MICROSECOND;
    private int clock_drift_rate = CLOCK_DRIFT_RATE_UNKNOWN;
    private byte clock_type = CLOCK_TYPE_UNKNOWN;

    public TAIClockInfoItemProperty() {
        super(TAIC_ATOM);
    }

    @Override
    public String getFullName() {
        return "TAIClockInfoItemProperty";
    }

    @Override
    public long getBodySize() {
        return 8 + 4 + 4 + 1;
    }

    public long getTimeUncertainty() {
        return time_uncertainty;
    }

    public void setTimeUncertainty(long time_uncertainty) {
        this.time_uncertainty = time_uncertainty;
    }

    public long getTime_uncertainty() {
        return time_uncertainty;
    }

    public void setTime_uncertainty(long time_uncertainty) {
        this.time_uncertainty = time_uncertainty;
    }

    public long getClock_resolution() {
        return clock_resolution;
    }

    public void setClock_resolution(long clock_resolution) {
        this.clock_resolution = clock_resolution;
    }

    public int getClock_drift_rate() {
        return clock_drift_rate;
    }

    public void setClock_drift_rate(int clock_drift_rate) {
        this.clock_drift_rate = clock_drift_rate;
    }

    public byte getClock_type() {
        return clock_type;
    }

    public void setClock_type(byte clock_type) {
        this.clock_type = clock_type;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeLong(time_uncertainty);
        stream.writeUnsignedInt32(clock_resolution);
        stream.writeInt(clock_drift_rate);
        stream.writeByte(clock_type << 6);
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
        sb.append(", clock_resolution: ");
        sb.append(clock_resolution);
        sb.append(", clock_drift_rate: ");
        if (clock_drift_rate == CLOCK_DRIFT_RATE_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(clock_drift_rate);
        }
        sb.append(", clock_type: ");
        if (clock_type == CLOCK_TYPE_UNKNOWN) {
            sb.append("unknown");
        } else {
            sb.append(clock_type);
        }
        return sb.toString();
    }
}
