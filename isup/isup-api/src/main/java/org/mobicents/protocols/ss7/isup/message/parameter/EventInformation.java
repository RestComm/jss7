/**
 * Start time:12:47:23 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:47:23 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface EventInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x24;
	
	/**
	 * See Q.763 3.21 Event indicator : ALERTING
	 */
	public static final int _EVENT_INDICATOR_ALERTING = 1;

	/**
	 * See Q.763 3.21 Event indicator : PROGRESS
	 */
	public static final int _EVENT_INDICATOR_PROGRESS = 2;

	/**
	 * See Q.763 3.21 Event indicator : in-band information or an appropriate
	 * pattern is now available
	 */
	public static final int _EVENT_INDICATOR_IIIOPA = 3;

	/**
	 * See Q.763 3.21 Event indicator : call forwarded on busy (national use)
	 */
	public static final int _EVENT_INDICATOR_CFOB = 4;

	/**
	 * See Q.763 3.21 Event indicator : call forwarded on no reply (national
	 * use)
	 */
	public static final int _EVENT_INDICATOR_CFONNR = 5;

	/**
	 * See Q.763 3.21 Event indicator : call forwarded unconditional (national
	 * use)
	 */
	public static final int _EVENT_INDICATOR_CFOU = 6;

	/**
	 * See Q.763 3.21 Event presentation restricted indicator (national use) :
	 * no indication
	 */
	public static final boolean _EVENT_PRESENTATION_INI = false;

	/**
	 * See Q.763 3.21 Event presentation restricted indicator (national use) :
	 * presentation restricted
	 */
	public static final boolean _EVENT_PRESENTATION_IPR = true;
	
	public int getEventIndicator();

	public void setEventIndicator(int eventIndicator) ;

	public boolean isEventPresentationRestrictedIndicator() ;

	public void setEventPresentationRestrictedIndicator(boolean eventPresentationRestrictedIndicator) ;

	
}
