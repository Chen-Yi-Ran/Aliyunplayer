package com.example.aliyunplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.aliyun.player.IPlayer;
import com.aliyun.player.alivcplayerexpand.constants.GlobalPlayerConfig;
import com.aliyun.player.alivcplayerexpand.widget.AliyunVodPlayerView;
import com.aliyun.player.aliyunplayerbase.util.ScreenUtils;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;

public class MainActivity extends AppCompatActivity {

    private AliyunVodPlayerView mAliyunVodPlayerView;

    private static String TAG="MainActivity";
    /**
     * 本地视频播放地址
     */
    private String mLocalVideoPath;
    private boolean mNeedOnlyFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initAliyunPlayerView();
//        playVideo();

//        setManualBright();
//        Intent intent = new Intent(MainActivity.this, AliyunPlayerSettingActivity.class);
//        startActivity(intent);
        initView();
        initListener();
    }

    private void initListener() {
        mAliyunVodPlayerView.setOnPlayStateBtnClickListener(new AliyunVodPlayerView.OnPlayStateBtnClickListener() {
            @Override
            public void onPlayBtnClick(int playerState) {
                if (playerState == IPlayer.started) {
                    Log.i(TAG, "onPlayBtnClick: 暂停");
                } else if (playerState == IPlayer.paused) {
                    Log.i(TAG, "onPlayBtnClick: 播放");
                } else {
                    Log.i(TAG, "onPlayBtnClick: else");
                }
            }
        });

        mAliyunVodPlayerView.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                Log.i(TAG, "onCompletion:  视频正常播放完成");
            }
        });
        mAliyunVodPlayerView.setOnFirstFrameStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                Log.i(TAG, "onRenderingStart: 视频第一帧开始");
            }
        });


        //网络连接监听
        mAliyunVodPlayerView.setNetConnectedListener(new AliyunVodPlayerView.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {

            }

            @Override
            public void onNetUnConnected() {

            }
        });

    }

    private void initView() {
        mAliyunVodPlayerView = findViewById(R.id.aliyunVodPlayerView);

        //镜像模式为默认
        GlobalPlayerConfig.mMirrorMode = IPlayer.MirrorMode.MIRROR_MODE_NONE;
        //自适应码
        GlobalPlayerConfig.PlayConfig.mAutoSwitchOpen = false;
        //seek模式
        GlobalPlayerConfig.PlayConfig.mEnableAccurateSeekModule = false;
        //是否允许后台播放
        GlobalPlayerConfig.PlayConfig.mEnablePlayBackground = false;
        //默认播放方式
        GlobalPlayerConfig.mCurrentPlayType = GlobalPlayerConfig.PLAYTYPE.DEFAULT;
        //起播码率3000
        GlobalPlayerConfig.mCurrentMutiRate = GlobalPlayerConfig.MUTIRATE.RATE_3000;
        //旋转0
        GlobalPlayerConfig.mRotateMode = IPlayer.RotateMode.ROTATE_0;
        mAliyunVodPlayerView.setEnableHardwareDecoder(GlobalPlayerConfig.mEnableHardDecodeType);
        mAliyunVodPlayerView.setRenderMirrorMode(GlobalPlayerConfig.mMirrorMode);
        mAliyunVodPlayerView.setRenderRotate(GlobalPlayerConfig.mRotateMode);
        mAliyunVodPlayerView.setDefaultBandWidth(GlobalPlayerConfig.mCurrentMutiRate.getValue());
        //播放配置设置
        PlayerConfig playerConfig = mAliyunVodPlayerView.getPlayerConfig();
        playerConfig.mStartBufferDuration = GlobalPlayerConfig.PlayConfig.mStartBufferDuration;
        playerConfig.mHighBufferDuration = GlobalPlayerConfig.PlayConfig.mHighBufferDuration;
        playerConfig.mMaxBufferDuration = GlobalPlayerConfig.PlayConfig.mMaxBufferDuration;
        playerConfig.mMaxDelayTime = GlobalPlayerConfig.PlayConfig.mMaxDelayTime;
        playerConfig.mNetworkTimeout = GlobalPlayerConfig.PlayConfig.mNetworkTimeout;
        playerConfig.mMaxProbeSize = GlobalPlayerConfig.PlayConfig.mMaxProbeSize;
        playerConfig.mReferrer = GlobalPlayerConfig.PlayConfig.mReferrer;
        playerConfig.mHttpProxy = GlobalPlayerConfig.PlayConfig.mHttpProxy;
        playerConfig.mNetworkRetryCount = GlobalPlayerConfig.PlayConfig.mNetworkRetryCount;
        playerConfig.mEnableSEI = GlobalPlayerConfig.PlayConfig.mEnableSei;
        playerConfig.mClearFrameWhenStop = GlobalPlayerConfig.PlayConfig.mEnableClearWhenStop;
        mAliyunVodPlayerView.setPlayerConfig(playerConfig);

        playVideo();

    }

    //更换屏幕
    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏状态栏
//                if (!isStrangePhone()) {
//                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                }
                //设置view的布局，宽高
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }



    private void playVideo(){
        UrlSource urlSource = new UrlSource();
        //视频地址
        urlSource.setUri("http://player.alicdn.com/video/aliyunmedia.mp4");

        mAliyunVodPlayerView.setLocalSource(urlSource);
    }
    public void setManualBright() {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume-> 执行onResume");
        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.setAutoPlay(false);
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop-> 执行onStop");
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.setAutoPlay(false);
            mAliyunVodPlayerView.onStop();
        }
    }

}