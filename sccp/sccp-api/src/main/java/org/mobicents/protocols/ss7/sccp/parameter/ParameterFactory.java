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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.Serializable;

/**
 *
 * @author kulikov
 */
public interface ParameterFactory extends Serializable  {
    /** 
     * Creates protocol class parameter.
     * 
     * @param value the value of the parameter
     * @return parameter
     */
    public ProtocolClass createProtocolClass(int value, int handling);
    
    /**
     * Create Importance parameter.
     * 
     * @param value the value of this parameter
     * @return parameter
     */
    public Importance createImportance(int value);
    
    /**
     * Create segemntation parameter
     * 
     * @return parameter.
     */
    public Segmentation createSegmentation();
}
