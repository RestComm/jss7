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

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.OptionalParameter;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;
import org.mobicents.protocols.ss7.sccp.ud.UDBase;
import org.mobicents.protocols.ss7.sccp.ud.XUnitData;

/**
 * See Q.713 4.18
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class XUnitDataImpl  extends UDBaseImpl implements XUnitData {

	
	// //////////////////
	// Fixed parts //
	// //////////////////

	
	/**
	 * See Q.713 3.18
	 */
	private byte hopCounter = HOP_COUNT_NOT_SET;
	//private ProtocolClassImpl pClass;

	// //////////////////
	// Variable parts //
	// //////////////////
	//private SccpAddressImpl calledParty;
	//private SccpAddressImpl callingParty;
	//private byte[] data;
	// //////////////////
	// Optional parts //
	// //////////////////
	private Segmentation segmentation;
	private Importance importance;

	// EOP

	/** Creates a new instance of UnitData */
	public XUnitDataImpl() {
	}

	public XUnitDataImpl(byte hopCounter, ProtocolClass pClass, SccpAddress calledParty, SccpAddress callingParty, byte[] data) {
		super();
		this.hopCounter = hopCounter;
		this.pClass = pClass;
		super.calledParty = (SccpAddressImpl) calledParty;
		super.callingParty = (SccpAddressImpl) callingParty;
		super.data = data;
	}

	public XUnitDataImpl(byte hopCounter, ProtocolClass pClass, SccpAddress calledParty, SccpAddress callingParty, byte[] data,
			Segmentation segmentation, Importance importance) {
		super();
		this.hopCounter = hopCounter;
		super.pClass = pClass;
		super.calledParty = (SccpAddressImpl) calledParty;
		super.callingParty = (SccpAddressImpl) callingParty;
		super.data = data;
		this.segmentation = segmentation;
		this.importance = importance;
	}

	public byte getHopCounter() {
		return hopCounter;
	}

	public void setHopCounter(byte hopCounter) {
		this.hopCounter = hopCounter;
	}

	

	public Segmentation getSegmentation() {
		return segmentation;
	}

	public void setSegmentation(Segmentation segmentation) {
		this.segmentation = segmentation;
	}

	public Importance getImportance() {
		return importance;
	}

	public void setImportance(Importance importance) {
		this.importance = importance;
	}

	public void encode(OutputStream out) throws IOException {
		out.write(_MT);

		pClass.encode(out);
		if (this.hopCounter == HOP_COUNT_NOT_SET) {
			throw new IOException("Failed parsing, hop counter is not set.");
		}
		out.write(this.hopCounter);

		byte[] cdp = calledParty.encode();
		byte[] cnp = callingParty.encode();

		// we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4
		// octests than
		int len = 4;
		out.write(len);

		len = (cdp.length + len);
		out.write(len);

		len += (cnp.length);
		out.write(len);
		boolean optionalPresent = false;
		if (segmentation != null || importance != null) {
			len += (data.length);
			out.write(len);
			optionalPresent = true;
		} else {
			// in case there is no optional
			out.write(0);
		}

		out.write((byte) cdp.length);
		out.write(cdp);

		out.write((byte) cnp.length);
		out.write(cnp);

		out.write((byte) data.length);
		out.write(data);

		if (segmentation != null) {
			optionalPresent = true;
			out.write(SegmentationImpl._PARAMETER_CODE);
			byte[] b = segmentation.encode();
			out.write(b.length);
			out.write(b);
		}

		if (importance != null) {
			optionalPresent = true;
			out.write(ImportanceImpl._PARAMETER_CODE);
			byte[] b = importance.encode();
			out.write(b.length);
			out.write(b);
		}

		if (optionalPresent) {

			out.write(OptionalParameter._EOP_.encode());
		}

	}

	public void decode(InputStream in) throws IOException {

		pClass = new ProtocolClassImpl();
		pClass.decode(in);

		this.hopCounter = (byte) in.read();
		if (this.hopCounter >= HOP_COUNT_HIGH_ || this.hopCounter <= HOP_COUNT_LOW_) {
			throw new IOException("Hop Counter must be between 1 and 5, it is: " + this.hopCounter);
		}

		int pointer = in.read() & 0xff;
		in.mark(in.available());
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		int len = in.read() & 0xff;

		byte[] buffer = new byte[len];
		in.read(buffer);
		this.calledParty = new SccpAddressImpl();
		this.calledParty.decode(buffer);

		in.reset();

		pointer = in.read() & 0xff;

		in.mark(in.available());

		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		len = in.read() & 0xff;

		buffer = new byte[len];
		in.read(buffer);

		callingParty = new SccpAddressImpl();
		callingParty.decode(buffer);

		in.reset();
		pointer = in.read() & 0xff;
		in.mark(in.available());
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		len = in.read() & 0xff;

		data = new byte[len];
		in.read(data);

		in.reset();
		pointer = in.read() & 0xff;
		in.mark(in.available());

		if (pointer == 0) {
			// we are done
			return;
		}
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}

		//FIXME: detect if there is only EOP present?
		int paramCode = 0;
		//                                      EOP
		while ((paramCode = in.read() & 0xFF) != 0) {
			len = in.read() & 0xff;
			buffer = new byte[len];
			in.read(buffer);
			this.decodeOptional(paramCode, buffer);

			// we should have one octet more here
		}

	}

	private void decodeOptional(int code, byte[] buffer) throws IOException {

		switch (code) {
		case SegmentationImpl._PARAMETER_CODE:
			this.segmentation = new SegmentationImpl();
			this.segmentation.decode(buffer);
			break;
		case ImportanceImpl._PARAMETER_CODE:
			this.importance = new ImportanceImpl();
			this.importance.decode(buffer);
			break;
		
		default:
			throw new IOException("Uknown optional parameter code: " + code);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calledParty == null) ? 0 : calledParty.hashCode());
		result = prime * result + ((callingParty == null) ? 0 : callingParty.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + hopCounter;
		result = prime * result + ((importance == null) ? 0 : importance.hashCode());
		result = prime * result + ((pClass == null) ? 0 : pClass.hashCode());
		result = prime * result + ((segmentation == null) ? 0 : segmentation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XUnitDataImpl other = (XUnitDataImpl) obj;
		if (calledParty == null) {
			if (other.calledParty != null)
				return false;
		} else if (!calledParty.equals(other.calledParty))
			return false;
		if (callingParty == null) {
			if (other.callingParty != null)
				return false;
		} else if (!callingParty.equals(other.callingParty))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (hopCounter != other.hopCounter)
			return false;
		if (importance == null) {
			if (other.importance != null)
				return false;
		} else if (!importance.equals(other.importance))
			return false;
		if (pClass == null) {
			if (other.pClass != null)
				return false;
		} else if (!pClass.equals(other.pClass))
			return false;
		if (segmentation == null) {
			if (other.segmentation != null)
				return false;
		} else if (!segmentation.equals(other.segmentation))
			return false;
		return true;
	}


}




