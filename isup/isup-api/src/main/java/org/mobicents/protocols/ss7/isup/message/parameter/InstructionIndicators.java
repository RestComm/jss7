/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:13:18:50 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:18:50 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InstructionIndicators extends ISUPParameter {
	//FIXME: fill this!
	public static final int _PARAMETER_CODE = 0;
	
	/**
	 * See Q.763 3.41 Transit at intermediate exchange indicator : transit
	 * interpretation
	 */
	public static final boolean _TI_TRANSIT_INTEPRETATION = false;
	/**
	 * See Q.763 3.41 Transit at intermediate exchange indicator :
	 */
	public static final boolean _TI_ETE_INTEPRETATION = true;
	/**
	 * See Q.763 3.41 Release call indicator : do not release
	 */
	public static final boolean _RCI_DO_NOT_RELEASE = false;
	/**
	 * See Q.763 3.41 Release call indicator : reelase call
	 */
	public static final boolean _RCI_RELEASE = true;

	/**
	 * See Q.763 3.41 Discard message indicator : do not discard message (pass
	 * on)
	 */
	public static final boolean _DMI_DO_NOT_DISCARD = false;
	/**
	 * See Q.763 3.41 Discard message indicator : discard message
	 */
	public static final boolean _DMI_DISCARD = true;

	/**
	 * See Q.763 3.41 Discard parameter indicator : do not discard parameter
	 * (pass on)
	 */
	public static final boolean _DPI_DO_NOT_DISCARD = false;
	/**
	 * See Q.763 3.41 Discard parameter indicator : discard parameter
	 */
	public static final boolean _DPI_INDICATOR_DISCARD = true;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : release call
	 */
	public static final int _PONPI_RELEASE_CALL = 0;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : discard message
	 */
	public static final int _PONPI_DISCARD_MESSAGE = 1;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : discard parameter
	 */
	public static final int _PONPI_DISCARD_PARAMETER = 2;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : pass on
	 */
	public static final int _BII_PASS_ON = 0;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : discard
	 * message
	 */
	public static final int _BII_DISCARD_MESSAGE = 1;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : release call
	 */
	public static final int _BII_RELEASE_CALL = 2;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : discard
	 * parameter
	 */
	public static final int _BII_DISCARD_PARAMETER = 3;
	
	public boolean isTransitAtIntermediateExchangeIndicator();

	public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator);

	public boolean isReleaseCallindicator();

	public void setReleaseCallindicator(boolean releaseCallindicator);

	public boolean isSendNotificationIndicator();

	public void setSendNotificationIndicator(boolean sendNotificationIndicator);

	public boolean isDiscardMessageIndicator();

	public void setDiscardMessageIndicator(boolean discardMessageIndicator);

	public boolean isDiscardParameterIndicator();

	public void setDiscardParameterIndicator(boolean discardParameterIndicator);

	public int getPassOnNotPossibleIndicator();

	public void setPassOnNotPossibleIndicator(int passOnNotPossibleIndicator2);

	public int getBandInterworkingIndicator();

	public void setBandInterworkingIndicator(int bandInterworkingIndicator);

	public boolean isSecondOctetPresenet();

	public void setSecondOctetPresenet(boolean secondOctetPresenet);

	public byte[] getRaw();

	public void setRaw(byte[] raw);

	public boolean isUseAsRaw();

	public void setUseAsRaw(boolean useAsRaw);

}
