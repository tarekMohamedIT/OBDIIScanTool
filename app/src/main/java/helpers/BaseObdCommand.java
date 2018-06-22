package helpers;

import android.content.Context;
import android.content.Intent;

import communications.ObdService;
import enums.ServiceCommand;

public abstract class BaseObdCommand<output> {
    String commandString;
    private final Intent intent;
    private Context context;

    public BaseObdCommand(Context context, String commandString) {
        this.context = context;
        this.commandString = commandString;
        intent = new Intent(context, ObdService.class);
    }

    public abstract String getName();

    public abstract output performCalculations(byte[] bytes);

    public void sendCommand() {
        synchronized (intent) {
            intent.putExtra("cmd", ServiceCommand.write);
            intent.putExtra("data", commandString);
            context.startService(intent);
        }
    }

    public String getCommandString() {
        return commandString;
    }
}
