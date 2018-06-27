package communications;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import Commands.FaultCodesCommand;
import Commands.FaultCodesNumber;
import Commands.FuelLevel;
import Commands.Speed;
import events.OnBroadcastReceivedListener;
import helpers.BaseObdCommand;

public class CommandsService extends Service {

    public static final String RECEIVER_ACTION = "com.r3tr0.OBDIIScanTool.communications.Command";

    ArrayList<BaseObdCommand> commands;
    ObdReceiver receiver;
    int currentWorkingType = -1;
    int currentWorkingCommand = -1;

    CommandThread commandThread;

    //final Intent commandIntent = new Intent(RECEIVER_ACTION);

    public CommandsService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentWorkingType = intent.getIntExtra("cmd", -1);

        if (currentWorkingType != -1) {
            if (commandThread == null)
                commandThread = new CommandThread();
            commandThread.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        commands = new ArrayList<>();
        commands.add(new FuelLevel(this));
        commands.add(new Speed(this));
        commands.add(new FaultCodesNumber(this));
        commands.add(new FaultCodesCommand(this, 0));

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;
                Object result;

                if (ObdService.isReadingRealData) {
                    result = commands.get(currentWorkingCommand)
                            .performCalculations(intent
                                    .getStringExtra("data")
                                    .getBytes());

                    if (currentWorkingCommand == 2) {
                        ((FaultCodesCommand) commands.get(3))
                                .setNumberOfFaults(
                                        (Integer) result);
                    }
                } else {
                    result = intent.getExtras().get("data");
                }

                intent.removeExtra("data");

                if (result instanceof Parcelable)
                    intent.putExtra("data", (Parcelable) result);
                else if (result instanceof ArrayList<?>)
                    intent.putExtra("data", (ArrayList) result);
                else if (result instanceof Serializable)
                    intent.putExtra("data", (Serializable) result);

                intent.putExtra("command", commands.get(currentWorkingCommand).getName());
                intent.setAction(RECEIVER_ACTION);
                sendBroadcast(intent);
            }
        });

        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getActivityName() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.flattenToShortString().replace("com.r3tr0.obdiiscantool/.", "");
    }

    public class CommandThread extends Thread {
        boolean started = false;

        @Override
        public synchronized void start() {
            if (!started) {
                super.start();
                started = true;
            }
        }

        @Override
        public void run() {
            super.run();
            while (currentWorkingType != -1) {
                for (int i = 0; i < commands.size(); i++) {
                    if (commands.get(i).getType() == currentWorkingType) {
                        currentWorkingCommand = i;
                        commands.get(i).sendCommand();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (currentWorkingType == -1) {
                        currentWorkingCommand = -1;
                        started = false;
                        break;
                    }
                }
            }
        }
    }
}
