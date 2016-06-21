package com.example.nkirilov.workwithbluetoothapp.presentation;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.R;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements Contarct.View {

    @Inject
    Contarct.Presenter presenter;


    private FloatingActionButton fab;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getComponent().inject(this);
        presenter.attachView(this);

        adapter = new Adapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFab();
            }
        });
    }

    @Override
    public void showResult(RxBleScanResult result) {
        adapter.addScanResult(result);
    }

    @Override
    public void clickFab() {
        presenter.onScanClick();
    }

    @Override
    public void updateFabSrc() {
        if (presenter.isScanning()){
            fab.setImageResource(R.drawable.ic_highlight_off_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_youtube_searched_for_black_24dp);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (presenter.isScanning()){
            presenter.unsub();
        }
    }


}
