package net.frogmouth.rnd.eofff.debugtools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BoxDumper {

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            Path path = new File(arg).toPath();
            dumpRecursive(path);
        }
    }

    protected static void dumpRecursive(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            dumpRegularFile(path);
        } else {
            Files.list(path)
                    .forEach(
                            p -> {
                                try {
                                    if (Files.isDirectory(p)) {
                                        dumpRecursive(p);
                                    } else if (Files.isRegularFile(p)) {
                                        dumpRegularFile(p);
                                    }
                                } catch (IOException ex) {
                                    System.out.println(
                                            "Failed to dump "
                                                    + p.toAbsolutePath()
                                                    + " - "
                                                    + ex.toString());
                                }
                            });
        }
    }

    protected static void dumpRegularFile(Path p) throws IOException {
        if ((p.toString().endsWith("jp2") || p.toString().endsWith("jph"))) {
            try {
                System.out.println("dumping " + p.toString());
                BoxParser parser = new BoxParser(p);
                parser.dumpBoxes();
                System.out.println("--------------------------");
            } catch (UnsupportedOperationException ex) {
                System.out.println(ex.toString());
            }
        } else {
            // System.out.println("[skipping - unsupported suffix]");
        }
    }
}
