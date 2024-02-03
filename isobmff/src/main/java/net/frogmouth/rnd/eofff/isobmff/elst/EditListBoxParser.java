package net.frogmouth.rnd.eofff.isobmff.elst;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class EditListBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(EditListBoxParser.class);

    public EditListBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("elst");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        EditListBox box = new EditListBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long itemCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < itemCount; i++) {
            EditListBoxEntry entry = parseEditListBoxEntry(parseContext, version);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }

    private EditListBoxEntry parseEditListBoxEntry(ParseContext parseContext, int version) {
        long segmentDuration;
        long mediaTime;
        if (version == 1) {
            segmentDuration = parseContext.readUnsignedInt64();
            mediaTime = parseContext.readInt64();
        } else { // version==0
            segmentDuration = parseContext.readUnsignedInt32();
            mediaTime = parseContext.readInt32();
        }
        int mediaRateInteger = parseContext.readInt16();
        int mediaRateFraction = parseContext.readInt16();
        return new EditListBoxEntry(
                segmentDuration, mediaTime, mediaRateInteger, mediaRateFraction);
    }
}
