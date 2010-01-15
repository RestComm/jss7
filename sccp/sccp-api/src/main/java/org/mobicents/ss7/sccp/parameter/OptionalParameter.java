/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.parameter;

import java.io.IOException;

/**
 * 
 * @author baranowb
 */
public abstract class OptionalParameter {
	/**
	 * End Of Optiona parameter
	 */
	public static OptionalParameter _EOP_ = new OptionalParameter() {

		@Override
		public void decode(byte[] buffer) throws IOException {

		}

		@Override
		public byte[] encode() throws IOException {
			return new byte[] { 0 };
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	};

	/** Creates a new instance of MandatoryVariablePart */
	public OptionalParameter() {
	}

	public abstract void decode(byte[] buffer) throws IOException;

	public abstract byte[] encode() throws IOException;

	
}
