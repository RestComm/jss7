package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;



/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class CancelLocationResponseImpl extends MobilityMessageImpl implements
		CancelLocationResponse {

	private MAPExtensionContainer extensionContainer;
	private long mapProtocolVersion;

	public CancelLocationResponseImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}
	
	public CancelLocationResponseImpl(MAPExtensionContainer extensionContainer,
			long mapProtocolVersion) {
		super();
		this.extensionContainer = extensionContainer;
		this.mapProtocolVersion = mapProtocolVersion;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.cancelLocation_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.cancelLocation;
	}

	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS)
			throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationResponse: "
							+ e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationResponse: "
							+ e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationResponse: "
							+ e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationResponse: "
							+ e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationResponse: "
							+ e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void _decode(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException, IOException, AsnException {
		this.extensionContainer = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		if (ais.available() == 0)
			return;
		int tag = ais.readTag();
		this.extensionContainer = new MAPExtensionContainerImpl();
		((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		try {
			this.encodeAll(asnOs, this.getTagClass(), this.getTag());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPException(e);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
			throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException(
					"AsnException when encoding CancelLocationResponse : "
							+ e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl) this.extensionContainer)
					.encodeAll(asnOs);
		}
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

}
