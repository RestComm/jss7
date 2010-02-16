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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class MandatoryVariableParameter {
    
    /** Creates a new instance of MandatoryVariablePart */
    public MandatoryVariableParameter() {
    }
    
    public abstract void decode(byte[] buffer) throws IOException;
    public abstract byte[] encode() throws IOException;

}
