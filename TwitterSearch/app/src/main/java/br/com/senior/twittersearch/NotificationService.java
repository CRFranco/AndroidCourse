package br.com.senior.twittersearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by cristiano.franco on 13/11/2015.
 */
public class NotificationService extends Service {


    private String accessToken;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    // verifica se o dispositivo está conectado
    private boolean estaConectado() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info.isConnected();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // cria um pool de threads para serem executadas d forma agendada
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        long delayInicial = 0;
        long periodo = 1;
        TimeUnit unit = TimeUnit.MINUTES;
        // efetivamente agenda a execução da thread...nesse caso NotiicacaoTask
        pool.scheduleAtFixedRate(new NotificacaoTask(), delayInicial, periodo, unit);
        return START_STICKY;
    }

    /****************************************************************************************/
    /*
    classe privada que faz a autenticaçao no twiter de acordo com o secret fornecido por eles
    TODO Transformar em uma classe externa, pois é usada por duas classes distintas como interna
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

                JSONObject token = new JSONObject(json);
                accessToken = token.getString("access_token");

            } catch (Exception e) {
                return null;
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

    /**
     * Classe Privada que representa a notificação propriamente dita.
     */
    private class NotificacaoTask implements Runnable {
        private String baseUrl = "https://api.twitter.com/1.1/search/tweets.json";
        private String refreshUrl = "?q=@crfranc0";

        @Override
        public void run() {
            if (!estaConectado()) {
                return;
            }
            try {
                // faz a conexão, enviando a url e o token de acesso
                String conteudo = HttpRequest.get(baseUrl + refreshUrl)
                        .authorization("Bearer " + accessToken)
                        .body();
                // cria um jsonObject, com base na string conteudo, que já contém o criterio de
                // filtro
                JSONObject jsonObject = new JSONObject(conteudo);

                // o twitter retorna uma informação de quando foi a última atualização (since_id)
                refreshUrl = jsonObject.getJSONObject("search_metadata")
                        .getString("refresh_url");

                JSONArray resultados = jsonObject.getJSONArray("statuses");

                for (int i = 0; i < resultados.length(); i++) {
                    JSONObject tweet = resultados.getJSONObject(i);
                    String texto = tweet.getString("text");
                    String usuario = tweet.getJSONObject("user").getString("screen_name");
                    criarNotificacao(usuario, texto, i);
                }
            } catch (Exception e) {
                Log.e(getPackageName(), e.getMessage(), e);
            }
        }

        private void criarNotificacao(String usuario, String texto, int id) {
            int icone = R.drawable.ic_launcher;
            String aviso = getString(R.string.aviso);
            long data = System.currentTimeMillis();
            String titulo = usuario + " " + getString(R.string.titulo);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, TweetActivity.class);
            intent.putExtra(TweetActivity.USUARIO, usuario.toString());
            intent.putExtra(TweetActivity.TEXTO, texto.toString());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 0);

            Notification notification = new NotificationCompat.Builder(context)
                    .setWhen(data)
                    .setAutoCancel(true)
                    .setContentTitle(titulo)
                    .setSmallIcon(icone)
                    .setContentIntent(pendingIntent)
                    .setContentText(texto)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .build();


            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(ns);
            notificationManager.notify(id, notification);
        }
    }

}
