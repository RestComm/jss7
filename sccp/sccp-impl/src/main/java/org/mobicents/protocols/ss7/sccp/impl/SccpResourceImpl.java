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
 *
 * @author amit bhayani
 *
 */
public class SccpResourceImpl implements SccpResource {
    private static final Logger logger = Logger.getLogger(SccpResourceImpl.class);

    private static final String SCCP_RESOURCE_PERSIST_DIR_KEY = "sccpresource.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String PERSIST_FILE_NAME = "sccpresource2.xml";

    private static final String REMOTE_SSN = "remoteSsns";
    private static final String REMOTE_SPC = "remoteSpcs";
    private static final String CONCERNED_SPC = "concernedSpcs";

    private final TextBuilder persistFile = TextBuilder.newInstance();

    private static final SccpResourceXMLBinding binding = new SccpResourceXMLBinding();
    private static final String TAB_INDENT = "\t";
    private static final String CLASS_ATTRIBUTE = "type";

    private String persistDir = null;

    protected RemoteSubSystemMap<Integer, RemoteSubSystem> remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystem>();
    private RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode> remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCode>();
    protected ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode> concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCode>();

    private final String name;

    public SccpResourceImpl(String name) {
        this.name = name;
        binding.setClassAttribute(CLASS_ATTRIBUTE);
        binding.setAlias(RemoteSubSystemImpl.class, "remoteSubSystem");
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    public void start() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(SCCP_RESOURCE_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("SCCP Resource configuration file path %s", persistFile.toString()));

        this.load();

        logger.info("Started Sccp Resource");
    }

    public void stop() {
        this.store();
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
            throw new Exception(SccpOAMMessage.RSS_DOESNT_EXIST);
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
            throw new Exception(SccpOAMMessage.RSS_DOESNT_EXIST);
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

        for (FastMap.Entry<Integer, RemoteSubSystem> e = this.remoteSsns.head(), end = this.remoteSsns.tail(); (e = e.getNext()) != end;) {
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

        RemoteSignalingPointCodeImpl rspcObj = new RemoteSignalingPointCodeImpl(remoteSpc, remoteSpcFlag, mask);

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
            throw new Exception(SccpOAMMessage.RSPC_DOESNT_EXIST);
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
            throw new Exception(SccpOAMMessage.RSPC_DOESNT_EXIST);
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
                .getNext()) != end;) {
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
            throw new Exception(SccpOAMMessage.CS_DOESNT_EXIST);
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
            throw new Exception(SccpOAMMessage.CS_DOESNT_EXIST);
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
                .getNext()) != end;) {
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

    /**
     * Persist
     */
    public void store() {

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
    private void load() {

        try {
            File f = new File(persistFile.toString());
            if (f.exists()) {
                // we have V3 config
                loadVer3(persistFile.toString());
            } else {
                String s1 = persistFile.toString().replace("2.xml", ".xml");
                f = new File(s1);

                if (f.exists()) {
                    if (!loadVer1(s1)) {
                        loadVer2(s1);
                    }
                }

                this.store();
                f.delete();
            }
        } catch (XMLStreamException ex) {
            logger.error(String.format("Failed to load the SS7 configuration file. \n%s", ex.getMessage()));
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        } catch (IOException e) {
            logger.error(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        }
    }

    private boolean loadVer1(String fn) throws XMLStreamException, IOException {
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
            return false;

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

        return true;
    }

    private void loadVer2(String fn) throws XMLStreamException, FileNotFoundException {
        XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));

        reader.setBinding(binding);
        remoteSsns = reader.read(REMOTE_SSN, RemoteSubSystemMap.class);
        remoteSpcs = reader.read(REMOTE_SPC, RemoteSignalingPointCodeMap.class);
        concernedSpcs = reader.read(CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);

        reader.close();
    }

    private void loadVer3(String fn) throws XMLStreamException, FileNotFoundException {
        XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));

        reader.setBinding(binding);
        remoteSsns = reader.read(REMOTE_SSN, RemoteSubSystemMap.class);
        remoteSpcs = reader.read(REMOTE_SPC, RemoteSignalingPointCodeMap.class);
        concernedSpcs = reader.read(CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);

        reader.close();
    }
}
