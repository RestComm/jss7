/**
 * Start time:14:06:12 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:06:12 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ServiceActivation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x33;

	public byte[] getFeatureCodes();

	public void setFeatureCodes(byte[] featureCodes);

}
