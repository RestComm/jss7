package org.mobicents.ss7.hardware.dahdi.oam;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp2;
import org.mobicents.ss7.hardware.dahdi.Channel;
import org.mobicents.ss7.linkset.oam.FormatterHelp;
import org.mobicents.ss7.linkset.oam.Link;
import org.mobicents.ss7.linkset.oam.LinkMode;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.LinkState;

/**
 * Instance of this class represents SS7 link for <tt>dahdi</tt> based SS7
 * Cards
 * 
 * @author amit bhayani
 * 
 */
public class DahdiLink extends Link {

    private static final Logger logger = Logger.getLogger(DahdiLink.class);

    private int span = -1;
    private int channelID = -1;
    private int code = -1;

    // TODO : Should Buffer Size also configurable from command line?
    private int ioBufferSize = 32;

    private static final String LINK_SPAN = "span";
    private static final String LINK_CHANNEL_ID = "channelId";
    private static final String LINK_CODE = "code";
    private static final String LINK_IO_BUFFER_SIZE = "iOBufferSize";

    private Channel channel = null;
    private Mtp2 mtp2 = null;

    public DahdiLink() {
    }

    public DahdiLink(String linkName, int span, int channelID, int code) {
        super(linkName);
        this.span = span;
        this.channelID = channelID;
        this.code = code;
    }

    @Override
    protected void configure() throws Exception {

        if (this.mode == LinkMode.CONFIGURED) {
            if (this.channel == null) {
                channel = new Channel();
                mtp2 = new Mtp2(this.linkName.toString() + "-" + this.code,
                        this.channel); // TODO : Optimize the String usage
            }

            channel.setChannelID(this.channelID);
            channel.setCode(this.code);
            channel.setIOBufferSize(this.ioBufferSize);
            channel.setLinkName(this.linkName.toString());
            channel.setSpan(this.span);
        }
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIoBufferSize() {
        return ioBufferSize;
    }

    public void setIoBufferSize(int ioBufferSize) {
        this.ioBufferSize = ioBufferSize;
    }

    @Override
    public void activate() throws Exception {
        if (this.state == LinkState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.LINK_ALREADY_ACTIVE);
        }

        // Add check that all parameters are set before initializing the
        // Link. Else send error message
        if (this.span == -1 || this.code == -1 || this.channelID == -1) {
            throw new Exception(LinkOAMMessages.LINK_NOT_CONFIGURED);
        }

        this.mode = LinkMode.CONFIGURED;

        this.configure();

        this.state = LinkState.UNAVAILABLE;
    }

    /**
     * Management Operations
     */
    public void deactivate() throws Exception {
        if (this.mode == LinkMode.UNCONFIGURED) {
            throw new Exception(LinkOAMMessages.LINK_ALREADY_DEACTIVE);
        }

        this.mtp2.stop();

        // TODO : SHouldn't this come from Mtp2Listener?
        this.state = LinkState.SHUTDOWN;
    }

    protected static final XMLFormat<DahdiLink> DAHDI_LINK_XML = new XMLFormat<DahdiLink>(
            DahdiLink.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                DahdiLink link) throws XMLStreamException {

            LINK_XML.read(xml, link);

            link.span = xml.getAttribute(LINK_SPAN, -1);
            link.channelID = xml.getAttribute(LINK_CHANNEL_ID, -1);
            link.code = xml.getAttribute(LINK_CODE, -1);
            link.ioBufferSize = xml.getAttribute(LINK_IO_BUFFER_SIZE, 32);

            try {
                link.configure();
            } catch (Exception e) {
                logger.error("Failed to initialize dahdi link", e);
            }
        }

        @Override
        public void write(DahdiLink link,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            LINK_XML.write(link, xml);

            xml.setAttribute(LINK_SPAN, link.getSpan());
            xml.setAttribute(LINK_CHANNEL_ID, link.getChannelID());
            xml.setAttribute(LINK_CODE, link.getCode());
            xml.setAttribute(LINK_IO_BUFFER_SIZE, link.getIoBufferSize());
        }
    };

    /**
     * 
     */
    protected Mtp2 getMtp2() {
        return this.mtp2;
    }

    @Override
    public void print(StringBuffer sb, int leftPad, int descPad) {
        FormatterHelp.createPad(sb, leftPad);

        // Add name
        sb.append(this.linkName);

        // check if length is less than Link.NAME_SIZE, add padding
        if (this.linkName.length() < Link.NAME_SIZE) {
            FormatterHelp
                    .createPad(sb, Link.NAME_SIZE - this.linkName.length());
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add span
        sb.append(LINK_SPAN).append(FormatterHelp.EQUAL_SIGN).append(this.span);

        // span can be max 2 digits. check if its one digit add one extra space
        if (this.span < 10) {
            FormatterHelp.createPad(sb, 1);
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add channel-id
        sb.append(LINK_CHANNEL_ID).append(FormatterHelp.EQUAL_SIGN).append(
                this.channelID);

        // channel can be max 2 digits. check if its one digit add one extra
        // space
        if (this.channelID < 10) {
            FormatterHelp.createPad(sb, 1);
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add code
        sb.append(LINK_CODE).append(FormatterHelp.EQUAL_SIGN).append(this.code);

        // code can be max 3 digits. check if its less than 3 digit add extra
        // space
        if (this.code < 10) {
            FormatterHelp.createPad(sb, 1);
        }

        if (this.code < 100) {
            FormatterHelp.createPad(sb, 1);
        }

        // add state
        sb.append(LINK_STATE).append(FormatterHelp.EQUAL_SIGN).append(
                FormatterHelp.getLinkState(this.state));
    }
}
