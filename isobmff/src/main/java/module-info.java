module net.frogmouth.rnd.eofff.isobmff {
    requires jdk.incubator.foreign;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemPropertiesBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemPropertyContainerBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation.ItemPropertyAssociationParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ILocBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.PitmBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.meta.property.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.meta.property.PropertyParser with
            net.frogmouth.rnd.eofff.isobmff.meta.property.HEVCConfigurationItemPropertyParser,
            net.frogmouth.rnd.eofff.isobmff.meta.property.ImageSpatialExtentsPropertyParser;
}
