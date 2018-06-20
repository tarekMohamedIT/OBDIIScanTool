package enums;

public enum RecyclerListAdapterMode {
    fullMode(0), fullWithTitleAndSubTitleMode(1), partMode(2);

    private final int value;

    RecyclerListAdapterMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
