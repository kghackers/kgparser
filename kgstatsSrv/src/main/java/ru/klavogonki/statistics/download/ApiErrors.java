package ru.klavogonki.statistics.download;

public final class ApiErrors {

    public static final int OK_CORRECT_VALUE = 1; // "ok: 1" and present "err" are mutually-exclusive

    // common error
    public static final String INVALID_USER_ID_ERROR = "invalid user id";

    // happens in /get-index-data
    public static final String HIDDEN_PROFILE_USER_ERROR = "hidden profile";

    // happens on /get-index-data
    public static final String MONGO_REFS_ERROR_USER_498727 = "mongo refs joining failed: invalid key users.achieves.achieve_id=599bd392df4e4d963a8b4570";

    // happens in /get-stats-overview
    public static final String PERMISSION_BLOCKED_ERROR = "permission blocked";

    // happens in /get-stats-overview (maybe "stats only visible to friends")
    public static final String PERMISSION_FRIENDS = "permission friends";

    private ApiErrors() {
    }
}
