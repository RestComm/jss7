/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.ss7.management.shell;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.ss7.management.shell.Command;
import org.mobicents.ss7.management.shell.Parameter;
import org.mobicents.ss7.management.shell.Value;

import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class CommandTest {

    public CommandTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of toString method, of class Command.
     */
    @Test
    public void testToString() {
        Command cmd = new Command("add", "Add new linkset of type dahdi | dialogic");
        
        Value dialogic = new Value("dialogic");
        dialogic.add(new Parameter("module_id"));
        
        Value dahdi = new Value("dahdi");
        dahdi.add(new Parameter("span"));
        dahdi.add(new Parameter("channel"));
        
        Parameter linkset = new Parameter("linkset", new Value[]{dialogic, dahdi});
        Parameter opc = new Parameter("opc");
        Parameter apc = new Parameter("apc");
        
        Value national = new Value("national");
        Value international = new Value("international");
        
        Parameter ni = new Parameter("network indicator", new Value[] {national, international});
        
        cmd.add(linkset);
        cmd.add(opc);
        cmd.add(apc);
        cmd.add(ni);
        
        System.out.println(cmd);
    }

}