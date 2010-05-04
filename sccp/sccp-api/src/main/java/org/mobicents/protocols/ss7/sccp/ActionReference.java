/**
 * 
 */
package org.mobicents.protocols.ss7.sccp;

/**
 * Tmp interface - this interface is defined to allow Dialogic like communication with mtp3 layer - dialogic expects fully formed Mtp3 message in byte[].
 * So we need to create and retain back route header for incoming data!.
 * @author baranowb
 *
 */
public interface ActionReference {
	public byte[] getBackRouteHeader();
}
