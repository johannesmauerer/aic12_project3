package aic12.project3.common.beans;

public class SentimentRequestCallback extends SentimentRequest
{
    private String callbackAddress;

    public String getCallbackAddress()
    {
        return callbackAddress;
    }

    public void setCallbackAddress(String callbackAddress)
    {
        this.callbackAddress = callbackAddress;
    }
}
