/**
 * Start time:12:20:07 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;

/**
 * Start time:12:20:07 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CircuitAssigmentMapImpl extends AbstractParameter implements CircuitAssigmentMap{

	
	
	

	private static final int _CIRCUIT_ENABLED = 1;
	
	private int mapType = 0;

	private int mapFormat = 0;

	public CircuitAssigmentMapImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public CircuitAssigmentMapImpl(int mapType, int mapFormat) {
		super();
		this.mapType = mapType;
		this.mapFormat = mapFormat;
	}

	public CircuitAssigmentMapImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 5) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 5");
		}

		this.mapType = b[0] & 0x3F;
		this.mapFormat = b[1];
		this.mapFormat |= b[2] << 8;
		this.mapFormat |= b[3] << 16;
		this.mapFormat |= (b[4] & 0x7F) << 24;
		
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		byte[] b = new byte[5];
		b[0] = (byte) (this.mapType & 0x3F);
		b[1] = (byte) this.mapFormat;
		b[2] = (byte) (this.mapFormat >> 8);
		b[3] = (byte) (this.mapFormat >> 16);
		b[4] = (byte) ((this.mapFormat >> 24) & 0x7F);
		return b;
	}

	public int getMapType() {
		return mapType;
	}

	public void setMapType(int mapType) {
		this.mapType = mapType;
	}

	public int getMapFormat() {
		return mapFormat;
	}

	public void setMapFormat(int mapFormat) {
		this.mapFormat = mapFormat;
	}

	/**
	 * Enables circuit
	 * 
	 * @param circuitNumber
	 *            - index of circuit - must be number <1,31>
	 * @throws IllegalArgumentException
	 *             - when number is not in range
	 */
	public void enableCircuit(int circuitNumber) throws IllegalArgumentException {
		if (circuitNumber < 1 || circuitNumber > 31) {
			throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
		}

		this.mapFormat |= 0x01 << (circuitNumber-1);
	}

	/**
	 * Disables circuit
	 * 
	 * @param circuitNumber
	 *            - index of circuit - must be number <1,31>
	 * @throws IllegalArgumentException
	 *             - when number is not in range
	 */
	public void disableCircuit(int circuitNumber) throws IllegalArgumentException {
		if (circuitNumber < 1 || circuitNumber > 31) {
			throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
		}
		this.mapFormat &= 0xFFFFFFFE << (circuitNumber -1);
	}

	
	public boolean isCircuitEnabled(int circuitNumber)throws IllegalArgumentException
	{
		if (circuitNumber < 1 || circuitNumber > 31) {
			throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
		}
		
		return ((this.mapFormat >> (circuitNumber-1)) & 0x01) ==_CIRCUIT_ENABLED;
	}
	
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
