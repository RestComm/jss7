/**
 * Start time:13:03:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:03:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ForwardGVNS extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x4C;

	public OriginatingParticipatingServiceProvider getOpServiceProvider();

	public void setOpServiceProvider(OriginatingParticipatingServiceProvider opServiceProvider);

	public GVNSUserGroup getGvnsUserGroup();

	public void setGvnsUserGroup(GVNSUserGroup gvnsUserGroup);

	public TerminatingNetworkRoutingNumber getTnRoutingNumber();

	public void setTnRoutingNumber(TerminatingNetworkRoutingNumber tnRoutingNumber);
}
