package org.mobicents.ss7.linkset.oam;

public class FormatterHelp {
    
    public static final String SPACE = " ";
    public static final String EQUAL_SIGN = "=";
    public static final String NEW_LINE = "\n";

    public static void createPad(StringBuffer sb, int pad) {
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
    }

    public static String getLinkState(int state) {
        switch (state) {
        case LinkState.AVAILABLE:
            return "AVAILABLE";
        case LinkState.FAILED:
            return "FAILED";
        case LinkState.SHUTDOWN:
            return "SHUTDOWN";
        case LinkState.UNAVAILABLE:
            return "UNAVAILABLE";
        default:
            return "UNKNOWN";
        }
    }

    public static String getLinksetState(int state) {
        switch (state) {
        case LinksetState.AVAILABLE:
            return "AVAILABLE";
        case LinksetState.SHUTDOWN:
            return "SHUTDOWN";
        case LinksetState.UNAVAILABLE:
            return "UNAVAILABLE";
        default:
            return "UNKNOWN";
        }
    }

}
