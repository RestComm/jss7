package org.mobicents.ss7.management.console;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.naming.NamingException;

import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javolution.util.FastList;

public class CliTest {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 5522;

    private Scheduler scheduler = new Scheduler();
    private List<ShellExecutor> shellExecutors = new FastList<ShellExecutor>();
    private MessageFactory messageFactory = ChannelProvider.provider().createMessageFactory();
    private ShellServer shellServer;

    protected class ClientThr extends Thread {

        private String str;
        private MessageFactory messageFactory = ChannelProvider.provider().createMessageFactory();
        private Semaphore semaphore;
        private Semaphore reverseSemaphore;
        private Message response;
        private Client client;

        public ClientThr(String str, Semaphore semaphore, Semaphore reverseSemaphore, Client client) {
            this.str = str;
            this.semaphore = semaphore;
            this.reverseSemaphore = reverseSemaphore;
            this.client = client;
        }

        public Message getResponse() {
            return response;
        }

        @Override
        public void run() {
            InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
            try {
                
                client.connect(addr);
                client.run(null);
                semaphore.release(1);  
                
                response = null;
                Message msg;
                msg = messageFactory.createMessage(str);
                response = client.run(msg);
                semaphore.release(1);
                
                try
                {
                    reverseSemaphore.acquire();
                }
                catch(InterruptedException ex)
                {
                }
                
                msg = messageFactory.createMessage("disconnect");
                client.run(msg);   
                
            } catch (IOException ioe) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            semaphore.release(1);            
        }
    }
    
    protected class ClientThrCloseChannel extends Thread {

        private String str;
        private MessageFactory messageFactory = ChannelProvider.provider().createMessageFactory();
        private Semaphore semaphore;
        private Semaphore reverseSemaphore;
        private Message response;
        private Client client;

        public ClientThrCloseChannel(String str, Semaphore semaphore, Semaphore reverseSemaphore, Client client) {
            this.str = str;
            this.semaphore = semaphore;
            this.reverseSemaphore = reverseSemaphore;
            this.client = client;
        }

        public Message getResponse() {
            return response;
        }

        @Override
        public void run() {
            InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
            try {
                
                client.connect(addr);
                client.run(null);
                semaphore.release(1);  
                
                response = null;
                Message msg;
                msg = messageFactory.createMessage(str);
                response = client.run(msg);
                semaphore.release(1);
                
                try
                {
                    reverseSemaphore.acquire();
                }
                catch(InterruptedException ex)
                {
                    System.out.println("INTERRUPTED!!");
                }
                
                client.stop();   
                
            } catch (IOException ioe) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            semaphore.release(1);            
        }
    }

    @BeforeTest
    public void init() throws IOException, NamingException {
        try {
            shellExecutors.add(new TestShellExecutor());
            scheduler.setClock(new DefaultClock());
            scheduler.start();
            shellServer = new ShellServerJboss(scheduler, shellExecutors);
            shellServer.setAddress(SERVER_ADDRESS);
            shellServer.setPort(SERVER_PORT);
            shellServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() {
        shellServer.stop();
        scheduler.stop();
        shellExecutors.clear();
    }

    @Test
    public void testNoSleepDisconnect() throws IOException {
        Client client = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client.connect(addr);
        assertTrue(client.isChannelConnected());
        assertTrue(client.isConnected());
        client.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client.isChannelConnected());

        client = new Client();
        client.connect(addr);
        assertTrue(client.isChannelConnected());
        assertTrue(client.isConnected());
        client.run(null);
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client.isChannelConnected());
    }
    
    @Test
    public void testNoSleepCloseChannel() throws IOException {
        Client client = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client.connect(addr);
        assertTrue(client.isChannelConnected());
        assertTrue(client.isConnected());
        client.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client.stop();
        assertFalse(client.isConnected());

        client = new Client();
        client.connect(addr);
        assertTrue(client.isChannelConnected());
        assertTrue(client.isConnected());
        client.run(null);
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client.isChannelConnected());
    }

    @Test
    public void testTwoClientsWithDisconnect() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client1.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client1.isChannelConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client2.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client2.isChannelConnected());

    }
    
    @Test
    public void testTwoClientsWithCloseChannel() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client1.stop();
        assertFalse(client1.isConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client2.stop();
        assertFalse(client2.isConnected());

    }

    @Test
    public void testTwoClientsSleep20Disconnect() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test sleep20");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client1.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client1.isChannelConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client2.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client2.isChannelConnected());

    }
    
    @Test
    public void testTwoClientsSleep20CloseChannel() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test sleep20");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client1.stop();
        assertFalse(client1.isConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client2.stop();
        assertFalse(client2.isConnected());

    }

    @Test
    public void testTwoClientsAfterNoResponseDisconnect() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test sleep40");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client1.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client1.isChannelConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        try {
            msg = messageFactory.createMessage("disconnect");
            response = client2.run(msg);
        } catch (IOException ioe) {
            // it is expected
        }
        assertFalse(client2.isChannelConnected());

    }
    
    @Test
    public void testTwoClientsAfterNoResponseCloseChannel() throws IOException {
        Client client1 = new Client();
        Client client2 = new Client();
        InetSocketAddress addr = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        client1.connect(addr);
        client2.connect(addr);
        assertTrue(client1.isChannelConnected());
        assertTrue(client1.isConnected());
        assertTrue(client2.isChannelConnected());
        assertTrue(client2.isConnected());
        client1.run(null);
        client2.run(null);
        Message response = null;
        Message msg;
        try {
            msg = messageFactory.createMessage("test sleep40");
            response = client1.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));

        client1.stop();
        assertFalse(client1.isConnected());

        try {
            msg = messageFactory.createMessage("test nosleep");
            response = client2.run(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(response.equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));

        client2.stop();
        assertFalse(client2.isConnected());

    }

    @Test
    public void testTwoClientsSimultaneously20Disconnect() throws IOException {
        Semaphore semaphore = new Semaphore(0);
        Semaphore reverseSemaphore = new Semaphore(0);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientThr clientThr1 = new ClientThr("test sleep20", semaphore, reverseSemaphore, client1);
        ClientThr clientThr2 = new ClientThr("test nosleep", semaphore, reverseSemaphore, client2);
                
        clientThr1.start();
        clientThr2.start();
                
        try {
            semaphore.acquire(2);
            
            assertTrue(client1.isChannelConnected());
            assertTrue(client1.isConnected());
            
            semaphore.acquire(1);
            
            boolean isFirstThrRespChecked = false;
            if (clientThr1.getResponse() != null) {
                isFirstThrRespChecked = true;
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            semaphore.acquire(1);
            
            if (!isFirstThrRespChecked) {
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            
            reverseSemaphore.release(2);
            semaphore.acquire(2);
            
            assertFalse(client1.isChannelConnected());
            assertFalse(client2.isChannelConnected());
        } catch(InterruptedException ie) {
        }
    }
    
    @Test
    public void testTwoClientsSimultaneously20CloseChannel() throws IOException {
        Semaphore semaphore = new Semaphore(0);
        Semaphore reverseSemaphore = new Semaphore(0);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientThrCloseChannel clientThr1 = new ClientThrCloseChannel("test sleep20", semaphore, reverseSemaphore, client1);
        ClientThrCloseChannel clientThr2 = new ClientThrCloseChannel("test nosleep", semaphore, reverseSemaphore, client2);
                
        clientThr1.start();
        clientThr2.start();
                
        try {
            semaphore.acquire(2);
            
            assertTrue(client1.isChannelConnected());
            assertTrue(client1.isConnected());
            
            semaphore.acquire(1);
            
            boolean isFirstThrRespChecked = false;
            if (clientThr1.getResponse() != null) {
                isFirstThrRespChecked = true;
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            semaphore.acquire(1);
            
            if (!isFirstThrRespChecked) {
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            
            reverseSemaphore.release(2);
            semaphore.acquire(2);
            
            assertFalse(client1.isConnected());
            assertFalse(client2.isConnected());
        } catch(InterruptedException ie) {
        }
    }
    
    @Test
    public void testTwoClientsSimultaneously40Disconnect() throws IOException {
        Semaphore semaphore = new Semaphore(0);
        Semaphore reverseSemaphore = new Semaphore(0);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientThr clientThr1 = new ClientThr("test sleep40", semaphore, reverseSemaphore, client1);
        ClientThr clientThr2 = new ClientThr("test nosleep", semaphore, reverseSemaphore, client2);
                
        clientThr1.start();
        clientThr2.start();
                
        try {
            semaphore.acquire(2);
            
            assertTrue(client1.isChannelConnected());
            assertTrue(client1.isConnected());
            
            semaphore.acquire(1);
            
            boolean isFirstThrRespChecked = false;
            if (clientThr1.getResponse() != null) {
                isFirstThrRespChecked = true;
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            semaphore.acquire(1);
            
            if (!isFirstThrRespChecked) {
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            
            reverseSemaphore.release(2);
            semaphore.acquire(2);
            
            assertFalse(client1.isChannelConnected());
            assertFalse(client2.isChannelConnected());
        } catch(InterruptedException ie) {
        }
    }
    
    @Test
    public void testTwoClientsSimultaneously40CloseChannel() throws IOException {
        Semaphore semaphore = new Semaphore(0);
        Semaphore reverseSemaphore = new Semaphore(0);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientThrCloseChannel clientThr1 = new ClientThrCloseChannel("test sleep40", semaphore, reverseSemaphore, client1);
        ClientThrCloseChannel clientThr2 = new ClientThrCloseChannel("test nosleep", semaphore, reverseSemaphore, client2);
                
        clientThr1.start();
        clientThr2.start();
                
        try {
            semaphore.acquire(2);
            
            assertTrue(client1.isChannelConnected());
            assertTrue(client1.isConnected());
            
            semaphore.acquire(1);
            
            boolean isFirstThrRespChecked = false;
            if (clientThr1.getResponse() != null) {
                isFirstThrRespChecked = true;
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            semaphore.acquire(1);
            
            if (!isFirstThrRespChecked) {
                assertTrue(clientThr1.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_40)));
            } else {
                assertTrue(clientThr2.getResponse().equals(messageFactory.createMessage(TestShellExecutor.SLEEP_20)));
            }
            
            reverseSemaphore.release(2);
            semaphore.acquire(2);
            
            assertFalse(client1.isConnected());
            assertFalse(client2.isConnected());
        } catch(InterruptedException ie) {
        }
    }
}
