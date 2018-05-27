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

package org.restcomm.protocols.ss7.tools.simulator.tests.ati;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
*
* @author sergey vetyutnev
*
*/
public class TestAtiServerConfigurationData {

    protected static final String ATI_REACTION = "atiReaction";

    protected ATIReaction atiReaction = new ATIReaction(ATIReaction.VAL_RETURN_SUCCESS);

    public ATIReaction getATIReaction() {
        return atiReaction;
    }

    public void setATIReaction(ATIReaction val) {
        atiReaction = val;
    }

    protected static final XMLFormat<TestAtiServerConfigurationData> XML = new XMLFormat<TestAtiServerConfigurationData>(
            TestAtiServerConfigurationData.class) {

        public void write(TestAtiServerConfigurationData clt, OutputElement xml) throws XMLStreamException {
            xml.add(clt.atiReaction.toString(), ATI_REACTION, String.class);
        }

        public void read(InputElement xml, TestAtiServerConfigurationData clt) throws XMLStreamException {
            String atiR = (String) xml.get(ATI_REACTION, String.class);
            clt.atiReaction = ATIReaction.createInstance(atiR);
        }
    };

}
