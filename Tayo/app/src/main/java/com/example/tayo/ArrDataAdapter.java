package com.example.tayo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ArrDataAdapter extends BaseAdapter {
    Context context;
    ArrayList total_arrdata;

    public ArrDataAdapter(Context context, ArrayList total_arrdata) {
        this.context = context;
        this.total_arrdata = total_arrdata;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout = new LinearLayout(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.line, layout, true);
        TextView route_no = layout.findViewById(R.id.route_no);
        TextView arr_state = layout.findViewById(R.id.arr_state);
        TextView cur_pos = layout.findViewById(R.id.cur_pos);

        ArrData data = (ArrData) total_arrdata.get(position);
        route_no.setText(data.route_no);
        arr_state.setText(data.arr_state);
        cur_pos.setText(data.cur_pos);

        return layout;
    }

    public int getCount() {
        return total_arrdata.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}
