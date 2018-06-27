package Commands;

import android.content.Context;

import enums.CommandType;
import helpers.BaseObdCommand;

public class FaultCodesNumber extends BaseObdCommand<Integer> {
    public FaultCodesNumber(Context context) {
        super(context, "0101");
    }

    @Override
    public String getName() {
        return "Faults number";
    }

    @Override
    public Integer getType() {
        return CommandType.diagnosis.getValue();
    }

    @Override
    public Integer performCalculations(byte[] bytes) {
        return Integer.parseInt(((char) (bytes[4] & 0xff)) + "" + ((char) (bytes[5] & 0xff)), 16);
    }
}
