package com.funbox.ecommerce.funbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Наиль on 02.08.2015.
 */
public class DataAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FieldCsv> fieldCsvs;

    public DataAdapter(Context context, ArrayList<FieldCsv> fieldCsvs) {
        this.context = context;
        this.fieldCsvs = fieldCsvs;
    }

    static class ViewHolder {
        TextView name;
        TextView number;
    }

    @Override
    public int getCount() {
        return fieldCsvs.size();
    }

    @Override
    public Object getItem(int i) {
        return fieldCsvs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.nameItem);
            viewHolder.number = (TextView) convertView.findViewById(R.id.numberItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(fieldCsvs.get(position).getTitle());
        viewHolder.number.setText(Integer.toString(fieldCsvs.get(position).getNumber()));

        return convertView;
    }
}
