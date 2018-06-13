package communications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import events.OnBroadcastReceivedListener;

/**
 * Created by r3tr0 on 5/23/18.
 */

public class ObdReceiver extends BroadcastReceiver {

    OnBroadcastReceivedListener onBroadcastReceivedListener;

    public ObdReceiver(OnBroadcastReceivedListener onBroadcastReceivedListener) {
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
