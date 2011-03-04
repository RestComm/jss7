/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;

/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ServiceActivationImpl extends AbstractISUPParameter implements ServiceActivation {

	// FIXME: this is again simple container
	/**
	 * See Q.763 3.49
	 */
	public final static byte _FEATURE_CODE_CALL_TRANSFER = 1;

	private byte[] featureCodes;

	public ServiceActivationImpl() {
		super();
		
	}

	public ServiceActivationImpl(byte[] featureCodes) {
		super();
		this.featureCodes = featureCodes;
	}

	public int decode(byte[] b) throws ParameterException {
		this.featureCodes = b;
		return b.length;
	}

	public byte[] encode() throws ParameterException {
		return this.featureCodes;
	}

	public byte[] getFeatureCodes() {
		return featureCodes;
	}

	public void setFeatureCodes(byte[] featureCodes) {
		this.featureCodes = featureCodes;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
