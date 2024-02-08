package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import static org.testng.Assert.*;

import java.lang.foreign.MemorySegment;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBoxParser;

public class PropertyTestSupport {

    protected AbstractItemProperty parseBytesToSingleProperty(byte[] bytes) {
        ItemPropertyContainerBoxParser parser = new ItemPropertyContainerBoxParser();
        ParseContext parseContext = new ParseContext(MemorySegment.ofArray(bytes));
        ItemPropertyContainerBox ipco =
                (ItemPropertyContainerBox)
                        parser.parse(parseContext, 0, bytes.length, new FourCC("cmpd"));
        List<AbstractItemProperty> properties = ipco.getProperties();
        assertEquals(properties.size(), 1);
        AbstractItemProperty prop = properties.get(0);
        return prop;
    }
}
