package com.example.ativbook62014ed.ahang01;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lai.OH on 2016-09-13.
 */
public class SearchDialectListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SearchListItem> mDialectList;

    public SearchDialectListAdapter(Context context, ArrayList<SearchListItem> dialectList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        SearchListItem item = mDialectList.get(position);
        View view;

        view = View.inflate(mContext, R.layout.custom_search_dialect_list, null);


        TextView dialect = (TextView) view.findViewById(R.id.search_dialect_dialect);
        TextView address = (TextView) view.findViewById(R.id.search_dialect_address);

        dialect.setText(item.getDialect());
        address.setText(item.getAddress());

        return view;
    }
}
