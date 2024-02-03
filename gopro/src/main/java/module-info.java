module net.frogmouth.rnd.eofff.gopro {
    requires net.frogmouth.rnd.eofff.isobmff;
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
            net.frogmouth.rnd.eofff.gopro.quicktime.BaseMediaInformationHeaderParser;

    exports net.frogmouth.rnd.eofff.gopro;
    exports net.frogmouth.rnd.eofff.gopro.gpmf;
    exports net.frogmouth.rnd.eofff.gopro.quicktime;
}
