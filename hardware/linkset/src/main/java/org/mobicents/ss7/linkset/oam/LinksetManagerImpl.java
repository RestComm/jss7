/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.linkset.oam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.scheduler.Scheduler;

/**
 * <p>
 * LinksetManager is responsible for managing {@link Linkset} and {@link Link}.
 * </p>
 * <p>
 * For every successful operation {@link #activateLinkset(String[] activateLinkset), {@link #deactivateLinkset(String[])
 * deactivateLinkset}, {@link #activateLink(String[]) activateLink}, {@link #deactivateLink(String[]) deactivateLink},
 * {@link #createLinkset(String[]) createLinkset}, {@link #createLink(String[]) createLink}, {@link #deleteLinkset(String[])
 * deleteLinkset}, {@link #deleteLink(String[]) deleteLink} LinksetManager will serialize the linkset's and link's managed. The
 * path for serialized file follows the rule as below
 * </p>
 * <p>
 * LinksetManager when {@link #start() started} looks for file <tt>linksetmanager.xml</tt> containing serialized information of
 * underlying linksets and links. Set the directory path by calling {@link #setPersistDir(String)} to direct LinksetManager to
 * look at specified directory for underlying serialized file.
 * </p>
 * <p>
 * If directory path is not set, LinksetManager searches for system property <tt>linkset.persist.dir</tt> to get the path for
 * directory
 * </p>
 *
 * <p>
 * Even if <tt>linkset.persist.dir</tt> system property is not set, LinksetManager will look at property <tt>user.dir</tt>
 * </p>
 *
 * <p>
 * For every new linkset created successfully, LinksetManager calls {@link Layer4#add(Linkset)}
 * </p>
 *
 * @author amit bhayani
 */
public class LinksetManagerImpl implements LinksetManager {

    private static final Logger logger = Logger.getLogger(LinksetManagerImpl.class);

    private static final String LINKSET = "linkset";
    private static final String LINK = "link";
    private static final String CLASS_ATTRIBUTE = "type";

    private static final String LINKSET_PERSIST_DIR_KEY = "linkset.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String PERSIST_FILE_NAME = "linksetmanager.xml";

    private static final String TAB_INDENT = "\t";

    private static final XMLBinding binding = new XMLBinding();

    private final TextBuilder persistFile = TextBuilder.newInstance();

    private String persistDir = null;

    // Hold LinkSet here. LinkSet's name as key and actual LinkSet as Object
    private FastMap<String, Linkset> linksets = new FastMap<String, Linkset>().shared();

    private LinksetFactoryFactory linksetFactoryFactory = null;

    private Layer4 layer4 = null;

    private Scheduler scheduler;

    private String name;

    public LinksetManagerImpl(String name) {
        this.name = name;
        binding.setAlias(Linkset.class, LINKSET);
        binding.setAlias(Link.class, LINK);
        binding.setClassAttribute(CLASS_ATTRIBUTE);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public LinksetFactoryFactory getLinksetFactoryFactory() {
        return linksetFactoryFactory;
    }

    @Override
    public void setLinksetFactoryFactory(LinksetFactoryFactory linksetFactoryFactory) {
        this.linksetFactoryFactory = linksetFactoryFactory;
    }

    @Override
    public FastMap<String, Linkset> getLinksets() {
        return linksets;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    public Layer4 getLayer4() {
        return layer4;
    }

    public void setLayer4(Layer4 layer4) {
        this.layer4 = layer4;
    }

    public void start() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(LINKSET_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("SS7 configuration file path %s", persistFile.toString()));

        try {
            this.load();
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        }

        logger.info("Started LinksetManager");
    }

    public void stop() {
        this.store();
    }

    /**
     * Operations
     */
    public String showLinkset(String[] options) throws Exception {
        StringBuffer sb = new StringBuffer();
        FastMap.Entry<String, Linkset> e = this.linksets.head();
        FastMap.Entry<String, Linkset> end = this.linksets.tail();
        for (; (e = e.getNext()) != end;) {
            Linkset linkset = e.getValue();
            linkset.print(sb, 0, 4);
            sb.append(FormatterHelp.NEW_LINE);
        }

        if (sb.length() == 0)
            sb.append(" ");

        System.out.println(sb.toString());

        return sb.toString();
    }

    /**
     * <p>
     * Expected command is "linkset create <linkset-type> <options> <linkset-name>". linkset is
     * {@link org.mobicents.ss7.management.console.Subject}
     * </p>
     * <p>
     * For example for creation of dahdi linkset, the command is : linkset create dahdi opc 1 apc 2 ni 3 linkset1
     * </p>
     *
     * @param options
     * @return
     */
    public String createLinkset(String[] options) throws Exception {

        Linkset linkset = null;

        linkset = this.linksetFactoryFactory.createLinkset(options);

        if (linkset == null) {
            return "Invalid Command";
        }

        // TODO huh, we parse first and then check for name :(
        if (this.linksets.containsKey(linkset.getName())) {
            return LinkOAMMessages.LINKSET_ALREADY_EXIST;
        }

        this.linksets.put(linkset.getName(), linkset);
        this.store();

        if (this.layer4 != null) {
            this.layer4.add(linkset);
        }
        return LinkOAMMessages.LINKSET_SUCCESSFULLY_ADDED;
    }

    /**
     * Expected command is "linkset delete <linkset-name>"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String deleteLinkset(String[] options) throws Exception {
        String linksetName = options[(options.length - 1)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        if (linkset.getState() == LinksetState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.CANT_DELETE_LINKSET);
        }

        this.linksets.remove(linksetName);

        this.store();

        if (this.layer4 != null) {
            this.layer4.remove(linkset);
        }

        return LinkOAMMessages.LINKSET_SUCCESSFULLY_REMOVED;
    }

    /**
     * Expected command is "linkset activate linkset1"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String activateLinkset(String[] options) throws Exception {
        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The last option is Linkset name
        String linksetName = options[(options.length - 1)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        linkset.activate();

        return LinkOAMMessages.ACTIVATE_LINKSET_SUCCESSFULLY;
    }

    /**
     * Expected command is "linkset deactivate linkset1"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String deactivateLinkset(String[] options) throws Exception {

        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The second last option is Linkset name
        String linksetName = options[(options.length - 1)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        linkset.deactivate();

        return LinkOAMMessages.ACTIVATE_LINK_SUCCESSFULLY;
    }

    /**
     * <p>
     * Create new Link. The expected command is "linkset link create <options> linkset1
     * link1" where linkset1 is the linkset name and "link1" is link name to be created and associated with "linkset1". The
     * <options> depends on type of link to be created
     * </p>
     * <p>
     * For example to create link for Dahdi linkset type is : linkset link create span 1 code 1 channel 1 linkset1 link1
     * </p>
     *
     * @param options
     * @return
     */
    public String createLink(String[] options) throws Exception {
        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The second last option is Linkset name
        String linksetName = options[(options.length - 2)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        linkset.createLink(options);

        this.store();

        return LinkOAMMessages.LINK_SUCCESSFULLY_ADDED;
    }

    /**
     * Expected command is "linkset link delete linkset1 link1"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String deleteLink(String[] options) throws Exception {
        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The second last option is Linkset name
        String linksetName = options[(options.length - 2)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        // Last is Link name
        linkset.deleteLink(options[(options.length - 1)]);

        this.store();

        return LinkOAMMessages.LINK_SUCCESSFULLY_REMOVED;
    }

    /**
     * Expected command is "linkset link activate linkset1 link1"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String activateLink(String[] options) throws Exception {

        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The second last option is Linkset name
        String linksetName = options[(options.length - 2)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        String linkName = options[(options.length - 1)];
        if (linkName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        linkset.activateLink(linkName);

        return LinkOAMMessages.ACTIVATE_LINK_SUCCESSFULLY;
    }

    /**
     * Expected command is "linkset link deactivate linkset1 link1"
     *
     * @param options
     * @return
     * @throws Exception
     */
    public String deactivateLink(String[] options) throws Exception {

        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The second last option is Linkset name
        String linksetName = options[(options.length - 2)];

        if (linksetName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        Linkset linkset = this.linksets.get(linksetName);

        if (linkset == null) {
            throw new Exception(LinkOAMMessages.LINKSET_DOESNT_EXIST);
        }

        String linkName = options[(options.length - 1)];
        if (linkName == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        linkset.deactivateLink(linkName);

        return LinkOAMMessages.ACTIVATE_LINK_SUCCESSFULLY;
    }

    /**
     * Persist
     */
    protected void store() {
        linksetFactoryFactory.loadBinding(binding);
        // TODO : Should we keep reference to Objects rather than recreating
        // everytime?
        try {
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
            writer.setBinding(binding);
            // Enables cross-references.
            // writer.setReferenceResolver(new XMLReferenceResolver());
            writer.setIndentation(TAB_INDENT);

            for (FastMap.Entry<String, Linkset> e = this.linksets.head(), end = this.linksets.tail(); (e = e.getNext()) != end;) {
                Linkset value = e.getValue();
                writer.write(value);
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
    protected void load() throws FileNotFoundException {
        linksetFactoryFactory.loadBinding(binding);

        XMLObjectReader reader = null;
        try {
            reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

            reader.setBinding(binding);
            // reader.setReferenceResolver(new XMLReferenceResolver());

            // FIXME : Bug in .hasNext()
            // http://markmail.org/message/c6lsehxlxv2hua5p. It shouldn't throw
            // Exception
            while (reader.hasNext()) {
                Linkset linkset = reader.read();
                linkset.setScheduler(scheduler);
                linkset.activateLinks();
                try {
                    linkset.activate();
                } catch (Exception e) {
                    // possibly already activated
                }

                this.linksets.put(linkset.getName(), linkset);
            }
        } catch (Exception ex) {
            // this.logger.info(
            // "Error while re-creating Linksets from persisted file", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        LinksetManagerImpl linkSetManager = new LinksetManagerImpl("LinksetManager");

        LinksetFactoryFactory linksetFactoryFactory = new LinksetFactoryFactory();
        // linksetFactoryFactory.addFactory(new DahdiLinksetFactory());
        // linksetFactoryFactory.addFactory(new DialogicLinksetFactory());
        // linksetFactoryFactory.addFactory(new M3UALinksetFactory());

        linkSetManager.setLinksetFactoryFactory(linksetFactoryFactory);

        linkSetManager.createLinkset("dahdi opc 1 dpc 2 ni 3 linkset1".split(" "));
        linkSetManager.store();

        LinksetManagerImpl linkSetManager1 = new LinksetManagerImpl("LinksetManager");
        linkSetManager1.load();

        System.out.println(linkSetManager1.linksets.size());
    }

    @Override
    public String getName() {
        return this.name;
    }
}