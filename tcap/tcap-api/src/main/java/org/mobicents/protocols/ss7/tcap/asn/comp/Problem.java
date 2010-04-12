/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * @author baranowb
 *
 */
public interface Problem extends Encodable {

	//this is a hell of a combo
	public ProblemType getType();
	public void setType(ProblemType t);
	
	
	
	//now depending on type, one of below values must not be null
	
	//mandatory, one for each type
	public void setGeneralProblemType(GeneralProblemType t);
	public GeneralProblemType getGeneralProblemType();
	
	public void setInvokeProblemType(InvokeProblemType t);
	public InvokeProblemType getInvokeProblemType();
	
	public void setReturnErrorProblemType(ReturnErrorProblemType t);
	public ReturnErrorProblemType getReturnErrorProblemType();
	
	public void setReturnResultProblemType(ReturnResultProblemType t);
	public ReturnResultProblemType getReturnResultProblemType();
	
	
}
