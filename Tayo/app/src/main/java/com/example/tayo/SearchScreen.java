package com.example.tayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchScreen extends Activity {
    ArrayList<StationData> total_stationInfo = new ArrayList<>();
    ListView listView;
    EditText location;
    Button search;
    StationDataAdapter adapter;

    Handler h = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            listView.setAdapter(adapter);
            search.setEnabled(true);
        }
    };

    public void stationInfoParsing(BufferedReader src) {
        try {
            Source s = new Source(src);
            s.fullSequentialParse();
            List<Element> all = s.getAllElements();
            StationData data = null;
            int count = 0;
            for(int i = 0; i < all.size(); i++) {
                Element el = all.get(i);
                if(el.getName().equals("td")) {
                    if(count == 0) {
                        data = new StationData();
                        data.name = el.getTextExtractor().toString();
                        count++;
                    }
                    else if(count == 1) {
                        data.number = el.getTextExtractor().toString();
                        total_stationInfo.add(data);
                        count = 0;
                    }
                }
            }
        } catch (IOException e) {
           Log.d("############", "파싱 오류 발생");
        }
    }

    class MyThread extends Thread {
        public void run() {
            try {
                URL url = new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                String parameter = "act=findByBusStopNo&bsNm="+location.getText();
                OutputStream wr = con.getOutputStream();
                wr.write(parameter.getBytes("euc-kr"));
                wr.flush();

                InputStream is = con.getInputStream();
                Reader r = new InputStreamReader(is, "euc-kr");
                BufferedReader br = new BufferedReader(r);

                stationInfoParsing(br);

                h.sendEmptyMessage(0);
            } catch (Exception e) {
                Log.d("Network Exception", "" + e);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reqstation);
        location = findViewById(R.id.location);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchScreen.this, "검색중...", Toast.LENGTH_SHORT).show();
                search.setBackgroundResource(R.drawable.search);
                search.setEnabled(false);
                new MyThread().start();
                InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(location.getWindowToken(), 0);
                total_stationInfo.clear();
            }
        });

        listView = findViewById(R.id.listview);
        adapter = new StationDataAdapter(this, total_stationInfo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LinearLayout layout = (LinearLayout)view;
                LinearLayout layout2 = (LinearLayout)layout.getChildAt(0);
                TextView number = (TextView)layout2.getChildAt(1);
                Intent i = new Intent(SearchScreen.this, SearchResult.class);
                i.putExtra("station_number", number.getText());
                startActivity(i);
            }
        });
    }
}
