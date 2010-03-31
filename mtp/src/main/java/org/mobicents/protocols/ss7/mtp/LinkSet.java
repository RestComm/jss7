package org.mobicents.protocols.ss7.mtp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LinkSet implements Mtp2Listener{

	private String linksetName = "DEFAULT_LINKSET";
	private byte linkSetID = Utils._VALUE_NOT_SET;
	/**
	 * This is list of links in linkset. Max links is 16(0-15 sls). Index in
	 * this array equals SLS assigned to link. it is desired that links have SLS
	 * assgined from 0. This array MUST not be modified.
	 */
	private ArrayList<Mtp2> links = new ArrayList(16);
	private ArrayList<Mtp2> activeLinks = new ArrayList(16);
	private transient Mtp2 activatingLink;

	
	/////////
	// PCs //
	/////////
	private int opc;
	private int dpc;
        
	public LinkSet(String linksetName, byte linkSetID,int opc, int dpc, List<Mtp2> linksList) {
		super();
		this.linksetName = linksetName;
		this.linkSetID = linkSetID;
		this.opc = opc;
		this.dpc = dpc;
		if (linksList.size() > 16) {
			throw new IllegalArgumentException(
					"Linkset can have only up to 16 links!");
		}

		for (Mtp2 mtp2 : linksList) {
			//FIXME: add this check
			//if (this.links.get(mtp2.getSls()) != null) {
			//	throw new IllegalArgumentException("There is link in this["
			//			+ this.linksetName + "] linkset with the same SLS["
			//			+ mtp2.getSls() + "]");
			//}
			//.... this is really a bug...
			// ArrayList.add(0,x) will throw exception when nothing is there ;[
			//this.links.add(mtp2.getSls(), mtp2); bullshit java contract...
			//this.links.add(mtp2.getSls(), mtp2);
			this.links.add(mtp2);
			mtp2.setLinkSet(this);
			Collections.sort(this.links, new Mtp2Comparator());
		}

	}

	public void threadTick(long thisTickStamp) {
		for(Mtp2 mtp2:this.links)
		{
			mtp2.threadTick(thisTickStamp);
		}

	}

	public ArrayList<Mtp2> getLinks() {
		// TODO Auto-generated method stub
		return this.links;
	}

	public ArrayList<Mtp2> getActiveLinks() {
		return this.activeLinks;
	}

	public void activateLink() {
		// search for link.
		if (activatingLink == null)
			for (Mtp2 mtp2 : this.links) {
				if (mtp2.getState() == Mtp2.MTP2_OUT_OF_SERVICE
						&& !mtp2.isT17()) {
					mtp2.start_T17();
					activatingLink = mtp2;
					break;
				}
			}

	}

	public void linkFailed(Mtp2 mtp2) {
		// dont use index, cause it can be diff.
		this.activeLinks.remove(mtp2);
		if(this.activatingLink == mtp2)
		{
			this.activatingLink = null;
		}
		
	}

	public void linkInService(Mtp2 mtp2) {
		this.activeLinks.add(mtp2.getSls(), mtp2);
		//check if its the same?
		if(this.activatingLink == mtp2)
		{
			this.activatingLink = null;
		}
		
	}
	
	public String getName() {
		return this.linksetName;
	}
	
	public byte getId() {
		return this.linkSetID;
	}

	public int getOpc()
	{
		return this.opc;
	}

	public int getDpc()
	{
		return this.dpc;
	}
	public void onMessage(int sio, byte[] sif, Mtp2 mtp2) {
		//this actualy is not used, mtp3 takes care of it.
		
	}

	public void start()
	{
		if(this.linkSetID == Utils._VALUE_NOT_SET)
		{
			throw new IllegalStateException("Linkset ID MUST be set.");
		}
	}
	
	private class Mtp2Comparator implements Comparator<Mtp2>
	{

		public int compare(Mtp2 o1, Mtp2 o2) {
			// TODO Auto-generated method stub
			if(o2 == null)
			{
				return 1;
			}else if(o1 == null)
			{
				return -1;
			}else
			{
				return o1.getSls()-o2.getSls();
			}
			
		}
		
	}
	
}
