package net.frogmouth.rnd.eofff.goprotools;

import java.io.IOException;

/**
 * @author bradh
 */
public class GoProDumper {

    public static void main(String[] args) throws IOException {
        GoProParser parser = new GoProParser(args[0]);
        parser.dumpBoxes();
        // parser.dumpH265Files();
        // parser.dumpGPMF();
        // parser.dumpTimingTrack();
        // parser.findMetadataTrack();
        parser.writeOutFile("dump_gopro.mp4");
    }
}
