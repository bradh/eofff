package net.frogmouth.rnd.eofff.isobmff.dref;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(DataReferenceParser.class)
public class DataEntryUrnBoxParser extends DataEntryBaseBoxParser implements DataReferenceParser {

    public DataEntryUrnBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DataEntryUrnBox.URN_ATOM;
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntryUrnBox box = new DataEntryUrnBox();
        if (parseVersionAndFlags(parseContext, box, initialOffset, boxSize, entry_name))
            return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, entry_name);
        box.setName(parseContext.readNullDelimitedString(boxSize));
        box.setLocation(parseContext.readNullDelimitedString(boxSize));
        return box;
    }
}
