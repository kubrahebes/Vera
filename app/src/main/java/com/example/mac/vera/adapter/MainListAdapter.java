package com.example.mac.vera.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mac.vera.R;
import com.example.mac.vera.models.YeniNot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainListAdapter extends ArrayAdapter<YeniNot> {
    ArrayList<YeniNot> arrayList;

    public MainListAdapter(Activity context, ArrayList<YeniNot> arrayList) {
        super(context, 0, arrayList);

        //addAll(CacheHelper.getInstance(getContext()).getStoreInfo().branches);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        view = LayoutInflater.from(getContext()).inflate(
                R.layout.main_list_item, parent, false
        );

        holder = new ViewHolder(view);
        view.setTag(holder);
        YeniNot goalObje = getItem(position);

        holder.txtSaat.setText(goalObje.getSaat());
        holder.txtKonum.setText(goalObje.getKonum_name());
        holder.txtBaslik.setText(goalObje.getBaslik());

        return view;
    }


    static class ViewHolder {
        @BindView(R.id.image_konum)
        ImageView imageKonum;
        @BindView(R.id.txt_saat)
        TextView txtSaat;
        @BindView(R.id.txt_baslik)
        TextView txtBaslik;
        @BindView(R.id.txt_konum)
        TextView txtKonum;
        @BindView(R.id.image_yapildi)
        ImageView imageYapildi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
