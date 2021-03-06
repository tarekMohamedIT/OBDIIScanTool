package Commands;

import android.content.Context;

import enums.CommandType;
import helpers.BaseObdCommand;

public class FuelLevel extends BaseObdCommand<Integer> {


    public FuelLevel(Context context) {
        super(context, "012F");
    }

    @Override
    public String getName() {
        return "fuel level";
    }

    @Override
    public Integer getType() {
        return CommandType.general.getValue();
    }

    @Override
    public Integer performCalculations(byte[] bytes) {
        return (100 / 255) * (((Byte) bytes[2]).intValue() & 0xff);
    }
}
