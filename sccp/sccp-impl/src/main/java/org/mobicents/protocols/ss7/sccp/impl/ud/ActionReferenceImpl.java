/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.ud;

import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.sccp.ActionReference;

/**
 * @author baranowb
 *
 */
public class ActionReferenceImpl implements ActionReference {

	//used for forging mtp3 message, 
	private byte[] mtp3Header;
	public void setBackRouteHeader(byte[] data)
	{
		//here in data is whole message, we want first 5 bytes!
		int thisPointCode = Mtp3._getFromSif_DPC(data, 1);
		int remotePointCode = Mtp3._getFromSif_OPC(data, 1);
		int sls = Mtp3._getFromSif_SLS(data, 1);
		int si = Mtp3._getFromSif_SI(data);
		int ssi = Mtp3._getFromSif_SSI(data);
		this.mtp3Header = new byte[5];
		Mtp3.writeRoutingLabel(mtp3Header, ssi, ssi, sls, remotePointCode, thisPointCode);
	}

	/**
	 * @return the mtp3Header
	 */
	public byte[] getBackRouteHeader() {
		return mtp3Header;
	}

}
