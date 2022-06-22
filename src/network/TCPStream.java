package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPStream extends Thread {
    private final int port;
    private final boolean asServer;
    private final String name;
    private Socket socket = null;

    private boolean fatalError = false;

    public final int WAIT_LOOP_IN_MILLIS = 1000; // 30 sec
    private Thread createThread = null;
    private TCPServer tcpServer = null;
    private TCPClient tcpClient = null;
    private long waitInMillis = WAIT_LOOP_IN_MILLIS;
    private String remoteEngine = "localhost";

    public TCPStream(int port, boolean asServer, String name) {
        this.port = port;
        this.asServer = asServer;
        this.name = name;
    }

    public void kill() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }

            if (this.tcpClient != null) {
                this.tcpClient.kill();
            }

            if (this.tcpServer != null) {
                this.tcpServer.kill();
            }
        } catch (IOException e) {
            System.err.println(this.getClass().getSimpleName() + ": problems while killing: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        this.createThread = Thread.currentThread();
        try {
            if (this.asServer) {
                this.tcpServer = new TCPServer();
                this.socket = tcpServer.getSocket();
            } else {
                this.tcpClient = new TCPClient();
                this.socket = this.tcpClient.getSocket();
            }

        } catch (IOException ex) {
            //<<<<<<<<<<<<<<<<<<debug
            String s = "couldn't establish connection";
            System.out.println(s);
            this.fatalError = true;
        }
    }

    public void close() throws IOException {
        if (this.socket != null) {
            this.socket.close();
            //<<<<<<<<<<<<<<<<<<debug
            System.out.println("socket closed");
        }
    }


    /**
     * holds thread until a connection is established
     */
    public void waitForConnection() throws IOException {
        if (this.createThread == null) {
            /* in unit tests there is a race condition between the test
            thread and those newly created tests to establish a connection.
            
            Thus, this call could be in the right order - give it a
            second chance
            */

            try {
                Thread.sleep(this.waitInMillis);
            } catch (InterruptedException ex) {
                // ignore
            }

            if (this.createThread == null) {
                // that's probably wrong usage:
                throw new IOException("must start TCPStream thread first by calling start()");
            }
        }


        while (!this.fatalError && this.socket == null) {
            try {
                Thread.sleep(this.waitInMillis);
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    }

    public boolean checkConnected() {
        if (this.socket == null) {
            return false;
        }
        return true;
    }

    public InputStream getInputStream() throws IOException {
        this.checkConnected();
        return this.socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        this.checkConnected();
        return this.socket.getOutputStream();
    }

    public void setRemoteEngine(String remoteEngine) {
        this.remoteEngine = remoteEngine;
    }

    private class TCPServer {
        private ServerSocket srvSocket = null;

        Socket getSocket() throws IOException {
            if (this.srvSocket == null) {
                this.srvSocket = new ServerSocket(port);
            }

            //<<<<<<<<<<<<<<<<<<debug
            StringBuilder b = new StringBuilder();
            b.append(this.getClass().getSimpleName());
            b.append(" (");
            b.append(name);
            b.append("): ");
            b.append("opened port ");
            b.append(port);
            b.append(" on localhost and wait");
            System.out.println(b.toString());
            //>>>>>>>>>>>>>>>>>>>debug

            Socket socket = this.srvSocket.accept();
            //<<<<<<<<<<<<<<<<<<debug
            b = new StringBuilder();
            b.append(this.getClass().getSimpleName());
            b.append(" (");
            b.append(name);
            b.append("): ");
            b.append("connected");
            System.out.println(b.toString());
            //>>>>>>>>>>>>>>>>>>>debug

            return socket;
        }

        public void kill() throws IOException {
            this.srvSocket.close();
        }
    }

    private class TCPClient {
        private boolean killed = false;

        public void kill() {
            this.killed = true;
        }

        Socket getSocket() throws IOException {
            while (!this.killed) {
                try {
                    //<<<<<<<<<<<<<<<<<<debug
                    StringBuilder b = new StringBuilder();
                    b.append(this.getClass().getSimpleName());
                    b.append(" (");
                    b.append(name);
                    b.append("): ");
                    b.append("try to connect localhost port ");
                    b.append(port);
                    System.out.println(b.toString());
                    //>>>>>>>>>>>>>>>>>>>debug
                    Socket socket = new Socket(TCPStream.this.remoteEngine, port);
                    return socket;
                } catch (IOException ioe) {
                    //<<<<<<<<<<<<<<<<<<debug
                    StringBuilder b = new StringBuilder();
                    b.append(this.getClass().getSimpleName());
                    b.append(" (");
                    b.append(name);
                    b.append("): ");
                    b.append("failed / wait and re-try");
                    b.append(port);
                    System.out.println(b.toString());
                    try {
                        Thread.sleep(waitInMillis);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                }
            }
            throw new IOException("thread was killed before establishing a connection");
        }
    }
}
