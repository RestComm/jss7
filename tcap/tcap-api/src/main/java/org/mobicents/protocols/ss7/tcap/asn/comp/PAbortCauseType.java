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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.ParseException;


/**
 * @author baranowb
 *
 */
public enum PAbortCauseType {
	// its encoded as INT

	UnrecogniedMessageType(0), UnrecognizedTxID(1),
	BadlyFormattedTxPortion(2), IncorrectTxPortion(3), 
	ResourceLimitation(4);

	private long type = -1;

	PAbortCauseType(long t) {
		this.type = t;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}

	public static PAbortCauseType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return UnrecogniedMessageType;
		} else if (t == 1) {
			return UnrecognizedTxID;
		} else if( t == 2)
		{
			return BadlyFormattedTxPortion;
		} else if ( t == 3)
		{
			return IncorrectTxPortion;
		}else if( t == 4 )
		{
			return ResourceLimitation;
		}

		throw new ParseException("Wrong value of response: " + t);
	}
}
