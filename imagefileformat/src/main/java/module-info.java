/** Implementation of ISO/IEC 23008-12 "Image File Format". */
module net.frogmouth.rnd.eofff.imagefileformat {
    requires com.google.auto.service;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.imagefileformat.ccst.CodingConstraintsBoxParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlternativesParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlbumCollectionParser,
            net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.GroupsListBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;

    provides net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory with
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.AuxiliaryImageFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.BaseImageFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.ContentDescribesFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.DerivedImageFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.DependentCodingFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.RegionReferenceFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.EVCSliceReferenceFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.ScalableImageFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.MaskReferenceFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.DataIntegrityReferenceFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.PredictivelyCodedFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.TileBasisFactory,
            net.frogmouth.rnd.eofff.imagefileformat.itemreferences.ThumbnailFactory;

    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
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

    uses net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser;

    exports net.frogmouth.rnd.eofff.imagefileformat.brands;
    exports net.frogmouth.rnd.eofff.imagefileformat.ccst;
    exports net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;
    exports net.frogmouth.rnd.eofff.imagefileformat.items.grid;
    exports net.frogmouth.rnd.eofff.imagefileformat.items.rgan;
    exports net.frogmouth.rnd.eofff.imagefileformat.itemreferences;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.colr;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.image;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.mski;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.udes;
    exports net.frogmouth.rnd.eofff.imagefileformat.properties.uuid;
}
