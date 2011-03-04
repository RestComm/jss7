/**
 * Start time:12:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface CircuitGroupSuperVisionMessageType extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x15;
	//FIXME: V->v
	
	/**
	 * See Q.763 3.13 Circuit group supervision message type indicator
	 * maintenance oriented
	 */
	public static final int _CIRCUIT_GROUP_SMTIMO = 0;
	/**
	 * See Q.763 3.13 Circuit group supervision message type indicator hardware
	 * failure oriented
	 */
	public static final int _CIRCUIT_GROUP_SMTIHFO = 1;
	
	public int getCircuitGroupSuperVisionMessageTypeIndicator() ;

	public void setCircuitGroupSuperVisionMessageTypeIndicator(int CircuitGroupSuperVisionMessageTypeIndicator) ;
}
