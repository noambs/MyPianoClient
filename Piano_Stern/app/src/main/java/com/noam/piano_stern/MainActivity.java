package com.noam.piano_stern;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.noam.piano_stern.srevice.CommunicationThread;
import com.noam.piano_stern.srevice.SendData;
import com.noam.piano_stern.utils.RecordInfo;
import android.media.SoundPool.OnLoadCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class MainActivity extends Activity {

    private static final int LOCATION = 1;

    private final AtomicLong playTick = new AtomicLong(0);

    private Map<Integer, Integer> tunesSoundMap = new HashMap<Integer, Integer>();

    private SoundPool soundPool;

    private CommunicationThread client;

    private boolean isPlaying = false;

    private Timer playTimer = null;

    public static final int RECORD_INTERVAL = 20;

    private Map<Long, List<RecordInfo>> timeTunesMap = new HashMap<>();

    private Button song1;

    private Button song2;

    private Button song3;

    private Button song4;

    private Button song5;

    private Button song6;

    private Handler handler;

    private long currentTick;

    private final Context context = MainActivity.this;

    private long song_array_size ;

    private int tunes ;

    private ImageView btn;

    private Timer timer;

    private TimerTask timerTask;

    private Timer timerWifi;

    private TimerTask timerTaskWifi;

    private MediaPlayer mp;

    private boolean loaded;

    private MediaPlayer.OnCompletionListener mediaListener;

    private boolean isRepeat;

    private boolean wifiStatus = false;

    private ArrayList<String> playList;

    private Button playListButton;

    private CheckBox repeat;

    private boolean isPlayList = false;

    private int playListCounter = 0;

    private int song_number;
    int s1;
    int s2;
    int s3;
    int s4;
    int s5;
    int s6;
    int s7;

    int s10;
    int s11;     // 1
    int s13 ;
    int s14 ;
    int s15 ;

    ImageView s10Btn;
    ImageView s11Btn;
    ImageView s13Btn;
    ImageView s14Btn;
    ImageView s15Btn;

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song_number = 0;
        if(getWifiStatus())
            client = new CommunicationThread();

        if(client!=null && client.isConnected())
        {
            startTimer();
           /* WifiManager wifiMgr = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);
            sendDataToServer("IP:"+ipAddress);*/
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(attributes)
                    .setMaxStreams(6)
                    .build();
        }else{
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mediaListener = new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mplayer) {
                if(!isRepeat && !isPlayList)
                {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    mp.stop();
                    mp.release();
                    mp = null;
                }else{
                    if(isPlayList && !isRepeat)
                    {
                        if(playListCounter < playList.size()-1)
                        {
                            mp.reset();
                            if (playTimer != null) {
                                playTimer.cancel();
                                playTimer = null;
                            }
                            playListCounter++;
                            PlayingSong(playList.get(playListCounter),null, 7);


                        }else{
                            isPlayList = false;
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            mp.stop();
                            mp.release();
                            mp = null;
                            PlayingSong("play list", playListButton, 7);
                        }
                    }else{
                        if(isPlayList && isRepeat)
                        {
                            if (playTimer != null) {
                                playTimer.cancel();
                                playTimer = null;
                            }


                            currentTick = 0;
                            playTick.set(0);

                            if(playListCounter == playList.size())
                            {
                                mp.stop();
                                mp.reset();
                                mp.release();
                                mp = null;
                                playListCounter = 0;
                                PlayingSong(playList.get(playListCounter),null, 7);
                                playListCounter++;
                            }else{
                                if(playListCounter < playList.size())
                                {
                                    mp.stop();
                                    mp.reset();
                                    mp.release();
                                    mp = null;


                                    PlayingSong(playList.get(playListCounter),null, 7);
                                    playListCounter++;

                                }

                            }



                        }else{
                            if(!isPlayList && isRepeat)
                            {
                                mp.seekTo(0);
                                mp.start();
                                currentTick = 0;
                                playTick.set(0);
                            }


                        }
                    }


                }

            }
        };
        loaded = false;


        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                tunesSoundMap.put(1, s1);
                tunesSoundMap.put(2, s2);
                tunesSoundMap.put(3, s3);
                tunesSoundMap.put(4, s4);
                tunesSoundMap.put(5, s5);
                tunesSoundMap.put(6, s6);
                tunesSoundMap.put(7, s7);


                tunesSoundMap.put(10, s10);
                tunesSoundMap.put(11, s11);
                tunesSoundMap.put(13, s13);
                tunesSoundMap.put(14, s14);
                tunesSoundMap.put(15, s15);
                loaded = true;

            }
        });


        s1 = soundPool.load(context, R.raw.note_c, 1);
        s2 = soundPool.load(context, R.raw.note_d, 1);
        s3 = soundPool.load(context, R.raw.note_e, 1);
        s4 = soundPool.load(context, R.raw.note_f, 1);
        s5 = soundPool.load(context, R.raw.note_g, 1);
        s6 = soundPool.load(context, R.raw.note_a, 1);
        s7 = soundPool.load(context, R.raw.note_b, 1);



        s10 = soundPool.load(context, R.raw.note_c1, 1);
        s11 = soundPool.load(context, R.raw.note_d1, 1);     // 1
        s13 = soundPool.load(context, R.raw.note_c2, 1);
        s14 = soundPool.load(context, R.raw.s14, 1);
        s15 = soundPool.load(context, R.raw.s15, 1);




        ImageView s1Btn = (ImageView) this.findViewById(R.id.s1_btn);
        ImageView s2Btn = (ImageView) this.findViewById(R.id.s2_btn);
        ImageView s3Btn = (ImageView) this.findViewById(R.id.s3_btn);
        ImageView s4Btn = (ImageView) this.findViewById(R.id.s4_btn);
        ImageView s5Btn = (ImageView) this.findViewById(R.id.s5_btn);
        ImageView s6Btn = (ImageView) this.findViewById(R.id.s6_btn);
        ImageView s7Btn = (ImageView) this.findViewById(R.id.s7_btn);


         s10Btn = (ImageView) this.findViewById(R.id.s10_btn);
         s11Btn = (ImageView) this.findViewById(R.id.s11_btn);
         s13Btn = (ImageView) this.findViewById(R.id.s13_btn);
         s14Btn = (ImageView) this.findViewById(R.id.s14_btn);
         s15Btn = (ImageView) this.findViewById(R.id.s15_btn);

        song1 = (Button) this.findViewById(R.id.btn_song1);
        song2 = (Button) this.findViewById(R.id.btn_song2);
        song3 = (Button) this.findViewById(R.id.btn_song3);
        song4 = (Button) this.findViewById(R.id.btn_song4);
        song5 = (Button) this.findViewById(R.id.btn_song5);
        song6 = (Button) this.findViewById(R.id.btn_song6);

        playList = new ArrayList<>();
        playList.add(getResources().getString(R.string.song1));
        playList.add(getResources().getString(R.string.song2));
        playList.add(getResources().getString(R.string.song3));
        playList.add(getResources().getString(R.string.song4));
        playList.add(getResources().getString(R.string.song5));
        playList.add(getResources().getString(R.string.song6));

        repeat = (CheckBox) this.findViewById(R.id.repeat);

        playListButton = (Button) this.findViewById(R.id.btn_playlist);


        s1Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return playSound(1, motionEvent, view);

            }
        });
        s2Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(2, motionEvent, view);
            }
        });
        s3Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(3, motionEvent, view);
            }
        });
        s4Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(4, motionEvent, view);
            }
        });
        s5Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(5, motionEvent, view);
            }
        });
        s6Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(6, motionEvent, view);
            }
        });
        s7Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(7, motionEvent, view);
            }
        });
        

        s10Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(10, motionEvent, view);
            }
        });
        s11Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(11, motionEvent, view);
            }
        });
        s13Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(13, motionEvent, view);
            }
        });
        s14Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(14, motionEvent, view);
            }
        });
        s15Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(15, motionEvent, view);
            }
        });




        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMyMessage(msg);
            }
        };

        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    isRepeat = true;
                    repeat.setButtonDrawable(getResources().getDrawable(R.drawable.repeat_icon_select));
                }else{
                    isRepeat = false;
                    repeat.setButtonDrawable(getResources().getDrawable(R.drawable.repeat_icon));
                }
            }
        });

        playListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlayList = !isPlayList;
                if(isPlaying)
                {
                    PlayingSong("play list", playListButton, 7);
                }else{
                    playListCounter = 0;
                    PlayingSong(playList.get(playListCounter),playListButton, 7);
                    playListCounter++;
                }

            }
        });


        song1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PlayingSong(getResources().getString(R.string.song1), song1, 1);

            }
        });

        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayingSong(getResources().getString(R.string.song2), song2, 2);
            }
        });



        song3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayingSong(getResources().getString(R.string.song3), song3, 3);
            }
        });

        song4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayingSong(getResources().getString(R.string.song4), song4, 4);
            }
        });

        song5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayingSong(getResources().getString(R.string.song5), song5, 5);
            }
        });

        song6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayingSong(getResources().getString(R.string.song6), song6, 6);
            }
        });

    }



    private boolean playSound(int id, MotionEvent motionEvent, View view) {
        if ((motionEvent == null || motionEvent.getAction() == MotionEvent.ACTION_DOWN) && loaded) {
            soundPool.play(tunesSoundMap.get(id), 1.0f, 1.0f, 0, 0, 1);

           if(client!=null)
           {
               if(client.isConnected())
                   if(id<8)
                    sendDataToServer(String.valueOf(id));

           }

            s10Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
            s11Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
            s13Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
            s14Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
            s15Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
        }
        return false;
    }





    private void addSongToPlay(String mySong)
    {
        long time = 1;
        int tunes = 1;
        timeTunesMap.clear();
        int []songs_notes;
        long  time_nots;
        switch(mySong)
        {
            case "clearday":
                                    songs_notes = getResources().getIntArray(R.array.clearday_Notes);
                                    time_nots = 30;
                                    for(int i=0;i<songs_notes.length;i++)
                                    {

                                        song_array_size = time_nots;

                                        List<RecordInfo> recordInfoList = new ArrayList<>();

                                        recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                        timeTunesMap.put(time_nots, recordInfoList);
                                        time_nots+=20;

                                    }
                                    mp = MediaPlayer.create(context,R.raw.bensound_clearday);
                                    mp.setOnCompletionListener(mediaListener);
                                    mp.start();

                                    break;

            case "hipjazz":
                            songs_notes = getResources().getIntArray(R.array.hipjazz_Notes);
                            time_nots = 1;
                            for(int i=0;i<songs_notes.length;i++)
                            {

                                song_array_size = time_nots;

                                List<RecordInfo> recordInfoList = new ArrayList<>();

                                recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                timeTunesMap.put(time_nots, recordInfoList);
                                time_nots+=20;

                            }
                            mp = MediaPlayer.create(context,R.raw.bensound_hipjazz);
                            mp.setOnCompletionListener(mediaListener);
                            mp.start();

                            break;

            case "retrosoul":
                            songs_notes = getResources().getIntArray(R.array.retrosoul_Notes);
                            time_nots = 30;
                            for(int i=0;i<songs_notes.length;i++)
                            {

                                song_array_size = time_nots;

                                List<RecordInfo> recordInfoList = new ArrayList<>();

                                recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                timeTunesMap.put(time_nots, recordInfoList);
                                time_nots+=20;

                            }
                            mp = MediaPlayer.create(context,R.raw.bensound_retrosoul);
                            mp.setOnCompletionListener(mediaListener);
                            mp.start();

                            break;

            case "memories":
                                songs_notes = getResources().getIntArray(R.array.memories_Notes);
                                time_nots = 1;
                                for(int i=0;i<songs_notes.length;i++)
                                {

                                    song_array_size = time_nots;

                                    List<RecordInfo> recordInfoList = new ArrayList<>();

                                    recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                    timeTunesMap.put(time_nots, recordInfoList);
                                    time_nots+=35;

                                }
                                mp = MediaPlayer.create(context,R.raw.bensound_memories);
                                mp.setOnCompletionListener(mediaListener);
                                mp.start();

                                break;

            case "allthat":
                            songs_notes = getResources().getIntArray(R.array.allthat_Notes);
                            time_nots = 1;
                            for(int i=0;i<songs_notes.length;i++)
                            {

                                song_array_size = time_nots;

                                List<RecordInfo> recordInfoList = new ArrayList<>();

                                recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                timeTunesMap.put(time_nots, recordInfoList);
                                time_nots+=20;

                            }
                            mp = MediaPlayer.create(context,R.raw.bensound_allthat);
                            mp.setOnCompletionListener(mediaListener);
                            mp.start();

                            break;

            case "ssanova":
                            songs_notes = getResources().getIntArray(R.array.ssanova_Notes);
                            time_nots = 50;
                            for(int i=0;i<songs_notes.length;i++)
                            {

                                song_array_size = time_nots;

                                List<RecordInfo> recordInfoList = new ArrayList<>();

                                recordInfoList.add(new RecordInfo(songs_notes[i], null));
                                timeTunesMap.put(time_nots, recordInfoList);
                                time_nots+=35;

                            }
                            mp = MediaPlayer.create(context,R.raw.bensound_theelevatorbossanova);
                            mp.setOnCompletionListener(mediaListener);
                            mp.start();

                            break;

        }



    }


    private void PlayingSong(final String song_name, Button button, int idSong)
    {

        if(song_number == idSong || song_number == 0)
        {
            if (isPlaying && !isPlayList) {
                isPlaying = false;
                song_number = 0;
                if(button!=null)
                    button.setText(song_name);
                if(mp!=null)
                {
                    mp.stop();
                    mp.release();
                    mp = null;
                }

                if (playTimer != null) {
                    playTimer.cancel();
                    playTimer = null;
                }
            } else {
                addSongToPlay(song_name);
                song_number = idSong;
                isPlaying = true;
                if(button!=null)
                    button.setText("Stop play");
                playTick.set(0);
                playTimer = new Timer();
                playTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        currentTick = playTick.incrementAndGet();
                        if (timeTunesMap.containsKey(currentTick)) {
                            List<RecordInfo> recordInfoList = timeTunesMap.get(currentTick);

                            if(recordInfoList!=null)
                            {
                                if (recordInfoList.isEmpty()) {
                                    playTimer.cancel();
                                    Message playEndMessage = new Message();
                                    playEndMessage.what = 2;
                                    handler.sendMessage(playEndMessage);
                                }

                                for (RecordInfo recordInfo : recordInfoList) {
                                    //int tunes = recordInfo.getTunes();
                                    //playSound(recordInfo.getTunes(), null, null);
                                    Message message = new Message();
                                    message.what = 1;
                                    message.arg1 = recordInfo.getTunes();
                                    message.obj = recordInfo.getButton();
                                    handler.sendMessage(message);

                                }
                            }



                        }



                    }
                }, 0, RECORD_INTERVAL);


            }
        }

    }


    private void handleMyMessage(Message msg)
    {
        tunes = 0;

        if (msg.what == 1) {


            if (msg.obj != null) {
                btn = (ImageView) msg.obj;
            } else {
                tunes = msg.arg1;
                btn = (ImageView) findViewById(getResources().getIdentifier("s" + tunes + "_btn", "id", "com.noam.piano_stern"));
            }

            if ("B".equals(btn.getTag())) {
                btn.setBackground(getResources().getDrawable(R.drawable.black_key_pressed));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                    }
                }, 150);
            } else {

                switch (tunes)
                {
                    case 1: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_1));

                            break;
                    case 2: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_2));

                            break;
                    case 3: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_3));

                            break;
                    case 4: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_4));

                            break;
                    case 5: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_5));

                            break;
                    case 6: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_6));

                            break;
                    case 7: btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed_7));

                            break;

                }
                sendDataToServer(String.valueOf(tunes));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //btn.setBackground(getResources().getDrawable(R.drawable.white_key_style));
                        switch (tunes)
                        {
                            case 1: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_1));

                                    break;
                            case 2:
                                    btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_2));

                                    break;
                            case 3: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_3));

                                    break;
                            case 4: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_4));

                                    break;
                            case 5: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_5));

                                    break;
                            case 6: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_6));

                                    break;
                            case 7: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_7));

                                     break;

                        }

                    }
                }, 150);

                s10Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                s11Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                s13Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                s14Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                s15Btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
            }

        } else if (msg.what == 2) {
            if (playTimer != null) {
                playTimer.cancel();
                playTimer = null;
            }
            isPlaying = false;
            isPlayList = false;
            song1.setText("clearday");
            song2.setText("hipjazz");
            song3.setText("retrosoul");
            song4.setText("memories");
            song5.setText("allthat");
            song6.setText("ssanova");
            playListButton.setText("play list");

            Toast.makeText(MainActivity.this, "End of play", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //


    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        sendDataToServer("@");
                        wifiStatus = getWifiStatus();
                        if(!wifiStatus)
                        {
                            try {
                                if(client!=null)
                                {
                                    if(client.getClientSocket()!=null)
                                        client.getClientSocket().close();
                                    client = null;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            if(client == null)
                            {
                                client = new CommunicationThread();
                            }
                        }
                    }
                });
            }
        };
    }

    private void sendDataToServer(String data)
    {
        if(client!=null && client.isConnected())
             new SendData(client).execute(data);
    }

    private boolean getWifiStatus()
    {
        boolean status = false;
        String ssid = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION);
        }else{
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(networkInfo.isConnected())
            {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
                    //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
                    ssid = connectionInfo.getSSID();
                    if(ssid.equals("\"HF-LPB100\""))
                    {
                        status = true;
                    }
                }
            }
        }


        return status;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
        try {
            client.getClientSocket().close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == LOCATION){
            //User allowed the location and you can read it now
            wifiStatus = getWifiStatus();
        }
    }

}
