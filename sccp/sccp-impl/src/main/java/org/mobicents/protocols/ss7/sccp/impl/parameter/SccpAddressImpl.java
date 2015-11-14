/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 *
 */
public class SccpAddressImpl extends AbstractParameter implements XMLSerializable, SccpAddress {

    private static final byte ROUTE_ON_PC_FLAG = 0x40;
    private static final short REMOVE_PC_FLAG = 0xFE;
    private static final short REMOVE_PC_FLAG_ANSI = 0xFD;
    private static final byte PC_PRESENT_FLAG = 0x01;
    private static final byte PC_PRESENT_FLAG_ANSI = 0x02;

    private static final String GLOBAL_TITLE = "gt";
    private static final String POINT_CODE = "pc";
    private static final String SUBSYSTEM_NUMBER = "ssn";
    private static final String AI = "ai";

    private GlobalTitle gt;
    private int pc = 0;
    private int ssn = -1;

    private AddressIndicator ai;

    // If this SccpAddress is translated address
    private boolean translated;

    public SccpAddressImpl() {
    }

    public SccpAddressImpl(final RoutingIndicator ri, final GlobalTitle gt, final int dpc, final int ssn) {
        this.gt = gt;
        this.pc = dpc;
        this.ssn = ssn;
        this.ai = new AddressIndicator(dpc > 0, ssn > 0, ri, gt == null ? GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED
                : gt.getGlobalTitleIndicator());
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public AddressIndicator getAddressIndicator() {
        return this.ai;
    }

    public int getSignalingPointCode() {
        return pc;
    }

    public int getSubsystemNumber() {
        return ssn;
    }

    public GlobalTitle getGlobalTitle() {
        return gt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SccpAddressImpl other = (SccpAddressImpl) obj;
        //NOTE: AI is rewritten during routing, this is a hack :/
//        if (ai == null) {
//            if (other.ai != null)
//                return false;
//        } else if (!ai.equals(other.ai))
//            return false;
        if (gt == null) {
            if (other.gt != null)
                return false;
        } else if (!gt.equals(other.gt))
            return false;
        if (pc != other.pc)
            return false;
        if (ssn != other.ssn)
            return false;
        //if (translated != other.translated)
        //    return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ai == null) ? 0 : ai.hashCode());
        result = prime * result + ((gt == null) ? 0 : gt.hashCode());
        result = prime * result + pc;
        result = prime * result + ssn;
        //result = prime * result + (translated ? 1231 : 1237);
        return result;
    }

    public String toString() {
        return ((new StringBuffer()).append("pc=").append(pc).append(",ssn=").append(ssn).append(",AI=").append(ai.getValue(SccpProtocolVersion.ITU))
                .append(",gt=").append(gt)).toString();
    }

    protected static final XMLFormat<SccpAddress> XML = new XMLFormat<SccpAddress>(SccpAddress.class) {

        public void write(SccpAddress ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(POINT_CODE, ai.getSignalingPointCode());
            xml.setAttribute(SUBSYSTEM_NUMBER, ai.getSubsystemNumber());
            xml.add(ai.getAddressIndicator(), AI, AddressIndicator.class);
            xml.add(ai.getGlobalTitle(), GLOBAL_TITLE);

        }

        public void read(InputElement xml, SccpAddress ai) throws XMLStreamException {
            SccpAddressImpl impl = (SccpAddressImpl) ai;
            impl.pc = xml.getAttribute(POINT_CODE).toInt();
            impl.ssn = xml.getAttribute(SUBSYSTEM_NUMBER).toInt();
            impl.ai = xml.get(AI, AddressIndicator.class);
            impl.gt = xml.get(GLOBAL_TITLE);
        }
    };

    @Override
    public void decode(final InputStream bin, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            int b = bin.read() & 0xff;
            this.ai = new AddressIndicator((byte) b, sccpProtocolVersion);

            if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
                if (this.ai.isSSNPresent()) {
                    this.ssn = bin.read() & 0xff;
                }

                if (this.ai.isPCPresent()) {
                    int b1 = bin.read() & 0xff;
                    int b2 = bin.read() & 0xff;
                    int b3 = bin.read() & 0xff;

                    this.pc = (b3 << 16) | (b2 << 8) | b1;
                }
            } else {
                if (this.ai.isPCPresent()) {
                    int b1 = bin.read() & 0xff;
                    int b2 = bin.read() & 0xff;

                    this.pc = ((b2 & 0x3f) << 8) | b1;
                }

                if (this.ai.isSSNPresent()) {
                    this.ssn = bin.read() & 0xff;
                }
            }

            if(this.ai.getGlobalTitleIndicator()!=GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED){
                this.gt = factory.createGlobalTitle(this.ai.getGlobalTitleIndicator());
                ((AbstractGlobalTitle) this.gt).decode(bin, factory, sccpProtocolVersion);
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public void encode(final OutputStream os, final boolean removeSpc, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            byte aiValue = ai.getValue(sccpProtocolVersion);

            if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
                if (removeSpc && ((aiValue & ROUTE_ON_PC_FLAG) == 0x00)) {
                    // Routing on GT so lets remove PC flag

                    aiValue = (byte) (aiValue & REMOVE_PC_FLAG_ANSI);
                }

                os.write(aiValue);

                if (ai.isSSNPresent()) {
                    os.write((byte) this.ssn);
                }

                if ((aiValue & PC_PRESENT_FLAG_ANSI) == PC_PRESENT_FLAG_ANSI) {
                    // If Point Code included in SCCP Address
                    byte b1 = (byte) this.pc;
                    byte b2 = (byte) (this.pc >> 8);
                    byte b3 = (byte) (this.pc >> 16);

                    os.write(b1);
                    os.write(b2);
                    os.write(b3);
                }
            } else {
                if (removeSpc && ((aiValue & ROUTE_ON_PC_FLAG) == 0x00)) {
                    // Routing on GT so lets remove PC flag

                    aiValue = (byte) (aiValue & REMOVE_PC_FLAG);
                }

                os.write(aiValue);

                if ((aiValue & PC_PRESENT_FLAG) == PC_PRESENT_FLAG) {
                    // If Point Code included in SCCP Address
                    byte b1 = (byte) this.pc;
                    byte b2 = (byte) ((this.pc >> 8) & 0x3f);

                    os.write(b1);
                    os.write(b2);
                }

                if (ai.isSSNPresent()) {
                    os.write((byte) this.ssn);
                }
            }

            if (ai.getGlobalTitleIndicator() != GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED) {
                ((AbstractGlobalTitle) this.gt).encode(os, removeSpc, sccpProtocolVersion);
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    public void decode(final byte[] b, final ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        ByteArrayInputStream bin = new ByteArrayInputStream(b);
        this.decode(bin, factory, sccpProtocolVersion);
    }

    @Override
    public byte[] encode(boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos, removeSPC, sccpProtocolVersion);
        return baos.toByteArray();
    }
}
