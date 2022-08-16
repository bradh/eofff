package net.frogmouth.rnd.eofff.dash.emsg;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventMessageBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(EventMessageBoxParser.class);

    public EventMessageBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("emsg");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        EventMessageBox box = new EventMessageBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0x00) {
            System.out.println("Version 0 - TODO");
        } else if (version == 0x01) {
            box.setTimescale(parseContext.readUnsignedInt32());
            box.setPresentationTime(parseContext.readUnsignedInt64());
            box.setEventDuration(parseContext.readUnsignedInt32());
            box.setId(parseContext.readUnsignedInt32());
            box.setSchemeIdUri(parseContext.readNullDelimitedString(boxSize));
            box.setValue(parseContext.readNullDelimitedString(boxSize));
        }
        long bytesConsumed = parseContext.getCursorPosition() - initialOffset;
        box.setMessageData(parseContext.getBytes(boxSize - bytesConsumed));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
