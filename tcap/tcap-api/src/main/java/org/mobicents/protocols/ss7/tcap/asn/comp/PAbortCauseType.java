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
 * @author amit bhayani
 * @author baranowb
 * 
 */
public enum PAbortCauseType {
	// its encoded as INT

	UnrecognizedMessageType(0), UnrecognizedTxID(1), BadlyFormattedTxPortion(2), IncorrectTxPortion(3), ResourceLimitation(4),

	// This is not there in specs, but used locally
	DialogueIdleTimeout(125), AbnormalDialogue(126), NoCommonDialoguePortion(127), NoReasonGiven(128);

	private int type = -1;

	PAbortCauseType(int t) {
		this.type = t;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public static PAbortCauseType getFromInt(int t) throws ParseException {

		switch (t) {
		case 0:
			return UnrecognizedMessageType;
		case 1:
			return UnrecognizedTxID;
		case 2:
			return BadlyFormattedTxPortion;
		case 3:
			return IncorrectTxPortion;
		case 4:
			return ResourceLimitation;
		case 125:
			return DialogueIdleTimeout;
		case 126:
			return AbnormalDialogue;
		case 127:
			return NoCommonDialoguePortion;
		case 128:
			return NoReasonGiven;
		default:
			throw new ParseException("Wrong value of response: " + t);
		}
	}
}
