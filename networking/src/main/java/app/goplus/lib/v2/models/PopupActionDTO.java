package app.goplus.lib.v2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.goplus.lib.v2.utils.appConfig.butler.ActionDTO;

/**
 * Created by ankitmaheswari on 28/08/17.
 */

public class PopupActionDTO {

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("action")
    @Expose
    public ActionDTO actionDTO;
}
