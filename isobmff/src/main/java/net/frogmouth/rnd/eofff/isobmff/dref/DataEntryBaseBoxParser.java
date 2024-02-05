package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataEntryBaseBoxParser implements DataReferenceParser {

    private static final Logger LOG = LoggerFactory.getLogger(DataEntryBaseBoxParser.class);

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "DataEntryBaseBox getFourCC() should not be called directly");
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, boxName);
    }

    protected boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    protected DataEntryBaseBox parseAsDataEntryBaseBox(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntryBaseBox dataEntryBaseBox = new DataEntryBaseBox(entry_name);
        this.parseVersionAndFlags(
                parseContext, dataEntryBaseBox, initialOffset, boxSize, entry_name);
        return dataEntryBaseBox;
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }

    protected boolean parseVersionAndFlags(
            ParseContext parseContext,
            DataEntryBaseBox dataEntryBox,
            long initialOffset,
            long boxSize,
            FourCC entry_name) {
        int version = parseContext.readByte();
        dataEntryBox.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base data reference.", version);
            return true;
        }
        dataEntryBox.setFlags(parseFlags(parseContext));
        return false;
    }
}
