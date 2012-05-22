package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.RouteingNumber;
import org.mobicents.protocols.ss7.map.primitives.TbcdString;

/**
 * @author amit bhayani
 * 
 */
public class RouteingNumberImpl extends TbcdString implements RouteingNumber {

	private String routeingNumber;

	public RouteingNumberImpl() {
	}

	public RouteingNumberImpl(String routeingNumber) {
		this.routeingNumber = routeingNumber;
	}

	@Override
	public String getRouteingNumber() {
		return this.routeingNumber;
	}

	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	public boolean getIsPrimitive() {
		return true;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding RouteingNumber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding RouteingNumber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {

		if (length < 1 || length > 5)
			throw new MAPParsingComponentException("Error decoding IMEI: the RouteingNumber field must contain from 1 to 5 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			this.routeingNumber = this.decodeString(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding RouteingNumber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding IMEI: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.routeingNumber == null)
			throw new MAPException("Error while encoding the RouteingNumber: RouteingNumber must not be null");

		if (this.routeingNumber.length() < 1 || this.routeingNumber.length() > 5)
			throw new MAPException("Error while encoding the RouteingNumber: Bad RouteingNumber length - must be 1 to 5");

		this.encodeString(asnOs, this.routeingNumber);
	}

	@Override
	public String toString() {
		return "RouteingNumber [RouteingNumber=" + this.routeingNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((routeingNumber == null) ? 0 : routeingNumber.hashCode());
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
		RouteingNumberImpl other = (RouteingNumberImpl) obj;
		if (routeingNumber == null) {
			if (other.routeingNumber != null)
				return false;
		} else if (!routeingNumber.equals(other.routeingNumber))
			return false;
		return true;
	}

}
