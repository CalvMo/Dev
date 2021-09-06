package tut.wildlifeobservation;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    private WebView webView;
   // Socket myAppSocket = null;
    private  static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    Button forward,reverse,left,right,ServoUp,ServoDown,ServoRight,ServoLeft, stopControl;
    String videoURL = "http://192.168.43.155:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        forward = findViewById(R.id.btn_Forward);
        reverse = findViewById(R.id.btn_Reverse);
        left = findViewById(R.id.btn_Left);
        right = findViewById(R.id.btn_Right);
        ServoDown = findViewById(R.id.btn_servo_Down);
        ServoUp = findViewById(R.id.btn_Servo_Up);
        ServoLeft = findViewById(R.id.btn_servo_left);
        ServoRight = findViewById(R.id.btn_servo_right);
        stopControl = findViewById(R.id.btn_stop);

        webView = findViewById(R.id.view);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.loadUrl(videoURL);

        getIPandPort();
        CMD = "i";
        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
        cmd_increase_servo.execute();

        if(serviceOk())
        {
            init();
        }


        ServoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "2";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        ServoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "8";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        ServoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "6";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        ServoLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "4";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "w";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "s";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "a";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "d";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        stopControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "h";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void init(){

        Button googleMaps = findViewById(R.id.btn_googleMaps);

        googleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GoogleMaps.class);
                startActivity(intent);
            }
        });
    }

    public boolean serviceOk() {
        Log.d(TAG, "Is Services Ok, checking Google Service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google services is working fine");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "Google Service version not available but requested the necessary version");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Failed to make Google maps request, the device doesn't support google services", Toast.LENGTH_SHORT).show();
        }
        return  false;
    }



    public void getIPandPort()
    {
        String iPandPort = "192.168.43.155:8080";
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
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}
