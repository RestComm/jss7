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
public enum InvokeProblemType {

	DuplicateInvokeID(0),UnrecognizedOperation(1),MistypedParameter(2),
	ResourceLimitation(3),InitiatingRelease(4),UnrechognizedLinkedID(5),
	LinkedResponseUnexpected(6), UnexpectedLinkedOperation(7);
	
	
	
	private long type;
	
	InvokeProblemType(long l)
	{
		this.type = l;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}
	
	public static InvokeProblemType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return DuplicateInvokeID;
		} else if (t == 1) {
			return UnrecognizedOperation;
		}  else if (t == 2) {
			return MistypedParameter;
		}   else if (t == 3) {
			return ResourceLimitation;
		}  else if (t == 4) {
			return InitiatingRelease;
		}   else if (t == 5) {
			return UnrechognizedLinkedID;
		}  else if (t == 6) {
			return LinkedResponseUnexpected;
		}   else if (t == 7) {
			return UnexpectedLinkedOperation;
		} 

		throw new ParseException("Wrong value of type: " + t);
	}
	
}
