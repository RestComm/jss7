/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GTCodec {
    private GTCodec getCodec(GlobalTitle gt) {
        switch (gt.getIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY :
                return new GT0001Codec((GT0001)gt);
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY :
                return new GT0010Codec((GT0010)gt);
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME :
                return new GT0011Codec((GT0011)gt);
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS :
                return new GT0100Codec((GT0100)gt);
            default : return null;
        }
    }
    
    public void encode(GlobalTitle gt, OutputStream out) throws IOException {
        GTCodec codec = this.getCodec(gt);
        codec.encode(out);
    }
    
    public GlobalTitle decode(GlobalTitleIndicator gti, InputStream in) throws IOException {
        GTCodec codec = null;
        switch (gti) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY :
                codec = new GT0001Codec();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY :
                codec = new GT0010Codec();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME :
                codec =new GT0011Codec();
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS :
                codec =new GT0100Codec();
                break;
        }
        return codec.decode(in);
    }
    
    protected void encode(OutputStream out) throws IOException {
    }
    
    protected GlobalTitle decode(InputStream in) throws IOException  {
        return null;
    }
}
