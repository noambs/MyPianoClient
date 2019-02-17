package com.noam.piano_stern.srevice;

import android.os.AsyncTask;


public class SendData extends AsyncTask<String, Void, Void> {

    private CommunicationThread device;

    public SendData(CommunicationThread mDevice)
    {
        device = mDevice;

    }


    @Override
    protected Void doInBackground(String... params) {
        String data = params[0];

        device.sendData("*"+data+"#");
        return null;
    }
}