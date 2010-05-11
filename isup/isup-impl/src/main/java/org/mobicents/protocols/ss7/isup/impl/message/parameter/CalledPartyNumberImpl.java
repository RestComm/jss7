/**
 * Start time:15:59:02 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;

/**
 * Start time:15:59:02 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * This represent called party number - Q.763 - 3.9
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class CalledPartyNumberImpl extends AbstractNAINumber implements CalledPartyNumber {

	protected int numberingPlanIndicator;

	protected int internalNetworkNumberIndicator;

	/**
	 * 
	 * 
	 * @param representation
	 * @throws ParameterRangeInvalidException
	 */
	public CalledPartyNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		
	}

	/**
	 * 
	 * 
	 * @param bis
	 * @throws ParameterRangeInvalidException
	 */
	public CalledPartyNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		
	}
	
	public CalledPartyNumberImpl() {
		super();
		
	}

	/**
	 * 
	 * @param natureOfAddresIndicator
	 * @param address
	 */
	public CalledPartyNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator) {
		super(natureOfAddresIndicator, address);
		this.numberingPlanIndicator = numberingPlanIndicator;
		this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io.
	 * ByteArrayInputStream)
	 */
	@Override
	public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
		int b = bis.read() & 0xff;

		this.internalNetworkNumberIndicator = (b & 0x80) >> 7;
		this.numberingPlanIndicator = (b & 0x70) >> 4;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io.
	 * ByteArrayOutputStream)
	 */
	@Override
	public int encodeBody(ByteArrayOutputStream bos) {
		int c = (this.numberingPlanIndicator & 0x07) << 4;
		c |= ((this.internalNetworkNumberIndicator & 0x01) << 7);
		bos.write(c);
		return 1;
	}

	public int getNumberingPlanIndicator() {
		return numberingPlanIndicator;
	}

	public void setNumberingPlanIndicator(int numberingPlanIndicator) {
		this.numberingPlanIndicator = numberingPlanIndicator;
	}

	public int getInternalNetworkNumberIndicator() {
		return internalNetworkNumberIndicator;
	}

	public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator) {
		this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
