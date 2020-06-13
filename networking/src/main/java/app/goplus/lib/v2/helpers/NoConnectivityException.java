package app.goplus.lib.v2.helpers;

import java.io.IOException;

import app.goplus.lib.v2.ErrorMessagesN;

/**
 * Created by Ankit Maheswari on 05/09/18.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return ErrorMessagesN.NETWORK_ISSUE;
    }
}
