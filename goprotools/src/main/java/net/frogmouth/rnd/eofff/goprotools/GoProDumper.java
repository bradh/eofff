package net.frogmouth.rnd.eofff.goprotools;

import java.io.IOException;

/**
 * @author bradh
 */
public class GoProDumper {

    public static void main(String[] args) throws IOException {
        GoProParser parser = new GoProParser(args[0]);
        parser.dumpBoxes();
        parser.findMetadataTrack();
    }
}
