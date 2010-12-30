package org.mobicents.ss7.hardware.dialogic.oam;

import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetFactory;

/**
 * <p>
 * Factory for creating <tt>dialogic</tt> based {@link Linkset}
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class DialogicLinksetFactory extends LinksetFactory {

    private static final String NAME = "dialogic";

    public DialogicLinksetFactory() {
        super();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Linkset createLinkset(String[] options) throws Exception {
        // the expected command is "linkset create dialogic opc 1 apc 2 ni 3
        // srcmod 61 dstmod 34 linkset1". We know length is 14
        if (options.length != 14) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        String option = options[3];

        // If first option is not OPC
        if (option.compareTo("opc") != 0) {
            return null;
        }

        int opc = Integer.parseInt(options[4]);

        if (options[5].compareTo("apc") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int dpc = Integer.parseInt(options[6]);

        if (options[7].compareTo("ni") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int ni = Integer.parseInt(options[8]);

        if (options[9].compareTo("srcmod") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int srcmod = Integer.parseInt(options[10]);

        if (options[11].compareTo("dstmod") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int dstmod = Integer.parseInt(options[12]);

        String name = options[13];

        if (name == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        return new DialogicLinkset(name, opc, dpc, ni, srcmod, dstmod);
    }

}
