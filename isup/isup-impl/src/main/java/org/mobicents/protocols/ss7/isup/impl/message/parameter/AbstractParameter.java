/**
 * Start time:12:54:56 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;

/**
 * Start time:12:54:56 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * Simple class to define common methods and fields for all.
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>

 */
public abstract class AbstractParameter implements ISUPParameter,ISUPComponent {

	//protected byte[] tag = null;
	protected Logger logger  = Logger.getLogger(this.getClass().getName());
//	public byte[] getTag() {
//		return this.tag;
//	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		//FIXME: this has to be removed, we should not create separate arrays?
		byte[] b = this.encodeElement();
		bos.write(b);
		return b.length;
	}

}
