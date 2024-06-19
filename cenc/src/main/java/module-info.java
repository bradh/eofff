/**
 * Implementation of ISO/IEC 23001-7 "Common encryption in ISO base media file format files".
 *
 * <p>This implementation mostly targets the fourth edition (ISO/IEC 23001-7:2023).
 */
module net.frogmouth.rnd.eofff.cenc {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.cenc.pssh.ProtectionSystemSpecificHeaderBoxParser,
            net.frogmouth.rnd.eofff.cenc.tenc.TrackEncryptionBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.cenc.ienc.ItemEncryptionBoxParser,
            net.frogmouth.rnd.eofff.cenc.iaux.ItemAuxiliaryInformationBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;

    provides net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory with
            net.frogmouth.rnd.eofff.cenc.auxr.AuxiliaryReferenceFactory;

    exports net.frogmouth.rnd.eofff.cenc.auxr;
    exports net.frogmouth.rnd.eofff.cenc.iaux;
    exports net.frogmouth.rnd.eofff.cenc.ienc;
    exports net.frogmouth.rnd.eofff.cenc.pssh;
    exports net.frogmouth.rnd.eofff.cenc.tenc;
}
