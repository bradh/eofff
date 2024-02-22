package net.frogmouth.rnd.eofff.quicktime.ilst;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MetadataItemListAtomParser extends BaseBoxParser {

    public MetadataItemListAtomParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ilst");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MetadataItemListAtom box = new MetadataItemListAtom(boxName);
        // TODO: there can be an itif atom here
        // TODO: there can be a name atom here
        // each entry has type indicator (32 bits), four byte locale indicator, then the data
        byte[] data = parseContext.getBytes(boxSize - (Integer.BYTES + FourCC.BYTES));
        box.setData(data);
        return box;
    }
}
