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

package org.mobicents.protocols.ss7.tools.traceparser;

/**
*
* @author sergey vetyutnev
*
*/
public class MainGui implements Runnable {

    private final Ss7ParseParameters par;
    private final String persistDir;

    public MainGui(String persistDir, Ss7ParseParameters par) {
        this.persistDir = persistDir;
        this.par = par;
    }

//    public static void main(String[] args) {
//
//        String appName = "main";
//        if (args != null && args.length > 0) {
//            appName = args[0];
//        }
//
//        EventQueue.invokeLater(new MainGui(appName));
//    }

    @Override
    public void run() {
        try {
            TraceParserForm frame = new TraceParserForm(par);
            frame.setPersistDir(persistDir);
            frame.frmSsTraceParser.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
