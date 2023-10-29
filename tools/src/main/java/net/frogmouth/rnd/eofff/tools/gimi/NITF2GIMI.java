package net.frogmouth.rnd.eofff.tools.gimi;

import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.FAKE_SECURITY_MIME_TYPE;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.HEIF_SUFFIX;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.MICROSECONDS_PER_SECOND;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.addPropertyFor;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.makeRandomContentId;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.makeUserDescriptionCopyright;
import static net.frogmouth.rnd.eofff.tools.gimi.MetadataUtils.dumpMisbMessage;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.sidd.SIDDParser;
import net.frogmouth.rnd.eofff.sidd.v2.gen.ExploitationFeaturesCollectionInformationType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.ExploitationFeaturesType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.ImageCornersType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.SIDD;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.tools.MediaDataBoxBuilder;
import net.frogmouth.rnd.eofff.tools.gimi.fakeSecurity.SecurityLevel;
import net.frogmouth.rnd.eofff.tools.gimi.fakeSecurity.gen.FakeSecurity;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.apache.commons.io.FilenameUtils;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.ParseStrategy;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.st0601.FullCornerLatitude;
import org.jmisb.st0601.FullCornerLongitude;
import org.jmisb.st0601.IUasDatalinkValue;
import org.jmisb.st0601.PrecisionTimeStamp;
import org.jmisb.st0601.UasDatalinkMessage;
import org.jmisb.st0601.UasDatalinkString;
import org.jmisb.st0601.UasDatalinkTag;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;
import picocli.CommandLine;

@CommandLine.Command(name = "NITF2GIMI", version = "NITF2GIMI 0.1", mixinStandardHelpOptions = true)
public class NITF2GIMI implements Runnable {

    private SIDD sidd;
    private ImageSegment imageSegment;

    private final FileTypeBox ftyp = new FileTypeBox(new FourCC("ftyp"));
    private final MetaBox meta = new MetaBox();
    private MediaDataBox mdat;
    private final ItemLocationBox iloc = new ItemLocationBox();
    private long ilocOffset = 0;
    private final ItemInfoBox iinf = new ItemInfoBox();
    private final ItemPropertiesBox iprp = new ItemPropertiesBox();
    private final ItemReferenceBox iref = new ItemReferenceBox();
    private final ItemDataBoxBuilder idatBuilder = new ItemDataBoxBuilder();
    private long primaryItemId;
    private long highestItemId = 0;

    @CommandLine.Option(
            names = {"--noSecurity"},
            description = "Skip adding fake security markings")
    private boolean skipFakeSecurity = false;

    @CommandLine.Option(
            names = {"--securityLevel"},
            description = "Security Level for fake security markings")
    private SecurityLevel securityLevel = SecurityLevel.Unrestricted;

    @CommandLine.Option(
            names = {"--caveat"},
            description = "Security caveat for fake security markings (repeatable)")
    private String[] caveats;

    @CommandLine.Option(
            names = {"--relTo"},
            description = "Releasability for fake security markings (repeatable)")
    private String[] releaseableTos;

    @CommandLine.Option(
            names = {"--declasDate"},
            description = "Security marking 'declassOn' date  (YYYY-MM-DD)")
    private LocalDate declasOnDate = LocalDate.now().plus(1, ChronoUnit.YEARS);

    @CommandLine.Option(
            names = {"--noCompatibleBrand"},
            description = "Skip adding geo1 compatible brand")
    private boolean skipCompatibleBrand = false;

    @CommandLine.Option(
            names = {"--noContentDescribes"},
            description = "Skip adding cdsc item references")
    private boolean skipContentDescribes = false;

    @CommandLine.Option(
            names = {"--noContentIDs"},
            description = "Skip adding content IDs")
    private boolean skipContentIDs = false;

    @CommandLine.Option(
            names = {"--copyright"},
            description = "Copyright statement")
    private String copyright;

    @CommandLine.Option(
            names = {"--noCornerPoints"},
            description = "Skip adding general metadata like corner points")
    private boolean skipGeneralMetadata = false;

    @CommandLine.Option(
            names = {"--tiled"},
            description = "Retile the source imagery to the specified size")
    private int tileSize = -1;

    @CommandLine.Parameters(
            paramLabel = "<source file>",
            arity = "1",
            description = "the path to the NITF source file",
            index = "0")
    private String sourcePath;

    @CommandLine.Parameters(
            paramLabel = "target file",
            arity = "0..1",
            description = "the path to the target file (derived from source if not provided)",
            index = "1")
    private String targetPath;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new NITF2GIMI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            loadSourceData();
            if (targetPath == null) {
                deriveTargetPath();
            }
            buildTargetData();
            addContentIdForPrimaryImage();
            addCopyrightDescription();
            if (!skipFakeSecurity) {
                addFakeSecurity();
            }
            if (!skipGeneralMetadata) {
                addGeneralMetadata();
            }
            ItemDataBox idat = idatBuilder.build();
            if (idat.getData().length > 0) {
                meta.addNestedBox(idat);
            }
            writeOutTargetFile();
        } catch (IOException | InterruptedException | JAXBException ex) {
            // TODO: better logging
            Logger.getLogger(NITF2GIMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSourceData() throws IOException, InterruptedException {
        try {
            final SlottedParseStrategy parseStrategy =
                    new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
            InputStream instream = new FileInputStream(sourcePath);
            NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(instream));
            NitfParser.parse(reader, parseStrategy);
            // TODO: we should not assume there is exactly one image segment
            imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
            // TODO: we should not assume the first DES is SIDD XML
            String xml = getDESXML(parseStrategy);
            SIDDParser siddParser = new SIDDParser();
            sidd = siddParser.parse(xml);
        } catch (NitfFormatException | JAXBException ex) {
            throw new IOException(ex.toString());
        }
    }

    private void deriveTargetPath() {
        targetPath = FilenameUtils.removeExtension(sourcePath) + "." + HEIF_SUFFIX;
    }

    private void buildTargetData() throws IOException {
        buildTargetFileTypeBox();
        buildTargetMetaBox();
        buildTargetMediaDataBox();
    }

    private void buildTargetFileTypeBox() throws IOException {

        ftyp.setMajorBrand(Brand.MIF2);
        ftyp.setMinorVersion(0);
        ftyp.addCompatibleBrand(Brand.MIF1);
        ftyp.addCompatibleBrand(Brand.MIAF);
        if (!skipCompatibleBrand) {
            ftyp.addCompatibleBrand(new Brand("geo1"));
        }
    }

    private void buildTargetMetaBox() throws IOException {
        meta.addNestedBox(GIMIUtils.makeHandlerBox());
        primaryItemId = getNextItemId();
        meta.addNestedBox(GIMIUtils.makePrimaryItemBox(primaryItemId));
        setupItemInfoBox();
        meta.addNestedBox(iinf);
        setupItemLocationBox();
        meta.addNestedBox(iloc);
        setupItemPropertiesBox();
        meta.addNestedBox(iprp);
        if (!skipContentDescribes) {
            meta.addNestedBox(iref);
        }
    }

    private void setupItemInfoBox() throws IOException {
        ItemInfoEntry primaryItemInfoEntry = new ItemInfoEntry();
        primaryItemInfoEntry.setVersion(2);
        primaryItemInfoEntry.setItemID(primaryItemId);
        FourCC unci = new FourCC("unci");
        primaryItemInfoEntry.setItemType(unci.asUnsigned());
        primaryItemInfoEntry.setItemName("Primary item");
        iinf.addItem(primaryItemInfoEntry);
    }

    private void setupItemLocationBox() {
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        iloc.addItem(makePrimaryItemILocItem());
    }

    private ILocItem makePrimaryItemILocItem() {
        ILocItem primaryItemLocation = new ILocItem();
        primaryItemLocation.setItemId(primaryItemId);
        ILocExtent primaryItemExtent = new ILocExtent();
        primaryItemExtent.setExtentIndex(0);
        primaryItemExtent.setExtentOffset(0);
        if (tileSize > 0) {
            primaryItemExtent.setExtentLength(this.getPaddedColumns() * this.getPaddedRows() * 1);
        } else {
            primaryItemExtent.setExtentLength(imageSegment.getDataLength());
        }
        primaryItemLocation.addExtent(primaryItemExtent);
        return primaryItemLocation;
    }

    private void setupItemPropertiesBox() {
        iprp.setItemProperties(new ItemPropertyContainerBox());
        addPropertiesForPrimaryItem();
    }

    private long getNextItemId() {
        highestItemId += 1;
        return highestItemId;
    }

    private void buildTargetMediaDataBox() throws IOException {
        MediaDataBoxBuilder mdatBuilder = new MediaDataBoxBuilder();
        long dataLength = imageSegment.getDataLength();
        byte[] data = new byte[(int) dataLength];
        imageSegment.getData().readFully(data);
        if (tileSize > 0) {
            if ((imageSegment.getNumberOfBitsPerPixelPerBand() != 8)
                    || (imageSegment.getActualBitsPerPixelPerBand()
                            != imageSegment.getNumberOfBitsPerPixelPerBand())
                    || (imageSegment.getNumberOfBlocksPerRow() != 1)
                    || (imageSegment.getNumberOfBlocksPerColumn() != 1)) {
                throw new UnsupportedOperationException("we only do the simple stuff so far");
            }
            byte[] paddedData;
            int paddedColumns = getPaddedColumns();
            if (needPaddingToTileSize()) {
                int paddedRows = getPaddedRows();
                paddedData = new byte[paddedColumns * paddedRows];
                for (int r = 0; r < imageSegment.getNumberOfRows(); r++) {
                    System.arraycopy(
                            data,
                            (int) (r * imageSegment.getNumberOfColumns()),
                            paddedData,
                            r * paddedColumns,
                            (int) imageSegment.getNumberOfColumns());
                }
                // The rest is already zero filled.
            } else {
                throw new UnsupportedOperationException("need to implement");
            }
            int numTilesX = getNumTilesX();
            int numTilesY = getNumTilesY();
            for (int tileY = 0; tileY < numTilesY; tileY++) {
                for (int tileX = 0; tileX < numTilesX; tileX++) {
                    byte[] tileBytes = new byte[tileSize * tileSize * 1];
                    int base = (tileY * numTilesX * tileBytes.length) + (tileX * tileSize);
                    // System.out.println(String.format("base [%d, %d]: %d", tileX, tileY, base));
                    for (int row = 0; row < tileSize; row++) {
                        // We are copying this tile row at a time.
                        System.arraycopy(
                                paddedData,
                                base + row * paddedColumns,
                                tileBytes,
                                row * tileSize,
                                tileSize);
                    }
                    mdatBuilder.addData(tileBytes);
                }
            }
        } else {
            mdatBuilder.addData(data);
        }
        mdat = mdatBuilder.build();
    }

    private int getNumTilesY() {
        return (int) ((imageSegment.getNumberOfRows() + (tileSize - 1)) / tileSize);
    }

    private int getNumTilesX() {
        return (int) ((imageSegment.getNumberOfColumns() + (tileSize - 1)) / tileSize);
    }

    private boolean needPaddingToTileSize() {
        return (imageSegment.getNumberOfColumns() % tileSize != 0)
                || (imageSegment.getNumberOfRows() % tileSize != 0);
    }

    private int getPaddedColumns() {
        long columns = imageSegment.getNumberOfColumns();
        long remainderX = (columns % tileSize);
        if (remainderX == 0) {
            // We are already aligned
            return (int) columns;
        } else {
            long paddingX = tileSize - remainderX;
            return (int) (columns + paddingX);
        }
    }

    private int getPaddedRows() {
        long rows = imageSegment.getNumberOfRows();
        long remainderY = (rows % tileSize);
        if (remainderY == 0) {
            // We are already aligned
            return (int) rows;
        } else {
            long paddingY = tileSize - remainderY;
            return (int) (rows + paddingY);
        }
    }

    private void writeOutTargetFile() throws IOException {
        List<Box> targetBoxes = new ArrayList<>();
        targetBoxes.add(ftyp);
        targetBoxes.add(meta);
        targetBoxes.add(mdat);
        long mdatOffset = ftyp.getSize() + meta.getSize() + (mdat.getSize() - mdat.getBodySize());
        for (ILocItem item : iloc.getItems()) {
            if (item.getConstructionMethod() == 0) {
                item.setBaseOffset(mdatOffset);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : targetBoxes) {
            box.writeTo(streamWriter);
        }
        Path target = Path.of(targetPath);
        Files.write(target, baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private String getDESXML(ParseStrategy parseStrategy) {
        DataExtensionSegment des = parseStrategy.getDataSource().getDataExtensionSegments().get(0);
        byte[] desData = new byte[(int) des.getDataLength()];
        final StringBuilder xml = new StringBuilder();
        des.consume(
                (x) -> {
                    try {
                        x.readFully(desData);
                        String s = new String(desData, StandardCharsets.UTF_8);
                        xml.append(s);
                    } catch (IOException ex) {
                        // TODO: better logging
                        java.util.logging.Logger.getLogger(NITF2GIMI.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                });
        return xml.toString();
    }

    private void addPropertiesForPrimaryItem() {
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox());
        ipco.addProperty(makeUncompressedFrameConfigBox());
        ipco.addProperty(makeImageSpatialExtentsProperty(imageSegment));
        iprp.setItemProperties(ipco);
        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(primaryItemId);
            {
                PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
                associationToComponentDefinitionBox.setPropertyIndex(1);
                associationToComponentDefinitionBox.setEssential(true);
                entry.addAssociation(associationToComponentDefinitionBox);
            }
            {
                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(3);
                associationToImageSpatialExtentsProperty.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }
    }

    private ComponentDefinitionBox makeComponentDefinitionBox() {
        // TODO: this should depend on what the image segment has
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition monoComponent = new ComponentDefinition(0, null);
        cmpd.addComponentDefinition(monoComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox() {
        // TODO: this should depend on what the image segment has
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        if (tileSize > 0) {
            uncc.setRowAlignSize(0);
            uncc.setTileAlignSize(0);
            uncc.setNumTileColumnsMinusOne(getNumTilesX() - 1);
            uncc.setNumTileRowsMinusOne(getNumTilesY() - 1);
        } else {
            uncc.setRowAlignSize(0);
            uncc.setTileAlignSize(0);
            uncc.setNumTileColumnsMinusOne(0);
            uncc.setNumTileRowsMinusOne(0);
        }
        return uncc;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty(ImageSegment imageSegment) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        // ispe.setImageHeight(imageSegment.getNumberOfRows());
        // ispe.setImageWidth(imageSegment.getNumberOfColumns());
        ispe.setImageHeight(this.getPaddedRows());
        ispe.setImageWidth(this.getPaddedColumns());
        return ispe;
    }

    private void addContentIdForPrimaryImage() {
        if (skipContentIDs) {
            return;
        }
        GIMIUtils.addPropertyFor(iprp, makeRandomContentId(), primaryItemId);
    }

    private void addCopyrightDescription() {
        if (copyright != null) {
            addPropertyFor(iprp, makeUserDescriptionCopyright(copyright), primaryItemId);
        }
    }

    private void addFakeSecurity() throws IOException {
        try {
            FakeSecurity fakeSecurity = new FakeSecurity();
            fakeSecurity.setFakeLevel(securityLevel.toString());
            if ((caveats != null) && (caveats.length > 0)) {
                fakeSecurity.getFakeCaveat().addAll(Arrays.asList(caveats));
            }
            if ((releaseableTos != null) && (releaseableTos.length > 0)) {
                fakeSecurity.getFakeRelTo().addAll(Arrays.asList(releaseableTos));
            }
            XMLGregorianCalendar xmlGregorianCalendar =
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(declasOnDate.toString());
            fakeSecurity.setFakeDeclassOn(xmlGregorianCalendar);
            JAXBContext context = JAXBContext.newInstance(FakeSecurity.class);
            Marshaller marshaller = context.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(fakeSecurity, baos);
            byte[] securityData = baos.toByteArray();
            System.out.println("Fake Security data:");
            System.out.println(new String(securityData, StandardCharsets.UTF_8));
            idatBuilder.addData(securityData);
            long fakeSecurityItemId = getNextItemId();
            {
                ItemInfoEntry fakeSecurityItem = new ItemInfoEntry();
                fakeSecurityItem.setVersion(2);
                fakeSecurityItem.setItemID(fakeSecurityItemId);
                FourCC mime_fourcc = new FourCC("mime");
                fakeSecurityItem.setItemType(mime_fourcc.asUnsigned());
                fakeSecurityItem.setContentType(FAKE_SECURITY_MIME_TYPE);
                fakeSecurityItem.setItemName("Security Marking (Fake XML)");
                iinf.addItem(fakeSecurityItem);
            }
            {
                ILocItem fakeSecurityItemLocation = new ILocItem();
                fakeSecurityItemLocation.setConstructionMethod(1);
                fakeSecurityItemLocation.setItemId(fakeSecurityItemId);
                fakeSecurityItemLocation.setBaseOffset(ilocOffset);
                ILocExtent securityExtent = new ILocExtent();
                securityExtent.setExtentIndex(0);
                securityExtent.setExtentOffset(0);
                securityExtent.setExtentLength(securityData.length);
                ilocOffset += securityExtent.getExtentLength();
                fakeSecurityItemLocation.addExtent(securityExtent);
                iloc.addItem(fakeSecurityItemLocation);
            }
            {
                if (!skipContentIDs) {
                    addPropertyFor(iprp, makeRandomContentId(), fakeSecurityItemId);
                }
            }
        } catch (JAXBException ex) {
            throw new IOException(ex.toString());
        } catch (DatatypeConfigurationException ex) {
            throw new IOException(ex.toString());
        }
    }

    private void addGeneralMetadata() throws IOException, JAXBException {
        long generalMetadataItemId = getNextItemId();
        byte[] generalMetadataBytes = getST0601MetadataBytes(true);
        idatBuilder.addData(generalMetadataBytes);
        {
            ItemInfoEntry st0601MetadataItem = new ItemInfoEntry();
            st0601MetadataItem.setVersion(2);
            st0601MetadataItem.setItemID(generalMetadataItemId);
            FourCC mime_fourcc = new FourCC("uri ");
            st0601MetadataItem.setItemType(mime_fourcc.asUnsigned());
            st0601MetadataItem.setItemUriType(GIMIUtils.ST0601_URI);
            st0601MetadataItem.setItemName("General Metadata (ST 0601)");
            iinf.addItem(st0601MetadataItem);
        }
        {
            ILocItem st0601MetadataItemLocation = new ILocItem();
            st0601MetadataItemLocation.setConstructionMethod(1);
            st0601MetadataItemLocation.setItemId(generalMetadataItemId);
            st0601MetadataItemLocation.setBaseOffset(ilocOffset);
            ILocExtent metadataExtent = new ILocExtent();
            metadataExtent.setExtentIndex(0);
            metadataExtent.setExtentOffset(0);
            metadataExtent.setExtentLength(generalMetadataBytes.length);
            ilocOffset += metadataExtent.getExtentLength();
            st0601MetadataItemLocation.addExtent(metadataExtent);
            iloc.addItem(st0601MetadataItemLocation);
        }
        if (!skipContentDescribes) {
            SingleItemReferenceBox st0601DescribedPrimaryItem =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            st0601DescribedPrimaryItem.setFromItemId(generalMetadataItemId);
            st0601DescribedPrimaryItem.addReference(primaryItemId);
            iref.addItem(st0601DescribedPrimaryItem);
        }
        if (!skipContentIDs) {
            addPropertyFor(iprp, makeRandomContentId(), generalMetadataItemId);
        }
    }

    private byte[] getST0601MetadataBytes(boolean dump) throws IOException, JAXBException {
        SortedMap<UasDatalinkTag, IUasDatalinkValue> map = new TreeMap<>();
        map.put(UasDatalinkTag.UasLdsVersionNumber, new org.jmisb.st0601.ST0601Version((short) 19));
        ImageCornersType imageCornersType = sidd.getGeoData().getImageCorners();
        for (ImageCornersType.ICP corner : imageCornersType.getICP()) {
            if (corner.getIndex().contains("FRFC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt1,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_1));
                map.put(
                        UasDatalinkTag.CornerLonPt1,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_1));
            }
            if (corner.getIndex().contains("FRLC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt2,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_2));
                map.put(
                        UasDatalinkTag.CornerLonPt2,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_2));
            }
            if (corner.getIndex().contains("LRLC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt3,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_3));
                map.put(
                        UasDatalinkTag.CornerLonPt3,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_3));
            }
            if (corner.getIndex().contains("LRFC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt4,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_4));
                map.put(
                        UasDatalinkTag.CornerLonPt4,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_4));
            }
        }
        ExploitationFeaturesType exploitationFeatures = sidd.getExploitationFeatures();
        if ((exploitationFeatures.getCollection() != null)
                && (!exploitationFeatures.getCollection().isEmpty())) {
            ExploitationFeaturesType.Collection collection0 =
                    exploitationFeatures.getCollection().get(0);
            if (collection0.getIdentifier() != null) {
                map.put(
                        UasDatalinkTag.MissionId,
                        new UasDatalinkString(
                                UasDatalinkString.MISSION_ID,
                                exploitationFeatures.getCollection().get(0).getIdentifier()));
            }
            if (collection0.getInformation() != null) {
                ExploitationFeaturesCollectionInformationType collectionInformation =
                        collection0.getInformation();
                if (collectionInformation.getCollectionDateTime() != null) {
                    XMLGregorianCalendar xmlDateTime =
                            collectionInformation.getCollectionDateTime();
                    Instant instant = xmlDateTime.toGregorianCalendar().toInstant();
                    long seconds = instant.getEpochSecond();
                    long microseconds = instant.getNano() / 1000;
                    PrecisionTimeStamp precisionTimeStamp =
                            new PrecisionTimeStamp(
                                    seconds * MICROSECONDS_PER_SECOND + microseconds);
                    map.put(UasDatalinkTag.PrecisionTimeStamp, precisionTimeStamp);
                    addTimestampTAI(instant);
                }
                if (collectionInformation.getSensorName() != null) {
                    map.put(
                            UasDatalinkTag.ImageSourceSensor,
                            new UasDatalinkString(
                                    UasDatalinkString.IMAGE_SOURCE_SENSOR,
                                    collectionInformation.getSensorName()));
                }
                // TODO: more - sensor mode, duration?
            }
        }
        var st0601 = new UasDatalinkMessage(map);
        if (dump) {
            dumpMisbMessage(st0601);
        }
        byte[] st0601bytes = st0601.frameMessage(false);
        BerField ber = BerDecoder.decode(st0601bytes, UniversalLabel.LENGTH, false);
        int lengthOfLength = ber.getLength();
        byte[] st0601ValueBytes = new byte[ber.getValue()];
        System.arraycopy(
                st0601bytes,
                UniversalLabel.LENGTH + lengthOfLength,
                st0601ValueBytes,
                0,
                st0601ValueBytes.length);
        return st0601ValueBytes;
    }

    private void addTimestampTAI(Instant instant) {
        // TODO: revisit these to see if we can add more fidelity.
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();
        long timestamp =
                GIMIUtils.NANOS_PER_SECOND * taiInstant.getTaiSeconds() + taiInstant.getNano();
        time_stamp_packet.setTAITimeStamp(timestamp);
        time_stamp_packet.setStatusBits((byte) 0x02);
        itai.setTimeStampPacket(time_stamp_packet);
        addPropertyFor(iprp, itai, primaryItemId);

        TAIClockInfoBox taic = new TAIClockInfoBox();
        taic.setTimeUncertainty(1 * GIMIUtils.NANOS_PER_SECOND);
        taic.setCorrectionOffset(0);
        taic.setClockDriftRate(Float.NaN);
        taic.setReferenceSourceType((byte) 0x01);
        addPropertyFor(iprp, taic, primaryItemId);
    }
}
