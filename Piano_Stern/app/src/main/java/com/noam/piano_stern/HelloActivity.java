package com.noam.piano_stern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class HelloActivity extends Activity {
    //public static HelloActivity context;
   // private CommunicationThread client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
       // context = this;
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CommunicationThread.permission = Settings.System.canWrite(this);
        } else{
            CommunicationThread.permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if(client==null)
        {
            client = new CommunicationThread();

        }*/
        /*if(!Server.permission)
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            this.startActivity(intent);
        }else{
            if(CommunicationThread.permission)
            {
                //server.setWifiProxySettings5();
                if(!client.isAlive())
                {
                    client.start();

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Intent intent = new Intent();
                        intent.setClass(HelloActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent();
                intent.setClass(HelloActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

   /* @Override
    protected void onResume()
    {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CommunicationThread.permission = Settings.System.canWrite(this);
        } else{
            CommunicationThread.permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if(CommunicationThread.permission)
        {
            //if(!server.getIpConfig())
              //  server.setWifiProxySettings5();

            if(!client.isAlive())
            {
                client.start();

            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent intent = new Intent();
                    intent.setClass(HelloActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 1000);

        }
    }*/
}
