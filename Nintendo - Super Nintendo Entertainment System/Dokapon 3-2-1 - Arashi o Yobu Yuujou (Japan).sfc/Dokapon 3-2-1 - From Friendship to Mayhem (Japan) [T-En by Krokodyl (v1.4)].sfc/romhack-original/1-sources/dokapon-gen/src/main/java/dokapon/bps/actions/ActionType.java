package dokapon.bps.actions;

public enum ActionType {

    SOURCE_READ,
    TARGET_READ,
    SOURCE_COPY,
    TARGET_COPY;

    public static ActionType getAction(int value) {
        return ActionType.values()[value];
    }

}
