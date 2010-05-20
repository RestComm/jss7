package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderAbortInfoImpl implements MAPProviderAbortInfo {

	protected static final int MAP_PROVIDER_ABORT_INFO_TAG = 0x05;

	protected static final int PROVIDER_ABORT_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean PROVIDER_ABORT_TAG_PC_CONSTRUCTED = false;

	private MAPDialog mapDialog = null;
	private MAPProviderAbortReason mapProviderAbortReason = null;

	public MAPProviderAbortInfoImpl() {
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public MAPProviderAbortReason getMAPProviderAbortReason() {
		return this.mapProviderAbortReason;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
		this.mapProviderAbortReason = mapProvAbrtReas;
	}

	public void decode(AsnInputStream ais) throws AsnException, IOException,
			MAPException {
		byte[] seqData = ais.readSequence();

		AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(
				seqData));

		int tag;

		while (localAis.available() > 0) {
			tag = localAis.readTag();
			if (tag == Tag.ENUMERATED) {
				int length = localAis.readLength();
				if (length != 1) {
					throw new MAPException(
							"Expected length of MAPProviderAbortInfoImpl.MAPProviderAbortReason to be 1 but found "
									+ length);
				}

				int code = localAis.read();
				this.mapProviderAbortReason = MAPProviderAbortReason
						.getInstance(code);
			}
		}
	}

	public void encode(AsnOutputStream asnOS) throws IOException, MAPException {
		byte[] data = new byte[3];
		data[0] = Tag.ENUMERATED;
		data[1] = 0x01;
		data[2] = (byte) this.mapProviderAbortReason.getCode();

		// Now let us write the MAP OPEN-INFO Tags
		asnOS.writeTag(PROVIDER_ABORT_TAG_CLASS,
				PROVIDER_ABORT_TAG_PC_CONSTRUCTED, MAP_PROVIDER_ABORT_INFO_TAG);
		asnOS.writeLength(data.length);
		asnOS.write(data);
	}

}
