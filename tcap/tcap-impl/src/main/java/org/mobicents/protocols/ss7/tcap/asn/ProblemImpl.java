/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 * @author baranowb
 * 
 */
public class ProblemImpl implements Problem {

	private ProblemType type;

	private GeneralProblemType generalProblemType;
	private InvokeProblemType invokeProblemType;
	private ReturnErrorProblemType returnErrorProblemType;
	private ReturnResultProblemType returnResultProblemType;

	/**
	 * @return the type
	 */
	public ProblemType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ProblemType type) {
		this.type = type;
	}

	/**
	 * @return the generalProblemType
	 */
	public GeneralProblemType getGeneralProblemType() {
		return generalProblemType;
	}

	/**
	 * @param generalProblemType
	 *            the generalProblemType to set
	 */
	public void setGeneralProblemType(GeneralProblemType generalProblemType) {
		this.generalProblemType = generalProblemType;
		this.setType(ProblemType.General);
	}

	/**
	 * @return the invokeProblemType
	 */
	public InvokeProblemType getInvokeProblemType() {
		return invokeProblemType;
	}

	/**
	 * @param invokeProblemType
	 *            the invokeProblemType to set
	 */
	public void setInvokeProblemType(InvokeProblemType invokeProblemType) {
		this.setType(ProblemType.Invoke);
		this.invokeProblemType = invokeProblemType;
	}

	/**
	 * @return the returnErrorProblemType
	 */
	public ReturnErrorProblemType getReturnErrorProblemType() {
		return returnErrorProblemType;
	}

	/**
	 * @param returnErrorProblemType
	 *            the returnErrorProblemType to set
	 */
	public void setReturnErrorProblemType(ReturnErrorProblemType returnErrorProblemType) {
		this.returnErrorProblemType = returnErrorProblemType;
		this.setType(ProblemType.ReturnError);
	}

	/**
	 * @return the returnResultProblemType
	 */
	public ReturnResultProblemType getReturnResultProblemType() {
		return returnResultProblemType;
	}

	/**
	 * @param returnResultProblemType
	 *            the returnResultProblemType to set
	 */
	public void setReturnResultProblemType(ReturnResultProblemType returnResultProblemType) {
		this.returnResultProblemType = returnResultProblemType;
		this.setType(ProblemType.ReturnResult);
	}

	public void decode(AsnInputStream ais) throws ParseException {

		try {
			long t = ais.readInteger();
			switch (type) {
			case General:
				this.generalProblemType = GeneralProblemType.getFromInt(t);
				break;
			case Invoke:
				this.invokeProblemType = InvokeProblemType.getFromInt(t);
				break;
			case ReturnError:
				this.returnErrorProblemType = ReturnErrorProblemType.getFromInt(t);
				break;
			case ReturnResult:
				this.returnResultProblemType = ReturnResultProblemType.getFromInt(t);
				break;
			default:
				// should not happen
				throw new ParseException();
			}
		} catch (AsnException e) {
			throw new ParseException(e);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

	public void encode(AsnOutputStream aos) throws ParseException {
		// TODO Auto-generated method stub

	}

}
