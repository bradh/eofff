package net.frogmouth.rnd.eofff.uncompressed_experiments;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;

public class GIMIUtils {

    public static final String HEIF_SUFFIX = "heif";
    // public static final String ST0601_URI = "urn:smpte:ul:060E2B34.020B0101.0E010301.01000000";
    public static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");
    public static final int NANOS_PER_SECOND = 1000 * 1000 * 1000;
    public static final int MICROSECONDS_PER_SECOND = 1000 * 1000;

    // public static final String FAKE_SECURITY_MIME_TYPE = "application/x.fake-dni-arh+xml";

    public static UUIDProperty makeRandomContentId() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        final String contentId = "urn:uuid:" + UUID.randomUUID();
        contentIdProperty.setPayload(contentId.getBytes(StandardCharsets.UTF_8));
        // System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    public static AbstractItemProperty makeUserDescriptionCopyright(String description) {
        return makeUserDescription("Copyright Statement", description, "copyright,usage");
    }

    public static AbstractItemProperty makeUserDescription(
            String name, String description, String tags) {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName(name);
        udes.setDescription(description);
        udes.setTags(tags);
        return udes;
    }

    public static HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    public static PrimaryItemBox makePrimaryItemBox(long primaryItemId) {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(primaryItemId);
        return pitm;
    }

    public static void addPropertyFor(
            ItemPropertiesBox iprp, AbstractItemProperty property, long targetItem) {
        int index = iprp.getItemProperties().addProperty(property);
        AssociationEntry entry = new AssociationEntry();
        entry.setItemId(targetItem);
        PropertyAssociation association = new PropertyAssociation();
        association.setEssential(false);
        association.setPropertyIndex(index);
        entry.addAssociation(association);
        iprp.addAssociationEntry(entry);
    }
}
