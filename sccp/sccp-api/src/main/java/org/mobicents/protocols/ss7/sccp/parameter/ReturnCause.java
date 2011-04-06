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

/**
 * Return cause values for XUDTS for instance.
 * 
 * @author baranowb
 */
public interface ReturnCause extends Parameter{
	
	public final static int PARAMETER_CODE = 0xB;
	
	
	public final static int NO_TRANSLATION_FOR_NATURE = 0x0;
	public final static int NO_TRANSLATION_FOR_ADDRESS = 0x1;
	public final static int SUBSYSTEM_CONGESTION = 0x2;
	public final static int SUBSYSTEM_FAILURE = 0x3;
	public final static int UNEQUIPED_USER = 0x4;
	public final static int MTP_FAILURE = 0x5;
	public final static int NETWORK_CONGESTION = 0x6;
	public final static int UNQALIFIED = 0x7;
	public final static int ERR_IN_MSG_TRANSPORT = 0x8;
	public final static int ERR_IN_LOCAL_PROCESSING = 0x9;
	public final static int CANNOT_REASEMBLE = 0xA;
	public final static int SCCP_FAILURE = 0xB;
	public final static int HOP_COUNTER_VIOLATION = 0xC;
	public final static int SEG_NOT_SUPPORTED = 0xD;
	public final static int SEG_FAILURE = 0xE;
	
    /**
     * Gets the value of this parameter.
     * 
     * @return the value of this parameter.
     */
    public int getValue();
}
