package net.frogmouth.rnd.ngiis.cine;

import java.io.IOException;

/**
 * @author bradh
 */
public class Cine {

    public static void main(String[] args) throws IOException {
        String filename =
                "/home/bradh/testbed20/uncompressed_joe/Launch_Fixed_High_Contrast_cbg591_egfb23_01.cine";
        // "/home/bradh/testbed20/uncompressed_joe/Impact_KTM_cbg839_egfb23_01.cine";
        System.out.println("Hello: " + filename);
        CineParser parser = new CineParser();
        ParseResult parseResult = parser.parse(filename);
        CineImage image = parseResult.getImage(1);
    }
}
