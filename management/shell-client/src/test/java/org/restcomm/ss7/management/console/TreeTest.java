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
package org.restcomm.ss7.management.console;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.restcomm.ss7.management.console.Tree;
import org.restcomm.ss7.management.console.Tree.Node;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author abhayani
 *
 */
public class TreeTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testMessage() throws IOException {
        Tree tree = new Tree("history");
        tree.getTopNode().addChild("enable");
        tree.getTopNode().addChild("disable");

        assertEquals("history", tree.getTopNode().getCommand());

        Node enable = tree.getTopNode().getChildren().get(0);
        assertEquals("history enable", enable.getCommand());

        Node disable = tree.getTopNode().getChildren().get(1);
        assertEquals(disable.getCommand(), "history disable");
    }
}
