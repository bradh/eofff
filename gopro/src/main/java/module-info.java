module net.frogmouth.rnd.eofff.gopro {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.gopro.BCIDParser,
            net.frogmouth.rnd.eofff.gopro.CAMEParser,
            net.frogmouth.rnd.eofff.gopro.FIRMParser,
            net.frogmouth.rnd.eofff.gopro.GUMIParser,
            net.frogmouth.rnd.eofff.gopro.HMMTParser,
            net.frogmouth.rnd.eofff.gopro.LENSParser,
            net.frogmouth.rnd.eofff.gopro.MUIDParser,
            net.frogmouth.rnd.eofff.gopro.SETTParser,
            net.frogmouth.rnd.eofff.gopro.XYZPositionParser,
            net.frogmouth.rnd.eofff.gopro.gpmf.GPMFParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.BaseMediaInfoAtomParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.BaseMediaInformationHeaderParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.GoProMetaDataBoxParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.TimeCodeContainerBoxParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.TimeCodeMediaInformationAtomParser;

    uses net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser;

    provides net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser with
            net.frogmouth.rnd.eofff.gopro.quicktime.DataEntryAliasBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

    provides net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser with
            net.frogmouth.rnd.eofff.gopro.quicktime.GoProMetadataSampleEntryParser,
            net.frogmouth.rnd.eofff.gopro.quicktime.TimecodeSampleDescriptionParser;

    exports net.frogmouth.rnd.eofff.gopro;
    exports net.frogmouth.rnd.eofff.gopro.gpmf;
    exports net.frogmouth.rnd.eofff.gopro.quicktime;
}
