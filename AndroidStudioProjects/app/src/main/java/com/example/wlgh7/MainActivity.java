package com.example.wlgh7;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> userRating = new ArrayList<>();
    private ArrayList<String> movieLink = new ArrayList<>();


    private EditText editTextMovie;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMovie = findViewById(R.id.editText);
        button= findViewById(R.id.button);

        ButtonListener buttonListener=new ButtonListener();
        button.setOnClickListener(buttonListener);

    }

    private class ButtonListener implements  Button.OnClickListener{
        @Override
        public void onClick(View v) {

            final MovieInfo movieInfo = new MovieInfo();

            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    initRecyclerView(movieInfo);
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    movieNamejsonParser(searchMovie(String.valueOf(editTextMovie.getText())), movieInfo);
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }
    private  void initRecyclerView(MovieInfo movieInfo){
        Log.d("APITEST", "initRecyclerView: called");
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, movieInfo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String searchMovie(String movieName){
        String clientId = "LYOAAgIxekD7dyGkNeYQ";//애플리케이션 클라이언트 아이디값;
        String clientSecret = "HXB6CmlZ3y";//애플리케이션 클라이언트 시크릿값";
         try {
            String text = URLEncoder.encode(movieName, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/movie?query="+ text; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
             HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
             BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public MovieInfo movieNamejsonParser(String jsonString, MovieInfo movieInfo) {
        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("items");
            for (int i = 0; i < jarray.length(); i++) {
                HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(i);
                movieInfo.movieTitle.add(jObject.optString("title"));
                movieInfo.movielink.add(jObject.optString("link"));
                movieInfo.movieThumb_img.add(jObject.optString("image"));
                movieInfo.movieSubtitle.add(jObject.optString("subtitle"));
                movieInfo.moviePubDate.add(jObject.optString("pubDate"));
                movieInfo.movieDirector.add(jObject.optString("director"));
                movieInfo.movieActor.add(jObject.optString("actor"));
                movieInfo.movieUserRating.add(jObject.optString("userRating"));

                Log.d("APITEST", jObject.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieInfo;
    }
}