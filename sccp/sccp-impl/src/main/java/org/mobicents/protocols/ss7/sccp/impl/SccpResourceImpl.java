/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpOAMMessage;

/**
 * @author amit bhayani
 */
public class SccpResourceImpl implements SccpResource {
    private static final Logger logger = Logger.getLogger(SccpResourceImpl.class);

    protected RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
    protected RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
    protected ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();

    private final String name;
    private String persistDir = null;
    protected final boolean rspProhibitedByDefault;
    private final PersistentStorage persistanceStorage = new PersistentStorage();

    public SccpResourceImpl(String name) {
        this(name, false);
    }

    public SccpResourceImpl(String name, boolean rspProhibitedByDefault) {
        this.name = name;
        this.rspProhibitedByDefault = rspProhibitedByDefault;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    public void start() {
        this.persistanceStorage.setPersistDir(this.persistDir, this.name);
        this.load();

        logger.info("Started Sccp Resource");
    }

    public void stop() {
        this.store();
    }

    protected void load() {
        PersistentStorage.ResourcesSet resources = this.persistanceStorage.load();
        if (resources == null) {
            return;
        }
        for (RemoteSignalingPointCode rsp : resources.remoteSpcs.values()) {
            ((RemoteSignalingPointCodeImpl) rsp).setProhibitedState(rspProhibitedByDefault, rspProhibitedByDefault);
        }
        this.remoteSpcs = resources.remoteSpcs;
        this.remoteSsns = resources.remoteSsns;
        this.concernedSpcs = resources.concernedSpcs;
    }

    protected synchronized void store() {
        this.persistanceStorage.store(remoteSpcs, remoteSsns, concernedSpcs);
    }

    public void addRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
                             boolean markProhibitedWhenSpcResuming) throws Exception {

        if (this.getRemoteSsn(remoteSsnid) != null) {
            throw new Exception(SccpOAMMessage.RSS_ALREADY_EXIST);
        }

        // TODO check if RemoteSignalingPointCode correspond to remoteSpc exist?

        RemoteSubSystemImpl rsscObj = new RemoteSubSystemImpl(remoteSpc, remoteSsn, remoteSsnFlag,
                markProhibitedWhenSpcResuming);

        synchronized (this) {
            RemoteSubSystemMap<Integer, RemoteSubSystem> newRemoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
            newRemoteSsns.putAll(this.remoteSsns);
            newRemoteSsns.put(remoteSsnid, rsscObj);
            this.remoteSsns = newRemoteSsns;
            this.store();
        }
    }

    public void modifyRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
                                boolean markProhibitedWhenSpcResuming) throws Exception {
        RemoteSubSystemImpl rsscObj = (RemoteSubSystemImpl) this.remoteSsns.get(remoteSsnid);
        if (rsscObj == null) {
            throw new Exception(String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.name));
        }

        // TODO check if RemoteSignalingPointCode correspond to remoteSpc
        // exist?

        synchronized (this) {
            rsscObj.setRemoteSsn(remoteSsn);
            rsscObj.setRemoteSpc(remoteSpc);
            rsscObj.setRemoteSsnFlag(remoteSsnFlag);
            rsscObj.setMarkProhibitedWhenSpcResuming(markProhibitedWhenSpcResuming);

            this.store();
        }
    }

    public void removeRemoteSsn(int remoteSsnid) throws Exception {

        if (this.getRemoteSsn(remoteSsnid) == null) {
            throw new Exception(String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.name));
        }

        synchronized (this) {
            RemoteSubSystemMap<Integer, RemoteSubSystem> newRemoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
            newRemoteSsns.putAll(this.remoteSsns);
            newRemoteSsns.remove(remoteSsnid);
            this.remoteSsns = newRemoteSsns;
            this.store();
        }
    }

    public RemoteSubSystem getRemoteSsn(int remoteSsnid) {
        return this.remoteSsns.get(remoteSsnid);
    }

    public RemoteSubSystem getRemoteSsn(int spc, int remoteSsn) {

        for (FastMap.Entry<Integer, RemoteSubSystem> e = this.remoteSsns.head(), end = this.remoteSsns.tail(); (e = e.getNext()) != end; ) {
            RemoteSubSystem remoteSubSystem = e.getValue();
            if (remoteSubSystem.getRemoteSpc() == spc && remoteSsn == remoteSubSystem.getRemoteSsn()) {
                return remoteSubSystem;
            }

        }
        return null;
    }

    public Map<Integer, RemoteSubSystem> getRemoteSsns() {
        Map<Integer, RemoteSubSystem> remoteSsnsTmp = new HashMap<Integer, RemoteSubSystem>();
        remoteSsnsTmp.putAll(remoteSsns);
        return remoteSsnsTmp;
    }

    public void addRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {

        if (this.getRemoteSpc(remoteSpcId) != null) {
            throw new Exception(SccpOAMMessage.RSPC_ALREADY_EXIST);
        }

        RemoteSignalingPointCodeImpl rspcObj = new RemoteSignalingPointCodeImpl(remoteSpc, remoteSpcFlag, mask, this.rspProhibitedByDefault);

        synchronized (this) {
            RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> newRemoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
            newRemoteSpcs.putAll(this.remoteSpcs);
            newRemoteSpcs.put(remoteSpcId, rspcObj);
            this.remoteSpcs = newRemoteSpcs;
            this.store();
        }
    }

    public void modifyRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {
        RemoteSignalingPointCodeImpl remoteSignalingPointCode = (RemoteSignalingPointCodeImpl) this.getRemoteSpc(remoteSpcId);
        if (remoteSignalingPointCode == null) {
            throw new Exception(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.name));
        }

        synchronized (this) {
            remoteSignalingPointCode.setRemoteSpc(remoteSpc);
            remoteSignalingPointCode.setRemoteSpcFlag(remoteSpcFlag);
            remoteSignalingPointCode.setMask(mask);

            this.store();
        }
    }

    public void removeRemoteSpc(int remoteSpcId) throws Exception {
        if (this.getRemoteSpc(remoteSpcId) == null) {
            throw new Exception(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.name));
        }

        synchronized (this) {
            RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> newRemoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
            newRemoteSpcs.putAll(this.remoteSpcs);
            newRemoteSpcs.remove(remoteSpcId);
            this.remoteSpcs = newRemoteSpcs;
            this.store();
        }
    }

    public RemoteSignalingPointCode getRemoteSpc(int remoteSpcId) {
        return this.remoteSpcs.get(remoteSpcId);
    }

    public RemoteSignalingPointCode getRemoteSpcByPC(int remotePC) {
        for (FastMap.Entry<Integer, RemoteSignalingPointCode> e = this.remoteSpcs.head(), end = this.remoteSpcs.tail(); (e = e
                .getNext()) != end; ) {
            RemoteSignalingPointCode remoteSignalingPointCode = e.getValue();
            if (remoteSignalingPointCode.getRemoteSpc() == remotePC) {
                return remoteSignalingPointCode;
            }

        }
        return null;
    }

    public Map<Integer, RemoteSignalingPointCode> getRemoteSpcs() {
        Map<Integer, RemoteSignalingPointCode> remoteSpcsTmp = new HashMap<Integer, RemoteSignalingPointCode>();
        remoteSpcsTmp.putAll(remoteSpcs);
        return remoteSpcsTmp;
    }

    public void addConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {

        if (this.getConcernedSpc(concernedSpcId) != null) {
            throw new Exception(SccpOAMMessage.CS_ALREADY_EXIST);
        }

        ConcernedSignalingPointCodeImpl concernedSpc = new ConcernedSignalingPointCodeImpl(remoteSpc);

        synchronized (this) {
            ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> newConcernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();
            newConcernedSpcs.putAll(this.concernedSpcs);
            newConcernedSpcs.put(concernedSpcId, concernedSpc);
            this.concernedSpcs = newConcernedSpcs;
            this.store();
        }
    }

    public void removeConcernedSpc(int concernedSpcId) throws Exception {

        if (this.getConcernedSpc(concernedSpcId) == null) {
            throw new Exception(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.name));
        }

        synchronized (this) {
            ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> newConcernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();
            newConcernedSpcs.putAll(this.concernedSpcs);
            newConcernedSpcs.remove(concernedSpcId);
            this.concernedSpcs = newConcernedSpcs;
            this.store();
        }
    }

    public void modifyConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {
        ConcernedSignalingPointCodeImpl concernedSignalingPointCode = (ConcernedSignalingPointCodeImpl) this
                .getConcernedSpc(concernedSpcId);

        if (concernedSignalingPointCode == null) {
            throw new Exception(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.name));
        }

        synchronized (this) {
            concernedSignalingPointCode.setRemoteSpc(remoteSpc);
            this.store();
        }

    }

    public ConcernedSignalingPointCode getConcernedSpc(int concernedSpcId) {
        return this.concernedSpcs.get(concernedSpcId);
    }

    public ConcernedSignalingPointCode getConcernedSpcByPC(int remotePC) {
        for (FastMap.Entry<Integer, ConcernedSignalingPointCode> e = this.concernedSpcs.head(), end = this.concernedSpcs.tail(); (e = e
                .getNext()) != end; ) {
            ConcernedSignalingPointCode concernedSubSystem = e.getValue();
            if (concernedSubSystem.getRemoteSpc() == remotePC) {
                return concernedSubSystem;
            }

        }
        return null;
    }

    public Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs() {
        Map<Integer, ConcernedSignalingPointCode> concernedSpcsTmp = new HashMap<Integer, ConcernedSignalingPointCode>();
        concernedSpcsTmp.putAll(concernedSpcs);
        return concernedSpcsTmp;
    }

    public void removeAllResourses() {

        synchronized (this) {
            if (this.remoteSsns.size() == 0 && this.remoteSpcs.size() == 0 && this.concernedSpcs.size() == 0)
                // no resources allocated - nothing to do
                return;

            remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
            remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
            concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();

            // We store the cleared state
            this.store();
        }
    }

    protected static class PersistentStorage {

        private static final Logger logger = Logger.getLogger(SccpResourceImpl.class);

        private static final String SCCP_RESOURCE_PERSIST_DIR_KEY = "sccpresource.persist.dir";
        private static final String USER_DIR_KEY = "user.dir";
        private static final String PERSIST_FILE_NAME = "sccpresource2.xml";

        private static final String REMOTE_SSN = "remoteSsns";
        private static final String REMOTE_SPC = "remoteSpcs";
        private static final String CONCERNED_SPC = "concernedSpcs";

        private final TextBuilder persistFile = TextBuilder.newInstance();

        protected static final SccpResourceXMLBinding binding = new SccpResourceXMLBinding();
        private static final String TAB_INDENT = "\t";
        private static final String CLASS_ATTRIBUTE = "type";

        protected PersistentStorage() {
            binding.setClassAttribute(CLASS_ATTRIBUTE);
            binding.setAlias(RemoteSubSystemImpl.class, "remoteSubSystem");
        }

        private void setPersistDir(String persistDir, String stackName) {
            this.persistFile.clear();

            if (persistDir != null) {
                this.persistFile.append(persistDir).append(File.separator).append(stackName).append("_").append(PERSIST_FILE_NAME);
            } else {
                persistFile.append(System.getProperty(SCCP_RESOURCE_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                        .append(File.separator).append(stackName).append("_").append(PERSIST_FILE_NAME);
            }

            logger.info(String.format("SCCP Resource configuration file path %s", persistFile.toString()));
        }

        /**
         * Persist
         *
         * @param remoteSpcs
         * @param remoteSsns
         * @param concernedSpcs
         */
        private void store(RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs, RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns, ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs) {

            // TODO : Should we keep reference to Objects rather than recreating
            // everytime?
            try {
                XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
                writer.setBinding(binding);
                // Enables cross-references.
                // writer.setReferenceResolver(new XMLReferenceResolver());
                writer.setIndentation(TAB_INDENT);
                writer.write(remoteSsns, REMOTE_SSN, RemoteSubSystemMap.class);
                writer.write(remoteSpcs, REMOTE_SPC, RemoteSignalingPointCodeMap.class);
                writer.write(concernedSpcs, CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);

                writer.close();
            } catch (Exception e) {
                this.logger.error("Error while persisting the Sccp Resource state in file", e);
            }
        }

        /**
         * Load and create LinkSets and Link from persisted file
         *
         * @throws Exception
         */
        protected ResourcesSet load() {
            ResourcesSet resources = null;
            try {
                File f = new File(persistFile.toString());
                if (f.exists()) {
                    // we have V3 config
                    resources = loadVer3(persistFile.toString());
                } else {
                    String s1 = persistFile.toString().replace("2.xml", ".xml");
                    f = new File(s1);
                    if (f.exists()) {
                        resources = loadVer1(s1);
                        if (resources == null) {
                            resources = loadVer2(s1);
                        }
                    }
                    f.delete();
                }
            } catch (XMLStreamException ex) {
                logger.error(String.format("Failed to load the SS7 configuration file. \n%s", ex.getMessage()));
            } catch (FileNotFoundException e) {
                logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
            } catch (IOException e) {
                logger.error(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
            }
            return resources;
        }

        private ResourcesSet loadVer1(String fn) throws XMLStreamException, IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s1 = br.readLine();
                if (s1 == null)
                    break;
                sb.append(s1);
                sb.append("\n");
            }
            br.close();
            String s2 = sb.toString();
            s2 = s2.replace("impl.RemoteSubSystem", "impl.RemoteSubSystemImpl");
            s2 = s2.replace("impl.RemoteSignalingPointCode", "impl.RemoteSignalingPointCodeImpl");
            s2 = s2.replace("impl.ConcernedSignalingPointCode", "impl.ConcernedSignalingPointCodeImpl");

            StringReader sr = new StringReader(s2);
            XMLObjectReader reader = XMLObjectReader.newInstance(sr);

            reader.setBinding(binding);

            String REMOTE_SSN_V1 = "remoteSsn";
            String REMOTE_SPC_V1 = "remoteSpc";
            String CONCERNED_SPC_V1 = "concernedSpc";
            XMLBinding binding2 = new XMLBinding();
            binding2.setClassAttribute(CLASS_ATTRIBUTE);

            FastMap<Integer, RemoteSubSystem> remoteSsnsX = reader.read(REMOTE_SSN_V1, FastMap.class);
            FastMap<Integer, RemoteSignalingPointCode> remoteSpcsX = reader.read(REMOTE_SPC_V1, FastMap.class);
            FastMap<Integer, ConcernedSignalingPointCode> concernedSpcsX = reader.read(CONCERNED_SPC_V1, FastMap.class);

            reader.close();

            if (remoteSsnsX == null)
                return null;

            RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
            RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
            ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();

            for (Integer id : remoteSsnsX.keySet()) {
                RemoteSubSystem val = remoteSsnsX.get(id);
                remoteSsns.put(id, val);
            }

            for (Integer id : remoteSpcsX.keySet()) {
                RemoteSignalingPointCode val = remoteSpcsX.get(id);
                remoteSpcs.put(id, val);
            }

            for (Integer id : concernedSpcsX.keySet()) {
                ConcernedSignalingPointCode val = concernedSpcsX.get(id);
                concernedSpcs.put(id, val);
            }

            return new ResourcesSet(remoteSpcs, remoteSsns, concernedSpcs);
        }

        private ResourcesSet loadVer2(String fn) throws XMLStreamException, FileNotFoundException {
            XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));

            reader.setBinding(binding);
            RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns = reader.read(REMOTE_SSN, RemoteSubSystemMap.class);
            RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs = reader.read(REMOTE_SPC, RemoteSignalingPointCodeMap.class);
            ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs = reader.read(CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);
            reader.close();

            return new ResourcesSet(remoteSpcs, remoteSsns, concernedSpcs);
        }

        protected ResourcesSet loadVer3(String fn) throws XMLStreamException, FileNotFoundException {
            XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));

            reader.setBinding(binding);
            return loadVer3(reader);
        }
        protected ResourcesSet loadVer3(XMLObjectReader reader) throws XMLStreamException{
            RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns = reader.read(REMOTE_SSN, RemoteSubSystemMap.class);
            RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs = reader.read(REMOTE_SPC, RemoteSignalingPointCodeMap.class);
            ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs = reader.read(CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);
            reader.close();

            return new ResourcesSet(remoteSpcs, remoteSsns, concernedSpcs);
        }

        static class ResourcesSet {
            final RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs;
            final RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns;
            final ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs;

            public ResourcesSet(RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs,
                                RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns,
                                ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs) {
                this.remoteSpcs = remoteSpcs;
                this.remoteSsns = remoteSsns;
                this.concernedSpcs = concernedSpcs;
            }
        }
    }
}
