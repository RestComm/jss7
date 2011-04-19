/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class SccpAddressCodec {

    private SccpAddress address;
    private GTCodec gtCodec = new GTCodec();
    
    /** Creates a new instance of UnitDataMandatoryVariablePart */
    public SccpAddressCodec() {
    }

    public SccpAddressCodec(SccpAddress address) {
        this.address = address;
    }
    
    public SccpAddress decode(byte[] buffer) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(buffer);
        
        int b = bin.read() & 0xff;
        AddressIndicator addressIndicator = new AddressIndicator((byte)b);
        
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
        
        if (addressIndicator.getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            return new SccpAddress(pc, ssn);
        }
        
        GlobalTitle globalTitle = gtCodec.decode(addressIndicator.getGlobalTitleIndicator(), bin);
        return new SccpAddress(globalTitle, ssn) ;
    }

    public byte[] encode(SccpAddress address) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        AddressIndicator ai = address.getAddressIndicator();
        out.write(ai.getValue());
        
        if (ai.pcPresent()) {
            byte b1 = (byte) address.getSignalingPointCode();
            byte b2 = (byte) ((address.getSignalingPointCode() >> 8) & 0x3f);

            out.write(b1);
            out.write(b2);
        }
        
        if (ai.ssnPresent()) {
            out.write((byte) address.getSubsystemNumber());
        }
        
        if (ai.getGlobalTitleIndicator() != GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED) {
            gtCodec.encode(address.getGlobalTitle(), out);
        }
        return out.toByteArray();
        
    }

}
