/** Implementation of ISO/IEC 23008-12 "Image File Format". */
module net.frogmouth.rnd.eofff.imagefileformat {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlternativesParser,
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
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageMirrorParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageRotationParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image
                    .ImageSpatialExtentsPropertyParser,
            net.frogmouth.rnd.eofff.imagefileformat.properties.image.PixelInformationPropertyParser;
}
