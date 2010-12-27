package org.mobicents.ss7.hardware.dahdi.oam;

import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetFactory;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DahdiLinksetFactory extends LinksetFactory {

    private static final String NAME = "dahdi";

    public DahdiLinksetFactory() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Linkset createLinkset(String[] options) {

        // opc 1 dpc 2 ni 3 linkset1
        if (options.length < 7) {
            return null;
        }

        String option = null;
        int i = 0;
        while (i < 7 && ((option = options[i]) == null)) {
            i++;
        }

        // If offset + command length is not equal to options length
        if (i + 7 != options.length) {
            return null;
        }

        // If first option is not OPC
        if (option.compareTo("opc") != 0) {
            return null;
        }

        i++;
        int opc;
        try {
            opc = Integer.parseInt(options[i]);
        } catch (NumberFormatException e) {
            return null;
        }

        i++;

        if (options[i].compareTo("dpc") != 0) {
            return null;
        }

        i++;

        int dpc;
        try {
            dpc = Integer.parseInt(options[i]);
        } catch (NumberFormatException e) {
            return null;
        }

        i++;

        if (options[i].compareTo("ni") != 0) {
            return null;
        }

        i++;

        int ni;
        try {
            ni = Integer.parseInt(options[i]);
        } catch (NumberFormatException e) {
            return null;
        }

        i++;

        String name = options[i];

        return new DahdiLinkset(name, opc, dpc, ni);
    }

}
