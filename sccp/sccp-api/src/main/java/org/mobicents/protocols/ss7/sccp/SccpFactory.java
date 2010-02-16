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

package org.mobicents.protocols.ss7.sccp;

/**
 *
 * @author Oleg Kulikov
 */
public class SccpFactory {
    
    /** Creates a new instance of SccpFactory */
    public SccpFactory() {
    }
    
    public static SccpProvider getProvider(String className) {
        try {
            return (SccpProvider) Class.forName(className).newInstance();
        } catch (Exception e) {
            return null;
        }
    }
    
}
