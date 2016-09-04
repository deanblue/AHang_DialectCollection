package com.example.ativbook62014ed.ahang01;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by LG PC on 2016-09-04.
 */
public class NamePopUpActivity extends Dialog {
    private Button mSave;
    private OnDismissListener _listener;
    private EditText mFileName;

    public NamePopUpActivity(Context context){
        super(context);     //Dialog를 사용하기 위한 초기화
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_pop_up);

        mSave = (Button)findViewById(R.id.btn_Save);
        mFileName = (EditText)findViewById(R.id.edit_FileName);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_listener == null){  }
                else{
                    _listener.onDismiss(NamePopUpActivity.this);    //종료됨
                }
                dismiss();      //파일명이 입력이 안되어있을 시에 그냥 종료가 되는데 그 것을 막으려면 if문안에 토스트를 넣고 이 행을 지운다.
            }
        });
    }

    public void setOnDismissListener(OnDismissListener $listener) { //종료될때 이벤트
        _listener = $listener ;     //꺼질때 이름을 저장
    }

    public String getName() {
        return mFileName.getText().toString() ;
    }
}
