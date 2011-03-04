/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.GVNSUserGroup;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;
import org.mobicents.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class ForwardGVNSTest extends ParameterHarness {

	/**
	 * @throws IOException
	 * @throws ParameterException 
	 */
	public ForwardGVNSTest() throws IOException, ParameterException {
		
		super.goodBodies.add(getBody(getSixDigitsString(),getSixDigitsString(),getFiveDigitsString()));

	}
	private byte[] getBody(String bs, String bs2, String bs3) throws ParameterException, IOException
	{
		//those are tested in other junits.
		OriginatingParticipatingServiceProviderImpl ops = new OriginatingParticipatingServiceProviderImpl();
		ops.setAddress(bs);
		GVNSUserGroupImpl gvns = new GVNSUserGroupImpl();
		gvns.setAddress(bs2);
		TerminatingNetworkRoutingNumberImpl tnrn = new TerminatingNetworkRoutingNumberImpl();
		tnrn.setAddress(bs3);
		tnrn.setNumberingPlanIndicator(tnrn._NPI_TELEX);
		tnrn.setNatureOfAddressIndicator(tnrn._NAI_NATIONAL_SN);
		return getBody(ops,gvns,tnrn);
	}
	private byte[] getBody(OriginatingParticipatingServiceProvider ops,GVNSUserGroup gnvs,TerminatingNetworkRoutingNumber tnrn) throws ParameterException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//bit weird tests...
		bos.write( ((AbstractISUPParameter)ops).encode());
		bos.write( ((AbstractISUPParameter)gnvs).encode());
		bos.write( ((AbstractISUPParameter)tnrn).encode());
		
		return bos.toByteArray();
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() {
		return new ForwardGVNSImpl();
	}

}
