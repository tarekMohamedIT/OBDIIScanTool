package Commands;

import android.content.Context;

import java.util.ArrayList;

import enums.CommandType;
import helpers.BaseObdCommand;

public class FaultCodesCommand extends BaseObdCommand<ArrayList<String>> {
    private int numberOfFaults;

    public FaultCodesCommand(Context context, int numberOfFaults) {
        super(context, "03");
        this.numberOfFaults = numberOfFaults;
    }

    public void setNumberOfFaults(int numberOfFaults) {
        this.numberOfFaults = numberOfFaults;
    }

    @Override
    public String getName() {
        return "Fault codes";
    }

    @Override
    public Integer getType() {
        return CommandType.diagnosis.getValue();
    }

    @Override
    public ArrayList<String> performCalculations(byte[] bytes) {
        if (numberOfFaults == 0)
            return null;
        else {
            ArrayList<String> strings = new ArrayList<>();
            int counter = (numberOfFaults * 4);

            for (int i = 2; i <= counter; i += 4) {
                int identifyErrorType = Integer.parseInt(String.valueOf(((char) (bytes[i] & 0xff))), 16);
                int FirstByte = Integer.parseInt(String.valueOf(((char) (bytes[i + 1] & 0xff))), 16);
                int SecondByte = Integer.parseInt(String.valueOf(((char) (bytes[i + 2] & 0xff))), 16);
                int ThirdByte = Integer.parseInt(String.valueOf(((char) (bytes[i + 3] & 0xff))), 16);
                if (identifyErrorType == 0 || identifyErrorType == 1 || identifyErrorType == 2 || identifyErrorType == 3) {
                    strings.add("Fault Code : " + "P" + identifyErrorType + FirstByte + SecondByte + ThirdByte);

                } else if (identifyErrorType == 4 || identifyErrorType == 5 || identifyErrorType == 6 || identifyErrorType == 7) {
                    strings.add("Fault Code : " + "C" + identifyErrorType + FirstByte + SecondByte + ThirdByte);
                } else if (identifyErrorType == 8 || identifyErrorType == 9 || identifyErrorType == 10 || identifyErrorType == 11) {
                    strings.add("Fault Code : " + "B" + identifyErrorType + FirstByte + SecondByte + ThirdByte);
                } else if (identifyErrorType == 12 || identifyErrorType == 13 || identifyErrorType == 14 || identifyErrorType == 15) {
                    strings.add("Fault Code : " + "U" + identifyErrorType + FirstByte + SecondByte + ThirdByte);
                }
            }

            return strings;
        }
    }
}
