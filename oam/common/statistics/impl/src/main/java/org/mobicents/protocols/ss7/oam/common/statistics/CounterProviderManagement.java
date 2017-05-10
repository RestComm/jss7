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

package org.mobicents.protocols.ss7.oam.common.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanHost;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterMediator;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterOutputFormat;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.SourceValueCounter;
import org.mobicents.protocols.ss7.oam.common.statistics.api.SourceValueObject;
import org.mobicents.protocols.ss7.oam.common.statistics.api.SourceValueSet;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CounterProviderManagement implements CounterProviderManagementMBean {
    protected final Logger logger;

    protected static final String COUNTER_PROVIDER_PERSIST_DIR_KEY = "counterprovider.persist.dir";
    protected static final String USER_DIR_KEY = "user.dir";
    protected static final String PERSIST_FILE_NAME = "CounterProvider.xml";
    private static final String CLASS_ATTRIBUTE = "type";
    private static final String TAB_INDENT = "\t";
    private static final String COUNTER_CAMPAIGNS = "counterCampaigns";
    private static final String COUNTER_CAMPAIGN = "counterCampaign";

    private static final XMLBinding binding = new XMLBinding();

    private final MBeanHost beanHost;

    private String name = "CounterHost";
    protected final TextBuilder persistFile = TextBuilder.newInstance();
    protected String persistDir = null;

    private boolean isStarted;
    private int minuteProcessed;
    private int secondProcessed;
    private final StatsPrinter statsPrinter;
    private final CsvStatsPrinter csvStatsPrinter;

    /**
     * A list of registered CounterMediator by there names
     */
    protected FastMap<String, CounterMediator> lstCounterMediator = new FastMap<String, CounterMediator>();
    /**
     * A list of registered CounterDefSet by names - values are corresponded CounterMediator's
     */
    private FastMap<String, CounterMediator> lstCounterDefSet = new FastMap<String, CounterMediator>();
    protected CounterCampaignMap<String, CounterCampaignImpl> lstCounterCampaign = new CounterCampaignMap<String, CounterCampaignImpl>();

    public CounterProviderManagement(MBeanHost beanHost) {
        this.beanHost = beanHost;

        binding.setClassAttribute(CLASS_ATTRIBUTE);
        binding.setAlias(CounterCampaignImpl.class, COUNTER_CAMPAIGN);
        binding.setAlias(String.class, "String");

        this.logger = Logger.getLogger(CounterProviderManagement.class.getCanonicalName() + "-" + this.name);

        this.statsPrinter = new StatsPrinter();
        this.csvStatsPrinter = new CsvStatsPrinter();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    public void start() {
        logger.info("Starting ...");

        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(COUNTER_PROVIDER_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("CounterManagement configuration file path %s", persistFile.toString()));

        this.lstCounterCampaign.clear();
        this.load();

        synchronized (this) {
            isStarted = true;

            lstCounterMediator.clear();
            lstCounterDefSet.clear();

            this.beanHost.registerMBean(CounterLayer.COUNTER, CounterManagementType.MANAGEMENT, this.name, this);

            secondProcessed = -1;
            minuteProcessed = -1;
            Thread t1 = new Thread(new CouterProcessingClass(true));
            t1.start();
            Thread t2 = new Thread(new CouterProcessingClass(false));
            t2.start();
        }

        logger.info("Started ...");
    }

    public void stop() {
        logger.info("Stopping ...");

        isStarted = false;

        logger.info("Stopped ...");
    }

    public void registerCounterMediator(CounterMediator val) {
        synchronized (this) {
            lstCounterMediator.put(val.getCounterMediatorName(), val);

            String[] ss = val.getCounterDefSetList();
            if (ss != null) {
                for (String s : ss) {
                    lstCounterDefSet.put(s, val);

                    for (CounterCampaignImpl cc : this.lstCounterCampaign.values()) {
                        if (cc.getCounterSetName().equals(s)) {
                            cc.setCounterSet(val.getCounterDefSet(s));
                        }
                    }
                }
            }

            logger.info("Registered CounterMediator: " + val.getCounterMediatorName());
        }
    }

    public void unRegisterCounterMediator(CounterMediator val) {
        synchronized (this) {
            lstCounterMediator.remove(val.getCounterMediatorName());

            ArrayList<String> toDel = new ArrayList<String>();
            for (String s : lstCounterDefSet.keySet()) {
                if (lstCounterDefSet.get(s).getCounterMediatorName().equals(val.getCounterMediatorName())) {
                    toDel.add(s);
                }
            }
            for (String s : toDel) {
                lstCounterDefSet.remove(s);
            }

            logger.info("Unregistered CounterMediator: " + val.getCounterMediatorName());
        }
    }

    @Override
    public String[] getCounterDefSetList() {
        String[] ress = new String[lstCounterDefSet.size()];
        int i1 = 0;
        synchronized (this) {
            for (String s : lstCounterDefSet.keySet()) {
                ress[i1++] = s;
            }
        }
        return ress;
    }

    @Override
    public CounterDefSet getCounterDefSet(String counterDefSetName) {
        synchronized (this) {
            CounterMediator cm = lstCounterDefSet.get(counterDefSetName);
            if (cm != null) {
                return cm.getCounterDefSet(counterDefSetName);
            }
        }
        return null;
    }

    @Override
    public void createCampaign(String campaignName, String counterSetName, int duration, int outputFormat) throws Exception {
        this.doCreateCampaign(campaignName, counterSetName, duration, false, outputFormat);
    }

    @Override
    public void createShortCampaign(String campaignName, String counterSetName, int duration, int outputFormat) throws Exception {
        this.doCreateCampaign(campaignName, counterSetName, duration, true, outputFormat);
    }

    private void doCreateCampaign(String campaignName, String counterSetName, int duration, boolean shortCampaign, int outputFormat)
            throws Exception {
        if (campaignName == null) {
            throw new Exception("Campaign Name cannot be null");
        }

        if (counterSetName == null) {
            throw new Exception("CounterSet Name cannot be null");
        }

        if (lstCounterCampaign.containsKey(campaignName))
            throw new Exception("Campaign " + campaignName + " already exists");

        if (duration != 5 && duration != 10 && duration != 15 && duration != 20 && duration != 30 && duration != 60)
            throw new Exception("Duration may be only 5, 10, 15, 20, 30 or 60 minutes/seconds");

        if (outputFormat != 0 && outputFormat != 1 && outputFormat != 2)
            throw new Exception("Output format may be only CSV, verbose or CSV and verbose");
        CounterOutputFormat counterOutputFormat = CounterOutputFormat.getInstance(outputFormat);

        CounterMediator cm = lstCounterDefSet.get(counterSetName);
        if (cm == null) {
            throw new Exception("CounterMediator is null for counterSetName=" + counterSetName);
        }

        synchronized (this) {
            CounterDefSet counterSet = cm.getCounterDefSet(counterSetName);
            CounterCampaignImpl camp = new CounterCampaignImpl(campaignName, counterSetName, counterSet, duration,
                    shortCampaign, counterOutputFormat);
            lstCounterCampaign.put(campaignName, camp);
            this.store();
        }

        if (logger.isInfoEnabled()) {
            logger.info("Created campaign: name=" + campaignName + ", counterSetName=" + counterSetName + ", duration="
                    + duration);
        }
    }

    @Override
    public void destroyCampaign(String campaignName) throws Exception {
        if (!lstCounterCampaign.containsKey(campaignName))
            throw new Exception("Campaign " + campaignName + " not found");

        synchronized (this) {
            lstCounterCampaign.remove(campaignName);
            this.store();
        }

        if (logger.isInfoEnabled()) {
            logger.info("Campaign destroyed: name=" + campaignName);
        }
    }

    @Override
    public String[] getCampaignsList() {
        synchronized (this) {
            String[] res = new String[lstCounterCampaign.size()];
            int i1 = 0;
            for (String s : lstCounterCampaign.keySet()) {
                res[i1++] = s;
            }
            return res;
        }
    }

    @Override
    public CounterCampaign getCampaign(String campaignName) {
        synchronized (this) {
            return lstCounterCampaign.get(campaignName);
        }
    }

    @Override
    public CounterValueSet getLastCounterValues(String campaignName) {
        synchronized (this) {
            CounterCampaign camp = lstCounterCampaign.get(campaignName);
            if (camp != null)
                return camp.getLastCounterValueSet();
            return null;
        }
    }

    protected void processCampaign(CounterCampaignImpl cc, Date endTime) {
        if (cc.isShortCampaign())
            logger.debug("Campaign processing: name=" + cc.getName());
        else
            logger.info("Campaign processing: name=" + cc.getName());

        try {
            CounterMediator cm = null;
            synchronized (this) {
                cm = lstCounterDefSet.get(cc.getCounterSetName());
            }
            if (cm != null) {
                SourceValueSet svs1 = cc.getLastSourceValueSet();
                CounterOutputFormat outputFormat = cc.getOutputFormat();
                if (outputFormat != null) {
                    switch (outputFormat) {
                        case CSV:
                            this.csvStatsPrinter.printCsvStats(cc);
                            break;
                        case VERBOSE:
                            this.statsPrinter.printStats(cc);
                            break;
                        case CSV_AND_VERBOSE:
                            this.statsPrinter.printStats(cc);
                            this.csvStatsPrinter.printCsvStats(cc);
                            break;
                    }
                } else {
                    logger.info("Output format not set for campaign: " + cc.getName() + ", using default");
                    this.statsPrinter.printStats(cc);
                }

                int durationInSeconds = cc.getDuration();
                if (!cc.isShortCampaign()) {
                    durationInSeconds *= 60;
                }
                SourceValueSet svs2 = cm.getSourceValueSet(cc.getCounterSetName(), cc.getName(), durationInSeconds);

                if (logger.isDebugEnabled()) {
                    logger.debug("svs1 = " + svs1 + " svs2 = " + svs2);
                }

                cc.setLastSourceValueSet(svs2);
                cc.setCounterValueSet(null);

                if (svs1 == null || svs2 == null || !svs1.getSessionId().equals(svs2.getSessionId())) {
                    return;
                }

                Date startTime;
                int duration;
                int durationSeconds;
                if (cc.isShortCampaign()) {
                    startTime = new Date(endTime.getTime() - cc.getDuration() * 1000);
                    duration = cc.getDuration();
                    durationSeconds = cc.getDuration() * 60;
                } else {
                    startTime = new Date(endTime.getTime() - cc.getDuration() * 60 * 1000);
                    duration = cc.getDuration();
                    durationSeconds = 0;
                }
                CounterValueSetImpl res = new CounterValueSetImpl(startTime, endTime, duration, durationSeconds);

                for (SourceValueCounter sv2 : svs2.getCounters().values()) {
                    CounterDef cd2 = sv2.getCounterDef();
                    SourceValueCounter sv1 = svs1.getCounters().get(sv2.getCounterDef().getCounterName());
                    if (sv1 == null)
                        continue;
                    CounterDef cd1 = sv1.getCounterDef();
                    if (cd1.getCounterType() != cd2.getCounterType())
                        continue;

                    for (SourceValueObject obj2 : sv2.getObjects().values()) {
                        SourceValueObject obj1 = sv1.getObjects().get(obj2.getObjectName());
                        if (obj1 == null
                                && (cd2.getCounterType() != CounterType.Minimal && cd2.getCounterType() != CounterType.Maximal && cd2
                                        .getCounterType() != CounterType.ComplexValue))
                            continue;

                        CounterValueImpl val = null;
                        switch (cd2.getCounterType()) {
                            case Summary_Cumulative:
                                long valr = obj2.getValue();
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), valr);
                                break;
                            case Summary:
                                valr = obj2.getValue() - obj1.getValue();
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), valr);
                                break;
                            case SummaryDouble:
                                double vald = obj2.getValueA() - obj1.getValueA();
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), 0);
                                val.setDoubleValue(vald);
                                break;
                            case Minimal:
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), obj2.getValue());
                                break;
                            case Maximal:
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), obj2.getValue());
                                break;
                            case Average:
                                double d1 = obj2.getValueA() - obj1.getValueA();
                                double d2 = obj2.getValueB() - obj1.getValueB();
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), 0);
                                if (d2 != 0) {
                                    val.setDoubleValue(d1 / d2);
                                }
                                break;
                            case ComplexValue:
                                val = new CounterValueImpl(cd2, obj2.getObjectName(), 0);
                                for (ComplexValue cv : obj2.getComplexValue()) {
                                    val.addComplexValue(cv);
                                }
                                break;
                        }

                        if (val != null)
                            res.addCounterValue(val);
                    }
                }

                cc.setCounterValueSet(res);
            } else {
                logger.error("CounterMediator not found for " + cc.getCounterSetName());
            }
        } catch (Throwable e) {
            logger.info("Exception when campaign processing: name=" + cc.getName() + " - " + e.getMessage(), e);
        }

        if (cc.isShortCampaign())
            logger.debug("Campaign processed: name=" + cc.getName());
        else
            logger.info("Campaign processed: name=" + cc.getName());
    }



    /**
     * Persist
     */
    public void store() {

        try {
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
            writer.setBinding(binding);
            writer.setIndentation(TAB_INDENT);

            writer.write(this.lstCounterCampaign, COUNTER_CAMPAIGNS, CounterCampaignMap.class);

            writer.close();
        } catch (Exception e) {
            logger.error("Error while persisting the CounterProvider state in file", e);
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
                XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

                reader.setBinding(binding);
                this.lstCounterCampaign = reader.read(COUNTER_CAMPAIGNS, CounterCampaignMap.class);

                reader.close();
            }
        } catch (XMLStreamException ex) {
            logger.error(String.format("Failed to load the CounterProvider configuration file. \n%s", ex.getMessage()));
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the CounterProvider configuration file. \n%s", e.getMessage()));
        } catch (IOException e) {
            logger.error(String.format("Failed to load the CounterProvider configuration file. \n%s", e.getMessage()));
        }
    }

    private class CouterProcessingClass implements Runnable {

        private boolean shortCampaign;

        public CouterProcessingClass(boolean shortCampaign) {
            this.shortCampaign = shortCampaign;
        }

        @Override
        public void run() {
            while (true) {
                if (!isStarted)
                    return;

                try {
                    Thread.sleep(shortCampaign ? 200 : 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // firstly we check if it a begin of minute 0-5-10-15-20-25-30-35-40-45-50-55
                Date dt = new Date();

                Date endTime;
                int minuteSecond;
                if (shortCampaign) {
                    int sc = dt.getSeconds();
                    if (sc != 0 && sc != 5 && sc != 10 && sc != 15 && sc != 20 && sc != 25 && sc != 30 && sc != 35 && sc != 40
                            && sc != 45 && sc != 50 && sc != 55)
                        continue;
                    if (sc == secondProcessed)
                        continue;
                    secondProcessed = sc;
                    minuteSecond = sc;
                    endTime = new Date(dt.getYear(), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes(),
                            dt.getSeconds());
                } else {
                    if (dt.getSeconds() > 30)
                        continue;

                    int mn = dt.getMinutes();
                    if (mn != 0 && mn != 5 && mn != 10 && mn != 15 && mn != 20 && mn != 25 && mn != 30 && mn != 35 && mn != 40
                            && mn != 45 && mn != 50 && mn != 55)
                        continue;
                    if (mn == minuteProcessed)
                        continue;
                    minuteProcessed = mn;
                    minuteSecond = mn;
                    endTime = new Date(dt.getYear(), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes(), 0);
                }

                // campaign list
                CounterCampaignImpl[] ccc;
                synchronized (this) {
                    ccc = new CounterCampaignImpl[lstCounterCampaign.size()];
                    int i1 = 0;
                    for (CounterCampaignImpl cc : lstCounterCampaign.values()) {
                        ccc[i1++] = cc;
                    }
                }

                for (CounterCampaignImpl cc : ccc) {
                    if (shortCampaign == cc.isShortCampaign() && minuteSecond % cc.getDuration() == 0) {
                        // we process only needed duration
                        processCampaign(cc, endTime);
                    }
                }
            }
        }
    }

    public enum CounterManagementType implements MBeanType {
        MANAGEMENT("Management");

        private final String name;

        public static final String NAME_MANAGEMENT = "Management";

        private CounterManagementType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static CounterManagementType getInstance(String name) {
            if (NAME_MANAGEMENT.equals(name)) {
                return MANAGEMENT;
            }

            return null;
        }
    }

}
