package communications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import events.OnBroadcastReceivedListener;

public class CommandsReceiver extends BroadcastReceiver {
    OnBroadcastReceivedListener onBroadcastReceivedListener;

    public CommandsReceiver(OnBroadcastReceivedListener onBroadcastReceivedListener) {
        this.onBroadcastReceivedListener = onBroadcastReceivedListener;
    }

    public void setOnBroadcastReceivedListener(OnBroadcastReceivedListener onBroadcastReceivedListener) {
        this.onBroadcastReceivedListener = onBroadcastReceivedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onBroadcastReceivedListener != null)
            onBroadcastReceivedListener.onBroadcastReceived(intent);
    }

}
