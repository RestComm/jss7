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
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class RejectImpl implements Reject {

	// all are mandatory

	// this can actaully be null in this case.
	private Long invokeId;

	private Problem problem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#getInvokeId()
	 */
	public Long getInvokeId() {

		return this.invokeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#getProblem()
	 */
	public Problem getProblem() {

		return this.problem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Reject#setInvokeId(java.lang
	 * .Long)
	 */
	public void setInvokeId(Long i) {
		if (i != null && (i < -128 || i > 127)) {
			throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
		}
		this.invokeId = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Reject#setProblem(org.mobicents
	 * .protocols.ss7.tcap.asn.comp.Problem)
	 */
	public void setProblem(Problem p) {

		this.problem = p;

	}

	public ComponentType getType() {

		return ComponentType.Reject;
	}

	
	public String toString() {
		return "Reject[invokeId=" + invokeId + ", problem=" + problem + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {

		try {
			AsnInputStream localAis = ais.readSequenceStream();

			int tag = localAis.readTag();
			if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
				throw new ParseException("Error while decoding Reject: bad tag class for InvokeID or NULL: tagClass = " + localAis.getTagClass());
			switch(tag) {
			case _TAG_IID:
				this.invokeId = localAis.readInteger();
				break;
			case Tag.NULL:
				localAis.readNull();
				break;
			}

			tag = localAis.readTag();
			if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
				throw new ParseException("Error while decoding Reject: bad tag class for a problem: tagClass = " + localAis.getTagClass());
			ProblemType pt = ProblemType.getFromInt(tag);
			if (pt == null)
				throw new ParseException("Error while decoding Reject: ProblemType not found");
			this.problem = TcapFactory.createProblem(pt, localAis);
			
		} catch (IOException e) {
			throw new ParseException("IOException while decoding Reject: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding Reject: " + e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {

		if (this.problem == null) {
			throw new ParseException("Problem not set!");
		}
		try {
			aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
			int pos = aos.StartContentDefiniteLength();

			if (this.invokeId == null)
				aos.writeNull();
			else
				aos.writeInteger(this.invokeId);			
			this.problem.encode(aos);
			
			aos.FinalizeContent(pos);
			
		} catch (IOException e) {
			throw new ParseException("IOException while encoding Reject: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while encoding Reject: " + e.getMessage(), e);
		}

	}

}
