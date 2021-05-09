package com.uibobo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @Author: bo
 * @Date: 2021/5/7 20:12
 */
public class WidgetProvider extends AppWidgetProvider {
    public static final String TAG = "widget";
    private static final String CLICK_ACTION = "click001";
    private ZWebSocketClient client = null;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        //remoteViews.setImageViewResource(R.id.tv_content, R.drawable.press_icon);
        Intent clickIntent = new Intent(CLICK_ACTION);
        clickIntent.setClass(context, WidgetProvider.class);
        clickIntent.setFlags(clickIntent.getFlags()| 0x01000000);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_content, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: ");

        String action = intent.getAction();
        //final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.id.tv_content);
        final Bitmap bitmap = getBitmap(context, R.drawable.press_icon);
        //判断是否是自定义点击action
        if (action.equals(CLICK_ACTION)) {
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            sendSocket(context);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
                        remoteViews.setImageViewBitmap(R.id.tv_content, rotateBitmap(bitmap, i * 10));
                        Intent clickIntent = new Intent();
                        clickIntent.setAction(CLICK_ACTION);
                        clickIntent.setClass(context, WidgetProvider.class);
                        clickIntent.setFlags(clickIntent.getFlags()| 0x01000000);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.tv_content, pendingIntent);
                        Log.d(TAG, "run: ");
                        manager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
                        SystemClock.sleep(2);
                    }
                }
            }).start();

        }
    }

    /*
    * 旋转
    * */
    private Bitmap rotateBitmap(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bm;
    }
    private Bitmap getBitmap(Context context,int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    /*
    * 发送webSocket
    * */
    private void sendSocket(final Context context){
        URI uri = URI.create(MsgConstant.url);
        client = new ZWebSocketClient(uri){
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.send(MsgConstant.send1);
            }
            @Override
            public void onMessage(String message) {
                Log.e("ZWebSocketClient", "onMessage()");
                System.out.println("---- "+message);
                int order = MsgConstant.order(message);
                switch (order){
                    case 1 :
                        super.send(MsgConstant.send2);
                        System.out.println("send2");
                        break;
                    case 2 :
                        super.send(MsgConstant.send3);
                        System.out.println("send3");
                        break;
                    case 3 :
                        break;
                    case 4 :
                        closeConnect();
                        //Toast.makeText(context, "开锁成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 0 :
                        closeConnect();
                        //Toast.makeText(context, "开锁失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        try {
            client.connect();
        } catch (Exception e) {
            //closeConnect(client);
            e.printStackTrace();
        }
    }
    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }
}
