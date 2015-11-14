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

package org.mobicents.protocols.ss7.map.api.service.mobility.pagingAndSearch;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
*
<code>
MAP_SEARCH_FOR_MS service

Parameter name             Request Indication  Response    Confirm
Invoke Id                  M       M(=)        M(=)        M(=)
IMSI                       M       M(=)
Current location area Id                       C           C(=)
User error                                     C           C(=)
Provider error                                             O

The following error causes defined in clause 7.6.1 shall be sent by the user if the search procedure fails, depending on the failure reason:
-   absent subscriber;
    this error cause is returned by the MSC if the MS does not respond to the paging request;
-   system failure;
-   this corresponds to the case where there is no call associated with the MAP_SEARCH_FOR_MS service, i.e. if the call has been released but the dialogue to the VLR has not been aborted;
-   busy subscriber;
-   unexpected data value.
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface SearchForMsRequest extends MobilityMessage {

    IMSI getImsi();

}
