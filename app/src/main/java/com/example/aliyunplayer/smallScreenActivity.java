package com.example.aliyunplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aliyun.player.IPlayer;
import com.aliyun.player.alivcplayerexpand.constants.GlobalPlayerConfig;
import com.aliyun.player.alivcplayerexpand.theme.Theme;
import com.aliyun.player.alivcplayerexpand.widget.AliyunVodPlayerView;
import com.aliyun.player.aliyunplayerbase.util.ScreenUtils;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;
import com.example.aliyunplayer.custom.CustomNestedScrollView;

public class smallScreenActivity extends AppCompatActivity {
    private static String TAG="smallScreenActivity";
    private CustomNestedScrollView mNestedScrollView;
    private LinearLayout mLinearLayout;
    private Button mBtnPlay;
    private LinearLayout mFrameLayout;
    private AliyunVodPlayerView mAliyunVodPlayerView;
    private View mView;
    private LinearLayout.LayoutParams layoutParams;
    private FrameLayout.LayoutParams layoutParamsSmallVideo;
    private boolean isSmallVideoDisplay = false;

    private int videoPlayHeight;
    private int smallVideoHeight;
    private FrameLayout mFrame_layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_screen);

        videoPlayHeight = dip2px(this, 200);
        smallVideoHeight = dip2px(this, 150);
        initView();
        initConfig();
        initEvent();
        //开始播放
        playVideo();
    }

    private void initConfig() {
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
        //保持屏幕高亮
        mAliyunVodPlayerView.setKeepScreenOn(true);
        //设置更新UI播放器的主题
        mAliyunVodPlayerView.setTheme(Theme.Blue);
        //是否循环播放
        mAliyunVodPlayerView.setCirclePlay(true);
        //是否自动播放
        mAliyunVodPlayerView.setAutoPlay(false);
        mAliyunVodPlayerView.enableNativeLog();

        mAliyunVodPlayerView.startNetWatch();
    }

    private void initEvent() {
        mNestedScrollView.setOnScrollChangedListener(new CustomNestedScrollView.OnScrollChangedListener() {

            @Override
            public void scrollChangedListener(int y) {
                if (y >= videoPlayHeight) {//顶部播放器不可见了
                    if (!isSmallVideoDisplay) {

                        isSmallVideoDisplay = true;
                        //不设置控制栏
                        mAliyunVodPlayerView.setControlBarCanShow(false);
                        //更新UI
                        mLinearLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mFrameLayout.removeView(mAliyunVodPlayerView);
                                //为了不让移除布局后总布局高度不变，动态增加一个同样高度的view
                                mLinearLayout.addView(mView, -1, new LinearLayout.LayoutParams(0, videoPlayHeight));
                                //小屏幕显示区域
                                layoutParamsSmallVideo = new FrameLayout.LayoutParams(smallVideoHeight, smallVideoHeight);
                                layoutParamsSmallVideo.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                                layoutParamsSmallVideo.rightMargin = 20;
                                mFrame_layout2.addView(mAliyunVodPlayerView, layoutParamsSmallVideo);
                            }
                        });
                    }
                } else {
                    if (isSmallVideoDisplay) {
                        isSmallVideoDisplay = false;
                        mAliyunVodPlayerView.setControlBarCanShow(true);
                        mFrameLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                //去除小屏幕
                                mFrame_layout2.removeView(mAliyunVodPlayerView);
                                //除去动态添加高度的view
                                mLinearLayout.removeView(mView);
                                //增加最顶部上面的播放界面
                                mFrameLayout.addView(mAliyunVodPlayerView, 0, layoutParams);
                            }
                        });
                    }
                }
            }

        });
    }

    private void initView() {
/**
 *    @BindView(R.id.btn_play)
 *     Button btnPlay;
 *     @BindView(R.id.linear_layout)
 *     LinearLayout mLinearLayout;
 *     @BindView(R.id.nestedscrollview)
 *     CustomNestedScrollView mNestedScrollView;
 *     @BindView(R.id.frame_layout)
 *     FrameLayout mFrameLayout;
 *
 */
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mNestedScrollView = (CustomNestedScrollView) findViewById(R.id.nestedscrollview);
        mFrameLayout = (LinearLayout) findViewById(R.id.frame_layout);
        mFrame_layout2 = (FrameLayout) findViewById(R.id.frame_layout2);


        mAliyunVodPlayerView = new AliyunVodPlayerView(this);
        mView = new View(this);

        //
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, videoPlayHeight);
        mFrameLayout.addView(mAliyunVodPlayerView, 0, layoutParams);
    }
    //变换横竖屏屏幕
    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView
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
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }

    private void playVideo(){

        UrlSource urlSource = new UrlSource();
        //视频地址
        urlSource.setUri("http://player.alicdn.com/video/aliyunmedia.mp4");
        mAliyunVodPlayerView.setLocalSource(urlSource);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
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