package helpers;

import android.content.Context;
import android.content.Intent;

import communications.ObdService;
import enums.ServiceCommand;

public abstract class BaseObdCommand<output> {
    protected output result;
    String commandString;
    Context context;

    public BaseObdCommand(Context context, String commandString) {
        this.context = context;
        this.commandString = commandString;
    }

    public void sendCommand() {
        Intent intent = new Intent(context, ObdService.class);
        intent.putExtra("cmd", ServiceCommand.write);
        intent.putExtra("data", commandString);
    }

    abstract void performCalculations(byte[] bytes);

    public output getResult() {
        return result;
    }
}
