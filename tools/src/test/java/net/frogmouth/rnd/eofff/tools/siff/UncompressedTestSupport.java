package net.frogmouth.rnd.eofff.tools.siff;

import static org.testng.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.CleanAperture;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.mski.MaskConfigurationProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBox;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;

public class UncompressedTestSupport {

    protected static final long MAIN_ITEM_ID = 0x1777;
    protected static final int NUM_BYTES_PER_PIXEL_RGB = 3;
    protected static final int NUM_BYTES_PER_PIXEL_RGBA = 4;

    protected static final int IMAGE_WIDTH = 1280;
    protected static final int IMAGE_HEIGHT = 720;

    protected static final int LENGTH_OF_FREEBOX_HEADER = 8;
    protected static final int MDAT_START = 2000;
    protected static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.
    protected static final Color[] COLOURS =
            new Color[] {
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.BLACK,
                Color.WHITE,
                Color.DARK_GRAY,
                Color.CYAN,
                Color.MAGENTA,
                Color.LIGHT_GRAY,
                Color.YELLOW,
                Color.PINK,
                Color.ORANGE,
                Color.GRAY
            };

    protected FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox(new FourCC("ftyp"));
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("ns01"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    protected AbstractItemProperty makeUserDescription() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Coloured squares");
        udes.setDescription("Four by Three grid with some bad pixels");
        udes.setTags("udes,grid");
        return udes;
    }

    protected ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(IMAGE_HEIGHT);
        ispe.setImageWidth(IMAGE_WIDTH);
        return ispe;
    }

    protected ItemLocationBox makeItemLocationBox_rgba_generic() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGBA);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    protected void checkAllOtherFieldsAreZero(UncompressedFrameConfigBox uncC) {
        String message =
                String.format(
                        "5.3.2 requires all other fields to be zero for profile: %s",
                        uncC.getProfile().toString());
        for (Component component : uncC.getComponents()) {
            assertEquals(component.getComponentFormat(), ComponentFormat.UnsignedInteger, message);
            assertEquals(component.getComponentAlignSize(), 0, message);
        }
        assertEquals(uncC.getBlockSize(), 0, message);
        assertFalse(uncC.isComponentLittleEndian(), message);
        assertFalse(uncC.isBlockPadLSB(), message);
        assertFalse(uncC.isBlockLittleEndian(), message);
        assertFalse(uncC.isBlockReversed(), message);
        assertFalse(uncC.isPadUnknown(), message);
        assertEquals(uncC.getPixelSize(), 0, message);
        assertEquals(uncC.getRowAlignSize(), 0, message);
        assertEquals(uncC.getTileAlignSize(), 0, message);
        assertEquals(uncC.getNumTileRowsMinusOne(), 0, message);
        assertEquals(uncC.getNumTileColumnsMinusOne(), 0, message);
    }

    protected void checkAtMostOneFAComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfFA = getNumberOfFAComponents(components, componentDefinitions);
        if (numberOfFA > 1) {
            fail(
                    "5.2.1.2 'There shall be at most one component with type 11 (FA) present in the component list'");
        }
    }

    protected void checkAtMostOnePaletteComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if (numberOfP > 1) {
            fail(
                    "5.2.1.2 'There shall be at most one component with type 10 (P) present in the component list'");
        }
    }

    protected void checkBlockReversed(UncompressedFrameConfigBox uncC) {
        if (!uncC.isBlockLittleEndian()) {
            assertFalse(
                    uncC.isBlockReversed(),
                    "5.2.1.7 'block_reversed shall be 0 if block_little_endian is 0");
        }
    }

    protected void checkBlockingConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getBlockSize() == 0) {
            assertFalse(
                    uncC.isBlockPadLSB(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            assertFalse(
                    uncC.isBlockLittleEndian(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            assertFalse(
                    uncC.isBlockReversed(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
        } else {
            // TODO: see what is possible.
        }
    }

    protected void checkComponentAlignSizeIsConsistent(UncompressedFrameConfigBox uncC) {
        if (uncC.isComponentLittleEndian()) {
            assertFalse(
                    uncC.isBlockLittleEndian(),
                    "5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
            for (Component component : uncC.getComponents()) {
                assertTrue(
                        component.getComponentAlignSize() != 0,
                        "5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
            }
        }
    }

    protected void checkComponentAlignSizeIsValid(List<Component> components) {
        for (Component component : components) {
            int component_bit_depth = component.getComponentBitDepthMinusOne() + 1;
            if (component.getComponentAlignSize() != 0) {
                assertTrue(
                        component.getComponentAlignSize() * 8 > component_bit_depth,
                        "5.2.1.3 ' component_align_size shall be either 0 or such that component_align_size*8 is greater than component_bit_depth'");
            }
        }
    }

    protected void checkComponentFormatIsValid(List<Component> components) {
        for (Component component : components) {
            if (component.getComponentFormat() == ComponentFormat.Reserved) {
                fail("Table 2. component_format field is outside of defined range");
            }
        }
    }

    protected void checkComponentPaletteBoxIsPresentIfNeeded(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            ComponentPaletteBox cpal) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if ((numberOfP > 0) && (cpal == null)) {
            fail(
                    "6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
        if ((numberOfP == 0) && (cpal != null)) {
            fail(
                    "6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
    }

    protected void checkInterleaveTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getInterleaveType().equals(Interleaving.Reserved)) {
            fail("Table 4. interleave_type field is outside of defined range");
        }
    }

    protected void checkPixelSizeConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getPixelSize() > 0) {
            // TODO: check pixel_size is large enough
        }
        if ((!uncC.getInterleaveType().equals(Interleaving.Pixel))
                && (!uncC.getInterleaveType().equals(Interleaving.MultiY))) {
            assertEquals(
                    uncC.getPixelSize(),
                    0,
                    "5.2.1.7 'pixel_size shall be 0 if interleave_type is different to 1 or 5'");
        }
    }

    protected void checkProfile(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        if ((uncC.getProfile().equals(new FourCC("gene"))) || (uncC.getProfile().hashCode() == 0)) {
            return;
        }
        List<ComponentDefinition> componentDefinitions = cmpd.getComponentDefinitions();
        List<Component> components = uncC.getComponents();
        switch (uncC.getProfile().toString()) {
            case "2vuy":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yuv2":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yvyu":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "vyuy":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "v308":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgb3":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgba":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "i420":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv12":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv21":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "abgr":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            default:
                fail("unhandled profile case: " + uncC.getProfile().toString());
        }
    }

    protected void checkSamplingTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getSamplingType().equals(SamplingType.Reserved)) {
            fail("Table 3. sampling_type field is outside of defined range");
        }
    }

    protected void checkSensorBadPixelsMap(
            SensorBadPixelsMapBox sbpm, ComponentDefinitionBox cmpd) {
        // TODO: implement checks
    }

    protected void checkTileAlignConsistency(UncompressedFrameConfigBox uncC) {
        if ((uncC.getNumTileColumnsMinusOne() == 0) && (uncC.getNumTileRowsMinusOne() == 0)) {
            assertEquals(
                    uncC.getTileAlignSize(),
                    0,
                    "5.2.1.7 'tile_align_size shall be 0 if a single tile is used'");
        }
    }

    protected void checkTileHeightIsConsistentWithFrameHeight(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        assertEquals(
                ispe.getImageHeight() % (uncC.getNumTileRowsMinusOne() + 1),
                0,
                "5.2.1.4 'The frame height shall be a multiple of num_tile_rows_minus_one+1");
    }

    protected void checkTileWidthIsConsistentWithFrameWidth(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        assertEquals(
                ispe.getImageWidth() % (uncC.getNumTileColumnsMinusOne() + 1),
                0,
                "5.2.1.4 'The frame width shall be a multiple of num_tile_cols_minus_one+1");
    }

    protected void checkUnccComponentsAreInRange(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        for (Component component : components) {
            assertTrue(
                    component.getComponentIndex() < componentDefinitions.size(),
                    "5.1.1. 'The component_index field shall be strictly less than the component_count field value of the associated ComponentDefinitionBox'");
        }
    }

    protected void validateFileTypeBox(FileTypeBox ftyp) {
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("ns01")),
                "NGA.STND.0078_0,1-02 SIFF files shall include the ns01 brand");
        // TODO: -03
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("mif2")),
                "NGA.STND.0078_0.1-04 SIFF files shall include the mif2 brand in the compatible brands list");
        // TODO: -05
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("unif")),
                "NGA.STND.0078_0.1-06 SIFF files shall include the unif brand in the compatible brands list");
        // TODO: -07
        // TODO: -08
        // TODO: -09
    }

    protected void validateItemProperties(List<AbstractItemProperty> properties) {
        ComponentDefinitionBox cmpd = null;
        UncompressedFrameConfigBox uncC = null;
        ImageSpatialExtentsProperty ispe = null;
        ComponentPaletteBox cpal = null;
        SensorBadPixelsMapBox sbpm = null;
        UserDescriptionProperty udes = null;
        MaskConfigurationProperty mskC = null;
        for (AbstractItemProperty property : properties) {
            if (property instanceof ComponentDefinitionBox componentDefinitionBox) {
                cmpd = componentDefinitionBox;
            } else if (property instanceof UncompressedFrameConfigBox uncompressedFrameConfigBox) {
                uncC = uncompressedFrameConfigBox;
            } else if (property
                    instanceof ImageSpatialExtentsProperty imageSpatialExtentsProperty) {
                ispe = imageSpatialExtentsProperty;
            } else if (property instanceof ComponentPaletteBox componentPaletteBox) {
                cpal = componentPaletteBox;
            } else if (property instanceof SensorBadPixelsMapBox sensorBadPixelsMapBox) {
                sbpm = sensorBadPixelsMapBox;
            } else if (property instanceof UserDescriptionProperty userDescriptionProperty) {
                udes = userDescriptionProperty;
            } else if (property instanceof CleanAperture clap) {
                // TODO
            } else if (property instanceof MaskConfigurationProperty maskConfigurationProperty) {
                mskC = maskConfigurationProperty;
            } else {
                fail("TODO: property: " + property.toString());
            }
        }
        if (cmpd == null) {
            fail("ComponentDefinitionBox is required (see 4.3)");
        }
        if (uncC == null) {
            fail("UncompressedFrameConfigBox is required (see 4.3)");
        }
        if (ispe == null) {
            fail("ImageSpatialExtentsProperty is required (see 4.3)");
        }
        checkUnccComponentsAreInRange(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkAtMostOneFAComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        // TODO: check we have ComponentPatternDefinitionBox if FA is present.
        checkAtMostOnePaletteComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkComponentPaletteBoxIsPresentIfNeeded(
                uncC.getComponents(), cmpd.getComponentDefinitions(), cpal);
        checkComponentFormatIsValid(uncC.getComponents());
        checkComponentAlignSizeIsValid(uncC.getComponents());
        checkComponentAlignSizeIsConsistent(uncC);
        checkTileWidthIsConsistentWithFrameWidth(uncC, ispe);
        checkTileHeightIsConsistentWithFrameHeight(uncC, ispe);
        checkSamplingTypeIsValid(uncC);
        // TODO: sampling_type == 1 checks
        // TODO: sampling_type == 2 checks
        // TODO: sampling_type == 3 checks
        checkInterleaveTypeIsValid(uncC);
        // TODO: interleave consistency checks
        checkBlockingConsistency(uncC);
        checkBlockReversed(uncC);
        checkPixelSizeConsistency(uncC);
        checkTileAlignConsistency(uncC);
        checkProfile(uncC, cmpd);
        checkSensorBadPixelsMap(sbpm, cmpd);
    }

    protected void validateItemPropertiesBox(ItemPropertiesBox iprp) {
        ItemPropertyContainerBox ipco = iprp.getItemProperties();
        validateItemProperties(ipco.getProperties());
    }

    protected void validateMetaBox(MetaBox metaBox) {
        for (Box box : metaBox.getNestedBoxes()) {
            if (box instanceof ItemPropertiesBox iprp) {
                validateItemPropertiesBox(iprp);
            } else if (box instanceof HandlerBox hdlr) {
                // Not yet
            } else if (box instanceof PrimaryItemBox pitm) {
                // Not yet
            } else if (box instanceof ItemInfoBox iinf) {
                // Not yet
            } else if (box instanceof ItemLocationBox iloc) {
                // Not yet
            } else if (box instanceof ItemDataBox idat) {
                // Not yet
            } else if (box instanceof ItemReferenceBox iref) {
                // Not yet
            } else {
                fail(
                        "metaBox validation is not yet implemented for this case: "
                                + box.getFullName());
            }
        }
    }

    protected void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            if (box instanceof FileTypeBox ftyp) {
                validateFileTypeBox(ftyp);
            }
            if (box instanceof MetaBox metaBox) {
                validateMetaBox(metaBox);
            }
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    protected int getNumberOfComponentsWithType(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            int componentType) {
        int count = 0;
        for (Component component : components) {
            if (componentDefinitions.get(component.getComponentIndex()).getComponentType()
                    == componentType) {
                count += 1;
            }
        }
        return count;
    }

    protected int getNumberOfFAComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 11);
    }

    protected int getNumberOfPaletteComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 10);
    }

    protected HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    protected ItemInfoBox makeItemInfoBox() {
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

    protected PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    protected void drawColouredRectangles(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                g2.setColor(Uncompressed_yuv_Test.COLOURS[r * 4 + c]);
                g2.fillRect(
                        c * IMAGE_WIDTH / 4,
                        r * IMAGE_HEIGHT / 3,
                        IMAGE_WIDTH / 4,
                        IMAGE_HEIGHT / 3);
            }
        }
    }

    protected ItemLocationBox makeItemLocationBox_rgb_generic() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGB);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    protected ComponentDefinitionBox makeComponentDefinitionBox_rgb_generic() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    // TODO: move to utility class?
    public byte[] shortArrayToByteArray(short[] data, ByteOrder byteOrder) {
        byte[] bytes = new byte[data.length * Short.BYTES];
        int c = 0;
        for (int i = 0; i < data.length; i++) {
            if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
                bytes[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
                bytes[c++] = (byte) (((data[i])) & 0xFF);

            } else {
                bytes[c++] = (byte) (((data[i])) & 0xFF);
                bytes[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
            }
        }
        return bytes;
    }

    protected ItemLocationBox makeItemLocationBox_two_byte_per_pixel() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * 2);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }
}
