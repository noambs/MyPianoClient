package com.noam.piano_stern.srevice;

import android.os.AsyncTask;


public class SendData extends AsyncTask<Integer, Void, Void> {

    private CommunicationThread device;

    public SendData(CommunicationThread mDevice)
    {
        device = mDevice;

    }


    @Override
    protected Void doInBackground(Integer... params) {
        int data = params[0];

        device.sendData("*"+data+"#");
        return null;
    }
}