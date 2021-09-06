package com.example.homeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homeapp.R;

public class HomeActivity extends AppCompatActivity {

        WebView wb;
        EditText addressBar;
        Button btnGo;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addressBar=findViewById(R.id.addressBar);
        btnGo = findViewById(R.id.btn_Go);
        wb=findViewById(R.id.webView);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = addressBar.getText().toString().trim();
                if(url.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addressBar.getWindowToken(),0);
                wb.setWebViewClient(new WebViewClient());
                wb.loadUrl(url);
                wb.getSettings().setJavaScriptEnabled(true);

            }
        });


    }
}
