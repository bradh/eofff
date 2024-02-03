@SuppressWarnings("module") // That is not a software version number - its the name of the standard
module net.frogmouth.rnd.eofff.ts26_244 {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;
    requires com.google.auto.service;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.ts26_244.auth.AuthorBoxParser;

    exports net.frogmouth.rnd.eofff.ts26_244.auth;
}
