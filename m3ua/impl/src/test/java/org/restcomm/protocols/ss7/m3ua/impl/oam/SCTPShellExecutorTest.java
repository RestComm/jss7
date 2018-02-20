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

package org.restcomm.protocols.ss7.m3ua.impl.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import javolution.util.FastMap;

import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.restcomm.protocols.ss7.m3ua.impl.oam.SCTPOAMMessages;
import org.restcomm.protocols.ss7.m3ua.impl.oam.SCTPShellExecutor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 * 
 */
public class SCTPShellExecutorTest {

    private FastMap<String, Management> sctpManagements = null;
    private SCTPShellExecutor sctpShellExecutor = null;

    private ManagementImpl management1;
    private ManagementImpl management2;

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.sctpShellExecutor = new SCTPShellExecutor();

        this.management1 = new ManagementImpl("TestSCTP1");
        this.management2 = new ManagementImpl("TestSCTP2");

        this.sctpManagements = new FastMap<String, Management>();

        this.sctpManagements.put("TestSCTP1", management1);
        this.sctpManagements.put("TestSCTP2", management2);

        this.sctpShellExecutor.setSctpManagements(sctpManagements);

        this.management1.start();
        this.management2.start();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        // Clean up
        this.management1.removeAllResourses();
        this.management2.removeAllResourses();

        this.management1.stop();
        this.management2.stop();
    }

    @Test
    public void testServerCommands() throws Exception {
        // CREATE

        // Test 0
        String sctpServerCommand = "sctp server create TestServer1 127.0.0.1 2905 stackname TestSCTP0";
        String result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.NO_SCTP_MANAGEMENT_BEAN_FOR_NAME, "TestSCTP0"));

        // Test`1
        sctpServerCommand = "sctp server create TestServer1 127.0.0.1 2905";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, "TestServer1", this.management1.getName()));
        assertEquals(1, this.management1.getServers().size());
        assertEquals(0, this.management2.getServers().size());

        // Test`2
        sctpServerCommand = "sctp server create TestServer2 127.0.0.1 2906 sockettype TCP";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, "TestServer2", this.management1.getName()));
        assertEquals(2, this.management1.getServers().size());
        assertEquals(0, this.management2.getServers().size());

        // Test`3
        sctpServerCommand = "sctp server create TestServer3 127.0.0.1 2907 stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, "TestServer3", this.management2.getName()));
        assertEquals(1, this.management2.getServers().size());
        assertEquals(2, this.management1.getServers().size());

        // Test`4
        sctpServerCommand = "sctp server create TestServer4 127.0.0.1 2908 stackname TestSCTP2 sockettype TCP";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, "TestServer4", this.management2.getName()));
        assertEquals(2, this.management2.getServers().size());
        assertEquals(2, this.management1.getServers().size());

        // START

        // Test 5
        sctpServerCommand = "sctp server start TestServer4";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.START_SERVER_SUCCESS, "TestServer4", this.management2.getName()));
        Server server = this.management2.getServers().get(1);
        assertTrue(server.isStarted());

        // Test 6
        sctpServerCommand = "sctp server start TestServer2 stackname TestSCTP1";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.START_SERVER_SUCCESS, "TestServer2", this.management1.getName()));
        server = this.management2.getServers().get(1);
        assertTrue(server.isStarted());

        // STOP
        // Test 7
        sctpServerCommand = "sctp server stop TestServer2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.STOP_SERVER_SUCCESS, "TestServer2", this.management1.getName()));
        server = this.management1.getServers().get(1);
        assertFalse(server.isStarted());

        // Test 8
        sctpServerCommand = "sctp server stop TestServer4 stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.STOP_SERVER_SUCCESS, "TestServer4", this.management2.getName()));
        server = this.management2.getServers().get(1);
        assertFalse(server.isStarted());

        // SHOW

        // Test 9
        sctpServerCommand = "sctp server show";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertTrue(result.contains("TestServer4") && result.contains("TestServer3"));
        assertFalse(result.contains("TestServer1") || result.contains("TestServer2"));

        // Test 10
        sctpServerCommand = "sctp server show stackname TestSCTP1";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertTrue(result.contains("TestServer1") && result.contains("TestServer2"));
        assertFalse(result.contains("TestServer3") || result.contains("TestServer4"));

        // DESTROY

        // Test 11
        sctpServerCommand = "sctp server destroy TestServer1";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, "TestServer1", this.management1.getName()));
        assertEquals(1, this.management1.getServers().size());

        // Test 12
        sctpServerCommand = "sctp server destroy TestServer2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, "TestServer2", this.management1.getName()));
        assertEquals(0, this.management1.getServers().size());

        // Test 13
        sctpServerCommand = "sctp server destroy TestServer3 stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, "TestServer3", this.management2.getName()));
        assertEquals(1, this.management2.getServers().size());

        // Test 14
        sctpServerCommand = "sctp server destroy TestServer4";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, "TestServer4", this.management2.getName()));
        assertEquals(0, this.management2.getServers().size());

    }

    @Test
    public void testSetGetCommands() throws Exception {
        String sctpServerCommand = "sctp set connectdelay 21500";
        String result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.PARAMETER_SUCCESSFULLY_SET, "TestSCTP1"));
        assertEquals(21500, this.management1.getConnectDelay());

        sctpServerCommand = "sctp set connectdelay 21501 stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.PARAMETER_SUCCESSFULLY_SET, "TestSCTP2"));
        assertEquals(21501, this.management2.getConnectDelay());

        sctpServerCommand = "sctp set workerthreads 12";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.PARAMETER_SUCCESSFULLY_SET, "TestSCTP2"));
        assertEquals(12, this.management2.getWorkerThreads());

        sctpServerCommand = "sctp get workerthreads";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        System.out.println(result);
        assertTrue(result.contains("TestSCTP2") && result.contains("workerthreads") && result.contains("12"));

        sctpServerCommand = "sctp get";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        System.out.println(result);
        assertTrue(result.contains("TestSCTP1") && result.contains("TestSCTP2"));

    }

    @Test
    public void testAssociationCommands() throws Exception {
        String sctpAssocCommand = "sctp association create Assoc1 CLIENT 127.0.0.1 2905 127.0.0.1 2906";
        String result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result,
                String.format(SCTPOAMMessages.ADD_CLIENT_ASSOCIATION_SUCCESS, "Assoc1", this.management1.getName()));
        assertEquals(1, this.management1.getAssociations().size());
        assertEquals(0, this.management2.getAssociations().size());

        sctpAssocCommand = "sctp association create Assoc2 CLIENT 127.0.0.1 2907 127.0.0.1 2908 stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result,
                String.format(SCTPOAMMessages.ADD_CLIENT_ASSOCIATION_SUCCESS, "Assoc2", this.management2.getName()));
        assertEquals(1, this.management1.getAssociations().size());
        assertEquals(1, this.management2.getAssociations().size());

        // Test creating server association
        String sctpServerCommand = "sctp server create TestServer1 127.0.0.1 2903 stackname TestSCTP1";
        result = this.sctpShellExecutor.execute(sctpServerCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, "TestServer1", this.management1.getName()));

        sctpAssocCommand = "sctp association create Assoc3 SERVER TestServer1 192.168.56.1 2905";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result,
                String.format(SCTPOAMMessages.ADD_SERVER_ASSOCIATION_SUCCESS, "Assoc3", this.management1.getName()));
        assertEquals(2, this.management1.getAssociations().size());
        assertEquals(1, this.management2.getAssociations().size());

        sctpAssocCommand = "sctp association show";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        System.out.println(result);
        assertTrue(result.contains("Assoc1") && result.contains("Assoc3"));
        assertFalse(result.contains("Assoc2"));

        sctpAssocCommand = "sctp association show stackname TestSCTP2";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        System.out.println(result);
        assertTrue(result.contains("Assoc2"));
        assertFalse(result.contains("Assoc1") || result.contains("Assoc3"));

        sctpAssocCommand = "sctp association destroy Assoc2";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_ASSOCIATION_SUCCESS, "Assoc2", this.management2.getName()));
        assertEquals(2, this.management1.getAssociations().size());
        assertEquals(0, this.management2.getAssociations().size());

        sctpAssocCommand = "sctp association destroy Assoc1 stackname TestSCTP1";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_ASSOCIATION_SUCCESS, "Assoc1", this.management1.getName()));
        assertEquals(1, this.management1.getAssociations().size());
        assertEquals(0, this.management2.getAssociations().size());

        sctpAssocCommand = "sctp association destroy Assoc3 stackname TestSCTP1";
        result = this.sctpShellExecutor.execute(sctpAssocCommand.split(" "));
        assertEquals(result, String.format(SCTPOAMMessages.REMOVE_ASSOCIATION_SUCCESS, "Assoc3", this.management1.getName()));
        assertEquals(0, this.management1.getAssociations().size());
        assertEquals(0, this.management2.getAssociations().size());
    }

}
