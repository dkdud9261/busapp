package com.example.tayo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StationDataAdapter extends BaseAdapter {

    Context context;
    ArrayList<StationData> data;

    public StationDataAdapter(Context context, ArrayList<StationData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int i) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = new LinearLayout(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stationinfo, layout, true);
        TextView name = layout.findViewById(R.id.station_name);
        TextView number = layout.findViewById(R.id.station_number);

        StationData d = data.get(position);
        name.setText(d.name);
        number.setText(d.number);

        return layout;
    }
}
