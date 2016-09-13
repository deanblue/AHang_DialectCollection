package com.example.ativbook62014ed.ahang01;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
public class SearchStandardListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SearchListItem> mDialectList;


    public SearchStandardListAdapter(Context context, ArrayList<SearchListItem> dialectList) {
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
        SearchListItem item;
        item = mDialectList.get(position);
        View view;

        view = View.inflate(mContext, R.layout.custom_search_list, null);

        CheckBox check;
        TextView dialect;
        final ImageView color;

        check = (CheckBox) view.findViewById(R.id.search_checkbox);
        dialect = (TextView) view.findViewById(R.id.search_dialect);
        color = (ImageView) view.findViewById(R.id.search_color);

        if (item.getCheck() == 1) {
            check.setChecked(true);
        } else {
            check.setChecked(false);
        }

        dialect.setText(item.getDialect());
        color.setImageResource(item.getColor());

        dialect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchDialectListActivity.class);
                intent.putExtra("dialect", mDialectList.get(position).getDialect());
                mContext.startActivity(intent);
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickDialogUtil dialog = new ColorPickDialogUtil(mContext, color, mDialectList, position);
                dialog.show();
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(mContext, "체크", Toast.LENGTH_SHORT);
                    Log.e("checkbox", "체크");
                    //check.setChecked(true);
                    mDialectList.get(position).setCheck(1);
                } else {
                    Toast.makeText(mContext, "해제", Toast.LENGTH_SHORT);
                    Log.e("checkbox", "해제");
                    //check.setChecked(false);
                    mDialectList.get(position).setCheck(0);
                }
            }
        });

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mDialectList.get(position).getDialect(), Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }
}
