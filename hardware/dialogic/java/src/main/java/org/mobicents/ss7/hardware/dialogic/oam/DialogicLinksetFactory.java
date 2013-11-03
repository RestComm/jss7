/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
    private static final String LINKSET_NAME = "dialogiclinkset";

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

    /**
     * Get linkset name
     *
     * @return
     */
    public String getLinksetName() {
        return LINKSET_NAME;
    }

    /**
     * Get linkset class
     *
     * @return
     */
    public Class getLinksetClass() {
        return DialogicLinkset.class;
    }

    /**
     * Get link name
     *
     * @return
     */
    public String getLinkName() {
        return null;
    }

    /**
     * Get link class
     *
     * @return
     */
    public Class getLinkClass() {
        return null;
    }
}
