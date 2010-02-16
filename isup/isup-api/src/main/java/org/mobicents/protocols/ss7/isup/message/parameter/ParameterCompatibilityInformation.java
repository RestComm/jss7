/**
 * Start time:13:41:49 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:41:49 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ParameterCompatibilityInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x39;
	
	
	public void addInstructions(Byte parameterCode, InstructionIndicators instructionIndicators);

	// FIXME: Crude API
	public InstructionIndicators getInstructionIndicators(int index) ;

	public Byte getParameterCode(int index) ;

	public int size();

	public void remove(int index);
	
}
