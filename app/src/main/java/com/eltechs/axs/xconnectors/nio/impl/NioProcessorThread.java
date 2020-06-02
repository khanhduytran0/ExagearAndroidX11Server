package com.eltechs.axs.xconnectors.nio.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.RequestHandler;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NioProcessorThread<Context> extends Thread {
    private transient boolean active;
    private final BufferSizeConfiguration bufferSizeConfiguration;
    private final Map<SelectionKey, NioClient<Context>> clientSockets = new HashMap();
    private final ConnectionHandler<Context> connectionHandler;
    private final RequestHandler<Context> requestHandler;
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final SelectionKey serverSocketKey;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] switchMap = new int[ProcessingResult.values().length];

        static {
            try {
                switchMap[ProcessingResult.PROCESSED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                switchMap[ProcessingResult.PROCESSED_KILL_CONNECTION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                switchMap[ProcessingResult.INCOMPLETE_BUFFER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public NioProcessorThread(SocketAddress addr, ConnectionHandler<Context> connectionHandler, RequestHandler<Context> requestHandler, BufferSizeConfiguration bufferSizeConfiguration) throws IOException {
        this.connectionHandler = connectionHandler;
        this.requestHandler = requestHandler;
        this.bufferSizeConfiguration = bufferSizeConfiguration;
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().setReuseAddress(true);
        this.serverSocketChannel.socket().bind(addr);
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketKey = this.serverSocketChannel.register(this.selector, 16);
    }

    public void run() {
        do {
        } while (runOnce());
        shutdown();
    }

    public void startProcessing() {
        Assert.state(!this.active, "Processing thread already started.");
        Assert.state(this.selector.isOpen(), "Processing thread can not be restarted.");
        this.active = true;
        start();
    }

    public void stopProcessing() throws IOException {
        Assert.state(this.selector.isOpen(), "Processing thread already stopped.");
        this.selector.close();
        this.active = false;
    }

    private boolean runOnce() {
        try {
            this.selector.select();
            Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
            synchronized (this.selector) {
                for (SelectionKey k : selectedKeys) {
                    NioClient<Context> client = (NioClient) this.clientSockets.get(k);
                    if (client != null) {
                        processClientMessages(k, client);
                    } else if (k == this.serverSocketKey) {
                        processNewConnection();
                    } else {
                        Assert.state(false, "Got a message from a client that has not been registered.");
                    }
                }
            }
            return true;
        } catch (ClosedSelectorException e) {
            return false;
        } catch (IOException e2) {
            return false;
        }
    }

    private synchronized void processNewConnection() {
        try {
            SocketChannel clientSocket = this.serverSocketChannel.accept();
            if (clientSocket != null) {
                clientSocket.configureBlocking(false);
                clientSocket.socket().setTcpNoDelay(true);
                SelectionKey clientKey = clientSocket.register(this.selector, 1);
                ByteBuffer clientInputBuffer = ByteBuffer.allocateDirect(this.bufferSizeConfiguration.getInitialInputBufferCapacity());
                ByteBuffer clientOutputBuffer = ByteBuffer.allocateDirect(this.bufferSizeConfiguration.getInitialOutputBufferCapacity());
                NioXInputStream inputStream = new NioXInputStream(clientInputBuffer);
                NioXOutputStream outputStream = new NioXOutputStream(clientSocket, clientOutputBuffer);
                inputStream.setBufferSizeHardLimit(this.bufferSizeConfiguration.getInputBufferSizeHardLimit());
                outputStream.setBufferSizeLimit(this.bufferSizeConfiguration.getOutputBufferSizeLimit());
                outputStream.setBufferSizeHardLimit(this.bufferSizeConfiguration.getOutputBufferSizeHardLimit());
                this.clientSockets.put(clientKey, new NioClient(this.connectionHandler.handleNewConnection(inputStream, outputStream), clientSocket, inputStream, outputStream));
            }
        } catch (IOException e) {
        }
    }

    private void processClientMessages(SelectionKey clientKey, NioClient<Context> client) {
        SocketChannel socket = client.getSocket();
        NioXInputStream inputStream = client.getInputStream();
        ByteBuffer inputBuffer = inputStream.getInputBuffer();
        while (true) {
            try {
                inputStream.growInputBufferIfNecessary();
                int nBytesRead = readMoreData(socket, inputBuffer);
                if (nBytesRead < 0) {
                    killConnection(clientKey, client);
                    return;
                } else if (nBytesRead != 0) {
                    inputStream.prepareForReading();
                    int nBytesProcessed = processMessagesInBuffer(client);
                    if (nBytesProcessed < 0) {
                        killConnection(clientKey, client);
                        return;
                    }
                    inputStream.doneWithReading(nBytesProcessed);
                } else {
                    return;
                }
            } catch (IOException e) {
                killConnection(clientKey, client);
                return;
            }
        }
    }

    private int processMessagesInBuffer(NioClient<Context> client) throws IOException {
        NioXInputStream inputStream = client.getInputStream();
        NioXOutputStream outputStream = client.getOutputStream();
        ByteBuffer activeRegion = inputStream.getActiveRegion();
        int nBytesProcessed = 0;
        while (true) {
            switch (AnonymousClass1.switchMap[this.requestHandler.handleRequest(client.getCtx(), inputStream, outputStream).ordinal()]) {
                case 1:
                    nBytesProcessed = activeRegion.position();
                    break;
                case 2:
                    return -1;
                case 3:
                    return nBytesProcessed;
                default:
                    Assert.state(false, "Request handler returned an unhandled processing result.");
                    break;
            }
        }
    }

    private int readMoreData(SocketChannel socket, ByteBuffer buffer) {
        try {
            return socket.read(buffer);
        } catch (IOException e) {
            return -1;
        }
    }

    private void killConnection(SelectionKey clientKey, NioClient<Context> client) {
        try {
            this.connectionHandler.handleConnectionShutdown(client.getCtx());
            client.getSocket().close();
        } catch (IOException e) {
        }
        this.clientSockets.remove(clientKey);
    }

    private void shutdown() {
        try {
            this.serverSocketChannel.close();
        } catch (IOException e) {
        }
        for (Entry<SelectionKey, NioClient<Context>> client : this.clientSockets.entrySet()) {
            try {
                this.connectionHandler.handleConnectionShutdown((Context) ((NioClient) client.getValue()).getCtx());
                ((NioClient) client.getValue()).getSocket().close();
            } catch (IOException e2) {
            }
        }
    }
}

