package com.example.ativbook62014ed.ahang01;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SearchListItem> mDialectList;

    public SearchListAdapter(Context context, ArrayList<SearchListItem> dialectList) {
        mContext = context;
        mDialectList = dialectList;
    }

    @Override
    public int getCount() {
        return mDialectList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDialectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchListItem item = mDialectList.get(position);
        View view;

        view = View.inflate(mContext, R.layout.custom_search_list, null);

        TextView dialect = (TextView) view.findViewById(R.id.search_dialect);
        final ImageView color = (ImageView) view.findViewById(R.id.search_color);

        dialect.setText(item.getDialect());
        color.setImageResource(item.getColor());

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickDialogUtil dialog = new ColorPickDialogUtil(mContext, color);
                dialog.show();
            }
        });
        return view;
    }
}
