package net.frogmouth.rnd.ngiis.cine;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

public record Time64(long fractions, long seconds) {
    public static final int NANOS_PER_SECOND = 1000 * 1000 * 1000;

    public boolean irigSynchronised() {
        return ((fractions & 0x01) == 0);
    }

    public long getTaiNanoSeconds() {
        TaiInstant taiInstant = getTaiInstant();
        long timestamp = NANOS_PER_SECOND * taiInstant.getTaiSeconds() + taiInstant.getNano();
        return timestamp;
    }

    private TaiInstant getTaiInstant() {
        long fractClean = (fractions & 0xFFFFFFFC);
        double partsOfSecond = fractClean / Math.pow(2, 32);
        long nanoSeconds = (long) (partsOfSecond * NANOS_PER_SECOND);
        Instant instant =
                Instant.EPOCH.plus(seconds, ChronoUnit.SECONDS).plus(nanoSeconds, ChronoUnit.NANOS);
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();
        return taiInstant;
    }

    @Override
    public String toString() {
        return "TIME64. Timestamp: "
                + getTaiNanoSeconds()
                + ", datetime: "
                + getTaiInstant().toUtcInstant().toString()
                + (irigSynchronised() ? ", IRIG synchronised" : ", not synchronised");
    }
}
