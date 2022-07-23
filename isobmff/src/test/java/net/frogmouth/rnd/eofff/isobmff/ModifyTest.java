package net.frogmouth.rnd.eofff.isobmff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.PitmBox;
import net.frogmouth.rnd.eofff.isobmff.meta.PrimaryItemBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.core.klv.ArrayUtils;
import org.jmisb.mimd.st1902.MimdId;
import org.jmisb.mimd.st1902.MimdIdReference;
import org.jmisb.mimd.st1903.MIMD;
import org.jmisb.mimd.st1903.MIMD_Platforms;
import org.jmisb.mimd.st1903.MIMD_SecurityOptions;
import org.jmisb.mimd.st1903.MIMD_Version;
import org.jmisb.mimd.st1903.Security;
import org.jmisb.mimd.st1903.Security_Classification;
import org.jmisb.mimd.st1903.Security_ClassifyingMethod;
import org.jmisb.mimd.st1905.Platform;
import org.jmisb.mimd.st1905.PlatformType;
import org.jmisb.mimd.st1905.Platform_Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ModifyTest {
    private static final Logger LOG = LoggerFactory.getLogger(ModifyTest.class);
    private static final int SECURITY_ID_GROUP = 1;
    private static final int SECURITY_ID_SERIAL = 2;
    private List<Box> boxes;

    public ModifyTest() {}

    @BeforeTest
    public void parseFile() throws IOException {
        File file = new File("/home/bradh/meva/2018-03-07.16-55-00.17-00-00.bus.G340.recoded.mp4");
        Path testFile = file.toPath();
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            LOG.info(box.toString());
        }
    }

    @Test
    public void hackBoxes() throws IOException {
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC("ftyp");
        if (ftyp != null) {
            ftyp.setMajorBrand(new FourCC("iso6"));
            ftyp.appendCompatibleBrand(new FourCC("misb"));
        }
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC("moov");
        if (moov != null) {
            Box udta = null;
            for (Box box : moov.getNestedBoxes()) {
                if (box.getFourCC().equals(new FourCC("udta"))) {
                    udta = box;
                    break;
                }
            }
            if (udta != null) {
                moov.removeNestedBox(udta);
            }
            HdlrBox hdlr =
                    new HdlrBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withHandlerType("meta")
                            .withName("MIMD static metadata")
                            .build();
            ItemInfoEntry infe0 =
                    new ItemInfoEntryBuilder()
                            .withVersion(2)
                            .withItemId(1)
                            .withItemType("uri ")
                            .withItemUriType("urn:misb.KLV.ul.060E2B34.02050101.0E010503.00000000")
                            .build();
            PitmBox pitm =
                    new PrimaryItemBoxBuilder().withVersion(0).withFlags(0).withItemId(1).build();
            ItemInfoBox iinf =
                    new ItemInfoBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withItemInfo(infe0)
                            .build();
            // TODO: make something meaningful in MIMD
            byte[] mimdMessageWithoutKeyAndLength = buildSimpleMIMD();
            ItemDataBox idat =
                    new ItemDataBoxBuilder().withData(mimdMessageWithoutKeyAndLength).build();
            MetaBox metaBox =
                    new MetaBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withNesteBox(hdlr)
                            .withNesteBox(pitm)
                            .withNesteBox(iinf)
                            .withNesteBox(idat)
                            .build();
            moov.appendNestedBox(metaBox);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Box box : boxes) {
            box.writeTo(baos);
        }
        File testOut = new File("G340_to_NGA.STND.0076_0.1_MIFF_static.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private Box getTopLevelBoxByFourCC(String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    private byte[] buildSimpleMIMD() throws IOException {
        try {
            MIMD mimd = new MIMD();
            mimd.setVersion(new MIMD_Version(1));
            Security security = new Security();
            MimdId securityId = new MimdId(SECURITY_ID_SERIAL, SECURITY_ID_GROUP);
            security.setMimdId(securityId);
            security.setClassification(new Security_Classification("UNCLASSIFIED//"));
            security.setClassifyingMethod(new Security_ClassifyingMethod("US-1"));
            List<Security> securities = new ArrayList<>();
            securities.add(security);
            MIMD_SecurityOptions securityOptions = new MIMD_SecurityOptions(securities);
            mimd.setSecurityOptions(securityOptions);
            MimdIdReference securityIdRef =
                    new MimdIdReference(
                            SECURITY_ID_SERIAL, SECURITY_ID_GROUP, "Security", "Security");
            mimd.setCompositeMetadataSecurity(securityIdRef);
            mimd.setCompositeProductSecurity(securityIdRef);
            mimd.setCompositeMotionImagerySecurity(securityIdRef);
            Platform platform = new Platform();
            Platform_Name platformName = new Platform_Name("MEVA Camera G340");
            platform.setName(platformName);
            platform.setType(PlatformType.Structure);
            // TODO: add location and orientation
            List<Platform> platformsList = new ArrayList<>();
            platformsList.add(platform);
            MIMD_Platforms platforms = new MIMD_Platforms(platformsList);
            mimd.setPlatforms(platforms);
            dumpMimd(mimd);
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }

    private void dumpMimd(MIMD mimd) {
        outputTopLevelMessageHeader(mimd);
        outputNestedKlvValue(mimd, 1);
    }

    private void outputTopLevelMessageHeader(IMisbMessage misbMessage) {
        String displayHeader = misbMessage.displayHeader();
        if (displayHeader.equalsIgnoreCase("Unknown")) {
            System.out.println(
                    displayHeader
                            + " ["
                            + ArrayUtils.toHexString(misbMessage.getUniversalLabel().getBytes())
                                    .trim()
                            + "]");
            outputUnknownMessageContent(misbMessage.frameMessage(true));
        } else {
            System.out.println(displayHeader);
        }
    }

    private void outputUnknownMessageContent(byte[] frameMessage) {
        System.out.println(ArrayUtils.toHexString(frameMessage, 16, true));
    }

    private void outputNestedKlvValue(INestedKlvValue nestedKlvValue, int indentationLevel) {
        for (IKlvKey identifier : nestedKlvValue.getIdentifiers()) {
            IKlvValue value = nestedKlvValue.getField(identifier);
            outputValueWithIndentation(value, indentationLevel);
            // if this has nested content, output that at the next indentation level
            if (value instanceof INestedKlvValue) {
                outputNestedKlvValue((INestedKlvValue) value, indentationLevel + 1);
            }
        }
    }

    private void outputValueWithIndentation(IKlvValue value, int indentationLevel) {
        for (int i = 0; i < indentationLevel; ++i) {
            System.out.print("\t");
        }
        System.out.println(value.getDisplayName() + ": " + value.getDisplayableValue());
    }
}
