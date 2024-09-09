package com.module.connect.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.module.connect.R;
import com.module.connect.adapter.AddApter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AddApter mAddApter;

    private RecyclerView mRAddRv;

    private Button mAddBtn;
    private Button mOtherBtn;

    private List<String> mNewAddList;
    private List<String> mOtherList;

    private MutableLiveData<String> mStartList;
    private MutableLiveData<String> mAfterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        Button mCrashBtn = findViewById(R.id.btn_native_crash);
//
//        mCrashBtn.setOnClickListener(v -> {
//            CrashTest.testNativeCrash();
//            CrashTest.init(this);
//        });

        mStartList = new MutableLiveData<>();
        mAfterList = new MutableLiveData<>();


        mAddBtn = findViewById(R.id.btn_add);
        mRAddRv = findViewById(R.id.rv_add);
        mOtherBtn = findViewById(R.id.btn_other);

        mNewAddList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mNewAddList.add("这是第：" + i + "条数据");
        }

        mAddApter = new AddApter();
        mRAddRv.setAdapter(mAddApter);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartList.postValue("start");
            }
        });

        mOtherList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mOtherList.add("另一组：" + i + "条数据");
        }

        mOtherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartList.postValue("after");
            }
        });

        mStartList.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("start")) {
                    mAddApter.setData(mNewAddList);
                } else {
                    mAddApter.setData(mOtherList);
                }
            }
        });

        mAfterList.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("after")) {
                    mAddApter.setData(mOtherList);
                }
            }
        });
    }
}
