package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Region Item.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.10.1.
 */
public class RegionItem {
    private long referenceWidth;
    private long referenceHeight;
    private final List<Region> regions = new ArrayList<>();
    private static final int version = 0;
    static final long MAX_UNSIGNED_16_BITS = 65535;
    private static final int LONG_FORMAT_FLAG = 0x01;

    public long getReferenceWidth() {
        return referenceWidth;
    }

    public void setReferenceWidth(long referenceWidth) {
        this.referenceWidth = referenceWidth;
    }

    public long getReferenceHeight() {
        return referenceHeight;
    }

    public void setReferenceHeight(long referenceHeight) {
        this.referenceHeight = referenceHeight;
    }

    public void addRegion(Region region) {
        this.regions.add(region);
    }

    public List<Region> getRegions() {
        return new ArrayList<>(regions);
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeByte(version);
        int flags = 0;
        if ((referenceWidth > MAX_UNSIGNED_16_BITS) || (referenceHeight > MAX_UNSIGNED_16_BITS)) {
            flags |= LONG_FORMAT_FLAG;
        } else {
            for (Region region : regions) {
                if (region.needsLongFormat()) {
                    flags |= LONG_FORMAT_FLAG;
                    break;
                }
            }
        }
        stream.writeByte(flags);
        boolean longForm = ((flags & LONG_FORMAT_FLAG) == LONG_FORMAT_FLAG);
        if (longForm) {
            stream.writeUnsignedInt32(this.referenceWidth);
            stream.writeUnsignedInt32(this.referenceHeight);
        } else {
            stream.writeUnsignedInt16(this.referenceWidth);
            stream.writeUnsignedInt16(this.referenceHeight);
        }
        stream.writeByte(regions.size());
        for (Region region : regions) {
            stream.writeByte(region.getGeometryType().getEncodedValue());
            region.writeTo(stream, longForm);
        }
    }
}
