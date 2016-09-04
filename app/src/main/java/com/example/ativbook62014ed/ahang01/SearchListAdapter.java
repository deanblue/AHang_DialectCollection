package com.example.ativbook62014ed.ahang01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ATIV Book 6 2014ED on 2016-09-04.
 */
/*
public class SearchListAdapter extends BaseAdapter {

    private ArrayList<SearchListItem> listViewItemList = new ArrayList<SearchListItem>();

    public SearchListAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(converView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            converView = inflater.inflate(R.layout.search_list_item, parent, false)''
        }

        TextView tvStandardLanguage = (TextView) converView.findViewById(R.id.tv_standard_language);
        TextView tvregionalLanguage = (TextView) converView.findViewById(R.id.tv_regional_langage);
        RadioButton btnColorSelection = (RadioButton) converView.findViewById(R.id.btn_color_selection);

        SearchListItem listViewItem = listViewItemList.get(position);

        tvStandardLanguage.setText(listViewItem.getStandardLanguage());
        tvregionalLanguage.setText(listViewItem.getRegionalLanguage());
        btnColorSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return null;
    }
}
*/