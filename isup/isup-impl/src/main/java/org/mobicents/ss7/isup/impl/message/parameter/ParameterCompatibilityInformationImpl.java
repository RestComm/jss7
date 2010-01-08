/**
 * Start time:12:39:34 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.InstructionIndicators;
import org.mobicents.ss7.isup.message.parameter.ParameterCompatibilityInformation;

/**
 * Start time:12:39:34 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * This is composed param ?
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class ParameterCompatibilityInformationImpl extends AbstractParameter implements ParameterCompatibilityInformation{

	
	private List<Byte> parameterCodes = new ArrayList<Byte>();
	private List<InstructionIndicators> instructionIndicators = new ArrayList<InstructionIndicators>();

	public ParameterCompatibilityInformationImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public ParameterCompatibilityInformationImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {

		if (b == null || b.length < 2) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  greater than 1");
		}

		
		
		ByteArrayOutputStream bos = null;
		boolean newParameter = true;
		byte parameterCode = 0;

		for (int index = 0; index < b.length; index++) {
			if (newParameter) {
				parameterCode = b[index];
				bos = new ByteArrayOutputStream();
				newParameter = false;
				continue;
			} else {
				bos.write(b[index]);

				if (((b[index] >> 7) & 0x01) == 0) {
					// ext bit is zero, this is last octet
					
					if (bos.size() < 3) {
						this.addInstructions(parameterCode, new InstructionIndicatorsImpl(bos.toByteArray()));
					} else {
						this.addInstructions(parameterCode, new InstructionIndicatorsImpl(bos.toByteArray(), true));
					}
					newParameter = true;
				} else {
				
					continue;
				}
			}

		}
		
	
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int index = 0; index < this.parameterCodes.size(); index++) {
			bos.write(this.parameterCodes.get(index).byteValue());
			bos.write(this.instructionIndicators.get(index).encodeElement());
		}
		return bos.toByteArray();
	}

	public void addInstructions(Byte parameterCode, InstructionIndicators instructionIndicators) {
		// FIXME: do we need to check for duplicate?
		this.parameterCodes.add(parameterCode);
		this.instructionIndicators.add(instructionIndicators);
	}

	// FIXME: Crude API
	public InstructionIndicators getInstructionIndicators(int index) {
		return this.instructionIndicators.get(index);
	}

	public Byte getParameterCode(int index) {
		return this.parameterCodes.get(index);
	}

	public int size() {
		return this.instructionIndicators.size();
	}

	public void remove(int index) {
		this.instructionIndicators.remove(index);
		this.parameterCodes.remove(index);
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
