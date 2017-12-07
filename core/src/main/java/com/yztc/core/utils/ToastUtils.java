package com.yztc.core.utils;


import android.content.Context;
import android.widget.Toast;

import com.yztc.core.App;


/**
 * Created by wanggang on 2016/12/12.
 *
 * 解决Toast重叠问题
 *
 *
 *  ToastCompat
 */

public class ToastUtils {

    private static Context mContext;
    private static ToastUtils mInstance;
    private Toast mToast;

    public static ToastUtils getInstance() {
        if (mInstance == null)
            init(App.getContext());
        return mInstance;
    }

    private static void init(Context ctx) {
        mInstance = new ToastUtils(ctx);
    }

    private ToastUtils(Context ctx) {
        mContext = ctx;
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void showLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
