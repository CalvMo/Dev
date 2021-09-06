package com.example.firefighter.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firefighter.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class HomeActivity extends AppCompatActivity {

        WebView wb;
        EditText addressBar;
        Button btnGo;


    // Socket myAppSocket = null;
    private  static final String TAG = "HomeActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    Button forward,reverse,left,right,pump, stopControl;
    private boolean isPump = false;
    //String videoURL = "http://192.168.43.155:8081";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        forward = findViewById(R.id.btn_Forward);
        reverse = findViewById(R.id.btn_Reverse);
        left = findViewById(R.id.btn_Left);
        right = findViewById(R.id.btn_Right);
        /*ServoDown = findViewById(R.id.btn_servo_Down);
        ServoUp = findViewById(R.id.btn_Servo_Up);
        ServoLeft = findViewById(R.id.btn_servo_left);
        ServoRight = findViewById(R.id.btn_servo_right);*/
        stopControl = findViewById(R.id.btn_stop);
        pump = findViewById(R.id.btn_water_pump);

        addressBar=findViewById(R.id.addressBar);
        btnGo = findViewById(R.id.btn_Go);
        wb=findViewById(R.id.webView);
        getIPandPort();
        CMD = "i";
        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
        cmd_increase_servo.execute();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = addressBar.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter URL", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addressBar.getWindowToken(), 0);
                wb.setWebViewClient(new WebViewClient());
                wb.loadUrl(url);
                wb.getSettings().setJavaScriptEnabled(true);

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "F";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "B";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "L";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "R";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        stopControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "S";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        pump.setOnClickListener(new View.OnClickListener() {
            //Drawable
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_water_pump) {
                    isPump = !isPump; // toggle the boolean flag
                    //v.setBackgroundResource(isLightBulb1 ? lightbulbon : R.drawable.lightbulboff);
                    if (isPump){
                        v.setBackgroundResource(R.drawable.waterpumpon);
                        getIPandPort();
                        CMD = "X";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();

                    }
                    else {
                        v.setBackgroundResource(R.drawable.waterpumpoff);
                        getIPandPort();
                        CMD = "Z";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();

                    }
                }

            }
        });


    }

    public void getIPandPort()
    {
        String iPandPort = "192.168.8.5:5050";
        Log.d("MYTEST","IP String: "+ iPandPort);
        String temp[]= iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiModuleIp);
        Log.d("MY TEST","PORT:"+wifiModulePort);
    }

    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(HomeActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,HomeActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}
