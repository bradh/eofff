package net.frogmouth.rnd.eofff.isobmff.dref;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(DataReferenceParser.class)
public class DataEntrySeqNumImdaBoxParser extends DataEntryBaseBoxParser
        implements DataReferenceParser {

    public DataEntrySeqNumImdaBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DataEntrySeqNumImdaBox.SNIM_ATOM;
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntrySeqNumImdaBox box = new DataEntrySeqNumImdaBox();
        if (parseVersionAndFlags(parseContext, box, initialOffset, boxSize, entry_name))
            return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, entry_name);
        return box;
    }
}
