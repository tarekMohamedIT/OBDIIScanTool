package communications;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import enums.ServiceCommand;
import enums.ServiceFlag;
import models.FaultCode;
import models.GeneralInformation;

public class ObdService extends Service {
    boolean isReadingRealData;
    boolean dataVerified;

    String jsonGeneralData;
    String jsonFaultCodes;

    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public static final String RECEIVER_ACTION = "com.r3tr0.OBDIIScanTool.communications.OBD";

    private Intent intent1;
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private ReadingThread readingThread;

    private ServiceFlag flag = ServiceFlag.disconnected;

    public ObdService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceCommand command = (ServiceCommand) intent.getSerializableExtra("cmd");

        if (intent1 == null)
            intent1 = new Intent(RECEIVER_ACTION);
        Log.e("Service", "Started with command : " + command);

        if (command == ServiceCommand.initializeJson) {
            isReadingRealData = false;
            initializeJson(intent.getStringExtra("general"), intent.getStringExtra("faults"));
            this.flag = ServiceFlag.readingJson;
        } else if (command == ServiceCommand.initializeBluetooth) {
            jsonGeneralData = null;
            this.flag = ServiceFlag.disconnected;
            initializeBluetooth((BluetoothDevice) intent.getParcelableExtra("device"));
            isReadingRealData = true;
            if (flag == ServiceFlag.connected)
                readingThread = new ReadingThread();
        } else if (command == ServiceCommand.disconnect) {
            if (bluetoothSocket != null)
                try {
                    inputStream = null;
                    outputStream = null;
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                    flag = ServiceFlag.disconnected;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else if (command == ServiceCommand.startReading) {
            if (isReadingRealData && flag == ServiceFlag.connected) {
                this.readingThread.start();
            }
        } else if (command == ServiceCommand.stopReading) {
            if (isReadingRealData) {
                this.readingThread.stopReading();
            }
        } else if (command == ServiceCommand.write) {
            String data = intent.getStringExtra("data");
            if (isReadingRealData) {
                try {
                    write((data + "\r").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                intent1.putExtra("data", readJsonFile(data));
                sendBroadcast(intent1);
                intent1.removeExtra("data");
            }
        } else if (command == ServiceCommand.getPairedDevices) {
            intent1.putExtra("devices", getAllPairedDevices());
            sendBroadcast(intent1);
            intent1.removeExtra("devices");
        } else if (command == ServiceCommand.getStatus) {
            intent1.putExtra("status", flag);
            sendBroadcast(intent1);
            intent1.removeExtra("status");
        } else {
            Toast.makeText(this, "Please put a command in the intent as integer extra", Toast.LENGTH_LONG).show();
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<GeneralInformation> getAllGeneralInformationItems() {
        ArrayList<GeneralInformation> generalInformations = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(jsonGeneralData).getJSONArray("car1");
            for (int j = 0; j < array.length(); j += 8) {
                for (int i = 0; i < 8; i++) {
                    JSONObject gaugeObject = array.getJSONObject(i + j);
                    if (j == 0)
                        generalInformations.add(new GeneralInformation(0, gaugeObject.getString("name"), new ArrayList<Float>()));
                    generalInformations.get(i).addValue((float) gaugeObject.getDouble("value"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return generalInformations;
    }

    private ArrayList<FaultCode> getAllFaultCodeItems() {
        ArrayList<FaultCode> faultCodes = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(jsonFaultCodes).getJSONArray("Hyundai");
            for (int i = 0; i < array.length(); i++) {
                JSONObject faultCodeObject = array.getJSONObject(i);
                faultCodes.add(new FaultCode(
                        faultCodeObject.getString("name")
                        , faultCodeObject.getString("Descrption")
                        , faultCodeObject.getString("ID")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return faultCodes;
    }

    public void initializeBluetooth(BluetoothDevice device) {
        //Server socket initialized
        try {
            intent1.putExtra("status", ServiceFlag.connecting);
            sendBroadcast(intent1);
            intent1.removeExtra("status");
            bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            bluetoothSocket.connect();
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket = null;
                throw new IOException("Failed to connect to this device");
            }
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                intent1.putExtra("status", ServiceFlag.connectionFailed);
                sendBroadcast(intent1);
                intent1.removeExtra("status");
                flag = ServiceFlag.connectionFailed;
                bluetoothSocket.close();
                bluetoothSocket = null;
            }

            inputStream = tempIn;
            outputStream = tempOut;

            flag = ServiceFlag.connected;
            intent1.putExtra("status", ServiceFlag.connected);
            sendBroadcast(intent1);
            intent1.removeExtra("status");

        } catch (IOException e) {
            e.printStackTrace();
            flag = ServiceFlag.connectionFailed;
            intent1.putExtra("status", ServiceFlag.connectionFailed);
            sendBroadcast(intent1);
            intent1.removeExtra("status");
            bluetoothSocket = null;
            return;
        }

        if (jsonFaultCodes != null)
            jsonFaultCodes = null;

        if (jsonGeneralData != null)
            jsonGeneralData = null;
    }

    public void initializeJson(String jsonGeneralData, String jsonFaultCodes) {

        try {
            this.jsonGeneralData = jsonGeneralData;
            getAllGeneralInformationItems();
        } catch (RuntimeException e) {
            this.flag = ServiceFlag.invalidGeneralInformation;
            intent1.putExtra("status", flag);
            sendBroadcast(intent1);
            intent1.removeExtra("status");
            return;
        }

        try {
            this.jsonFaultCodes = jsonFaultCodes;
            getAllFaultCodeItems();
        } catch (RuntimeException e) {
            this.flag = ServiceFlag.invalidFaultCodes;
            intent1.putExtra("status", flag);
            sendBroadcast(intent1);
            intent1.removeExtra("status");
            return;
        }

        if (bluetoothSocket != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bluetoothSocket = null;
        }


        this.flag = ServiceFlag.readingJson;
        intent1.putExtra("status", flag);
        sendBroadcast(intent1);
        intent1.removeExtra("status");
    }

    public void write(byte[] bytes) throws IOException {
        Log.e("sent data as bytes ", Arrays.toString(bytes));
        outputStream.write(bytes);
        outputStream.flush();
    }

    public ArrayList readJsonFile(String type) {
        if (type.equals("general"))
            return getAllGeneralInformationItems();

        else if (type.equals("fault"))
            return getAllFaultCodeItems();

        return null;
    }

    public ArrayList<BluetoothDevice> getAllPairedDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.e("bt", "size of paired : " + mBluetoothAdapter.getBondedDevices().size());
        return new ArrayList<>(mBluetoothAdapter.getBondedDevices());
    }

    private class ReadingThread extends Thread {
        private volatile boolean isStop = true;

        @Override
        public synchronized void start() {
            super.start();
            isStop = true;
        }

        @Override
        public void run() {
            super.run();

            char c;
            while (isStop) {
                try {
                    byte b = 0;
                    StringBuilder res = new StringBuilder();
                    while (((b = (byte) inputStream.read()) > -1)) {
                        c = (char) b;
                        if (c == '>') // read until '>' arrives
                        {
                            break;
                        }
                        Log.e("Data", c + "");
                        res.append(c);
                        if (res.toString().matches("TDA[0-9]+\\sV[0-9]+\\.[0-9]+\\s"))
                            break;
                    }

                    String result = res.toString().replaceAll("SEARCHING\\.+", "");
                    result = result.replaceAll("\\s", "");
                    result = result.replaceAll("(BUS INIT)|(BUSINIT)|(\\.)", "");

                    if (result.length() > 0) {
                        Log.e("test thread", "read");
                        intent1.putExtra("data", result);
                        sendBroadcast(intent1);
                        intent1.removeExtra("data");
                    } else {
                        Log.e("test thread", "read");
                        intent1.putExtra("data", "");
                        sendBroadcast(intent1);
                        intent1.removeExtra("data");
                    }
                    //bytes = inputStream.read(buffer);
                    //tempMsg = new String(buffer,0,bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopReading() {
            this.isStop = false;
        }
    }


}
