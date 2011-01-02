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

import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;

/**
 *
 * @author kulikov
 */
public class ParameterFactoryImpl implements ParameterFactory {

    public ProtocolClass createProtocolClass(int value, int handling) {
        return new ProtocolClassImpl(value, handling);
    }

    public Importance createImportance(int value) {
        return new ImportanceImpl((byte)value);
    }

    public Segmentation createSegmentation() {
        return new SegmentationImpl();
    }

    public HopCounter createHopCounter(int hopCount)
    {
    	return new HopCounterImpl(hopCount);
    }
    
}
