module net.frogmouth.rnd.eofff.uncompressed {
    requires net.frogmouth.rnd.eofff.imagefileformat;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;
    // TODO: this is only for a test, so remove it
    requires java.desktop;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.uncompressed.cloc.ChromaLocationBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.depi.DepthInfoBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoBoxParser,
            net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBoxParser;

    exports net.frogmouth.rnd.eofff.uncompressed.cloc;
    exports net.frogmouth.rnd.eofff.uncompressed.cpal;
    exports net.frogmouth.rnd.eofff.uncompressed.cmpd;
    exports net.frogmouth.rnd.eofff.uncompressed.depi;
    exports net.frogmouth.rnd.eofff.uncompressed.itai;
    exports net.frogmouth.rnd.eofff.uncompressed.sbpm;
    exports net.frogmouth.rnd.eofff.uncompressed.taic;
    exports net.frogmouth.rnd.eofff.uncompressed.uncc;
}
