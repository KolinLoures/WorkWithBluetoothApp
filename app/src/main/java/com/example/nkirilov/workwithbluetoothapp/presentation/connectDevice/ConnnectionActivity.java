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
    private TextView textStatus;
    private TextView textOut;
    private Button btnConnect;
    private Button btnStart;
    private Button btnVibro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connnection);

        App.getComponent().inject(this);

        connText = (TextView) findViewById(R.id.connectionState);
        textStatus = (TextView) findViewById(R.id.textStatus);
        textOut = (TextView) findViewById(R.id.textOut);
        btnConnect = (Button) findViewById(R.id.reConnect);
        btnStart = (Button) findViewById(R.id.startBtn);
        btnVibro = (Button) findViewById(R.id.vibroBtn);

        presenter.setView(this);
        final String mac = getIntent().getStringExtra("MAC");

        presenter.createBleDevice(mac);
        presenter.connListener();

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isConnected()) {
                    presenter.triggerDisconnect();
                }
                else {
                    presenter.createBleDevice(mac);
                }
                updateUI();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    presenter.onClickStartCommucation();
            }
        });

        btnVibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    presenter.onClickVibroCmd();
            }
        });
//        btnVibro.setEnabled(false);

    }

    @Override
    public void updateUI() {
        if (presenter.isConnected()){
            btnConnect.setText("Disconnect");
            connText.setTextColor(Color.GREEN);
            textStatus.setText("");
            textOut.setText("");
        }
        if (!presenter.isConnected()){
            btnConnect.setText("Connect");
            connText.setTextColor(Color.RED);
            textStatus.setText("");
            textOut.setText("");
        }
    }

    @Override
    public void setTextConn(String s) {
        connText.setText(s);
    }

    @Override
    public void setTextStatus(String s) {
        textStatus.setText(s);
    }

    @Override
    public void setOutText(String s) {
        String ss = (String) textOut.getText();
        textOut.setText(ss+"\n"+s);
    }
}
