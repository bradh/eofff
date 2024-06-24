package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static net.frogmouth.rnd.eofff.uncompressed_experiments.GIMIUtils.makeRandomContentId;
import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
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
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

/** Write file. */
public class WriteSignedIntegerTest {

    private static final long MAIN_ITEM_ID = 10;
    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private final int imageWidth = 128;
    private final int imageHeight = 2;
    private static final int MDAT_START = 512;

    private final LocalDateTime TIMESTAMP =
            LocalDateTime.of(LocalDate.of(2024, 5, 24), LocalTime.of(17, 26, 15, 0));

    private static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.

    public WriteSignedIntegerTest() {}

    @Test
    public void signed_8() throws IOException {
        MediaDataBox mdat = createMediaDataBox8();

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox8();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "signed_int8.hif");
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(Brand.MIF2);
        fileTypeBox.addCompatibleBrand(Brand.MIF1);
        fileTypeBox.addCompatibleBrand(new Brand("geo1"));
        fileTypeBox.addCompatibleBrand(Brand.UNIF);
        return fileTypeBox;
    }

    private MetaBox createMetaBox8() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox());
        boxes.add(makeItemDataBox());
        boxes.add(makeItemPropertiesBox8());
        boxes.add(makeItemReferenceBox());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
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
        infe0.setItemName("Uncompressed Image");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox() throws IOException {
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
        mainItemExtent.setExtentLength(imageHeight * imageWidth);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemDataBox makeItemDataBox() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox8() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox());
        ipco.addProperty(makeUncompressedFrameConfigBox());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeRandomContentId()); // 4
        ipco.addProperty(makeClockInfoItemProperty()); // 5
        ipco.addProperty(makeTimeStampBox()); // 6
        iprp.setItemProperties(ipco);

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

            PropertyAssociation associationToContentIDProperty = new PropertyAssociation();
            associationToContentIDProperty.setPropertyIndex(4);
            associationToContentIDProperty.setEssential(false);
            mainItemAssociations.addAssociation(associationToContentIDProperty);

            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(5);
                associationToClockInfo.setEssential(false);
                mainItemAssociations.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(6);
                assocationToTimestampTAI.setEssential(false);
                mainItemAssociations.addAssociation(assocationToTimestampTAI);
            }

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        iprp.addItemPropertyAssociation(itemPropertyAssociation);

        return iprp;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition monoComponent = new ComponentDefinition(0, null);
        cmpd.addComponentDefinition(monoComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC(0));
        uncc.addComponent(new Component(0, 7, ComponentFormat.SignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(imageHeight);
        ispe.setImageWidth(imageWidth);
        return ispe;
    }

    private Box makeItemReferenceBox() {
        ItemReferenceBox iref = new ItemReferenceBox();
        return iref;
    }

    private MediaDataBox createMediaDataBox8() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int j = 0; j < imageHeight; j++) {
            for (int i = 0; i < imageWidth; i++) {
                int sign = (j % 2 == 0) ? 1 : -1;
                byte value = (byte) (i * sign);
                System.out.println("value: " + value);
                baos.write(value);
            }
        }
        mdat.setData(baos.toByteArray());
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

    private TAITimeStampBox makeTimeStampBox() {
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        Instant instant = TIMESTAMP.toInstant(ZoneOffset.UTC);
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();
        long timestamp =
                GIMIUtils.NANOS_PER_SECOND * taiInstant.getTaiSeconds() + taiInstant.getNano();
        time_stamp_packet.setTAITimeStamp(timestamp);
        time_stamp_packet.setStatusBits((byte) 0x02);
        itai.setTimeStampPacket(time_stamp_packet);
        return itai;
    }

    private TAIClockInfoItemProperty makeClockInfoItemProperty() {

        TAIClockInfoItemProperty taic = new TAIClockInfoItemProperty();
        taic.setTimeUncertainty(5 * GIMIUtils.NANOS_PER_SECOND);
        taic.setCorrectionOffset(0);
        taic.setClockDriftRate(Float.NaN);
        taic.setReferenceSourceType((byte) 0x01);
        return taic;
    }
}
