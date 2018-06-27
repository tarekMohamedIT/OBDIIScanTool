package Commands;

import android.content.Context;

import enums.CommandType;
import helpers.BaseObdCommand;

public class Speed extends BaseObdCommand<Integer> {

    public Speed(Context context) {
        super(context, "010D");
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public Integer getType() {
        return CommandType.general.getValue();
    }

    @Override
    public Integer performCalculations(byte[] bytes) {
        Byte speed = bytes[2];
        return (speed.intValue() & 0xff);
    }
}
