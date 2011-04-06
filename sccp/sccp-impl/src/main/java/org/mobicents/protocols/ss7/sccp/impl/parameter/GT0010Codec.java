/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GT0010Codec extends GTCodec {
    
    private GT0010 gt;
    
    public GT0010Codec() {
    }
    
    public GT0010Codec(GT0010 gt) {
        this.gt = gt;
    }
    
    
    public GlobalTitle decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;        
        int tt = b;        
        return new GT0010(tt, "");
    }

    
    public void encode(OutputStream in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
