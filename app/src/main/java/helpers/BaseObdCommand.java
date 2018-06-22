package helpers;

import android.content.Context;
import android.content.Intent;

import communications.ObdService;
import enums.ServiceCommand;

public abstract class BaseObdCommand<output> {
    String commandString;
    Context context;

    public BaseObdCommand(Context context, String commandString) {
        this.context = context;
        this.commandString = commandString;
    }

    public abstract String getName();

    public abstract output performCalculations(byte[] bytes);

    public void sendCommand() {
        Intent intent = new Intent(context, ObdService.class);
        intent.putExtra("cmd", ServiceCommand.write);
        intent.putExtra("data", commandString);
    }

    public String getCommandString() {
        return commandString;
    }
}
