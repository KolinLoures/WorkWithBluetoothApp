package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.R;

import javax.inject.Inject;

public class ConnnectionActivity extends AppCompatActivity implements ContractConnect.ConnView {


    @Inject
    ContractConnect.ConnPresenter presenter;

    private TextView connText;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connnection);

        App.getComponent().inject(this);

        connText = (TextView) findViewById(R.id.connectionState);
        btnConnect = (Button) findViewById(R.id.reConnect);

        presenter.setView(this);
        final String mac = getIntent().getStringExtra("MAC");
        presenter.connectBleDevice(mac);

        presenter.connListener();

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isConnected()){
                    presenter.triggerDisconnect();
                    presenter.clearSub();
                } else {
                    presenter.connectBleDevice(mac);
                }
            }
        });
    }

    @Override
    public void updateUI() {
        if (presenter.isConnected()){
            btnConnect.setText("Disconnect");
            connText.setTextColor(Color.GREEN);
        }
        if (!presenter.isConnected()){
            btnConnect.setText("Connect");
            connText.setTextColor(Color.RED);
        }
    }

    @Override
    public void setTextConn(String s) {
        connText.setText(s);
    }
}
