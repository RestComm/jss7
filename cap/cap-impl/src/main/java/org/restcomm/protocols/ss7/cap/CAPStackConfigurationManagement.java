package org.restcomm.protocols.ss7.cap;

import javolution.text.TextBuilder;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CAPStackConfigurationManagement {
    private static final String PERSIST_FILE_NAME = "management.xml";
    private static final String CAP_MANAGEMENT_PERSIST_DIR_KEY = "capmanagement.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String TAB_INDENT = "\t";
    private static final String DEFAULT_CONFIG_FILE_NAME = "CapStack";

    private static final String TIMER_CIRCUITSWITCHEDCALLCONTROL_SHORT = "timercircuitswitchedcallcontrolshort";
    private static final String TIMER_CIRCUITSWITCHEDCALLCONTROL_MEDIUM = "timercircuitswitchedcallcontrolmedium";
    private static final String TIMER_CIRCUITSWITCHEDCALLCONTROL_LONG = "timercircuitswitchedcallcontrollong";
    private static final String TIMER_SMS_SHORT = "timersmsshort";
    private static final String TIMER_GPRS_SHORT = "timergprsshort";

    private static final XMLBinding binding = new XMLBinding();
    private static CAPStackConfigurationManagement instance = new CAPStackConfigurationManagement();

    private final TextBuilder persistFile = TextBuilder.newInstance();
    private String configFileName = DEFAULT_CONFIG_FILE_NAME;
    private String persistDir = null;

    private int _Timer_CircuitSwitchedCallControl_Short = 6000; // 1 - 10 sec
    private int _Timer_CircuitSwitchedCallControl_Medium = 30000; // 1 - 60 sec
    private int _Timer_CircuitSwitchedCallControl_Long = 300000; // 1 s - 30 minutes
    private int _Timer_Sms_Short = 10000; // 1 - 20 sec
    private int _Timer_Gprs_Short = 10000; // 1 - 20 sec

    private CAPStackConfigurationManagement() {
    }

    public static CAPStackConfigurationManagement getInstance() {
        return instance;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
        this.setPersistFile();
    }

    private void setPersistFile() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.configFileName).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(CAP_MANAGEMENT_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY))).append(File.separator).append(this.configFileName)
                    .append("_").append(PERSIST_FILE_NAME);
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
            writer.setIndentation(TAB_INDENT);

            writer.write(this._Timer_CircuitSwitchedCallControl_Short, TIMER_CIRCUITSWITCHEDCALLCONTROL_SHORT, Integer.class);
            writer.write(this._Timer_CircuitSwitchedCallControl_Medium, TIMER_CIRCUITSWITCHEDCALLCONTROL_MEDIUM, Integer.class);
            writer.write(this._Timer_CircuitSwitchedCallControl_Long, TIMER_CIRCUITSWITCHEDCALLCONTROL_LONG, Integer.class);
            writer.write(this._Timer_Sms_Short, TIMER_SMS_SHORT, Integer.class);
            writer.write(this._Timer_Gprs_Short, TIMER_GPRS_SHORT, Integer.class);

            writer.close();
        } catch (Exception e) {
            System.err.println(String.format("Error while persisting the CAP Resource state in file=%s", persistFile.toString()));
            e.printStackTrace();
        }
    }

    /**
     * Load and create LinkSets and Link from persisted file
     * <p>
     * load() is called from CAPStackImpl
     */
    public void load() {
        try {
            setPersistFile();
            XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));
            reader.setBinding(binding);
            load(reader);
        } catch (XMLStreamException | FileNotFoundException e) {
            System.err.println(String.format("Error while load the CAP Resource state from file=%s", persistFile.toString()));
            e.printStackTrace();
        }
    }

    private void load(XMLObjectReader reader) throws XMLStreamException {
        Integer val = reader.read(TIMER_CIRCUITSWITCHEDCALLCONTROL_SHORT, Integer.class);
        if (val != null)
            this._Timer_CircuitSwitchedCallControl_Short = val;

        val = reader.read(TIMER_CIRCUITSWITCHEDCALLCONTROL_MEDIUM, Integer.class);
        if (val != null)
            this._Timer_CircuitSwitchedCallControl_Medium = val;

        val = reader.read(TIMER_CIRCUITSWITCHEDCALLCONTROL_LONG, Integer.class);
        if (val != null)
            this._Timer_CircuitSwitchedCallControl_Long = val;

        val = reader.read(TIMER_SMS_SHORT, Integer.class);
        if (val != null)
            this._Timer_Sms_Short = val;

        val = reader.read(TIMER_GPRS_SHORT, Integer.class);
        if (val != null)
            this._Timer_Gprs_Short = val;

        reader.close();
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public int getTimerCircuitSwitchedCallControlShort() {
        return _Timer_CircuitSwitchedCallControl_Short;
    }

    public int getTimerCircuitSwitchedCallControlMedium() {
        return _Timer_CircuitSwitchedCallControl_Medium;
    }

    public int getTimerCircuitSwitchedCallControlLong() {
        return _Timer_CircuitSwitchedCallControl_Long;
    }

    public int getTimerSmsShort() {
        return _Timer_Sms_Short;
    }

    public int getTimerGprsShort() {
        return _Timer_Gprs_Short;
    }

    public void set_Timer_CircuitSwitchedCallControl_Short(int _Timer_CircuitSwitchedCallControl_Short) {
        this._Timer_CircuitSwitchedCallControl_Short = _Timer_CircuitSwitchedCallControl_Short;
        this.store();
    }

    public void set_Timer_CircuitSwitchedCallControl_Medium(int _Timer_CircuitSwitchedCallControl_Medium) {
        this._Timer_CircuitSwitchedCallControl_Medium = _Timer_CircuitSwitchedCallControl_Medium;
        this.store();
    }

    public void set_Timer_CircuitSwitchedCallControl_Long(int _Timer_CircuitSwitchedCallControl_Long) {
        this._Timer_CircuitSwitchedCallControl_Long = _Timer_CircuitSwitchedCallControl_Long;
        this.store();
    }

    public void set_Timer_Sms_Short(int _Timer_Sms_Short) {
        this._Timer_Sms_Short = _Timer_Sms_Short;
        this.store();
    }

    public void set_Timer_Gprs_Short(int _Timer_Gprs_Short) {
        this._Timer_Gprs_Short = _Timer_Gprs_Short;
        this.store();
    }
}
