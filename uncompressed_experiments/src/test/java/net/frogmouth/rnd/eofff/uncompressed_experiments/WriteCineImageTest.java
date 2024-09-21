package net.frogmouth.rnd.eofff.uncompressed_experiments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpat.ComponentPatternDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpat.PatternDefinitionEntry;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.ngiis.cine.CineImage;
import net.frogmouth.rnd.ngiis.cine.CineParser;
import net.frogmouth.rnd.ngiis.cine.ParseResult;
import net.frogmouth.rnd.ngiis.cine.Time64;
import org.testng.annotations.Test;

/** Write file. */
public class WriteCineImageTest {

    private static final long MAIN_ITEM_ID = 1;
    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int MDAT_START = 412;

    private static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.

    @Test
    public void filterArray() throws IOException {
        String filename =
                "/home/bradh/testbed20/uncompressed_joe/Launch_Fixed_High_Contrast_cbg591_egfb23_01.cine";
        System.out.println("Hello: " + filename);
        CineParser parser = new CineParser();
        ParseResult parseResult = parser.parse(filename);
        CineImage image = parseResult.getImage(100);
        Time64 timestamp = parseResult.getTimestamp(100);
        MediaDataBox mdat = createMediaDataBox_from_cine(image);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox(mdat.getBodySize(), timestamp);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "uncompressed_cine_filterarray.heif");
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        // fileTypeBox.addCompatibleBrand(new Brand("geo1"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox(long extentLength, Time64 timestamp) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox(extentLength));
        boxes.add(makeItemPropertiesBox(timestamp));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(new FourCC("pict"));
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    private ItemInfoBox makeItemInfoBox() {
        ItemInfoBox iinf = new ItemInfoBox();
        ItemInfoEntry infe0 = new ItemInfoEntry();
        infe0.setVersion(2);
        infe0.setItemID(MAIN_ITEM_ID);
        FourCC unci = new FourCC("unci");
        infe0.setItemType(unci.asUnsigned());
        infe0.setItemName("Filter Array image");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox(long extentLength) {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        ILocItem mainItemLocation = new ILocItem();
        mainItemLocation.setConstructionMethod(0);
        mainItemLocation.setItemId(MAIN_ITEM_ID);
        ILocExtent mainItemExtent = new ILocExtent();
        mainItemExtent.setExtentIndex(0);
        mainItemExtent.setExtentOffset(IMAGE_DATA_START);
        mainItemExtent.setExtentLength(extentLength);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox(Time64 timestamp) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox());
        ipco.addProperty(makeUncompressedFrameConfigBox());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makePatternBox());
        ipco.addProperty(makeClockInfoItemProperty());
        ipco.addProperty(makeTimeStampBox(timestamp));
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations());

        return iprp;
    }

    private ItemPropertyAssociation makePropertyAssociations() {
        ItemPropertyAssociation itemPropertyAssociation = new ItemPropertyAssociation();
        {
            AssociationEntry mainItemAssociations = new AssociationEntry();
            mainItemAssociations.setItemId(MAIN_ITEM_ID);

            PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
            associationToComponentDefinitionBox.setPropertyIndex(1);
            associationToComponentDefinitionBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToComponentDefinitionBox);

            PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
            associationToUncompressedFrameConfigBox.setPropertyIndex(2);
            associationToUncompressedFrameConfigBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToUncompressedFrameConfigBox);

            PropertyAssociation associationToImageSpatialExtentsProperty =
                    new PropertyAssociation();
            associationToImageSpatialExtentsProperty.setPropertyIndex(3);
            associationToImageSpatialExtentsProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            PropertyAssociation associationToPatternProperty = new PropertyAssociation();
            associationToPatternProperty.setPropertyIndex(4);
            associationToPatternProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToPatternProperty);

            PropertyAssociation associationToClockInfoItemProperty = new PropertyAssociation();
            associationToClockInfoItemProperty.setPropertyIndex(5);
            associationToClockInfoItemProperty.setEssential(false);
            mainItemAssociations.addAssociation(associationToClockInfoItemProperty);

            PropertyAssociation associationToTimestampBox = new PropertyAssociation();
            associationToTimestampBox.setPropertyIndex(6);
            associationToTimestampBox.setEssential(false);
            mainItemAssociations.addAssociation(associationToTimestampBox);

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        return itemPropertyAssociation;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition fa = new ComponentDefinition(11, null);
        cmpd.addComponentDefinition(fa);
        ComponentDefinition red = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(red);
        ComponentDefinition green = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(green);
        ComponentDefinition blue = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blue);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC(0));
        // TODO: the bit depth should come from the cine image
        uncc.addComponent(new Component(0, 9, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(true);
        // TODO: check this
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileRowsMinusOne(0);
        return uncc;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        // TODO: get this from the cine image
        ispe.setImageHeight(960);
        ispe.setImageWidth(1280);
        return ispe;
    }

    private ComponentPatternDefinitionBox makePatternBox() {
        ComponentPatternDefinitionBox cpat = new ComponentPatternDefinitionBox();
        cpat.setPatternWidth(2);
        // GB/RG - this should come from the cine file.
        cpat.addEntry(new PatternDefinitionEntry(2, 1.0f));
        cpat.addEntry(new PatternDefinitionEntry(3, 1.0f));
        cpat.addEntry(new PatternDefinitionEntry(1, 1.0f));
        cpat.addEntry(new PatternDefinitionEntry(2, 1.0f));
        return cpat;
    }

    private TAITimeStampBox makeTimeStampBox(Time64 timestamp) {
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        time_stamp_packet.setTAITimeStamp(timestamp.getTaiNanoSeconds());
        time_stamp_packet.setStatusBits(TAITimeStampPacket.STATUS_SYNC_VALID);
        itai.setTimeStampPacket(time_stamp_packet);
        return itai;
    }

    private TAIClockInfoItemProperty makeClockInfoItemProperty() {

        TAIClockInfoItemProperty taic = new TAIClockInfoItemProperty();
        taic.setTimeUncertainty(TAIClockInfoItemProperty.TIME_UNCERTAINTY_UNKNOWN);
        taic.setClock_resolution(TAIClockInfoItemProperty.CLOCK_RESOLUTION_NANOSECOND);
        taic.setClock_drift_rate(TAIClockInfoItemProperty.CLOCK_DRIFT_RATE_UNKNOWN);
        taic.setClock_type(TAIClockInfoItemProperty.CLOCK_TYPE_CAN_SYNCHRONISE);
        return taic;
    }

    private MediaDataBox createMediaDataBox_from_cine(CineImage image) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        mdat.setData(image.getPixelData());
        return mdat;
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            // consider validating here
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }
}
