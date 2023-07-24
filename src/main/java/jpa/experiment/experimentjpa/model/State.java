/**
 * @author Ruslan
 */

package jpa.experiment.experimentjpa.model;

import java.util.Arrays;

public enum State {
    ACTIVE("0", "0"),
    BLOCKED("1", "1"),
    DB_BLOCKED("2"),
    CR_BLOCKED("3"),
    CLOSED("4", "2"),
    PIN_BLOCK,
    EXPIRED,
    SMS_DISABLED,
    DELETED,
    SERVICE_UNAVAILABLE,
    NOT_MIGRATED;

    private String absStatus;
    private String accountStatus;

    State() {
        this.absStatus = "";
    }

    State(String absStatus) {
        this.absStatus = absStatus;
    }

    State(String absStatus, String accountStatus) {
        this.absStatus = absStatus;
        this.accountStatus = accountStatus;
    }

    public static State getByAbsStatus(String status) {
        return Arrays.stream(State.values())
                .filter(state -> state.absStatus.equals(status))
                .findFirst()
                .orElse(BLOCKED);
    }

    public static State getByAccountStatus(String accountStatus) {
        return Arrays.stream(State.values())
                .filter(state -> state.accountStatus.equals(accountStatus))
                .findFirst()
                .orElse(BLOCKED);
    }

    public String getAbsStatus() {
        return absStatus;
    }

    public void setAbsStatus(String absStatus) {
        this.absStatus = absStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
