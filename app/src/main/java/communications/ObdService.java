package communications;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import models.GeneralInformationModel;

public class ObdService extends Service {
    public static final int COMMAND_JSON = 0;
    public static final int COMMAND_BLUETOOTH = 1;
    public static final int COMMAND_READ_ALL = 2;

    public static final int TYPE_GENERAL_INFO = 0;
    public static final int TYPE_FAULT_CODE = 1;

    boolean isReadingRealData;
    boolean dataVerified;
    String jsonData;
    BluetoothDevice device;

    ArrayList<GeneralInformationModel> generalInformations;


    public ObdService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra("cmd", -1);

        Log.e("Service", "Started with command : " + command);

        if (command == COMMAND_JSON) {
            jsonData = intent.getStringExtra("json");
            Log.e("jason_data",jsonData);
            if (jsonData != null && !jsonData.equals("")) {
                Toast.makeText(this, "Json data inserted!", Toast.LENGTH_LONG).show();
                dataVerified = true;
            } else {
                Toast.makeText(this, "Json data does not exist!", Toast.LENGTH_LONG).show();
                Log.e("dsa", "inserted");
                dataVerified = false;
            }

            device = null;
            isReadingRealData = false;
        } else if (command == COMMAND_BLUETOOTH) {
            jsonData = null;
            device = intent.getParcelableExtra("device");
            isReadingRealData = true;
            Toast.makeText(this, "Device is ready!", Toast.LENGTH_LONG).show();
        } else if (command == COMMAND_READ_ALL) {
            if (intent.getIntExtra("type", -1) == TYPE_GENERAL_INFO) {
                Intent intent1 = new Intent("com.r3tr0.obdiiscantool.Obd");
                intent1.putExtra("type", "general");
                intent1.putExtra("data", getAllGeneralInformationItems(false));
                sendBroadcast(intent1);
            }
        } else {
            Toast.makeText(this, "Please put a command in the intent as integer extra", Toast.LENGTH_LONG).show();
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<GeneralInformationModel> getAllGeneralInformationItems(boolean makeNew) {

        if (makeNew || generalInformations == null) {
            try {
                generalInformations = new ArrayList<>();
                JSONArray array = new JSONObject(jsonData).getJSONArray("car1");
                for (int j = 0; j < array.length(); j += 8) {
                    for (int i = 0; i < 8; i++) {
                        JSONObject gaugeObject = array.getJSONObject(i + j);
                        if (j == 0)
                            generalInformations.add(new GeneralInformationModel(0, gaugeObject.getString("name"), new ArrayList<Float>()));
                        generalInformations.get(i).addValue((float) gaugeObject.getDouble("value"));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return generalInformations;

    }

    private class BluetoothConnection {
        BluetoothSocket bluetoothSocket;

        public BluetoothConnection() {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                bluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
