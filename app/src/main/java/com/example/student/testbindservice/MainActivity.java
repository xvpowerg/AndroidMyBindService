package com.example.student.testbindservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.student.testbindservice.MyBindService.MyBinder;

public class MainActivity extends AppCompatActivity {

    private Handler handler = null;
    private  MyBinder myBinder;
    private class MyServiceContion implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             myBinder  =  (MyBinder)service;
            myBinder.setHandler(handler);
            myBinder.startCountDown();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Howard","onServiceDisconnected");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyServiceContion scon = new MyServiceContion();
        Button btn =  findViewById(R.id.bind_btn);
        final TextView textView =  findViewById(R.id.show_time_txt);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch(msg.what){
                    case 1:
                        textView.setText(String.valueOf(msg.arg1));
                        break;
                }

            }
        };



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBinder == null){
                    Intent myIntent= new Intent(MainActivity.this,MyBindService.class);
                    bindService(myIntent,scon, Service.BIND_AUTO_CREATE |
                            Service.BIND_ADJUST_WITH_ACTIVITY);
                }else{
                    myBinder.reStart();

                }

            }
        });
    }
}
