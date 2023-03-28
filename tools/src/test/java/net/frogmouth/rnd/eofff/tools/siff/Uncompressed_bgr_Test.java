/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_HEIGHT;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_WIDTH;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MAIN_ITEM_ID;
import static org.testng.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.PixelCoordinate;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class Uncompressed_bgr_Test extends UncompressedTestSupport {
    @Test
    public void writeFile_bgr() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_bgr();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_bgr();
        boxes.add(mdat);
        writeBoxes(boxes, "test_uncompressed_bgr.mp4");
    }

    private MetaBox createMetaBox_bgr() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_bgr());
        boxes.add(makeItemPropertiesBox_bgr());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemLocationBox makeItemLocationBox_bgr() {
        return makeItemLocationBox_rgb_generic();
    }

    private Box makeItemPropertiesBox_bgr() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_bgr());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_bgr() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(2, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
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

    private MediaDataBox createMediaDataBox_bgr() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        drawColouredRectangles(image);
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private MediaDataBox createMediaDataBox_bgr_sbpm() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        drawColouredRectangles(image);
        overdrawBadPixels(image);
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private void overdrawBadPixels(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        for (int r = 100; r < 105; r++) {
            g2.setColor(Color.WHITE);
            g2.drawLine(0, r, IMAGE_WIDTH - 1, r);
        }

        for (int c = 80; c < 82; c++) {
            g2.setColor(Color.BLACK);
            g2.drawLine(c, 0, c, IMAGE_HEIGHT - 1);
        }

        image.setRGB(120, 160, Color.BLACK.getRGB());
        image.setRGB(121, 160, Color.BLACK.getRGB());
        image.setRGB(122, 160, Color.BLACK.getRGB());
        image.setRGB(123, 160, Color.BLACK.getRGB());
        image.setRGB(120, 161, Color.BLACK.getRGB());
        image.setRGB(121, 161, Color.BLACK.getRGB());
        image.setRGB(122, 161, Color.BLACK.getRGB());
        image.setRGB(123, 161, Color.BLACK.getRGB());
    }

    private MetaBox createMetaBox_bgr_sbpm() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_bgr());
        boxes.add(makeItemPropertiesBox_bgr_sbpm());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    @Test
    public void writeFile_bgr_sbpm() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_bgr_sbpm();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_bgr_sbpm();
        boxes.add(mdat);
        writeBoxes(boxes, "test_uncompressed_bgr_sbpm.mp4");
    }

    private Box makeItemPropertiesBox_bgr_sbpm() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_bgr());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeSensorBadPixelsMap());
        ipco.addProperty(makeUserDescription());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        PropertyAssociation associationToSensorBadPixelsMapBox = new PropertyAssociation();
        associationToSensorBadPixelsMapBox.setPropertyIndex(4);
        associationToSensorBadPixelsMapBox.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(associationToSensorBadPixelsMapBox);

        PropertyAssociation associationToUserDescription = new PropertyAssociation();
        associationToUserDescription.setPropertyIndex(5);
        associationToUserDescription.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(associationToUserDescription);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);

        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private AbstractItemProperty makeSensorBadPixelsMap() {
        SensorBadPixelsMapBox sbpm = new SensorBadPixelsMapBox();
        sbpm.addComponentIndex(0);
        sbpm.addComponentIndex(1);
        sbpm.addComponentIndex(2);
        sbpm.setCorrectionApplied(false);
        sbpm.addBadRow(100);
        sbpm.addBadRow(101);
        sbpm.addBadRow(102);
        sbpm.addBadRow(105);
        sbpm.addBadRow(104);
        sbpm.addBadRow(103);
        sbpm.addBadColumn(80);
        sbpm.addBadColumn(81);
        sbpm.addBadColumn(82);
        sbpm.addBadPixel(new PixelCoordinate(120, 160));
        sbpm.addBadPixel(new PixelCoordinate(120, 161));
        sbpm.addBadPixel(new PixelCoordinate(121, 160));
        sbpm.addBadPixel(new PixelCoordinate(121, 161));
        sbpm.addBadPixel(new PixelCoordinate(122, 160));
        sbpm.addBadPixel(new PixelCoordinate(122, 161));
        sbpm.addBadPixel(new PixelCoordinate(123, 160));
        sbpm.addBadPixel(new PixelCoordinate(123, 161));
        return sbpm;
    }
}
