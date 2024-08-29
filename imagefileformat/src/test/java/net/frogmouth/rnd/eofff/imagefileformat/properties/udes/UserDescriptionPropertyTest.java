package net.frogmouth.rnd.eofff.imagefileformat.properties.udes;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.PropertyTestSupport;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import org.testng.annotations.Test;

/** Unit test for UserDescriptionProperty. */
public class UserDescriptionPropertyTest extends PropertyTestSupport {
    private static final byte[] UDES_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x30, 0x75, 0x64, 0x65, 0x73, 0x00, 0x00, 0x00, 0x00, 0x65, 0x6e,
                0x2d, 0x41, 0x55, 0x00, 0x74, 0x65, 0x73, 0x74, 0x20, 0x6e, 0x61, 0x6d, 0x65, 0x00,
                0x64, 0x65, 0x73, 0x63, 0x72, 0x69, 0x70, 0x74, 0x2e, 0x00, 0x74, 0x65, 0x73, 0x74,
                0x2c, 0x64, 0x65, 0x6d, 0x6f, 0x00
            };

    @Test
    public void checkWrite() throws IOException {
        UserDescriptionProperty box = new UserDescriptionProperty();
        box.setLang("en-AU");
        box.setDescriptiveName("test name");
        box.setDescription("descript.");
        box.setTags("test,demo");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, UDES_BYTES);
        assertEquals(
                box.toString(0),
                "UserDescriptionProperty 'udes': lang=en-AU, name=test name, description=descript., tags=test,demo");
    }

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(UDES_BYTES);
        assertTrue(prop instanceof UserDescriptionProperty);
        UserDescriptionProperty udes = (UserDescriptionProperty) prop;
        assertEquals(udes.getLang(), "en-AU");
        assertEquals(udes.getDescriptiveName(), "test name");
        assertEquals(udes.getDescription(), "descript.");
        assertEquals(udes.getTags(), "test,demo");
    }
}
