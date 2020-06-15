package app.goplus.lib.v2;

import app.goplus.lib.R;
import app.goplus.lib.v2.network.Network;

/**
 * Created by ankitsharma on 3/2/16.
 */
public class ErrorMessagesN {

    public static final String GENERIC_ERROR_MSG = Network.Companion.getStringFromRes(R.string.error_generic_message);
    public static final String NETWORK_ISSUE = Network.Companion.getStringFromRes(R.string.error_network_issue);
    public static final String REQUEST_TIMED_OUT = Network.Companion.getStringFromRes(R.string.error_request_timed_out);
    public static final String SOCKET_EXCEPTION = Network.Companion.getStringFromRes(R.string.error_message_retry);
    public static final String IO_EXCEPTION = Network.Companion.getStringFromRes(R.string.error_message_retry);;
    public static final String GENERIC_ERROR_TITLE = "Error";
}
