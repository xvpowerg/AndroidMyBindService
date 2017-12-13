package com.example.student.testbindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * Created by Student on 2017/11/24.
 */

public class MyBindService extends Service {
    private Handler mainActivityHandler;
    private boolean _runCountdown = false;
    private  int i =10;
    private boolean reStart = false;
    private  CountDownLatch latch = new CountDownLatch(1);

    public class MyBinder extends Binder {

        public void startCountDown(){
            getCountDownLatch().countDown();
        }

        private  CountDownLatch getCountDownLatch(){
            return latch;
        }
        public void setHandler(Handler handler){
            mainActivityHandler = handler;
        }

        public void reStart(){

                reStart =true;
                latch.countDown();
        }
    }


    private void runCountdown(){

        while(true){
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i=10  ; i>=0 ;i--){
                if (reStart){
                    reStart = false;
                    i =10;
                }
                Message msg = Message.obtain(mainActivityHandler,1,i,i);
                mainActivityHandler.sendMessage(msg);
                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {}
            }
            latch = new CountDownLatch(1);

        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Howard","onBind!");
        MyBinder myBinder = new MyBinder();

        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Howard","onCreate!");
        new Thread(){
         public void run(){
             runCountdown();
         }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Howard","onStartCommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Howard","onDestroy!");
    }
}
