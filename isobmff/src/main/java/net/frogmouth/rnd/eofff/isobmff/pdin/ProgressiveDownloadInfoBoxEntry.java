package net.frogmouth.rnd.eofff.isobmff.pdin;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ProgressiveDownloadInfoBoxEntry {
    public static final long BYTES = 2 * Integer.BYTES;
    private long rate;
    private long initialDelay;

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    @Override
    public String toString() {
        return "rate=" + rate + ", initial_delay=" + initialDelay;
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32(rate);
        stream.writeUnsignedInt32(initialDelay);
    }
}
