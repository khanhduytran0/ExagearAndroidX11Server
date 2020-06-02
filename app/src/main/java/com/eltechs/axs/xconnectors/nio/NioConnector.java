package com.eltechs.axs.xconnectors.nio;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.nio.impl.BufferSizeConfiguration;
import com.eltechs.axs.xconnectors.nio.impl.NioProcessorThread;
import java.io.IOException;
import java.net.SocketAddress;

public class NioConnector<Context> {
    private final SocketAddress addr;
    private final BufferSizeConfiguration bufferSizeConfiguration = new BufferSizeConfiguration();
    private final ConnectionHandler<Context> connectionHandler;
    private transient NioProcessorThread processorThread;
    private final RequestHandler<Context> requestHandler;

    public NioConnector(SocketAddress addr, ConnectionHandler<Context> connectionHandler, RequestHandler<Context> requestHandler) {
        this.addr = addr;
        this.connectionHandler = connectionHandler;
        this.requestHandler = requestHandler;
    }

    public void start() throws IOException {
        Assert.state(this.processorThread == null, "The connector is already running.");
        this.processorThread = new NioProcessorThread(this.addr, this.connectionHandler, this.requestHandler, this.bufferSizeConfiguration);
        this.processorThread.startProcessing();
    }

    public void stop() throws IOException {
        this.processorThread.stopProcessing();
        while (this.processorThread.isAlive()) {
            try {
                this.processorThread.join();
            } catch (InterruptedException e) {
            }
        }
        this.processorThread = null;
    }

    public void setInitialInputBufferCapacity(int initialInputBufferCapacity) {
        this.bufferSizeConfiguration.setInitialInputBufferCapacity(initialInputBufferCapacity);
    }

    public void setInitialOutputBufferCapacity(int initialOutputBufferCapacity) {
        this.bufferSizeConfiguration.setInitialOutputBufferCapacity(initialOutputBufferCapacity);
    }

    public void setOutputBufferSizeLimit(int outputBufferSizeLimit) {
        this.bufferSizeConfiguration.setOutputBufferSizeLimit(outputBufferSizeLimit);
    }

    public void setInputBufferSizeHardLimit(int inputBufferSizeHardLimit) {
        this.bufferSizeConfiguration.setInputBufferSizeHardLimit(inputBufferSizeHardLimit);
    }

    public void setOutputBufferSizeHardLimit(int outputBufferSizeHardLimit) {
        this.bufferSizeConfiguration.setOutputBufferSizeHardLimit(outputBufferSizeHardLimit);
    }
}

