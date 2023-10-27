package net.frogmouth.rnd.eofff.tools.gimi;

public enum Codec {
    HEVC(new String[] {}, "hevc"),
    HEIC(new String[] {"--encoder=x265"}, "heic"),
    JPEG(new String[] {"--jpeg"}, "hejp"),
    JPEG2000(
            new String[] {"--jpeg2000", "-L", "-p", "chroma=444", "--matrix_coefficients=0"},
            "hej2"),
    UNCOMPRESSED(new String[] {"--uncompressed"}, "heif"),
    AVIF(new String[] {"--avif"}, "avif");

    private String[] arguments;
    private String suffix;

    private Codec(String[] arguments, String suffix) {
        this.arguments = arguments;
        this.suffix = suffix;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getSuffix() {
        return suffix;
    }
}
