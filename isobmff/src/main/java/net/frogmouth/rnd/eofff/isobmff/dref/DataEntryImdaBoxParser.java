package net.frogmouth.rnd.eofff.isobmff.dref;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(DataReferenceParser.class)
public class DataEntryImdaBoxParser extends DataEntryBaseBoxParser implements DataReferenceParser {

    public DataEntryImdaBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DataEntryImdaBox.IMDT_ATOM;
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntryImdaBox box = new DataEntryImdaBox();
        if (parseVersionAndFlags(parseContext, box, initialOffset, boxSize, entry_name))
            return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, entry_name);
        box.setImdaRefIdentifier(parseContext.readUnsignedInt32());
        return box;
    }
}
