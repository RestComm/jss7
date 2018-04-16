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

package org.restcomm.protocols.ss7.tools.simulatorgui;

import java.awt.EventQueue;

import org.restcomm.protocols.ss7.tools.simulator.TesterHostFactoryImpl;
import org.restcomm.protocols.ss7.tools.simulator.TesterHostFactoryInterface;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MainGui implements Runnable {

    private final String appName;

    public MainGui(String appName) {
        this.appName = appName;
    }

    protected TesterHostFactoryInterface getTesterHostFactory() {
        return new TesterHostFactoryImpl();
    }

    public static void main(String[] args) {

        String appName = "main";
        if (args != null && args.length > 0) {
            appName = args[0];
        }

        EventQueue.invokeLater(new MainGui(appName));
    }

    protected ConnectionForm createConnectionForm() {
        return new ConnectionForm(this.getTesterHostFactory());
    }

    @Override
    public void run() {
        try {
            ConnectionForm frame = createConnectionForm();
            frame.setAppName(appName);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
