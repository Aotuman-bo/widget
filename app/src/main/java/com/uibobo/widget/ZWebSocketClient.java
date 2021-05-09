package com.uibobo.widget;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
/**
 * @Author: bo
 * @Date: 2021/5/8 22:21
 */
public class ZWebSocketClient extends WebSocketClient {
    public ZWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("ZWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.e("ZWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("ZWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("ZWebSocketClient", "onError()");
    }
}