package org.mobicents.protocols.ss7.mtp;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */

/**
 * @author baranowb
 *
 */
public final class Mtp3Factory {

	private static final Mtp3Factory _INSTANCE = new Mtp3Factory();
	/**
	 * 
	 */
	private Mtp3Factory() {
		// TODO Auto-generated constructor stub
	}

	public static Mtp3Factory getInstance()
	{
		return _INSTANCE;
	}
	
	public Mtp3 createMtp(List<Mtp2> linkset,String name, int opc, int dpc, SelectorFactory selectorFactory)
	{
		Mtp2 xxx = null;
		if(linkset == null)
		{
			throw new NullPointerException("Argument must not be null.");
		}
		if(linkset.contains(xxx))
		{
			throw new IllegalArgumentException("Channel must not be null! Index: "+linkset.indexOf(xxx));
		}
		
		//not more than 16 allowed?
		if(linkset.size() >16)
		{
			throw new IllegalArgumentException("To many links in to fit to one set!");
		}
		Mtp3 mtp3 = new Mtp3Impl(name);
		mtp3.setDpc(dpc);
		mtp3.setOpc(opc);
		mtp3.setSelectorFactory(selectorFactory);
		mtp3.setLinks(linkset);
		
		
		return mtp3;
	}
	
}
