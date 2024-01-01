/** Implementation of ISO/IEC 23008-12 "Image File Format". */
module net.frogmouth.rnd.eofff.imagefileformat {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlternativesParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlbumCollectionParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.GroupsListBoxParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBoxParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.properties
                    .ItemPropertyContainerBoxParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.properties
                    .ItemPropertyAssociationParser;
    provides net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser with
            net.frogmouth.rnd.eofff.imagefileformat.properties.hevc
                    .HEVCConfigurationItemPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.AuxiliaryTypePropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.CleanApertureParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageMirrorParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageRotationParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image
                    .ImageSpatialExtentsPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.colr.ColourInformationPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.PixelAspectRatioPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.PixelInformationPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.mski.MaskConfigurationPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionPropertyParser;

    exports net.frogmouth.rnd.eofff.imagefileformat.brands;
    exports net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;
    exports net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;
    exports net.frogmouth.rnd.eofff.imagefileformat.items.grid;
    exports net.frogmouth.rnd.eofff.imagefileformat.items.rgan;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.colr;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.image;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.mski;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.udes;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.uuid;
}
