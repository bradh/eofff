module net.frogmouth.rnd.eofff.quicktime {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.quicktime.XYZPositionParser,
            net.frogmouth.rnd.eofff.quicktime.BaseMediaInfoAtomParser,
            net.frogmouth.rnd.eofff.quicktime.BaseMediaInformationHeaderParser,
            net.frogmouth.rnd.eofff.quicktime.TimeCodeContainerBoxParser,
            net.frogmouth.rnd.eofff.quicktime.TimeCodeMediaInformationAtomParser;

    uses net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser;

    provides net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser with
            net.frogmouth.rnd.eofff.quicktime.DataEntryAliasBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

    provides net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser with
            net.frogmouth.rnd.eofff.quicktime.TimecodeSampleDescriptionParser;

    exports net.frogmouth.rnd.eofff.quicktime;
}
