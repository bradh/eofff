package net.frogmouth.rnd.eofff.tools.gimi;

import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.addPropertyFor;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.makeRandomContentId;
import static net.frogmouth.rnd.eofff.tools.gimi.GIMIUtils.makeUserDescriptionCopyright;
import static net.frogmouth.rnd.eofff.tools.gimi.MetadataUtils.dumpMisbMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
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
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoBox;
import org.apache.commons.io.FilenameUtils;
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
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "GIMISum", version = "GIMISum 0.1", mixinStandardHelpOptions = true)
public class GIMISum implements Runnable {

    @Option(
            names = {"--caveat"},
            description = "Security caveat for fake security markings (repeatable)")
    private String[] caveats;

    @Option(
            names = {"--noCompatibleBrand"},
            description = "Skip adding geo1 compatible brand")
    private boolean skipCompatibleBrand = false;

    @Option(
            names = {"--relTo"},
            description = "Releasability for fake security markings (repeatable)")
    private String[] releaseableTos;

    @Option(
            names = {"--declasDate"},
            description = "Security marking 'declassOn' date  (YYYY-MM-DD)")
    private LocalDate declasOnDate = LocalDate.now().plus(1, ChronoUnit.YEARS);

    @Option(
            names = {"--noCornerPoints"},
            description = "Skip adding general metadata like corner points")
    private boolean skipGeneralMetadata = false;

    @Option(
            names = {"--noContentDescribes"},
            description = "Skip adding cdsc item references")
    private boolean skipContentDescribes = false;

    @Option(
            names = {"--noContentIDs"},
            description = "Skip adding content IDs")
    private boolean skipContentIDs = false;

    @Option(
            names = {"--dateTime"},
            description = "Precision Time Stamp date/time")
    private LocalDateTime dateTime;

    @Option(
            names = {"--copyright"},
            description = "Copyright statement")
    private String copyright;

    @Option(
            names = {"--missionId"},
            description = "Mission identifier for metadata (defaults to source file name)")
    private String missionId;

    @Option(
            names = {"--codec"},
            description = "Coding to use for the image item")
    private Codec codec = Codec.HEVC;

    @Parameters(
            paramLabel = "<source file>",
            arity = "1",
            description = "the path to the source file",
            index = "0")
    private String sourcePath;

    @Parameters(
            paramLabel = "target file",
            arity = "0..1",
            description = "the path to the target file (derived from source if not provided)",
            index = "1")
    private String targetPath;

    private List<Box> sourceBoxes = new ArrayList<>();
    private final FileTypeBox ftyp = new FileTypeBox(new FourCC("ftyp"));
    private final MetaBox meta = new MetaBox();
    private final MediaDataBox mdat = new MediaDataBox();
    private long primaryItemId;
    private long highestItemId;
    private final ItemInfoBox iinf = new ItemInfoBox();
    private final ItemPropertiesBox iprp = new ItemPropertiesBox();
    // TODO: iloc builder?
    private final ItemDataBoxBuilder idatBuilder = new ItemDataBoxBuilder();
    private final ItemLocationBox iloc = new ItemLocationBox();
    private long ilocOffset = 0;
    private final ItemReferenceBox iref = new ItemReferenceBox();

    private FileDirectory directory;
    private GeoTransform transform;
    private int imageHeight;
    private int imageWidth;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new GIMISum()).execute(args);
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
            addTimestampTAI();
            if (!skipGeneralMetadata) {
                addGeneralMetadata();
            }
            ItemDataBox idat = idatBuilder.build();
            if (idat.getData().length > 0) {
                meta.addNestedBox(idat);
            }
            writeOutTargetFile();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GIMISum.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSourceData() throws IOException, InterruptedException {
        String pngFile;
        if (sourcePath.endsWith(".png")) {
            pngFile = sourcePath;
        } else {
            pngFile = Files.createTempFile(null, ".png").toString();
            ProcessBuilder builder =
                    new ProcessBuilder(
                            "/usr/local/bin/gdal_translate", "-of", "PNG", sourcePath, pngFile);
            if (sourcePath.endsWith(".tif")) {
                mil.nga.tiff.TIFFImage tiffImage =
                        mil.nga.tiff.TiffReader.readTiff(new File(sourcePath));
                List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
                directory = directories.get(0);
                List<Double> pixelScale = directory.getModelPixelScale();
                List<Double> modelTiePoint = directory.getModelTiepoint();
                imageHeight = (int) directory.getImageHeight();
                imageWidth = (int) directory.getImageWidth();
                transform = new GeoTransform(modelTiePoint, pixelScale);
            } else {
                throw new IOException("Need to add support for file type: " + sourcePath);
            }
            Process process = builder.start();
            process.waitFor();
            // TODO: better error checks and handling
            InputStream errorStream = process.getErrorStream();
            String errors = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(errors);
        }
        {
            String heifFile = Files.createTempFile(null, ".heif").toString();
            ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/heif-enc");
            builder.command().addAll(Arrays.asList(codec.getArguments()));
            builder.command().add("-o");
            builder.command().add(heifFile);
            builder.command().add(pngFile);
            // System.out.println(builder.command());
            Process process = builder.start();
            process.waitFor();
            // TODO: better error checks and handling
            InputStream errorStream = process.getErrorStream();
            String errors = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(errors);
            FileParser fileParser = new FileParser();
            sourceBoxes = new ArrayList<>();
            sourceBoxes.addAll(fileParser.parse(Path.of(heifFile)));
        }
    }

    private void deriveTargetPath() {
        targetPath = FilenameUtils.removeExtension(sourcePath) + codec.getSuffix();
    }

    private void buildTargetData() throws IOException {
        buildTargetFileTypeBox();
        buildTargetMetaBox();
        buildTargetMediaDataBox();
    }

    private void buildTargetFileTypeBox() throws IOException {
        FileTypeBox sourceFtyp = (FileTypeBox) getTopLevelBoxByFourCC(sourceBoxes, "ftyp");
        if (sourceFtyp == null) {
            throw new IOException("failed to find source ftyp box");
        }
        ftyp.setMajorBrand(new Brand(sourceFtyp.getMajorBrand().toString()));
        ftyp.setMinorVersion(sourceFtyp.getMinorVersion());
        for (Brand compatibleBrand : sourceFtyp.getCompatibleBrands()) {
            ftyp.addCompatibleBrand(compatibleBrand);
        }
        if (!skipCompatibleBrand) {
            ftyp.addCompatibleBrand(new Brand("geo1"));
        }
    }

    private void buildTargetMetaBox() throws IOException {
        MetaBox sourceMeta = (MetaBox) getTopLevelBoxByFourCC(sourceBoxes, "meta");
        meta.addNestedBox(GIMIUtils.makeHandlerBox());
        if (sourceMeta == null) {
            throw new IOException("failed to find source meta box");
        }
        ItemInfoBox sourceIinf = (ItemInfoBox) findChildBox(sourceMeta, "iinf");
        if (sourceIinf == null) {
            throw new IOException("failed to find source iinf box");
        }
        PrimaryItemBox sourcePitm = (PrimaryItemBox) findChildBox(sourceMeta, "pitm");
        if (sourcePitm != null) {
            primaryItemId = sourcePitm.getItemID();
        } else {
            System.out.println("No source pitm box - assuming first item in iinf is the one...");
            primaryItemId = sourceIinf.getItems().get(0).getItemID();
        }
        highestItemId = primaryItemId;
        meta.addNestedBox(GIMIUtils.makePrimaryItemBox(primaryItemId));
        setupItemInfoBox(sourceIinf);
        meta.addNestedBox(iinf);
        {
            ItemLocationBox sourceIloc = (ItemLocationBox) findChildBox(sourceMeta, "iloc");
            ILocItem sourcePrimaryItemLocationEntry = sourceIloc.findItemById(primaryItemId);
            if (sourcePrimaryItemLocationEntry.getExtents().size() != 1) {
                throw new IOException("cannot handle multiple source extents yet");
            }
            ILocExtent sourcePrimaryItemExtent = sourcePrimaryItemLocationEntry.getExtents().get(0);
            if (sourcePrimaryItemLocationEntry.getConstructionMethod() != 0) {
                throw new IOException("assumed primary item would be in mdat, not true");
            }
            setupItemLocationBox(sourcePrimaryItemExtent);
        }
        meta.addNestedBox(iloc);
        {
            ItemPropertiesBox sourceIprp = (ItemPropertiesBox) findChildBox(sourceMeta, "iprp");
            if (sourceIprp == null) {
                throw new IOException("failed to find source iprp box");
            }
            setupItemPropertiesBox(sourceIprp);
        }
        meta.addNestedBox(iprp);
        if (!skipContentDescribes) {
            meta.addNestedBox(iref);
        }
    }

    private void setupItemInfoBox(ItemInfoBox sourceIinf) throws IOException {
        ItemInfoEntry primaryItemItemInfoEntry = sourceIinf.findItemById(primaryItemId);
        if (primaryItemItemInfoEntry == null) {
            throw new IOException("did not discover primary item infe");
        }
        iinf.addItem(primaryItemItemInfoEntry);
    }

    private void setupItemLocationBox(ILocExtent sourcePrimaryItemExtent) {
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        iloc.addItem(makePrimaryItemILocItem(sourcePrimaryItemExtent));
    }

    private ILocItem makePrimaryItemILocItem(ILocExtent sourcePrimaryItemExtent) {
        ILocItem primaryItemLocation = new ILocItem();
        primaryItemLocation.setItemId(primaryItemId);
        ILocExtent primaryItemExtent = new ILocExtent();
        primaryItemExtent.setExtentIndex(0);
        primaryItemExtent.setExtentOffset(0);
        primaryItemExtent.setExtentLength(sourcePrimaryItemExtent.getExtentLength());
        primaryItemLocation.addExtent(primaryItemExtent);
        return primaryItemLocation;
    }

    private void setupItemPropertiesBox(ItemPropertiesBox sourceIprp) {
        iprp.setItemProperties(new ItemPropertyContainerBox());
        addPropertiesForPrimaryItem(sourceIprp);
    }

    private void buildTargetMediaDataBox() throws IOException {
        // TODO: we should actually find the real extent
        MediaDataBox sourceMdat = (MediaDataBox) getTopLevelBoxByFourCC(sourceBoxes, "mdat");
        if (sourceMdat == null) {
            throw new IOException("failed to find source mdat box");
        }
        // TODO: possibly use MediaDataBoxBuilder
        mdat.setData(sourceMdat.getData());
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    private static Box findChildBox(MetaBox parent, String fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
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

    private void addContentIdForPrimaryImage() {
        if (skipContentIDs) {
            return;
        }
        addPropertyFor(iprp, makeRandomContentId(), primaryItemId);
    }

    private void addCopyrightDescription() {
        if (copyright != null) {
            addPropertyFor(iprp, makeUserDescriptionCopyright(copyright), primaryItemId);
        }
    }

    private void addPropertiesForPrimaryItem(ItemPropertiesBox sourceIprp) {
        // TODO: this is making assumptions that are not always valid
        for (AbstractItemProperty property : sourceIprp.getItemProperties().getProperties()) {
            iprp.getItemProperties().addProperty(property);
        }
        for (ItemPropertyAssociation itemPropertyAssociation :
                sourceIprp.getItemPropertyAssociations()) {
            for (AssociationEntry associationEntry : itemPropertyAssociation.getEntries()) {
                if (associationEntry.getItemId() == primaryItemId) {
                    ItemPropertyAssociation ipma = new ItemPropertyAssociation();
                    AssociationEntry entry = new AssociationEntry();
                    entry.setItemId(primaryItemId);
                    ipma.addEntry(entry);
                    for (PropertyAssociation propertyAssociation :
                            associationEntry.getAssociations()) {
                        entry.addAssociation(propertyAssociation);
                    }
                    iprp.addItemPropertyAssociation(ipma);
                }
            }
        }
    }

    private long getNextItemId() {
        highestItemId += 1;
        return highestItemId;
    }

    private void addGeneralMetadata() {
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
        {
            if (!skipContentIDs) {
                addPropertyFor(iprp, makeRandomContentId(), generalMetadataItemId);
            }
        }
    }

    private byte[] getST0601MetadataBytes(boolean dump) {
        SortedMap<UasDatalinkTag, IUasDatalinkValue> map = new TreeMap<>();
        map.put(UasDatalinkTag.UasLdsVersionNumber, new org.jmisb.st0601.ST0601Version((short) 19));
        map.put(
                UasDatalinkTag.CornerLatPt1,
                new FullCornerLatitude(
                        transform.getLatitude(0, 0), FullCornerLatitude.CORNER_LAT_1));
        map.put(
                UasDatalinkTag.CornerLonPt1,
                new FullCornerLongitude(
                        transform.getLongitude(0, 0), FullCornerLongitude.CORNER_LON_1));
        map.put(
                UasDatalinkTag.CornerLatPt2,
                new FullCornerLatitude(
                        transform.getLatitude(imageWidth, 0), FullCornerLatitude.CORNER_LAT_2));
        map.put(
                UasDatalinkTag.CornerLonPt2,
                new FullCornerLongitude(
                        transform.getLongitude(imageWidth, 0), FullCornerLongitude.CORNER_LON_2));
        map.put(
                UasDatalinkTag.CornerLatPt3,
                new FullCornerLatitude(
                        transform.getLatitude(imageWidth, imageHeight),
                        FullCornerLatitude.CORNER_LAT_3));
        map.put(
                UasDatalinkTag.CornerLonPt3,
                new FullCornerLongitude(
                        transform.getLongitude(imageWidth, imageHeight),
                        FullCornerLongitude.CORNER_LON_3));
        map.put(
                UasDatalinkTag.CornerLatPt4,
                new FullCornerLatitude(
                        transform.getLatitude(0, imageHeight), FullCornerLatitude.CORNER_LAT_4));
        map.put(
                UasDatalinkTag.CornerLonPt4,
                new FullCornerLongitude(
                        transform.getLongitude(0, imageHeight), FullCornerLongitude.CORNER_LON_4));
        if (dateTime != null) {
            map.put(UasDatalinkTag.PrecisionTimeStamp, new PrecisionTimeStamp(dateTime));
        }
        if (missionId != null) {
            map.put(
                    UasDatalinkTag.MissionId,
                    new UasDatalinkString(UasDatalinkString.MISSION_ID, missionId));
        } else {
            map.put(
                    UasDatalinkTag.MissionId,
                    new UasDatalinkString(UasDatalinkString.MISSION_ID, sourcePath));
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

    private void addTimestampTAI() {
        if (dateTime == null) {
            // TODO: we could do an invalid timestamp?
            return;
        }
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
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
