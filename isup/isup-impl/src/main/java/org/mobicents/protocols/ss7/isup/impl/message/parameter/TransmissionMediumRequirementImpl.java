/**
 * Start time:17:55:13 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;

/**
 * Start time:17:55:13 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class TransmissionMediumRequirementImpl extends AbstractISUPParameter implements TransmissionMediumRequirement{

	
	

	public TransmissionMediumRequirementImpl(int transimissionMediumRequirement) {
		super();
		this.transimissionMediumRequirement = transimissionMediumRequirement;
	}

	public TransmissionMediumRequirementImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public TransmissionMediumRequirementImpl() {
		super();
		
	}

	// Defualt indicate speech
	private int transimissionMediumRequirement;

	// FIXME: again wrapper class but hell there is a lot of statics....

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}

		this.transimissionMediumRequirement = b[0];

		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) this.transimissionMediumRequirement };
	}

	
	public int encode(ByteArrayOutputStream bos) throws ParameterException {
		byte[] b = this.encode();
		try {
			bos.write(b);
		} catch (IOException e) {
			throw new ParameterException(e);
		}
		return b.length;
	}

	public int getTransimissionMediumRequirement() {
		return transimissionMediumRequirement;
	}

	public void setTransimissionMediumRequirement(int transimissionMediumRequirement) {
		this.transimissionMediumRequirement = transimissionMediumRequirement;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
