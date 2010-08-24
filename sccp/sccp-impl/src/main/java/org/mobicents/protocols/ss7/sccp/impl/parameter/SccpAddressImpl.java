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
public class SccpAddressImpl implements SccpAddress {

    private AddressIndicator addressIndicator;
    private int pointCode;
    private int ssn;
    private GlobalTitle globalTitle;

    /** Creates a new instance of UnitDataMandatoryVariablePart */
    public SccpAddressImpl() {
    }

    protected SccpAddressImpl(int pointCode, int ssn) {
        this.pointCode = pointCode;
        this.globalTitle = null;
        this.ssn = ssn;

        addressIndicator = new AddressIndicator(
                pointCode != 0, 
                ssn != 0, 
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 
                GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
    }

    protected SccpAddressImpl(GlobalTitle gt, int ssn) {
        this.globalTitle = gt;
        this.ssn = ssn;

        addressIndicator = new AddressIndicator(
                false, 
                ssn != 0, 
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 
                gt.getIndicator());
    }
    
    public void decode(byte[] buffer) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(buffer);
        
        int b = bin.read() & 0xff;
        addressIndicator = new AddressIndicator((byte)b);
        
        if (addressIndicator.pcPresent()) {
            int b1 = bin.read() & 0xff;
            int b2 = bin.read() & 0xff;
            
            pointCode = ((b2 & 0x3f) << 8) | b1;
        }
        
        if (addressIndicator.ssnPresent()) {
            ssn = bin.read() & 0xff;
        }
        
        
        switch (addressIndicator.getGlobalTitleIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY :
                globalTitle = new GT0001();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY :
                globalTitle = new GT0010();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME :
                globalTitle = new GT0011();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS :
                globalTitle = new GT0100();
                break;
        }
        
        if (globalTitle != null) {
            ((GlobalTitleImpl)globalTitle).decode(bin);
        }
/*        DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(buffer));

        int i = in.readUnsignedByte();

        pointCodeIndicator = i & 0x01;
        ssnIndicator = (i & 0x02) >> 1;
        globalTitleIndicator = (i & 0x3c) >> 2;
        routingIndicator = (i & 0x40) >> 6;

        if (pointCodeIndicator == 1) {
            int b1 = in.readUnsignedByte();
            int b2 = in.readUnsignedByte();

            pointCode = ((b2 & 0x3f) << 8) | b1;
        }

        if (ssnIndicator == 1) {
            ssn = in.readUnsignedByte();
        }

        switch (globalTitleIndicator) {
            case 4:
                globalTitle = new GT0100Impl();
                break;
            default:
                throw new IOException("Uknown GT: " + globalTitleIndicator);
        }
        if (globalTitle != null) {
            globalTitle.decode(in);
        } else {
            //FIXME: read it anyway?
        }
 */ 
    }

    public byte[] encode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(addressIndicator.getValue());
        
        if (addressIndicator.pcPresent()) {
            byte b1 = (byte) pointCode;
            byte b2 = (byte) ((pointCode >> 8) & 0x3f);

            out.write(b1);
            out.write(b2);
        }
        
        if (addressIndicator.ssnPresent()) {
            out.write((byte) ssn);
        }
        
        if (addressIndicator.getGlobalTitleIndicator() != GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED) {
            ((GlobalTitleImpl)globalTitle).encode(out);
        }
        return out.toByteArray();
        
/*        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte b = (byte) (pointCodeIndicator | ssnIndicator << 1 |
                globalTitleIndicator << 2 | routingIndicator << 6);
        out.write(b);

        if (pointCodeIndicator == 1) {
            byte b1 = (byte) pointCode;
            byte b2 = (byte) ((pointCode >> 8) & 0x3f);

            out.write(b1);
            out.write(b2);
        }

        if (ssnIndicator == 1) {
            out.write((byte) ssn);
        }

        globalTitle.encode(out);
        return out.toByteArray();
  
 */ 
    }

    public int getSignalingPointCode() {
        return pointCode;
    }

    public int getSubsystemNumber() {
        return ssn;
    }

    public GlobalTitle getGlobalTitle() {
        return globalTitle;
    }

}
