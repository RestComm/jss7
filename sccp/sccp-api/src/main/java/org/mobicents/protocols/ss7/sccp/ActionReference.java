/**
 * 
 */
package org.mobicents.protocols.ss7.sccp;

import org.mobicents.protocols.ss7.mtp.Mtp3;

/**
 * This class is defined to allow Dialogic like communication with mtp3 layer - dialogic expects fully formed Mtp3 message in byte[].
 * So we need to create and retain back route header for incoming data(and have one forged for outgoing for newly created sessions)!.
 * @author baranowb
 *
 */
public class ActionReference {
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
		Mtp3.writeRoutingLabel(mtp3Header, si, ssi, sls, remotePointCode, thisPointCode);
	}

	/**
	 * @return the mtp3Header
	 */
	public byte[] getBackRouteHeader() {
		return mtp3Header;
	}

	public ActionReference(int opc,int dpc,int sls,int si,int ssi) {
		super();
		this.mtp3Header = new byte[5];
		Mtp3.writeRoutingLabel(mtp3Header, si, ssi, sls, dpc, opc);
	}
	
	public ActionReference()
	{}
}
