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

package org.mobicents.protocols.ss7.sccp.impl.ud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.UnitData;

/**
 *
 * @author Oleg Kulikov
 */
public class UnitDataImpl extends UDBaseImpl implements UnitData {
    
   
	//private SccpAddressImpl calledParty;
    //private SccpAddressImpl callingParty;
    //private ProtocolClassImpl pClass;
    
    /** Creates a new instance of UnitData */
    public UnitDataImpl() {
    }

    public UnitDataImpl(ProtocolClass pClass, SccpAddress calledParty, 
            SccpAddress callingParty, byte[] data) {
    	super.pClass = pClass;
        super.calledParty = (SccpAddressImpl)calledParty;
        super.callingParty = (SccpAddressImpl)callingParty;
        super.data = data;
    }

    public void encode(OutputStream out) throws IOException {
        out.write(0x09);
        
        pClass.encode(out);
        
        byte[] cdp = calledParty.encode();
        byte[] cnp = callingParty.encode();
        
        int len = 3;
        out.write(len);
        
        len = (cdp.length + 3);
        out.write(len);
        
        len += (cnp.length);
        out.write(len);
        
        out.write((byte)cdp.length);
        out.write(cdp);

        out.write((byte)cnp.length);
        out.write(cnp);
        
        out.write((byte)data.length);
        out.write(data);
        
    }

    public void decode(InputStream in) throws IOException {
        pClass = new ProtocolClassImpl();
        pClass.decode(in);
        
        int cpaPointer = in.read() & 0xff;
        in.mark(in.available());
        
        in.skip(cpaPointer - 1);
        int len = in.read() & 0xff;
        
        byte[] buffer = new byte[len];
        in.read(buffer);
        
        calledParty = new SccpAddressImpl();
        calledParty.decode(buffer);
        
        in.reset();
        cpaPointer = in.read() & 0xff;
        in.mark(in.available());
        
        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;
                
        buffer = new byte[len];
        in.read(buffer);
        
        callingParty = new SccpAddressImpl();
        callingParty.decode(buffer);
        
        in.reset();
        cpaPointer = in.read() & 0xff;
        
        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;
        
        data = new byte[len];
        in.read(data);
        
    }
    
}
