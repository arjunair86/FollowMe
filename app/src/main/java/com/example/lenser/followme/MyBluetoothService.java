package com.example.lenser.followme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import BluetoothFunctions.ConnectedBT;

import static com.example.lenser.followme.MyBluetoothService.bt;
import static com.example.lenser.followme.MyBluetoothService.downButton;
import static com.example.lenser.followme.MyBluetoothService.isAuto;
import static com.example.lenser.followme.MyBluetoothService.leftButton;
import static com.example.lenser.followme.MyBluetoothService.rightButton;
import static com.example.lenser.followme.MyBluetoothService.upButton;

/**
 * Created by lenser on 3/6/17.
 */

public class MyBluetoothService extends AppCompatActivity {

    public static Button disconnect, upButton, downButton, leftButton, rightButton;
    InputStream inputStream;
    OutputStream outputStream;
    Switch aSwitch;
    public static boolean isAuto = true;
    public static ConnectedBT bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        Toolbar toolbar = (Toolbar)findViewById(R.id.connected_toolbar);
        setSupportActionBar(toolbar);

        disconnect = (Button)findViewById(R.id.disconnect);
        upButton = (Button)findViewById(R.id.upButton);
        downButton = (Button)findViewById(R.id.downButton);
        leftButton = (Button)findViewById(R.id.leftButton);
        downButton = (Button)findViewById(R.id.downButton);
        rightButton = (Button)findViewById(R.id.rightButton);

        aSwitch = (Switch) findViewById(R.id.switch1);


        //////////get IO stream
        try {
            inputStream = MainActivity.mmSocket.getInputStream();
            outputStream = MainActivity.mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("Service", "Error getting IO stream");
        }
        bt = new ConnectedBT(inputStream, outputStream, MyBluetoothService.this);

        bt.write("A".toString());


        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout relativeLayoutForAuto = (RelativeLayout) findViewById(R.id.relativeLayoutForAuto);
                RelativeLayout relativeLayoutForManual = (RelativeLayout) findViewById(R.id.relativeLayoutForManual);
                if (isChecked) {
                    isAuto = false;
                    bt.write("M".toString());
                    relativeLayoutForManual.setVisibility(View.VISIBLE);
                    relativeLayoutForAuto.setVisibility(View.INVISIBLE);
                } else {
                    isAuto = true;
                    bt.write("A".toString());
                    relativeLayoutForManual.setVisibility(View.INVISIBLE);
                    relativeLayoutForAuto.setVisibility(View.VISIBLE);
                }
            }
        });

        ///////////////////MOTOR CONTROLS
            manualThread manualThread = new manualThread();
        manualThread.start();
        /////////////////disconnect device
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mmSocket != null){
                    bt.write("B".toString());
                    bt.disconnect();
                }
            }
        });

        /////////////////read message
        bt.start();

    }
}

class manualThread extends Thread{
    @Override
    public void run() {
        if(!isAuto) {
            upButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("U".toString());
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("S".toString());
                        }
                    }
                    return false;
                }
            });
            downButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("D".toString());
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("S".toString());
                        }
                    }
                    return false;
                }
            });
            leftButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("L".toString());
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("S".toString());
                        }
                    }
                    return false;
                }
            });
            rightButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("R".toString());
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (MainActivity.mmSocket != null) {
                            bt.write("S".toString());
                        }
                    }
                    return false;
                }
            });
        }
    }
}
