package bollymusicdeveloper.android.com.youtubetomp3;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editUrl;
    TextView textSongName;
    Button dwnldButton;
    Button searchButton;
    private String title;
    private String dwnldLink;
    private String lenght;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        editUrl = (EditText) findViewById(R.id.url);
        textSongName = (TextView) findViewById(R.id.fileName);
        dwnldButton = (Button) findViewById(R.id.downloadLink);
        searchButton = (Button) findViewById(R.id.searchLink);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                //new AsyncListViewLoaderTiled().execute(editUrl.getText().toString());
                String appender = "https://www.youtubeinmp3.com/fetch/?format=JSON&video="+editUrl.getText().toString();
                callSearchYoutubeSong(appender);
                
            }
        });

        dwnldButton.setVisibility(View.INVISIBLE);
        textSongName.setVisibility(View.INVISIBLE);

        dwnldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDownloadManagerAvailable(MainActivity.this)){

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dwnldLink));
                    request.setDescription("Downloading "+title);
                    request.setTitle("Downloading "+title);
                    request.setMimeType("audio/MP3");
// in order for this if to run, you must use the android 3.2 to compile your app
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title+".mp3");

// get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);

                }
                else{
                    Toast.makeText(MainActivity.this,"Download Manager Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


    public void callSearchYoutubeSong(final String url){

        String tag_json_obj = "json_obj_req";

        /*String url = "http://api.androidhive.info/volley/person_object.json";*/

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Searching Song Details...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("JSON RESPONSE VOLLEY", response.toString());
                        Log.e("JSON RESPONSE VOLLEY", response.toString());
                        Log.e("JSON RESPONSE VOLLEY", response.toString());
                        try{
                        if(response.isNull("title"))
                            title = "NA";
                        else{
                            title = response.getString("title");
                        }

                        if(response.isNull("length"))
                            lenght = "NA";
                        else{
                            lenght = response.getString("length").toUpperCase();
                        }
                        if(response.isNull("link"))
                            dwnldLink = "NA";
                        else{
                            dwnldLink = response.getString("link");
                            Log.e("LINK US ELSE: ",dwnldLink);
                            Log.e("LINK US: ELSE",dwnldLink);
                            Log.e("LINK US: ELSE ",dwnldLink);
                        }


                            Log.e("LINK US: ",dwnldLink);
                            Log.e("LINK US: ",dwnldLink);
                            Log.e("LINK US: ", dwnldLink);
                            pDialog.hide();
                            dwnldButton.setVisibility(View.VISIBLE);
                            textSongName.setVisibility(View.VISIBLE);
                            textSongName.setText(title.toString());
                            textSongName.setText(dwnldLink.toString());


                        }
                        catch(JSONException e){
                            pDialog.hide();
                            Log.e("JsOn catch exc: ",e.toString());
                            Log.e("JsOn catch exc: ",e.toString());
                            Toast.makeText(MainActivity.this,"Sorry. Something went wrong... Please try after some time..",Toast.LENGTH_LONG).show();
                        }
                        finally {

                            AppController.getInstance().getRequestQueue().getCache().clear();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY ERROR RESP", "Error: " + error.getMessage());
                Log.e("VOLLEY ERROR RESP","Error: " + error.getMessage());
                Log.e("VOLLEY ERROR RESP","Error: " + error.getMessage());
                Log.e("VOLLEY ERROR RESP","Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                Toast.makeText(MainActivity.this,"Sorry. Something went wrong... Please try after some time..",Toast.LENGTH_LONG).show();

            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

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

    private class AsyncListViewLoaderTiled extends AsyncTask<String, String, String> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        InputStreamReader inputStream = null;
        String result = "";
        String listName;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result == null){
                Toast.makeText(MainActivity.this, "Something went wrong! Check your network connection or Try Later", Toast.LENGTH_LONG).show();

            }
            else if (result != null && result.length() > 0 && MainActivity.this != null) {

                Log.e("LINK US: ",dwnldLink);
                Log.e("LINK US: ",dwnldLink);
                Log.e("LINK US: ", dwnldLink);
                dwnldButton.setVisibility(View.VISIBLE);
                textSongName.setVisibility(View.VISIBLE);
                textSongName.setText(title.toString());
                textSongName.setText(dwnldLink.toString());


            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            URL u;
            try {

                Log.e("Params 0 is ",params[0].toString());
                Log.e("Params 0 is ",params[0].toString());
                Log.e("Params 0 is ",params[0].toString());
                u = new URL("https://www.youtubeinmp3.com/fetch/?format=JSON&video="+params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                inputStream = new InputStreamReader(urlConnection.getInputStream());

                try {
                    BufferedReader bReader = new BufferedReader(inputStream);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    inputStream.close();
                    result = sBuilder.toString();
                    Log.e("Results is: ",result);
                    Log.e("Results is: ",result);
                    Log.e("Results is: ",result);
                    int mid = 0,sid=0;
                    String lenght="";
                    try {

                        JSONObject jObj = new JSONObject(result);

                        if(jObj.isNull("title"))
                                title = "NA";
                            else{
                                title = jObj.getString("title");
                            }

                            if(jObj.isNull("length"))
                                lenght = "NA";
                            else{
                                lenght = jObj.getString("length").toUpperCase();
                            }
                        if(jObj.isNull("link"))
                                dwnldLink = "NA";
                            else{
                                dwnldLink = jObj.getString("link");
                                Log.e("LINK US ELSE: ",dwnldLink);
                                Log.e("LINK US: ELSE",dwnldLink);
                                Log.e("LINK US: ELSE ",dwnldLink);
                            }
                            return dwnldLink;

                        
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                        return null;
                    }


                } catch (Exception e) {
                    Log.e("StringBuilding ", "Error converting result " + e.toString());
                    return null;
                }

            } catch (MalformedURLException e) {
                Log.e("Malformed URL ", "Error converting result " + e.toString());
                return null;
            } catch (ProtocolException e) {
                Log.e("ProtocolExc", "Error converting result " + e.toString());
                return null;
            } catch (IOException e) {
                Log.e("IO EXCEPTION ", "Error converting result " + e.toString());
                return null;

            }

        }
    }

}
