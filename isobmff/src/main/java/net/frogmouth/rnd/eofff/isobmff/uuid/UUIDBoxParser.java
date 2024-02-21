package net.frogmouth.rnd.eofff.isobmff.uuid;

import com.google.auto.service.AutoService;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxFactoryManager;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.UUIDBaseBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class UUIDBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(UUIDBoxParser.class);

    public UUIDBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return BoxFactoryManager.UUID_FOURCC;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UUID extended_type = parseContext.readUUID();
        BoxParser parser = BoxFactoryManager.getParser(extended_type);
        Box box = parser.parse(parseContext, initialOffset, boxSize, boxName);
        if (box instanceof UUIDBaseBox) {
            UUIDBaseBox uuidBox = (UUIDBaseBox) box;
            uuidBox.setUuid(extended_type);
        }
        parseContext.setCursorPosition(initialOffset + boxSize);
        return box;
    }
}
