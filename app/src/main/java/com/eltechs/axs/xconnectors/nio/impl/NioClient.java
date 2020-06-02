package com.eltechs.axs.xconnectors.nio.impl;

import java.nio.channels.SocketChannel;

public class NioClient<Context> {
    private final Context ctx;
    private final NioXInputStream inputStream;
    private final NioXOutputStream outputStream;
    private final SocketChannel socket;

    public NioClient(Context ctx, SocketChannel socket, NioXInputStream inputStream, NioXOutputStream outputStream) {
        this.ctx = ctx;
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Context getCtx() {
        return this.ctx;
    }

    public SocketChannel getSocket() {
        return this.socket;
    }

    public NioXInputStream getInputStream() {
        return this.inputStream;
    }

    public NioXOutputStream getOutputStream() {
        return this.outputStream;
    }
}

