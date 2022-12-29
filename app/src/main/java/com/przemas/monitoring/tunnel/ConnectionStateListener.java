package com.przemas.monitoring.tunnel;

public interface ConnectionStateListener {
    void connectionAlive();

    void connectionDown();

    void connectionFailure();

    void connectionSuccess();
}
