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
package org.mobicents.ss7.management.console;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mobicents.ss7.management.console.Tree.Node;
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
