
package com.cbnu.sweng.randombox.dictation_user.dictation_user.model.PnuNlpSpeller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Error implements Serializable
{

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

}
