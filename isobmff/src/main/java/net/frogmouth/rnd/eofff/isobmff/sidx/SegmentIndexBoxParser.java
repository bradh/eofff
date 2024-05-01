package net.frogmouth.rnd.eofff.isobmff.sidx;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SegmentIndexBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(SegmentIndexBoxParser.class);
    private static final long REFERENCE_TYPE_MASK = 0x80000000;
    private static final long REFERENCED_SIZE_MASK = 0x7FFFFFFF;
    private static final long STARTS_WITH_SAP_MASK = 0x80000000;
    private static final long SAP_TYPE_MASK = 0x70000000;
    private static final int SAP_TYPE_SHIFT = 28;
    private static final long SAP_DELTA_TIME_MASK = 0x0FFFFFFF;

    public SegmentIndexBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SegmentIndexBox.SIDX_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SegmentIndexBox box = new SegmentIndexBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));

        box.setReferenceId(parseContext.readUnsignedInt32());
        box.setTimescale(parseContext.readUnsignedInt32());
        if (box.getVersion() == 0x00) {
            box.setEarliestPresentationTime(parseContext.readUnsignedInt32());
            box.setFirstOffset(parseContext.readUnsignedInt32());
        } else {
            box.setEarliestPresentationTime(parseContext.readUnsignedInt64());
            box.setFirstOffset(parseContext.readUnsignedInt64());
        }
        // skip reserved field
        parseContext.readUnsignedInt16();
        int referenceCount = parseContext.readUnsignedInt16();
        for (int i = 0; i < referenceCount; i++) {
            long tempRef = parseContext.readUnsignedInt32();
            int referenceType = 0;
            if ((tempRef & REFERENCE_TYPE_MASK) == REFERENCE_TYPE_MASK) {
                referenceType = 1;
            }
            int referencedSize = ((int) (tempRef & REFERENCED_SIZE_MASK));
            long subSegmentDuration = parseContext.readUnsignedInt32();
            tempRef = parseContext.readUnsignedInt32();
            boolean startsWithSAP = ((tempRef & STARTS_WITH_SAP_MASK) == STARTS_WITH_SAP_MASK);
            int sapTypeBeforeShift = (int) (tempRef & SAP_TYPE_MASK);
            int sapTypeAfterShift = sapTypeBeforeShift >> SAP_TYPE_SHIFT;
            int sapDeltaTime = (int) (tempRef & SAP_DELTA_TIME_MASK);
            SegmentIndexReference reference =
                    new SegmentIndexReference(
                            referenceType,
                            referencedSize,
                            subSegmentDuration,
                            startsWithSAP,
                            sapTypeAfterShift,
                            sapDeltaTime);
            box.addReference(reference);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
