package org.mobicents.protocols.ss7.mtp.oam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

/**
 * 
 * @author amit bhayani
 * 
 */
public class LinksetManager {

	private static final Logger logger = Logger.getLogger(LinksetManager.class);

	private static final String LINKSET = "linkset";
	private static final String LINK = "link";
	private static final String CLASS_ATTRIBUTE = "type";

	private static final String LINKSET_PERSIST_DIR_KEY = "linkset.persist.dir";
	private static final String USER_DIR_KEY = "user.dir";
	private static final String PERSIST_FILE_NAME = "linksetmanager.xml";

	private static final String TAB_INDENT = "\t";

	private static final XMLBinding binding = new XMLBinding();

	private final TextBuilder persistDir = TextBuilder.newInstance();

	// Hold LinkSet here. LinkSet's name as key and actual LinkSet as Object
	private FastMap<TextBuilder, Linkset> linksets = new FastMap<TextBuilder, Linkset>();

	// Hold the LinkSetFactory here
	private FastList<LinksetFactory> linksetFactories = new FastList<LinksetFactory>();

	public LinksetManager() {
		persistDir.append(
				System.getProperty(LINKSET_PERSIST_DIR_KEY, System
						.getProperty(USER_DIR_KEY))).append(File.separator)
				.append(PERSIST_FILE_NAME);

		logger.info(String.format(
				"SS7 configuration file will be persisted at %s", logger
						.toString()));

		binding.setAlias(Linkset.class, LINKSET);
		binding.setAlias(Link.class, LINK);
		binding.setClassAttribute(CLASS_ATTRIBUTE);
	}

	/**
	 * LinkSetFactories
	 */
	public FastList<LinksetFactory> getLinksetFactories() {
		return linksetFactories;
	}

	public void setLinksetFactories(FastList<LinksetFactory> linksetFactories) {
		this.linksetFactories = linksetFactories;
	}
	
	public FastMap<TextBuilder, Linkset> getLinksets() {
		return linksets;
	}

	/**
	 * Operations
	 */
	public void showLinkset(boolean statistics, TextBuilder linksetName,
			ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	public void shutdownLinkset(TextBuilder linksetName, ByteBuffer byteBuffer) {
		// TODO
	}

	public void shutdownLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	public void noshutdownLinkset(TextBuilder linksetName, ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.noShutdown(byteBuffer)) {
			this.store();
			byteBuffer.put(ShellMessages.NOSHUTDOWN_LINKSET_SUCCESSFULLY);
			return;
		}

		byteBuffer.put(ShellMessages.NOSHUTDOWN_LINKSET_FAILED);
	}

	public void noshutdownLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		Link link = linkset.getLink(linkName);

		if (link == null) {
			byteBuffer.put(ShellMessages.LINK_DOESNT_EXIST);
			return;
		}

		if (link.noShutdown(byteBuffer)) {
			this.store();
			byteBuffer.put(ShellMessages.NOSHUTDOWN_LINK_SUCCESSFULLY);
			return;
		}

		byteBuffer.put(ShellMessages.NOSHUTDOWN_LINK_FAILED);

	}

	public Linkset addLinkset(TextBuilder linksetName, int linksetType,
			ByteBuffer byteBuffer) {
		Linkset linkset = null;
		if (linksets.containsKey(linksetName)) {
			byteBuffer.put(ShellMessages.LINKSET_ALREADY_EXIST);
			return linkset;
		}

		for (FastList.Node<LinksetFactory> n = linksetFactories.head(), end = linksetFactories
				.tail(); (n = n.getNext()) != end;) {
			LinksetFactory linksetFactory = n.getValue();
			if (linksetFactory.getType() == linksetType) {
				linkset = linksetFactory.createLinkSet(linksetName);
				this.linksets.put(linkset.getLinkSetName(), linkset);

				byteBuffer.put(ShellMessages.LINKSET_SUCCESSFULLY_ADDED);

				this.store();

				return linkset;
			}
		}

		byteBuffer.put(ShellMessages.INCORRECT_LINKSET_TYPE);
		return linkset;

	}

	public void deleteLinkset(TextBuilder linksetName, ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	public void deleteLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	public void networkIndicator(TextBuilder linksetName, int networkInd,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linkset == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}
		linkset.setNi(networkInd);
		this.store();
		byteBuffer.put(ShellMessages.NETWORK_INDICATOR_SUCCESSFULLY_SET);
	}

	public void localPointCode(TextBuilder linksetName, int opc,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		linkset.setOpc(opc);
		this.store();
		byteBuffer.put(ShellMessages.OPC_SUCCESSFULLY_SET);
	}

	public void adjacentPointCode(TextBuilder linksetName, int adjacentPC,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		linkset.setDpc(adjacentPC);
		this.store();
		byteBuffer.put(ShellMessages.APC_SUCCESSFULLY_SET);
	}

	public void localIpPort(TextBuilder linksetName, TextBuilder localIp,
			int localPort, ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.getType() == LinksetType.M3UA) {
			((M3UALinkset) linkset).setLocalAddress(localIp);
			((M3UALinkset) linkset).setLocalPort(localPort);
			this.store();
			byteBuffer.put(ShellMessages.LOCAL_IP_PORT_SUCCESSFULLY_SET);
			return;
		}
		byteBuffer.put(ShellMessages.LINKSET_NOT_M3UA);
	}

	public void addLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linkset == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.addLink(linkName, byteBuffer)) {
			this.store();
			byteBuffer.put(ShellMessages.LINK_SUCCESSFULLY_ADDED);
			return;
		}

		byteBuffer.put(ShellMessages.LINK_ADD_FAILED);
	}

	public void span(TextBuilder linksetName, TextBuilder linkName, int span,
			ByteBuffer byteBuffer) {

		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.getType() == LinksetType.DAHDI) {
			Link link = linkset.getLink(linkName);

			if (link == null) {
				byteBuffer.put(ShellMessages.LINK_DOESNT_EXIST);
				return;
			}
			((DahdiLink) link).setSpan(span);
			this.store();
			byteBuffer.put(ShellMessages.LINK_SPAN_SUCCESSFULL);
		} else {
			byteBuffer.put(ShellMessages.LINKSET_NOT_DAHDI);
		}

	}

	public void channel(TextBuilder linksetName, TextBuilder linkName,
			int channel, ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.getType() == LinksetType.DAHDI) {
			Link link = linkset.getLink(linkName);

			if (link == null) {
				byteBuffer.put(ShellMessages.LINK_DOESNT_EXIST);
				return;
			}
			((DahdiLink) link).setChannelID(channel);
			this.store();
			byteBuffer.put(ShellMessages.LINK_CHANNEL_SUCCESSFULL);
		} else {
			byteBuffer.put(ShellMessages.LINKSET_NOT_DAHDI);
		}
	}

	public void code(TextBuilder linksetName, TextBuilder linkName, int code,
			ByteBuffer byteBuffer) {
		Linkset linkset = linksets.get(linksetName);

		if (linksets == null) {
			byteBuffer.put(ShellMessages.LINKSET_DOESNT_EXIST);
			return;
		}

		if (linkset.getType() == LinksetType.DAHDI) {
			Link link = linkset.getLink(linkName);

			if (link == null) {
				byteBuffer.put(ShellMessages.LINK_DOESNT_EXIST);
				return;
			}
			((DahdiLink) link).setCode(code);
			this.store();
			byteBuffer.put(ShellMessages.LINK_CODE_SUCCESSFULL);
		} else {
			byteBuffer.put(ShellMessages.LINKSET_NOT_DAHDI);
		}

	}

	public void inhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer) {
		// TODO
		byteBuffer.put(ShellMessages.NOT_IMPLEMENTED);
	}

	/**
	 * Persist
	 */
	public void store() {

		// TODO : Should we keep reference to Objects rather than recreating
		// everytime?
		try {
			XMLObjectWriter writer = XMLObjectWriter
					.newInstance(new FileOutputStream(persistDir.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);

			for (FastMap.Entry<TextBuilder, Linkset> e = this.linksets.head(), end = this.linksets
					.tail(); (e = e.getNext()) != end;) {
				Linkset value = e.getValue();
				writer.write(value, LINKSET, value.getClass().getName());
			}

			writer.close();
		} catch (Exception e) {
			this.logger.error("Error while persisting the state in file", e);
		}
	}

	/**
	 * Load and create LinkSets and Link from persisted file
	 * 
	 * @throws Exception
	 */
	public void load() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(persistDir
					.toString()));

			reader.setBinding(binding);
			// reader.setReferenceResolver(new XMLReferenceResolver());

			// FIXME : Bug in .hasNext()
			// http://markmail.org/message/c6lsehxlxv2hua5p. It shouldn't throw
			// Exception
			while (reader.hasNext()) {
				Linkset linkset = reader.read(LINKSET);
				this.linksets.put(linkset.getLinkSetName(), linkset);
			}
		} catch (XMLStreamException ex) {
//			this.logger.info(
//					"Error while re-creating Linksets from persisted file", ex);
		}
	}

	public static void main(String args[]) throws Exception {
		ByteBuffer byteBuffer = ByteBuffer.allocate(128);
		LinksetManager linkSetManager = new LinksetManager();

		FastList<LinksetFactory> linkSetFactories = new FastList<LinksetFactory>();
		linkSetFactories.add(new DahdiLinksetFactory());
		linkSetFactories.add(new DialogicLinksetFactory());
		linkSetFactories.add(new M3UALinksetFactory());

		linkSetManager.setLinksetFactories(linkSetFactories);

		linkSetManager.addLinkset(new TextBuilder("LinkSet1"), 105, byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();
		linkSetManager.addLinkset(new TextBuilder("LinkSet2"), 106, byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();

		linkSetManager.addLink(new TextBuilder("LinkSet1"), new TextBuilder(
				"Link1"), byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();

		linkSetManager.addLink(new TextBuilder("LinkSet1"), new TextBuilder(
				"Link2"), byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();

		linkSetManager.addLink(new TextBuilder("LinkSet1"), new TextBuilder(
				"Link3"), byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();

		linkSetManager.addLinkset(new TextBuilder("LinkSet3"), 107, byteBuffer);
		System.out.println(new String(byteBuffer.array()));
		byteBuffer.clear();

		linkSetManager.store();

		LinksetManager linkSetManager1 = new LinksetManager();
		linkSetManager1.load();

		System.out.println(linkSetManager1.linksets.size());
	}

}
