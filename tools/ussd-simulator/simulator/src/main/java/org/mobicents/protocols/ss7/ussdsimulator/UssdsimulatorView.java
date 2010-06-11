/*
 * UssdsimulatorView.java
 */

package org.mobicents.protocols.ss7.ussdsimulator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.impl.sctp.SccpSCTPProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;
import org.mobicents.protocols.ss7.ussdsimulator.mtp.USSDSimultorMtpProvider;

/**
 * The application's main frame.
 */
public class UssdsimulatorView extends FrameView implements MAPDialogListener,MAPServiceListener{

    public UssdsimulatorView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
              //  statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
       // statusAnimationLabel.setIcon(idleIcon);
       // progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                       // statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                   // progressBar.setVisible(true);
                   // progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                   // statusAnimationLabel.setIcon(idleIcon);
                   // progressBar.setVisible(false);
                   // progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                   // statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(false);
                    //progressBar.setValue(value);
                }
            }
        });

        //stupid net beans, can do that from GUI
        _field_peer_ip.setText("127.0.0.1");

        enableKeyPad(false);

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = UssdsimulatorApp.getApplication().getMainFrame();
            aboutBox = new UssdsimulatorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        UssdsimulatorApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        cell_keypad_master_panel = new javax.swing.JPanel();
        cell_keypad_call_buttons_panel = new javax.swing.JPanel();
        _keypad_button_call = new javax.swing.JButton();
        _keypad_button_break = new javax.swing.JButton();
        _keypad_button_1 = new javax.swing.JButton();
        _keypad_button_2 = new javax.swing.JButton();
        _keypad_button_3 = new javax.swing.JButton();
        _keypad_button_4 = new javax.swing.JButton();
        _keypad_button_5 = new javax.swing.JButton();
        _keypad_button_6 = new javax.swing.JButton();
        _keypad_button_7 = new javax.swing.JButton();
        _keypad_button_8 = new javax.swing.JButton();
        _keypad_button_9 = new javax.swing.JButton();
        _keypad_button_star = new javax.swing.JButton();
        _keypad_button_0 = new javax.swing.JButton();
        _keypad_button_hash = new javax.swing.JButton();
        _field_result_display = new java.awt.TextArea();
        _field_punch_display = new java.awt.TextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        _label_peer_IP = new javax.swing.JLabel();
        _label_peer_port = new javax.swing.JLabel();
        _field_peer_port = new javax.swing.JTextField();
        _button_open_server = new javax.swing.JButton();
        _button_close_server = new javax.swing.JButton();
        _field_peer_ip = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.mobicents.protocols.ss7.ussdsimulator.UssdsimulatorApp.class).getContext().getResourceMap(UssdsimulatorView.class);
        cell_keypad_master_panel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, resourceMap.getColor("cell_keypad_master_panel.border.matteColor"))); // NOI18N
        cell_keypad_master_panel.setName("cell_keypad_master_panel"); // NOI18N

        cell_keypad_call_buttons_panel.setName("cell_keypad_call_buttons_panel"); // NOI18N

        _keypad_button_call.setText(resourceMap.getString("_keypad_button_call.text")); // NOI18N
        _keypad_button_call.setName("_keypad_button_call"); // NOI18N
        _keypad_button_call.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_callActionPerformed(evt);
            }
        });

        _keypad_button_break.setText(resourceMap.getString("_keypad_button_break.text")); // NOI18N
        _keypad_button_break.setName("_keypad_button_break"); // NOI18N
        _keypad_button_break.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_breakActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout cell_keypad_call_buttons_panelLayout = new org.jdesktop.layout.GroupLayout(cell_keypad_call_buttons_panel);
        cell_keypad_call_buttons_panel.setLayout(cell_keypad_call_buttons_panelLayout);
        cell_keypad_call_buttons_panelLayout.setHorizontalGroup(
            cell_keypad_call_buttons_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cell_keypad_call_buttons_panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(_keypad_button_call)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE)
                .add(_keypad_button_break)
                .addContainerGap())
        );
        cell_keypad_call_buttons_panelLayout.setVerticalGroup(
            cell_keypad_call_buttons_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, cell_keypad_call_buttons_panelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(cell_keypad_call_buttons_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(_keypad_button_call)
                    .add(_keypad_button_break))
                .addContainerGap())
        );

        _keypad_button_1.setText(resourceMap.getString("_keypad_button_1.text")); // NOI18N
        _keypad_button_1.setName("_keypad_button_1"); // NOI18N
        _keypad_button_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_1ActionPerformed(evt);
            }
        });

        _keypad_button_2.setText(resourceMap.getString("_keypad_button_2.text")); // NOI18N
        _keypad_button_2.setName("_keypad_button_2"); // NOI18N
        _keypad_button_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_2ActionPerformed(evt);
            }
        });

        _keypad_button_3.setText(resourceMap.getString("_keypad_button_3.text")); // NOI18N
        _keypad_button_3.setName("_keypad_button_3"); // NOI18N
        _keypad_button_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_3ActionPerformed(evt);
            }
        });

        _keypad_button_4.setText(resourceMap.getString("_keypad_button_4.text")); // NOI18N
        _keypad_button_4.setName("_keypad_button_4"); // NOI18N
        _keypad_button_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_4ActionPerformed(evt);
            }
        });

        _keypad_button_5.setText(resourceMap.getString("_keypad_button_5.text")); // NOI18N
        _keypad_button_5.setName("_keypad_button_5"); // NOI18N
        _keypad_button_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_5ActionPerformed(evt);
            }
        });

        _keypad_button_6.setText(resourceMap.getString("_keypad_button_6.text")); // NOI18N
        _keypad_button_6.setName("_keypad_button_6"); // NOI18N
        _keypad_button_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_6ActionPerformed(evt);
            }
        });

        _keypad_button_7.setText(resourceMap.getString("_keypad_button_7.text")); // NOI18N
        _keypad_button_7.setName("_keypad_button_7"); // NOI18N
        _keypad_button_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_7ActionPerformed(evt);
            }
        });

        _keypad_button_8.setText(resourceMap.getString("_keypad_button_8.text")); // NOI18N
        _keypad_button_8.setName("_keypad_button_8"); // NOI18N
        _keypad_button_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_8ActionPerformed(evt);
            }
        });

        _keypad_button_9.setText(resourceMap.getString("_keypad_button_9.text")); // NOI18N
        _keypad_button_9.setName("_keypad_button_9"); // NOI18N
        _keypad_button_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_9ActionPerformed(evt);
            }
        });

        _keypad_button_star.setText(resourceMap.getString("_keypad_button_star.text")); // NOI18N
        _keypad_button_star.setName("_keypad_button_star"); // NOI18N
        _keypad_button_star.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_starActionPerformed(evt);
            }
        });

        _keypad_button_0.setText(resourceMap.getString("_keypad_button_0.text")); // NOI18N
        _keypad_button_0.setName("_keypad_button_0"); // NOI18N
        _keypad_button_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_0ActionPerformed(evt);
            }
        });

        _keypad_button_hash.setText(resourceMap.getString("_keypad_button_hash.text")); // NOI18N
        _keypad_button_hash.setName("_keypad_button_hash"); // NOI18N
        _keypad_button_hash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _keypad_button_hashActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout cell_keypad_master_panelLayout = new org.jdesktop.layout.GroupLayout(cell_keypad_master_panel);
        cell_keypad_master_panel.setLayout(cell_keypad_master_panelLayout);
        cell_keypad_master_panelLayout.setHorizontalGroup(
            cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cell_keypad_master_panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(cell_keypad_master_panelLayout.createSequentialGroup()
                        .add(_keypad_button_4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(cell_keypad_master_panelLayout.createSequentialGroup()
                        .add(_keypad_button_7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(cell_keypad_master_panelLayout.createSequentialGroup()
                        .add(_keypad_button_star)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_0)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_hash))
                    .add(cell_keypad_call_buttons_panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cell_keypad_master_panelLayout.createSequentialGroup()
                        .add(_keypad_button_1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(_keypad_button_3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cell_keypad_master_panelLayout.setVerticalGroup(
            cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cell_keypad_master_panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(cell_keypad_call_buttons_panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(_keypad_button_1)
                    .add(_keypad_button_2)
                    .add(_keypad_button_3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(_keypad_button_4)
                    .add(_keypad_button_5)
                    .add(_keypad_button_6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(_keypad_button_7)
                    .add(_keypad_button_8)
                    .add(_keypad_button_9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cell_keypad_master_panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(_keypad_button_star)
                    .add(_keypad_button_0)
                    .add(_keypad_button_hash))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        _field_result_display.setEditable(false);
        _field_result_display.setName("_field_result_display"); // NOI18N

        _field_punch_display.setName("_field_punch_display"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N

        _label_peer_IP.setText(resourceMap.getString("_label_peer_IP.text")); // NOI18N
        _label_peer_IP.setName("_label_peer_IP"); // NOI18N

        _label_peer_port.setText(resourceMap.getString("_label_peer_port.text")); // NOI18N
        _label_peer_port.setName("_label_peer_port"); // NOI18N

        _field_peer_port.setText(resourceMap.getString("_field_peer_port.text")); // NOI18N
        _field_peer_port.setName("_field_peer_port"); // NOI18N

        _button_open_server.setText(resourceMap.getString("_button_open_server.text")); // NOI18N
        _button_open_server.setName("_button_open_server"); // NOI18N
        _button_open_server.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _button_open_serverActionPerformed(evt);
            }
        });

        _button_close_server.setText(resourceMap.getString("_button_close_server.text")); // NOI18N
        _button_close_server.setEnabled(false);
        _button_close_server.setName("_button_close_server"); // NOI18N
        _button_close_server.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _button_close_serverActionPerformed(evt);
            }
        });

        _field_peer_ip.setText(resourceMap.getString("_field_peer_ip.text")); // NOI18N
        _field_peer_ip.setName("_field_peer_ip"); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(_field_result_display, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(_field_punch_display, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cell_keypad_master_panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                                .add(_label_peer_port)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(_field_peer_port))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                                .add(_label_peer_IP)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(_field_peer_ip, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(_button_close_server, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(_button_open_server, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE))
                .add(103, 103, 103))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(cell_keypad_master_panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(80, 80, 80)
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(mainPanelLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(_field_punch_display, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 175, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(_field_result_display, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 370, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(7, 7, 7)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(_label_peer_IP)
                        .add(_field_peer_ip, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(_button_open_server))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(_label_peer_port)
                        .add(_field_peer_port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(_button_close_server))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.mobicents.protocols.ss7.ussdsimulator.UssdsimulatorApp.class).getContext().getActionMap(UssdsimulatorView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void _button_open_serverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__button_open_serverActionPerformed
       this._button_close_server.setEnabled(true);
       this._button_open_server.setEnabled(false);

       this.initSS7Future = this._EXECUTOR.schedule(new Runnable() {

            public void run() {
                try {
                    initSS7();
                } catch (Exception ex) {
                    Logger.getLogger(UssdsimulatorView.class.getName()).log(Level.SEVERE, null, ex);
                    resetServer();
                    _field_punch_display.setText("Failed to initiate connection.");
                    onlyKeyPadContent = false;
                }

                if(!mtpLayer.isLinkUp())
                {
                    resetServer();
                    _field_punch_display.setText("Failed to initiate connection.");
                    onlyKeyPadContent = false;
                }else
                {
                    _field_punch_display.setText("Connected to RA.");
                    onlyKeyPadContent = false;
                   
                    //init some fake addressses
                    //create some fake addresses.
		peer1Address =  sccpProvider.getSccpParameterFactory().getSccpAddress();
		GlobalTitle gt = sccpProvider.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5557779");
		peer1Address.setGlobalTitle(gt);
                peer1Address.setGlobalTitleIndicator(4);//for GT 100

		peer2Address = sccpProvider.getSccpParameterFactory().getSccpAddress();
		gt = sccpProvider.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5888879");
		peer2Address.setGlobalTitle(gt);
                peer2Address.setGlobalTitleIndicator(4);//for GT 100

                //map/ussd part
                mapUssdAppContext = MAPApplicationContext.networkUnstructuredSsContextV2;

                //fake data again
                //FIXME: Amit add proper comments.
                orgiReference = mapStack.getMAPProvider().getMapServiceFactory()
				.createAddressString(AddressNature.international_number,
						NumberingPlan.ISDN, "31628968300");
		destReference = mapStack.getMAPProvider().getMapServiceFactory()
				.createAddressString(AddressNature.international_number,
						NumberingPlan.land_mobile, "204208300008002");

                //we are done, lets enable keypad
                enableKeyPad(true);
                }
            }
        },0,TimeUnit.MICROSECONDS);
       
    }//GEN-LAST:event__button_open_serverActionPerformed

    private void _button_close_serverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__button_close_serverActionPerformed
     
       this.resetServer();
    }//GEN-LAST:event__button_close_serverActionPerformed

    private void _keypad_button_callActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_callActionPerformed

            //here we make map call to peer :)
            MAPProvider mapProvider = this.mapStack.getMAPProvider();
            //here, if no dialog exists its initial call :)
            String punchedText = this._field_punch_display.getText();
            if (punchedText == null || punchedText.equals("")) {
                return;
            }
            USSDString ussdString = mapProvider.getMapServiceFactory().createUSSDString(punchedText);
            if (this.clientDialog == null) {
                try {
                    this.clientDialog = mapProvider.createNewDialog(this.mapUssdAppContext, this.peer1Address, orgiReference, this.peer2Address, destReference);
                } catch (MAPException ex) {
                    Logger.getLogger(UssdsimulatorView.class.getName()).log(Level.SEVERE, null, ex);
                    this._field_punch_display.setText("Failed to create MAP dialog: " + ex);
                    this.onlyKeyPadContent = false;
                    return;
                }
            }
        try {
            AddressString msisdn = this.mapStack.getMAPProvider().getMapServiceFactory()
		.createAddressString(AddressNature.international_number,
				NumberingPlan.ISDN, "31628838002");
            clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString,msisdn);
            clientDialog.send();
            this._field_punch_display.setText("");
            this._keypad_button_break.setEnabled(true);
            this._field_result_display.append("\n");
            this._field_result_display.append(punchedText);
        } catch (MAPException ex) {
            Logger.getLogger(UssdsimulatorView.class.getName()).log(Level.SEVERE, null, ex);
             this._field_punch_display.setText("Failed to pass USSD request: " + ex);
             this.onlyKeyPadContent = false;
             return;
        }
    }//GEN-LAST:event__keypad_button_callActionPerformed

    private void _keypad_button_breakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_breakActionPerformed
       //this is set once call should end
        if(this.clientDialog!=null)
        {
            try {
                this.clientDialog.close(true);
            } catch (MAPException ex) {
                Logger.getLogger(UssdsimulatorView.class.getName()).log(Level.SEVERE, null, ex);
                this._field_punch_display.setText("Failed to close MAP Dialog: " + ex);
                this.onlyKeyPadContent = false;

            }
            this._keypad_button_break.setEnabled(false);
        }
    }//GEN-LAST:event__keypad_button_breakActionPerformed

    private void _keypad_button_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_1ActionPerformed
       this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_1ActionPerformed

    private void _keypad_button_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_2ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_2ActionPerformed

    private void _keypad_button_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_3ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_3ActionPerformed

    private void _keypad_button_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_4ActionPerformed
       this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_4ActionPerformed

    private void _keypad_button_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_5ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_5ActionPerformed

    private void _keypad_button_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_6ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_6ActionPerformed

    private void _keypad_button_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_7ActionPerformed
       this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_7ActionPerformed

    private void _keypad_button_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_8ActionPerformed
       this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_8ActionPerformed

    private void _keypad_button_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_9ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_9ActionPerformed

    private void _keypad_button_starActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_starActionPerformed
       this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_starActionPerformed

    private void _keypad_button_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_0ActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_0ActionPerformed

    private void _keypad_button_hashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__keypad_button_hashActionPerformed
        this.keypadDigitPressed(evt);
    }//GEN-LAST:event__keypad_button_hashActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton _button_close_server;
    private javax.swing.JButton _button_open_server;
    private javax.swing.JTextField _field_peer_ip;
    private javax.swing.JTextField _field_peer_port;
    private java.awt.TextArea _field_punch_display;
    private java.awt.TextArea _field_result_display;
    private javax.swing.JButton _keypad_button_0;
    private javax.swing.JButton _keypad_button_1;
    private javax.swing.JButton _keypad_button_2;
    private javax.swing.JButton _keypad_button_3;
    private javax.swing.JButton _keypad_button_4;
    private javax.swing.JButton _keypad_button_5;
    private javax.swing.JButton _keypad_button_6;
    private javax.swing.JButton _keypad_button_7;
    private javax.swing.JButton _keypad_button_8;
    private javax.swing.JButton _keypad_button_9;
    private javax.swing.JButton _keypad_button_break;
    private javax.swing.JButton _keypad_button_call;
    private javax.swing.JButton _keypad_button_hash;
    private javax.swing.JButton _keypad_button_star;
    private javax.swing.JLabel _label_peer_IP;
    private javax.swing.JLabel _label_peer_port;
    private javax.swing.JPanel cell_keypad_call_buttons_panel;
    private javax.swing.JPanel cell_keypad_master_panel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private void enableKeyPad(boolean b) {

        this._keypad_button_1.setEnabled(b);
        this._keypad_button_2.setEnabled(b);
        this._keypad_button_3.setEnabled(b);
        this._keypad_button_4.setEnabled(b);
        this._keypad_button_5.setEnabled(b);
        this._keypad_button_6.setEnabled(b);
        this._keypad_button_7.setEnabled(b);
        this._keypad_button_8.setEnabled(b);
        this._keypad_button_9.setEnabled(b);
        this._keypad_button_0.setEnabled(b);
        this._keypad_button_star.setEnabled(b);
        this._keypad_button_hash.setEnabled(b);

        this._keypad_button_call.setEnabled(b);
        this._keypad_button_break.setEnabled(false);



    }

    private void keypadDigitPressed(ActionEvent evt) {
        if(!this.onlyKeyPadContent)
        {
            //clear
            this._field_punch_display.setText("");
            this.onlyKeyPadContent = true;
        }
       this._field_punch_display.append(evt.getActionCommand());
    }

    /////////////////
    // async stuff //
    /////////////////
    private ScheduledExecutorService _EXECUTOR = Executors.newScheduledThreadPool(2);
    private Future initSS7Future;

    /////////////////
    // State stuff //
    /////////////////
    private boolean onlyKeyPadContent = false;
    private SccpAddress peer1Address,peer2Address;
    private MAPApplicationContext mapUssdAppContext;
    private AddressString orgiReference,destReference;

    private MAPDialog clientDialog;
    //////////////
    // SS7 part //
    //////////////

    private Properties stackProperties;
//    private TCAPStack tcapStack;
//    private TCAPProvider tcapProvider;
    //this sucks...
    private SccpSCTPProviderImpl sccpProvider;
    private MAPStack mapStack;
    //this we have to create
    private USSDSimultorMtpProvider mtpLayer;

    //method to init stacks
    private void initSS7() throws Exception
    {
        this.stackProperties = new Properties();
        //fake values;
        this.stackProperties.put("sccp.opc", "13150");
        this.stackProperties.put("sccp.dpc", "31510");
        this.stackProperties.put("sccp.sls", "0");
        this.stackProperties.put("sccp.ssi", "3");


        mtpLayer = new USSDSimultorMtpProvider(_field_peer_ip, _field_peer_port);
        mtpLayer.start();
        long startTime = System.currentTimeMillis();
        while(!mtpLayer.isLinkUp())
        {
            Thread.currentThread().sleep(5000);
            if(startTime+20000<System.currentTimeMillis())
            {
                break;
            }
        }

        if(!this.mtpLayer.isLinkUp())
        {
            throw new StartFailedException();
        }
        this.sccpProvider = new SccpSCTPProviderImpl(this.mtpLayer, stackProperties);
        this.mapStack = new MAPStackImpl(sccpProvider);
        this.mapStack.getMAPProvider().addMAPDialogListener(this);
        this.mapStack.getMAPProvider().addMAPServiceListener(this);
        //indicate linkup, since we mockup now, its done by hand.
        //this method is called by M3UserConnector!
        this.sccpProvider.linkUp();
    }


    //FIXME: add proper dialog kill

    private void terminateSS7()
    {
        if(this.mtpLayer!=null)
        {
            this.mtpLayer.stop();
            //this.mtpLayer = null;
        }
    }
    private void resetServer()
    {
        if(this.initSS7Future!=null)
        {
            this.initSS7Future.cancel(false);
            this.initSS7Future = null;
        }
        terminateSS7();
        _button_close_server.setEnabled(false);
        _button_open_server.setEnabled(true);
        this.enableKeyPad(false);
    }

    public void onMAPAcceptInfo(MAPAcceptInfo arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onMAPOpenInfo(MAPOpenInfo arg0) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onMAPCloseInfo(MAPCloseInfo close) {
        
        this._field_punch_display.setText("Dialog Closed");
        this.onlyKeyPadContent = false;
        this.clientDialog = null;
        this._keypad_button_break.setEnabled(false);
    }

    public void onMAPProviderAbortInfo(MAPProviderAbortInfo arg0) {
        this._field_punch_display.setText("Dialog Aborted: "+arg0);
        this.onlyKeyPadContent = false;
        this.clientDialog = null;
        this._keypad_button_break.setEnabled(false);
    }

    public void onMAPRefuseInfo(MAPRefuseInfo arg0) {
        this._field_punch_display.setText("Dialog Refused: "+arg0);
        this.onlyKeyPadContent = false;
        this.clientDialog = null;
        this._keypad_button_break.setEnabled(false);
    }

    public void onMAPUserAbortInfo(MAPUserAbortInfo arg0) {
        this._field_punch_display.setText("Dialog UAborted: "+arg0);
        this.onlyKeyPadContent = false;
        this.clientDialog = null;
        this._keypad_button_break.setEnabled(false);
       
    }

    public void onProcessUnstructuredSSIndication(ProcessUnstructuredSSIndication arg0) {
        //we dont server requests.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onUnstructuredSSIndication(UnstructuredSSIndication ussdInditaion) {
       //here RA responds.
        USSDString string =ussdInditaion.getUSSDString();
        this._field_result_display.setText(string.getString());
    }

}
