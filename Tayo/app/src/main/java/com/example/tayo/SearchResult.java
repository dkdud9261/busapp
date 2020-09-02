package com.example.tayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchResult extends Activity {
    ListView listView;
    ArrayList total_arrdata = new ArrayList();
    ArrDataAdapter adapter;
    String station_number;

    Handler h = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            listView.setAdapter(adapter);
        }
    };

    class MyThread extends Thread {
        public void run() {
            //버스도착정보 http request
            try {
                URL url = new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                String parameter = "act=arrbus&winc_id=" + station_number;

                OutputStream wr = con.getOutputStream();
                wr.write(parameter.getBytes());
                wr.flush();

                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "euc-kr"));
                Source s = new Source(rd);
                s.fullSequentialParse();
                List<Element> all = s.getAllElements();

                ArrData data = null;
                for (int i = 0; i < all.size(); i++) {
                    Element e = all.get(i);
                    if (e.getName().equals("span")) {
                        String att = e.getAttributeValue("class");
                        switch (att) {
                            case "route_no":
                                data = new ArrData();
                                data.route_no = e.getTextExtractor().toString();
                                Log.d("##############", "route_no = " + e.getTextExtractor());
                                break;
                            case "arr_state":
                                data.arr_state = e.getTextExtractor().toString();
                                Log.d("##############", "arr_state = " + e.getTextExtractor());
                                break;
                            case "cur_pos busNN":
                            case "cur_pos busDN":
                                data.cur_pos = e.getTextExtractor().toString();
                                total_arrdata.add(data);
                                Log.d("##############", "cur_pos = " + e.getTextExtractor());
                                break;
                        }
                    }
                }
                h.sendEmptyMessage(0);
            } catch (Exception e) {
                Log.d("Network Exception", "" + e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        station_number = i.getStringExtra("station_number");
        MyThread mt = new MyThread();
        mt.start();
        listView = new ListView(this);
        adapter = new ArrDataAdapter(this, total_arrdata);
        setContentView(listView);
    }
}