package net.frogmouth.rnd.eofff.isobmff.xml;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class XMLBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(XMLBoxParser.class);

    public XMLBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return XMLBox.XML_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        XMLBox box = new XMLBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long remainingBytes = (initialOffset + boxSize) - parseContext.getCursorPosition();
        box.setXml(parseContext.readNullDelimitedString(remainingBytes));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
