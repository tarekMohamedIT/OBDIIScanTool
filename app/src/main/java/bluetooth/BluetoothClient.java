package bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tarek on 7/24/17.
 */

public class BluetoothClient {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final int STATE_IDLE = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCONNECTED = 3;


    public static final int STATE_CONNECTION_IDLE = 0;
    public static final int STATE_CONNECTION_FAILED = 1;
    public static final int STATE_CONNECTION_SUCCESS = 2;

    private int state = STATE_IDLE;
    private int connectionState = STATE_CONNECTION_IDLE;

    private BluetoothSocket bluetoothSocket;
    private BluetoothAdapter adapter;
    private Context context;

    private OutputStream outputStream;
    private InputStream inputStream;

    private OnClientStateChangingListener onClientStateChangingListener;

    public BluetoothClient(Context context){
        this.context = context;
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int getState() {
        return state;
    }

    public int getConnectionState() {
        return connectionState;
    }

    public void setOnClientStateChangingListener(OnClientStateChangingListener onClientStateChangingListener) {
        this.onClientStateChangingListener = onClientStateChangingListener;
    }

    public BluetoothDevice getBluetoothDeviceByAddress(String address){
        return adapter.getRemoteDevice(address);
    }

    public List<BluetoothDevice> getPairedDevicesList(){
        if (!adapter.isEnabled())
            startBluetoothService();
        adapter.cancelDiscovery();
        return new ArrayList<>(adapter.getBondedDevices());
    }

    public void write(String line){
        try {
            adapter.cancelDiscovery();
            outputStream.write(line.getBytes());
            //outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(){
        try {
            adapter.cancelDiscovery();
            return inputStream.read() + "";
            //outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public synchronized void startBluetoothService(){
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)context).startActivityForResult(enableBtIntent, 1);
        }
    }

    public synchronized void connectToDevice(BluetoothDevice device){
        ConnectThread connectThread = new ConnectThread(device);
        connectThread.run();
    }

    public synchronized void disconnect(){

        if (bluetoothSocket != null) try {

            bluetoothSocket.close();
            bluetoothSocket = null;
            state = STATE_DISCONNECTED;
            connectionState = STATE_CONNECTION_IDLE;
            if (onClientStateChangingListener != null) onClientStateChangingListener.onClientDisconnected();

        } catch (IOException e) {
            Log.e("disconnection process", "Disconnection failed");
            e.printStackTrace();
        }
    }

    private class ConnectThread extends Thread {


        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                Log.e("connect thread", tmp != null ? "RFcomm created" : "RFcomm failed");
                state = STATE_CONNECTING;
                connectionState = STATE_CONNECTION_IDLE;
            } catch (IOException e) {
                e.printStackTrace();
            }

            bluetoothSocket = tmp;
        }


        @Override
        public void run() {
            super.run();

            adapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                Log.e("connect thread", "Connected to remote device");
                connectionState = STATE_CONNECTION_SUCCESS;
                state = STATE_CONNECTED;

                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
                if (onClientStateChangingListener != null) onClientStateChangingListener.onClientConnected();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("connect thread", "Connection failed");
                try {
                    if (bluetoothSocket != null)
                        bluetoothSocket.close();
                    connectionState = STATE_CONNECTION_FAILED;
                    state = STATE_DISCONNECTED;
                    if (onClientStateChangingListener != null) onClientStateChangingListener.onClientConnectionFailed();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    connectionState = STATE_CONNECTION_FAILED;
                    state = STATE_DISCONNECTED;
                    if (onClientStateChangingListener != null) onClientStateChangingListener.onClientConnectionFailed();
                }
            }

        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (bluetoothSocket != null) {
            bluetoothSocket.close();
            bluetoothSocket = null;
        }
        if (adapter != null){
            adapter = null;
        }
    }

    public interface OnClientStateChangingListener {
        void onClientConnected();
        void onClientDisconnected();
        void onClientConnectionFailed();
    }

    public void kill(){
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothSocket = null;
        }
        if (adapter != null){
            adapter = null;
        }
    }

}
