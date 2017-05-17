package br.com.senior.tp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import br.com.senior.tp2.domain.DomainObject;

public class SendMessage extends AppCompatActivity {
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);


        this.editText =
                (EditText) findViewById(R.id.editText);
    }












    public void sendMessage(View view){
        Editable edt = editText.getText();
        String message = edt.toString();
        Intent intent = new Intent(this, ReceiveMessage.class);

        DomainObject domainObject = new DomainObject();
        domainObject.setMessage(message);

        MessageApp ma = (MessageApp) getApplicationContext();
        ma.setDomainObject(domainObject);


        //intent.putExtra("message", message);

        startActivity(intent);


    }




    /*
    *
    *
    *
    *
    *
    *
    * */
}
