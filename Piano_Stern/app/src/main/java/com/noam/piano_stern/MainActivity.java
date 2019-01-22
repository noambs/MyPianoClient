package com.noam.piano_stern;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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


    private Map<Integer, Integer> tunesSoundMap = new HashMap<Integer, Integer>();

    private SoundPool soundPool;

    private CommunicationThread client;

    private boolean isPlaying = false;

    private Timer playTimer = null;

    public static final int RECORD_INTERVAL = 20;

    private Map<Long, List<RecordInfo>> timeTunesMap = new HashMap<>();

    private Button happyB;

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

    private boolean loaded;

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

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new CommunicationThread();


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


        ImageView s10Btn = (ImageView) this.findViewById(R.id.s10_btn);
        ImageView s11Btn = (ImageView) this.findViewById(R.id.s11_btn);
        ImageView s13Btn = (ImageView) this.findViewById(R.id.s13_btn);
        ImageView s14Btn = (ImageView) this.findViewById(R.id.s14_btn);
        ImageView s15Btn = (ImageView) this.findViewById(R.id.s15_btn);


        song2 = (Button) this.findViewById(R.id.btn_song2);
        happyB = (Button) this.findViewById(R.id.happy_b);



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

        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // PlayingSong("Titsy Bitsy Spider", song2);
            }
        });

        happyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayingSong("Happy Birthday", happyB);

            }
        });


    }



    private boolean playSound(int id, MotionEvent motionEvent, View view) {
        if ((motionEvent == null || motionEvent.getAction() == MotionEvent.ACTION_DOWN) && loaded) {
            soundPool.play(tunesSoundMap.get(id), 1.0f, 1.0f, 0, 0, 1);

           if(client!=null)
           {
               if(client.isConnected())
                   new SendData(client).execute(id);
           }


        }
        return false;
    }





    private void addSongToPlay(String mySong)
    {
        long time = 1;
        int tunes = 1;
        String[] songArray = getResources().getStringArray(R.array.song_list);
        timeTunesMap.clear();
        int []songs_notes;
        int [] time_nots;
        switch(mySong)
        {
            case "Happy Birthday":
                songs_notes = getResources().getIntArray(R.array.HappyB_Notes);
                time_nots = getResources().getIntArray(R.array.happy_b_time);
                for(int i=0;i<songs_notes.length;i++)
                {

                    song_array_size = time;
                    List<RecordInfo> recordInfoList = new ArrayList<>();

                    recordInfoList.add(new RecordInfo(songs_notes[i], null));
                    timeTunesMap.put(time, recordInfoList);
                    if(i<time_nots.length)
                        time=time_nots[i];

                }
                break;


        }



    }


    private void PlayingSong(final String song_name, Button button)
    {

        //if (timeTunesMap.isEmpty()) return;
        if (isPlaying) {
            isPlaying = false;
            button.setText(song_name);

            if (playTimer != null) {
                playTimer.cancel();
                playTimer = null;
            }
        } else {
            addSongToPlay(song_name);
            isPlaying = true;
            button.setText("Stop play");
            final AtomicLong playTick = new AtomicLong(0);
            playTimer = new Timer();
            playTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    currentTick = playTick.incrementAndGet();
                    if (timeTunesMap.containsKey(currentTick)) {
                        List<RecordInfo> recordInfoList = timeTunesMap.get(currentTick);


                        if (recordInfoList.isEmpty()) {
                            playTimer.cancel();
                            Message playEndMessage = new Message();
                            playEndMessage.what = 2;
                            handler.sendMessage(playEndMessage);
                        }

                        for (RecordInfo recordInfo : recordInfoList) {
                            //int tunes = recordInfo.getTunes();
                            playSound(recordInfo.getTunes(), null, null);
                            Message message = new Message();
                            message.what = 1;
                            message.arg1 = recordInfo.getTunes();
                            message.obj = recordInfo.getButton();
                            handler.sendMessage(message);

                        }


                    }

                    if (isPlaying && (song_array_size +18) == currentTick) {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);

                    }
                }
            }, 0, RECORD_INTERVAL);


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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //btn.setBackground(getResources().getDrawable(R.drawable.white_key_style));
                        switch (tunes)
                        {
                            case 1: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_1));
                                break;
                            case 2: btn.setBackground(getResources().getDrawable(R.drawable.white_key_style_2));
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
            }

        } else if (msg.what == 2) {
            isPlaying = false;
            happyB.setText("Happy Birthday");
            if (playTimer != null) {
                playTimer.cancel();
                playTimer = null;
            }
            Toast.makeText(MainActivity.this, "End of play", Toast.LENGTH_SHORT).show();
        }
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

    }

}
