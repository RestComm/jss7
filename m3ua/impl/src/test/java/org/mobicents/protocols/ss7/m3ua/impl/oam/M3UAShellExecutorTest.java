package org.mobicents.protocols.ss7.m3ua.impl.oam;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.as.RemSigGatewayImpl;
import org.mobicents.protocols.ss7.m3ua.impl.sg.SigGatewayImpl;

public class M3UAShellExecutorTest {

    M3UAShellExecutor m3uaExec = null;
    RemSigGatewayImpl rsgw = null;
    SigGatewayImpl sgw = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        m3uaExec = new M3UAShellExecutor();
        rsgw = new RemSigGatewayImpl();
        sgw = new SigGatewayImpl("127.0.0.1", 1771);

        sgw.start();
        rsgw.start();
    }

    @After
    public void tearDown() {
        sgw.stop();
        rsgw.stop();
    }

    @Test
    public void testSgwCommands() {
        m3uaExec.setSigGatewayImpl(sgw);

        // Test creating new AS
        String result = m3uaExec.execute("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode loadshare testas"
                .split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas"), result);

        // Try adding same again
        result = m3uaExec.execute("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode loadshare testas".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, "testas"), result);

        // Create AS with only DPC
        result = m3uaExec.execute("m3ua ras create rc 100 rk dpc 124 testas1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas1"), result);

        // Create AS with DPC and OPC list
        result = m3uaExec.execute("m3ua ras create rc 100 rk dpc 125 opc 1774,1778 testas2".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas2"), result);

        // create ASP
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2777 testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, "testasp1"), result);

        // Error for same name
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2778 testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, "testasp1"), result);

        // Error for same IP:Port
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2777 testasp2".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_IPPORT_EXIST, "127.0.0.1", 2777), result);

        // assign ASP to AS
        result = m3uaExec.execute("m3ua ras add testas testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, "testasp1", "testas"), result);

        // add again
        result = m3uaExec.execute("m3ua ras add testas testasp1".split(" "));
        assertEquals(String.format("Asp name=%s already added", "testasp1"), result);
    }
    
    @Test
    public void testRemSgwCommands() {
        m3uaExec.setSigGatewayImpl(rsgw);

        // Test creating new AS
        String result = m3uaExec.execute("m3ua ras create rc 100 testas"
                .split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas"), result);

        // Try adding same again
        result = m3uaExec.execute("m3ua ras create rc 100 testas".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, "testas"), result);

        // create ASP
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2777 remip 127.0.0.1 remport 2777 testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, "testasp1"), result);

        // Error for same name
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2778 remip 127.0.0.1 remport 2777 testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, "testasp1"), result);

        // Error for same IP:Port
        result = m3uaExec.execute("m3ua rasp create ip 127.0.0.1 port 2777 remip 127.0.0.1 remport 2777 testasp2".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_IPPORT_EXIST, "127.0.0.1", 2777), result);

        // assign ASP to AS
        result = m3uaExec.execute("m3ua ras add testas testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, "testasp1", "testas"), result);

        // add again
        result = m3uaExec.execute("m3ua ras add testas testasp1".split(" "));
        assertEquals(String.format("Asp name=%s already added", "testasp1"), result);
    }    
}
