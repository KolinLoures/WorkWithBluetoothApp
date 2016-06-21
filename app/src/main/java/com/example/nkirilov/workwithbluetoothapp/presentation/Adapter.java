package com.example.nkirilov.workwithbluetoothapp.presentation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.R;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    @Inject
    Context context;

    private final List<RxBleScanResult> list = new ArrayList<>();

    public Adapter() {
        App.getComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RxBleScanResult rxBleScanResult = list.get(position);
        RxBleDevice bleDevice = rxBleScanResult.getBleDevice();
        holder.textView1.setText(bleDevice.getMacAddress() +
                "(" + bleDevice.getName()+")");
        holder.textView2.setText("RSSI: " + rxBleScanResult.getRssi());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addScanResult(RxBleScanResult result){
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getBleDevice().equals(result.getBleDevice())) {
                list.set(i, result);
                notifyItemChanged(i);
                return;
            }
        }

        list.add(result);
        notifyDataSetChanged();
    }

    public void clearResults(){
        list.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView) itemView.findViewById(R.id.textView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
