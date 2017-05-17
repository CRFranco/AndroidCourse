package br.com.senior.tp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.com.senior.tp2.domain.DomainObject;

public class ReceiveMessage extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);

        this.textView = (TextView)
                findViewById(R.id.textView);


        //Intent intent = getIntent();

        //String message = intent.getStringExtra("message");
        MessageApp ma = (MessageApp) getApplication();
        DomainObject domainObject = ma.getDomainObject();
        String message = domainObject.getMessage();

        textView.setText(message);
    }
}






/*
*
*

*
*
*
*
* */
