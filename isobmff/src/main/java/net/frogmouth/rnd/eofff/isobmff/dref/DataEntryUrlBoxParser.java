package net.frogmouth.rnd.eofff.isobmff.dref;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(DataReferenceParser.class)
public class DataEntryUrlBoxParser extends DataEntryBaseBoxParser implements DataReferenceParser {

    public DataEntryUrlBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DataEntryUrlBox.URL_ATOM;
    }

    @Override
    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        DataEntryUrlBox box = new DataEntryUrlBox();
        if (parseVersionAndFlags(parseContext, box, initialOffset, boxSize, entry_name))
            return parseAsDataEntryBaseBox(parseContext, initialOffset, boxSize, entry_name);
        if (box.getFlags() != DataEntryBaseBox.MEDIA_DATA_IN_SAME_FILE_FLAG) {
            box.setLocation(parseContext.readNullDelimitedString(boxSize));
        }
        return box;
    }
}
