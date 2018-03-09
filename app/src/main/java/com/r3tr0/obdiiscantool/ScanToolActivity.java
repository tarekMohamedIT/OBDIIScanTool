package com.r3tr0.obdiiscantool;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.r3tr0.graphengine.core.circles.CircularProgressBar;

import bluetooth.BluetoothClient;

public class ScanToolActivity extends AppCompatActivity {

    BluetoothClient client;

    Button disconnectButton;
    CircularProgressBar progressBar;
    TextView speedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_tool);

        disconnectButton = findViewById(R.id.disconnectButton);
        progressBar = findViewById(R.id.speedBar);
        speedTextView = findViewById(R.id.speedTextView);

        progressBar.setTotalProgress(200);
        progressBar.setCurrentProgress(80);

        speedTextView.setText("Current Speed : 80 km/hr");

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScanToolActivity.this, "Disconnected", Toast.LENGTH_LONG).show();
                if (client.getConnectionState() == BluetoothClient.STATE_CONNECTED){
                    client.disconnect();
                }
                finish();
            }
        });

        client = new BluetoothClient(this);
        String address = getIntent().getStringExtra("mac_address");

        BluetoothDevice device = client.getBluetoothDeviceByAddress(address);
        client.setOnClientStateChangingListener(new BluetoothClient.OnClientStateChangingListener() {
            @Override
            public void onClientConnected() {
                Toast.makeText(ScanToolActivity.this, "Connected to remote device", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClientDisconnected() {
                Toast.makeText(ScanToolActivity.this, "Disconnected from remote device", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onClientConnectionFailed() {
                Toast.makeText(ScanToolActivity.this, "Failed to connect to remote device", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        client.connectToDevice(device);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.kill();
    }
}
