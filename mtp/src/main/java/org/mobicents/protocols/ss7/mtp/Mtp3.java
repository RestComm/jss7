package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;
import java.util.List;

public interface Mtp3 {

	public void addMtp3Listener(Mtp3Listener lst);

	public void removeMtp3Listener(Mtp3Listener lst);

	/**
	 * Set implementation of Mtp1 I/O selector factory.
	 * 
	 * @param selectorFactory
	 */
	public void setSelectorFactory(SelectorFactory selectorFactory);

	public boolean send(byte[] msg);
	/**
	 * Sets Mtp2 links to be used as transport medium.
	 * @param channels
	 */
	public void setLinks(List<Mtp2> channels);//should it be add/remove?
	/**
	 * Assigns originated point code
	 * 
	 * @param opc
	 *            the originated point code
	 */
	public void setOpc(int opc);

	/**
	 * Assigns destination point code.
	 * 
	 * @param dpc
	 *            destination point code in decimal format.
	 */
	public void setDpc(int dpc);

	/**
	 * @return the dpc
	 */
	public int getDpc();

	/**
	 * @return the opc
	 */
	public int getOpc();
	
	
	
    public void start() throws IOException ;

    public void stop() throws IOException ;
}
