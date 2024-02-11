package net.frogmouth.rnd.eofff.gopro.quicktime;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryBaseBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryBaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser;

@AutoService(DataReferenceParser.class)
public class DataEntryAliasBoxParser extends DataEntryBaseBoxParser implements DataReferenceParser {

    public DataEntryAliasBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DataEntryAliasBox.ALIS_ATOM;
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntryAliasBox box = new DataEntryAliasBox();
        if (parseVersionAndFlags(parseContext, box, initialOffset, boxSize, entry_name))
            return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, entry_name);
        return box;
    }
}
