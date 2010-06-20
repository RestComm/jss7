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
public final class Mtp2Factory {

	private static final Mtp2Factory _INSTANCE = new Mtp2Factory();
	/**
	 * 
	 */
	private Mtp2Factory() {
		// TODO Auto-generated constructor stub
	}

	public static Mtp2Factory getInstance()
	{
		return _INSTANCE;
	}
	
	public List<Mtp2> createMtpLinkSet(List<Mtp1> channels,String name)
	{
		Mtp1 xxx = null;
		if(channels == null)
		{
			throw new NullPointerException("Argument must not be null.");
		}
		if(channels.contains(xxx))
		{
			throw new IllegalArgumentException("Channel must not be null! Index: "+channels.indexOf(xxx));
		}
		
		//not more than 16 allowed?
		if(channels.size() >16)
		{
			throw new IllegalArgumentException("To many links in to fit to one set!");
		}
		
		List<Mtp2> result = new ArrayList<Mtp2>();
		for(int sls = 0;sls<channels.size();sls++)
		{
			Mtp1 mtp1 = channels.get(sls);
			Mtp2 mtp2 = new Mtp2Impl(name+"-"+sls, sls);
			mtp2.setLayer1(mtp1);
			result.add(mtp2);
		}
		
		return result;
	}
	
}
