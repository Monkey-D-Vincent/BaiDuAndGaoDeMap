package com.xianzhi.baidumap.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.xianzhi.baidumap.base.BaseApplication;
import com.xianzhi.baidumap.service.VideoService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author liMing
 * @Demo class NavigationStartFragment
 * @Description TODO 导航
 * @date 2019-10-14 17:35
 */
public class NavigationStartFragment extends Fragment {

    private Context context;
    private static Disposable observable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    private View initView() {
        observable = Observable.interval(0, 10, TimeUnit.MINUTES).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                context.startService(new Intent(context, VideoService.class).putExtra("tag", "1"));
                Log.i("VideoService_______________________log", "跳转...");
            }
        });

        Bundle bundle = new Bundle();
        bundle.putBoolean(BNaviCommonParams.ProGuideKey.ADD_MAP, false);
        return BaiduNaviManagerFactory.getRouteGuideManager().onCreate(getActivity(), new IBNRouteGuideManager.OnNavigationListener() {
            @Override
            public void onNaviGuideEnd() {
                // 退出导航
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
                if (actionType == 0) {
                    // 导航到达目的地 自动退出
                    BaiduNaviManagerFactory.getRouteGuideManager().forceQuitNaviWithoutDialog();
                }
            }
        }, bundle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        BaiduNaviManagerFactory.getRouteGuideManager().onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();
        BaiduNaviManagerFactory.getRouteGuideManager().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        BaiduNaviManagerFactory.getRouteGuideManager().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        BaiduNaviManagerFactory.getRouteGuideManager().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaiduNaviManagerFactory.getRouteGuideManager().onStop();
    }

    @Override
    public void onDestroy() {
        if (observable != null && !observable.isDisposed()) {
            observable.dispose();
        }
        context.stopService(new Intent(context, VideoService.class));
        if (new File(BaseApplication.videoPath).exists()) {
            MediaMetadataRetriever retr = new MediaMetadataRetriever();
            retr.setDataSource(BaseApplication.videoPath);
        }
        BaiduNaviManagerFactory.getRouteGuideManager().onDestroy(false);
        super.onDestroy();
    }
}
