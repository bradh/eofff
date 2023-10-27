package net.frogmouth.rnd.eofff.imagefileformat.properties.colr;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.PropertyTestSupport;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for UserDescriptionProperty. */
public class ColourInformationPropertyTest extends PropertyTestSupport {
    private static final byte[] COLR_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x13,
                0x63, // colr
                0x6f,
                0x6c,
                0x72,
                0x6e, // nclx
                0x63,
                0x6c,
                0x78,
                0x01,
                0x02,
                0x03,
                0x04,
                0x05,
                0x60,
                (byte) 0x80
            };

    @Test
    public void checkWrite() throws IOException {
        ColourInformationProperty box = new ColourInformationProperty();
        box.setColourType(new FourCC("nclx"));
        box.setColourPrimaries((short) 0x0102);
        box.setTransferCharacteristics((short) 0x0304);
        box.setMatrixCoefficients((short) 0x0560);
        box.setFullRange(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, COLR_BYTES);
        assertEquals(
                box.toString(),
                "ColourInformationProperty 'colr': colour_type=nclx, colour_primaries=0x0102, transfer_characteristics=0x0304, matrix_coefficients=0x0560, full_range_flag=1");
    }

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(COLR_BYTES);
        assertTrue(prop instanceof ColourInformationProperty);
        ColourInformationProperty colr = (ColourInformationProperty) prop;
        assertEquals(colr.getColourType().toString(), "nclx");
        assertEquals(colr.getColourPrimaries(), 0x0102);
        // TODO the rest of the checks
    }
}
