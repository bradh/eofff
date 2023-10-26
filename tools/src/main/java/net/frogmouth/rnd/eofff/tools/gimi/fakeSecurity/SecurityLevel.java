package net.frogmouth.rnd.eofff.tools.gimi.fakeSecurity;

public enum SecurityLevel {
    Unrestricted("UNRESTRICTED"),
    Secretiveish("SECRETIVE-ISH"),
    TotallySecret("TOTALLY SECRET");

    private final String displayName;

    private SecurityLevel(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
