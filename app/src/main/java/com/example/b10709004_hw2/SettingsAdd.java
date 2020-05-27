package com.example.b10709004_hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsAdd extends AppCompatActivity{
    private Button btnback,o;
    private EditText num,n;
    private String[] z=new String[2] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_add);
        btnback=(Button)findViewById(R.id.cancel);
        btnback.setOnClickListener(btn);
        num=(EditText)findViewById(R.id.count);
        n=(EditText)findViewById(R.id.name);
        o=(Button)findViewById(R.id.ok);
        o.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     z[0] = n.getText().toString();
                                     z[1] = num.getText().toString();
                                     Intent intent = new Intent(SettingsAdd.this, MainActivity.class);
                                     Bundle bundle = new Bundle();
                                     bundle.putStringArray("name", z);
                                     intent.putExtras(bundle);
                                     startActivity(intent);
                                 }
                             }
        );
    }
    private Button.OnClickListener btn=new Button.OnClickListener(){
        public void onClick(View v)
        {
            finish();
        }
    };
}
