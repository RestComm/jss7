package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ExtendedRoutingInfoImpl implements ExtendedRoutingInfo, MAPAsnPrimitive  {
	private RoutingInfo routingInfo = null;
	private byte[] camelRoutingInfo = null;
	
	private static final int TAG_camel = 8;
	private static final String _PrimitiveName = "ExtendedRoutingInfo";
	
	
	public ExtendedRoutingInfoImpl() {}

	public ExtendedRoutingInfoImpl(RoutingInfo routingInfo, byte[] camelRoutingInfo) {
		this.routingInfo = routingInfo;
		this.camelRoutingInfo = camelRoutingInfo;
	}
	
	@Override
	public RoutingInfo getRoutingInfo() {
		return this.routingInfo;
	}
	
	@Override
	public byte[] getCamelRoutingInfo() {
		return this.camelRoutingInfo;
	}
	
	@Override
	public int getTag() throws MAPException {
		if(routingInfo != null) 
		  return ((RoutingInfoImpl) routingInfo).getTag();
	    return TAG_camel;
	}
	
	@Override
	public int getTagClass() {
		if(routingInfo != null) 
		  return Tag.CLASS_UNIVERSAL;
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}
	
	@Override
	public boolean getIsPrimitive() {
		if(routingInfo != null) 
		  return ((RoutingInfoImpl) routingInfo).getIsPrimitive();
		return false;
	}
	
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
	
	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.routingInfo = null;
		this.camelRoutingInfo = null;	

		int tag = ais.getTag();
		if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
			switch (tag) {
			case Tag.SEQUENCE: 
			case Tag.STRING_OCTET: this.routingInfo = new RoutingInfoImpl();
								   ((RoutingInfoImpl) this.routingInfo).decodeData(ais, length); 
								   break;
			}
		} else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
			switch (tag) {
			case TAG_camel: break; // TODO: decode CAMEL routing info
			default: throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagNumber",
					 MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
		else { 
		  throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}
	
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.routingInfo == null && this.camelRoutingInfo == null)
			throw new MAPException("Both routingInfo and camelRoutingInfo must not be null");
		if (this.routingInfo != null && this.camelRoutingInfo != null)
			throw new MAPException("Both routingInfo and camelRoutingInfo must not be not null");

		if (this.routingInfo != null) {
			((RoutingInfoImpl) this.routingInfo).encodeData(asnOs);
		} else { // TODO: encode CAMEL routing info
		}
	}
}