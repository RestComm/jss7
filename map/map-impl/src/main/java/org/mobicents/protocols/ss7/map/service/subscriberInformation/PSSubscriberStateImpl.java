package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class PSSubscriberStateImpl implements PSSubscriberState, MAPAsnPrimitive {

	public static final int _ID_notProvidedFromSGSNorMME = 0;
	public static final int _ID_ps_Detached = 1;
	public static final int _ID_ps_AttachedNotReachableForPaging = 2;
	public static final int _ID_ps_AttachedReachableForPaging = 3;
	public static final int _ID_ps_PDP_ActiveNotReachableForPaging = 4;
	public static final int _ID_ps_PDP_ActiveReachableForPaging = 5;

	public static final String _PrimitiveName = "PSSubscriberState";

	private boolean notProvidedFromSGSNorMME = false;
	private boolean psDetached = false;
	private boolean psAttachedNotReachableForPaging = false;
	private boolean psAttachedReachableForPaging = false;
	private boolean psPDPActiveNotReachableForPaging = false;
	private boolean psPDPActiveReachableForPaging = false;
	private NotReachableReason netDetNotReachable = null;

	/**
	 * 
	 */
	public PSSubscriberStateImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isNotProvidedFromSGSNorMME()
	 */
	@Override
	public boolean isNotProvidedFromSGSNorMME() {
		return this.notProvidedFromSGSNorMME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isPsDetached()
	 */
	@Override
	public boolean isPsDetached() {
		return this.psDetached;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isPsAttachedNotReachableForPaging()
	 */
	@Override
	public boolean isPsAttachedNotReachableForPaging() {
		return this.psAttachedNotReachableForPaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isPsAttachedReachableForPaging()
	 */
	@Override
	public boolean isPsAttachedReachableForPaging() {
		return this.psAttachedReachableForPaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isPsPDPActiveNotReachableForPaging()
	 */
	@Override
	public boolean isPsPDPActiveNotReachableForPaging() {
		return this.psPDPActiveNotReachableForPaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#isPsPDPActiveReachableForPaging()
	 */
	@Override
	public boolean isPsPDPActiveReachableForPaging() {
		return this.psPDPActiveReachableForPaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState
	 * #setNetDetNotReachable(org.mobicents.protocols.ss7.map.api
	 * .service.subscriberInformation.NotReachableReason)
	 */
	@Override
	public void setNetDetNotReachable(NotReachableReason notReachableReason) {
		this.netDetNotReachable = notReachableReason;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * PSSubscriberState#getNetDetNotReachable()
	 */
	@Override
	public NotReachableReason getNetDetNotReachable() {
		return this.netDetNotReachable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		if (this.notProvidedFromSGSNorMME) {
			return _ID_notProvidedFromSGSNorMME;
		} else if (this.psDetached) {
			return _ID_ps_Detached;
		} else if (this.psAttachedNotReachableForPaging) {
			return _ID_ps_AttachedNotReachableForPaging;
		} else if (this.psAttachedReachableForPaging) {
			return _ID_ps_AttachedReachableForPaging;
		} else if (this.psPDPActiveNotReachableForPaging) {
			return _ID_ps_PDP_ActiveNotReachableForPaging;
		} else if (this.psPDPActiveReachableForPaging) {
			return _ID_ps_PDP_ActiveReachableForPaging;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		if (ansIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ansIS.isTagPrimitive())
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag class or is not primitive: TagClass="
					+ ansIS.getTagClass(), MAPParsingComponentExceptionReason.MistypedParameter);

		switch (ansIS.getTag()) {
		case _ID_notProvidedFromSGSNorMME:
			this.notProvidedFromSGSNorMME = true;
			ansIS.readNullData(length);
			break;

		case _ID_ps_Detached:
			this.psDetached = true;
			ansIS.readNullData(length);
			break;

		case _ID_ps_AttachedNotReachableForPaging:
			this.psAttachedNotReachableForPaging = true;
			ansIS.readNullData(length);
			break;

		case _ID_ps_AttachedReachableForPaging:
			this.psAttachedReachableForPaging = true;
			ansIS.readNullData(length);
			break;

		case _ID_ps_PDP_ActiveNotReachableForPaging:
			this.psPDPActiveNotReachableForPaging = true;
			ansIS.readNullData(length);
			break;

		case _ID_ps_PDP_ActiveReachableForPaging:
			this.psPDPActiveReachableForPaging = true;
			ansIS.readNullData(length);
			break;

		default:
			throw new MAPParsingComponentException("Error while SM_RP_DA: bad tag: " + ansIS.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		asnOs.writeNullData();
	}
}
