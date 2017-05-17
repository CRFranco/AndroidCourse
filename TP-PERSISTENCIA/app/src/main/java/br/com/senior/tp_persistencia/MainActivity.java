package br.com.senior.tp_persistencia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("Configuracoes", MODE_PRIVATE);

        String enderecoServidor = pref.getString("endereco", null);
        Log.d("Configuracoes", enderecoServidor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                createPreferencesDialog();
                break;
            case R.id.preferencias:
                createPreferencesDialog();
                break;

            default:
                break;
        }
        return true;
    }

    public void abrirBancoDeDadosActivity(View view){
        Intent intent = new Intent(this, BancoDeDadosActivity.class);
        startActivity(intent);
    }

    public void createPreferencesDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme);

        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.config_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        pref = getApplicationContext().getSharedPreferences("Configuracoes", MODE_PRIVATE);
        editor = pref.edit();

        dialogBuilder.setTitle("Configurações");
        dialogBuilder.setMessage("Forneça o endereço IP e porta do servidor:");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                editor.putString("endereco", "http://"+edt.getText().toString());
                editor.putInt("codigo",12);
                editor.commit();
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
        // RECUPERAR O CONTEUDO DAS PREFERENCIAS
        /*pref = getApplicationContext().getSharedPreferences("Configuracoes", MODE_PRIVATE);
        editor = pref.edit();
        String enderecoServidor = pref.getString("endereco", null);
        */
    }
}
