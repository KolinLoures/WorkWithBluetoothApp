package com.example.nkirilov.workwithbluetoothapp.presentation.search;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.R;
import com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice.ConnnectionActivity;
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

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnAdapterItemClickListener(new Adapter.OnAdapterItemClickListener() {
            @Override
            public void onAdapterClick(View view) {
                int positionAdapter = recyclerView.getChildAdapterPosition(view);
                RxBleScanResult item = adapter.getItemAtPosition(positionAdapter);
                onAdapterItemClick(item);
            }
        });
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
        if (presenter.isScanning()){
            presenter.unsub();
            clearResult();
        } else {
            adapter.clearResults();
            presenter.onScanClick();
        }
    }

    @Override
    public void updateFabSrc() {
        if (presenter.isScanning()){
            fab.setImageResource(R.drawable.ic_highlight_off_black_24dp);
        }
        if (!presenter.isScanning()){
            fab.setImageResource(R.drawable.ic_youtube_searched_for_black_24dp);
        }
    }

    @Override
    public void clearResult() {
        presenter.clearSubscription();
        updateFabSrc();
    }

    @Override
    public void onAdapterItemClick(RxBleScanResult results) {
        String macAddress = results.getBleDevice().getMacAddress();
        Intent intent = new Intent(this, ConnnectionActivity.class);
        intent.putExtra("MAC", macAddress);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (presenter.isScanning()){
            presenter.unsub();
        }
    }


}
