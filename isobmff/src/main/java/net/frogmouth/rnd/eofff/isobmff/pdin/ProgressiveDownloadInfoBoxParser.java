package net.frogmouth.rnd.eofff.isobmff.pdin;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ProgressiveDownloadInfoBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(ProgressiveDownloadInfoBoxParser.class);

    public ProgressiveDownloadInfoBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ProgressiveDownloadInfoBox.PDIN_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ProgressiveDownloadInfoBox box = new ProgressiveDownloadInfoBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long bodyBytes = (boxSize - (parseContext.getCursorPosition() - initialOffset));
        for (int i = 0; i < bodyBytes / ProgressiveDownloadInfoBoxEntry.BYTES; i++) {
            ProgressiveDownloadInfoBoxEntry entry = new ProgressiveDownloadInfoBoxEntry();
            entry.setRate(parseContext.readUnsignedInt32());
            entry.setInitialDelay(parseContext.readUnsignedInt32());
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
