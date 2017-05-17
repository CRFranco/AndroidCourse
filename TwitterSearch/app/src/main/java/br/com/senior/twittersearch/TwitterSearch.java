package br.com.senior.twittersearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by cristiano.franco on 12/11/2015.
 */
public class TwitterSearch extends Activity {
    private ListView lista;
    private EditText texto;
    private String accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_search);
        lista = (ListView) findViewById(R.id.lista);
        texto = (EditText) findViewById(R.id.texto);

        // o que extends Async task deve chamar o execute()
        new AutenticacaoTask().execute();

    }


    public void buscar(View v) {


        String filtro = texto.getText().toString();


        if (accessToken == null) {
            Toast.makeText(this, "Token não disponível",
                    Toast.LENGTH_SHORT).show();
        } else {
            new TwitterTask().execute(filtro);
        }
    }

    /*
    classe privada que faz a autenticaçao no twiter de acordo com o secret fornecido por eles
     */

    private class AutenticacaoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Map<String, String> data = new HashMap<String, String>();
                data.put("grant_type", "client_credentials");

                String json = HttpRequest
                        .post("https://api.twitter.com/oauth2/token")
                        .authorization("Basic " + gerarChave())
                        .form(data)
                        .body();
                // fins de debug
                Log.d("retorno do log", json);
                JSONObject token = new JSONObject(json);
                accessToken = token.getString("access_token");
            } catch (Exception e) {

            }
            return null;
        }




        private String gerarChave() throws UnsupportedEncodingException {
            String key = "uIPZKz6dRfPnUW02wx3EBd4hG";
            String secret = "AH0VjIltMOjcqyJtCcFgVpCDYpcuZVg0IjrToaxHJpNXWel31H";
            String token = key + ":" + secret;
            String base64 = Base64.encodeToString(token.getBytes(), Base64.NO_WRAP);
            return base64;
        }
    }

    /*
    classe privada que representa a tarefa de busca que ocorerá em background
    extends AsncTask

     */
    private class TwitterTask extends AsyncTask<String, Void, String[]> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // abre diálogo de progresso
            dialog = new ProgressDialog(TwitterSearch.this);
            dialog.setMessage("Aguarde");
            dialog.show();
        }







        @Override
        protected String[] doInBackground(String... params) {
            try {
                String filtro = params[0];
                if (TextUtils.isEmpty(filtro)) {
                    return null;
                }
                String urlTwitter = "https://api.twitter.com/1.1/search/tweets.json?q=";
                String url = Uri.parse(urlTwitter + filtro).toString();
                //Log.i(getPackageName(), url);
                String conteudo = HttpRequest.get(url)
                        .authorization("Bearer " + accessToken)
                        .body();
                JSONObject jsonObject = new JSONObject(conteudo);
                JSONArray resultados = jsonObject.getJSONArray("statuses");
                Log.d("retorno da busca ",conteudo);

                String[] tweets = new String[resultados.length()];







                for (int i = 0; i < resultados.length(); i++) {
                    JSONObject tweet = resultados.getJSONObject(i);
                    String texto = tweet.getString("text");
                    String usuario = tweet.getJSONObject("user").getString("screen_name");
                    tweets[i] = usuario + " - " + texto;
                    Log.e(getPackageName(), tweets[i]);
                }

                return tweets;

            } catch (Exception e) {
                Log.e(getPackageName(), e.getMessage(), e);
                return null;
            }
        }




        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getBaseContext(),
                                android.R.layout.simple_list_item_1, result);

                lista.setAdapter(adapter);
            }
            // fecha diálogo de progresso
            dialog.dismiss();
        }
    }
}



