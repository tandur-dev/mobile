package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;
}
