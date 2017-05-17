package br.edu.fameg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.senior.tp1.R;

public class MainActivity extends AppCompatActivity {

    private EditText nomeEditText;
    private TextView saudacaoTextView;
    private String saudacao = "Olá ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.nomeEditText =
                (EditText) findViewById(R.id.nomeEditText);
        this.saudacaoTextView = (TextView)
                findViewById(R.id.saudacaoTextView);
        Log.d("Passei no evento: ","On Create");
    }

    public void surpreenderUsuario(View view){
        Editable texto = this.nomeEditText.getText();
        String msg = saudacao + " " + texto.toString();
        this.saudacaoTextView.setText(msg);
        // fins de demonstração
        saudacao = "persistiu";
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Passei no evento: ","On Resume - saudacao vale "+saudacao);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Passei no evento: ","On Pause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("Passei no evento: ","On Stop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Passei no evento: ","On Destroy");
    }
}
