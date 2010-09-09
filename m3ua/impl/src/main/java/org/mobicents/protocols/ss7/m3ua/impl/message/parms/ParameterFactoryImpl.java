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

package org.mobicents.protocols.ss7.m3ua.impl.message.parms;

import org.mobicents.protocols.ss7.m3ua.message.parm.*;
import org.mobicents.protocols.ss7.m3ua.impl.message.ParameterImpl;

/**
 *
 * @author kulikov
 */
public class ParameterFactoryImpl implements ParameterFactory {
    public ProtocolDataImpl createProtocolData(int opc, int dpc, int si, int ni, int mp, int sls, byte[] data) {
        return new ProtocolDataImpl(opc, dpc, si, ni, mp, sls, data);
    }
    
    public ParameterImpl createParameter(int tag, byte[] value) {
        ParameterImpl p = null;
        switch (tag) {
            case ParameterImpl.Protocol_Data : 
                p = new ProtocolDataImpl(value);
                break;
            default :
                p = new UnknownParameterImpl(tag, value.length, value);
                break;
        }
        return p;
    }
    
}
