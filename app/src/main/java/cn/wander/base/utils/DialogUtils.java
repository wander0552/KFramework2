package cn.wander.base.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by wander on 2016/4/27.
 * email 805677461@qq.com
 */
public class DialogUtils {

    public static Dialog showDesDialog(Context context, String des) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("PS");

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    /**
     * 对话框类
     *
     * @param context
     * @param msg
     */
    public static void showDialog_NOResponse(Context context, String msg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ps")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                listener.onClick(dialog, which);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }


    /**
     * 对话框类
     *
     * @param context
     * @param msg
     */
    public static void showDialog_NOResponse(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ps")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("ok", null);
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

}
