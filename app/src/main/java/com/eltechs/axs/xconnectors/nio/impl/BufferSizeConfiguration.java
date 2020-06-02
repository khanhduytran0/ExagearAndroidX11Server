package com.eltechs.axs.xconnectors.nio.impl;

public final class BufferSizeConfiguration {
    private int initialInputBufferCapacity = 4096;
    private int initialOutputBufferCapacity = 4096;
    private int inputBufferSizeHardLimit = 2097152;
    private int outputBufferSizeHardLimit = 2097152;
    private int outputBufferSizeLimit = 65536;

    public synchronized int getInitialInputBufferCapacity() {
        return this.initialInputBufferCapacity;
    }

    public synchronized void setInitialInputBufferCapacity(int initialInputBufferCapacity) {
        this.initialInputBufferCapacity = initialInputBufferCapacity;
    }

    public synchronized int getInitialOutputBufferCapacity() {
        return this.initialOutputBufferCapacity;
    }

    public synchronized void setInitialOutputBufferCapacity(int initialOutputBufferCapacity) {
        this.initialOutputBufferCapacity = initialOutputBufferCapacity;
    }

    public synchronized int getOutputBufferSizeLimit() {
        return this.outputBufferSizeLimit;
    }

    public synchronized void setOutputBufferSizeLimit(int outputBufferSizeLimit) {
        this.outputBufferSizeLimit = outputBufferSizeLimit;
    }

    public synchronized int getInputBufferSizeHardLimit() {
        return this.inputBufferSizeHardLimit;
    }

    public synchronized void setInputBufferSizeHardLimit(int inputBufferSizeHardLimit) {
        this.inputBufferSizeHardLimit = inputBufferSizeHardLimit;
    }

    public synchronized int getOutputBufferSizeHardLimit() {
        return this.outputBufferSizeHardLimit;
    }

    public synchronized void setOutputBufferSizeHardLimit(int outputBufferSizeHardLimit) {
        this.outputBufferSizeHardLimit = outputBufferSizeHardLimit;
    }
}

