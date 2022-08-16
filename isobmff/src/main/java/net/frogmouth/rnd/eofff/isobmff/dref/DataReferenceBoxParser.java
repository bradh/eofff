package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReferenceBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(DataReferenceBoxParser.class);

    public DataReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("dref");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DataReferenceBox box = new DataReferenceBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (int i = 0; i < entryCount; i++) {
            Box childBox = parseDataEntryBox(parseContext);
            if (childBox instanceof DataEntryBox dataEntryBox) {
                box.addDataEntryBox(dataEntryBox);
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    private Box parseDataEntryBox(ParseContext parseContext) {
        long initialOffset = parseContext.getCursorPosition();
        long boxSize = parseContext.readUnsignedInt32();
        FourCC boxName = parseContext.readFourCC();
        int version = parseContext.readByte();
        DataEntryBox box;
        if (boxName.toString().equals("url ")) {
            box = new DataEntryUrlBox();
        } else if (boxName.toString().equals("urn ")) {
            box = new DataEntryUrnBox();
        } else {
            LOG.warn("Got unsupported DataEntryBox {}, parsing as base box.", boxName);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setVersion(version);
        if (!box.isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (!box.isFlagSet(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            if (box instanceof DataEntryUrlBox dataEntryUrlBox) {
                dataEntryUrlBox.setLocation(parseContext.readNullDelimitedString(boxSize));
            } else if (box instanceof DataEntryUrnBox dataEntryUrnBox) {
                dataEntryUrnBox.setName(parseContext.readNullDelimitedString(boxSize));
                dataEntryUrnBox.setLocation(parseContext.readNullDelimitedString(boxSize));
            }
        }
        return box;
    }
}
