package enums;

public enum CommandType {
    general(0), diagnosis(1), emergency(2), diagnosis_emergency(3), general_emergency(4), none(-1);

    private final int value;

    CommandType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
