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

/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author Oleg Kulikov
 */
public class SccpAddressCodec {

    private static final byte ROUTE_ON_PC_FLAG = 0x40;

    private static final short REMOVE_PC_FLAG = 0xFE;

    private static final byte PC_PRESENT_FLAG = 0x01;

    // private GTCodec gtCodec = new GTCodec();
    // private boolean removeSpc = false;

    // /** Creates a new instance of UnitDataMandatoryVariablePart */
    // public SccpAddressCodec(boolean removeSpc) {
    // this.removeSpc = removeSpc;
    // }

    public static SccpAddress decode(byte[] buffer) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(buffer);

        int b = bin.read() & 0xff;
        AddressIndicator addressIndicator = new AddressIndicator((byte) b);

        int pc = 0;
        if (addressIndicator.pcPresent()) {
            int b1 = bin.read() & 0xff;
            int b2 = bin.read() & 0xff;

            pc = ((b2 & 0x3f) << 8) | b1;
        }

        int ssn = 0;
        if (addressIndicator.ssnPresent()) {
            ssn = bin.read() & 0xff;
        }

        GlobalTitle globalTitle = GTCodec.decode(addressIndicator.getGlobalTitleIndicator(), bin);
        return new SccpAddress(addressIndicator.getRoutingIndicator(), pc, globalTitle, ssn);
    }

    public static byte[] encode(SccpAddress address, boolean removeSpc) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        AddressIndicator ai = address.getAddressIndicator();
        byte aiValue = ai.getValue();

        if (removeSpc && ((aiValue & ROUTE_ON_PC_FLAG) == 0x00)) {
            // Routing on GT so lets remove PC flag

            aiValue = (byte) (aiValue & REMOVE_PC_FLAG);
        }

        out.write(aiValue);

        if ((aiValue & PC_PRESENT_FLAG) == PC_PRESENT_FLAG) {
            // If Point Code included in SCCP Address
            byte b1 = (byte) address.getSignalingPointCode();
            byte b2 = (byte) ((address.getSignalingPointCode() >> 8) & 0x3f);

            out.write(b1);
            out.write(b2);
        }

        if (ai.ssnPresent()) {
            out.write((byte) address.getSubsystemNumber());
        }

        if (ai.getGlobalTitleIndicator() != GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED) {
            GTCodec.encode(address.getGlobalTitle(), out);
        }
        return out.toByteArray();

    }

}
