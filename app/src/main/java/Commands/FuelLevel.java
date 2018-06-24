package Commands;

import android.content.Context;

import helpers.BaseObdCommand;

public class FuelLevel extends BaseObdCommand<Integer> {


    public FuelLevel(Context context, String commandString) {
        super(context, commandString);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer performCalculations(byte[] bytes) {
        return null;
    }
}
