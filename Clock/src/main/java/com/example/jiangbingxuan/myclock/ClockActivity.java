package com.example.jiangbingxuan.myclock;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import animppt.Anim;
import animppt.AnimBaiYeChuang;
import animppt.AnimCaChu;
import animppt.AnimHeZhuang;
import animppt.AnimJieTi;
import animppt.AnimLingXing;
import animppt.AnimLunZi;
import animppt.AnimPiLie;
import animppt.AnimQiPan;
import animppt.AnimQieRu;
import animppt.AnimShanXingZhanKai;
import animppt.AnimShiZiXingKuoZhan;
import animppt.AnimSuiJiXianTiao;
import animppt.AnimXiangNeiRongJie;
import animppt.AnimYuanXingKuoZhan;
import animppt.EnterAnimLayout;
import imageUtils.AlbumHelper;
import imageUtils.ImageBucket;
import imageUtils.ImageItem;
import objectAnimUtils.Techniques;
import objectAnimUtils.YoYo;
import utils.SharedPreferencesUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ClockActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private TextClock tc_textclock,tc_textclockday;
    private SeekBar id_seekBartimesize;
    private LinearLayout ll_set;
    private int textsize;
    private RelativeLayout rl_bg;
    private YoYo.YoYoString rope;
    private ImageView my_bgimage_view;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnClickListener mDelayHideTouchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
        }
    };
    private ArrayList<Anim> animList=new ArrayList<Anim>();
    private EnterAnimLayout anim_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        textsize= (int) SharedPreferencesUtils.getParam(this, "int", 200);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        my_bgimage_view= (ImageView) findViewById(R.id.my_bgimage_view);
        ll_set= (LinearLayout) findViewById(R.id.ll_set);
        rl_bg= (RelativeLayout) findViewById(R.id.rl_bg);
        ll_set.setVisibility(View.GONE);
        tc_textclock= (TextClock) findViewById(R.id.tc_textclock);
        tc_textclockday= (TextClock) findViewById(R.id.tc_textclockday);
        id_seekBartimesize= (SeekBar) findViewById(R.id.id_seekBartimesize);
        initClock();
        initSeekBar();
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
//        anim_layout= (EnterAnimLayout) findViewById(R.id.anim_layout);
//        initAnim(anim_layout);
//        setbganim();
        initImage();
        findViewById(R.id.dummy_button).setOnClickListener(mDelayHideTouchListener);

    }

    private void initAnim(EnterAnimLayout view) {
        animList.add(new AnimBaiYeChuang( view));
        animList.add(new AnimCaChu( view));
        animList.add(new AnimHeZhuang( view));
        animList.add(new AnimJieTi( view));
        animList.add(new AnimLingXing( view));
        animList.add(new AnimLunZi( view));
        animList.add(new AnimPiLie( view));
        animList.add(new AnimQieRu( view));
        animList.add(new AnimQiPan( view));
        animList.add(new AnimShanXingZhanKai( view));
        animList.add(new AnimShiZiXingKuoZhan( view));
        animList.add(new AnimSuiJiXianTiao( view));
        animList.add(new AnimXiangNeiRongJie( view));
        animList.add(new AnimYuanXingKuoZhan( view));
    }

    private void initBGimage() {
        if(imageDatas!=null){
            if(imageDatas.size()>0){
//                int a = (int)(Math.random()*animList.size());
//                Anim anim = animList.get(a);
//                anim.startAnimation(2000);//开始播放动画，动画播放时长2500ms，默认2000

                if(Util.isOnMainThread()){
                    int i = (int)(Math.random()*imageDatas.size());
                    Glide.with(this)
                            .load(imageDatas.get(i).getImagePath())
                            .diskCacheStrategy( DiskCacheStrategy.NONE )
                            .into(my_bgimage_view);
                }

            }
        }
    }

    private Handler clockhandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 2:
                    initBGimage();
                    break;
                default:
                    break;
            }
        }
    };

    private ArrayList<ImageItem> imageDatas=new ArrayList<ImageItem>();;
    private AlbumHelper helper;
    public  List<ImageBucket> contentList;
    private void initImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;

                    if (imageDatas != null)
                        imageDatas.clear();
                    AlbumHelper albumHelper = new AlbumHelper();
                    helper = albumHelper.getHelper();
                    helper.init(getApplicationContext());
                    contentList = helper.getImagesBucketList(false);//filelist
                    for (int i = 0; i < contentList.size(); i++) {//所有图片
                        imageDatas.addAll(contentList.get(i).imageList);
                    }
                    DateHighToLowComparator comparator = new DateHighToLowComparator();
                    Collections.sort(imageDatas, comparator);


                clockhandler.sendMessage(message);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(15000);//休眠3秒
                        clockhandler.sendEmptyMessage(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
    public class DateHighToLowComparator implements Comparator<ImageItem> {

        @Override
        public int compare(ImageItem itemBean1, ImageItem itemBean2) {

            long date1 = itemBean1.getImageDate();
            long date2 = itemBean2.getImageDate();

            if (date1 > date2) {
                return -1;
            } else if (date1 < date2) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    private void initSeekBar() {
        id_seekBartimesize.setProgress(textsize);
        id_seekBartimesize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tc_textclock.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private String hour="";
    private String min="";
    private void initClock() {
        // 设置24时制显示格式
        tc_textclockday.setFormat24Hour("yyyy-MM-dd EEEE");
         tc_textclock.setFormat24Hour("HH:mm");
        tc_textclock.setTextSize(textsize);
        tc_textclock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] aa = s.toString().split(":");
                if(!hour.equals(aa[0])){
                    setTimeChange(0);
                }
                if(!min.equals(aa[1])){
                    setTimeChange(1);
                }
                hour=aa[0];
                min=aa[1];
            }
        });
    }
    //0小时 1分钟
    private void setTimeChange(int i){
        if(i==0){
            sethouranim();
        }else{
            setminanim();
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
            ll_set.setVisibility(View.GONE);
        } else {
            show();
            ll_set.setVisibility(View.VISIBLE);
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.setParam(this, "int", id_seekBartimesize.getProgress());
    }


    private void sethouranim(){
        rope = YoYo.with(Techniques.Wobble)
                .duration(1000)
//                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(tc_textclock);
    }

    private void setminanim() {
        rope = YoYo.with(Techniques.Flash)
                .duration(600)
//                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(tc_textclock);
    }
    private void setbganim() {
        rope = YoYo.with(Techniques.FlashBg)
                .duration(2000)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(rl_bg);
    }
}
