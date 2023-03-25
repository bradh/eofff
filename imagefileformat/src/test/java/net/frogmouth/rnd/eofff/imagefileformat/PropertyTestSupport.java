package net.frogmouth.rnd.eofff.imagefileformat;

import static org.testng.Assert.*;

import java.util.List;
import jdk.incubator.foreign.MemorySegment;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

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
