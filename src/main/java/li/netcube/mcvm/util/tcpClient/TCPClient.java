package li.netcube.mcvm.util.tcpClient;

import java.io.*;
import java.net.*;

public class TCPClient {

    private Socket client;

    public TCPClient(String host, int port) throws IOException {
        client = new Socket(host, port);
    }

    public void close() throws IOException {
        client.close();
    }

    public InputStream getInputStream() throws IOException {
        return client.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return client.getOutputStream();
    }
}