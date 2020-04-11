package com.udemy.realminsertview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class adapter extends BaseAdapter {
    List<KisiBilgileri> list;
    Context context;

    public adapter(List<KisiBilgileri> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout,parent,false);
        TextView isim = convertView.findViewById(R.id.kullaniciAdiIsim);
        TextView sifre = convertView.findViewById(R.id.kullaniciSifre);
        TextView kullaniciAdi = convertView.findViewById(R.id.kullaniciAdiText);
        TextView cinsiyet = convertView.findViewById(R.id.kullaniciCinsiyet);

        isim.setText(list.get(position).getIsim());
        sifre.setText(list.get(position).getSifre());
        kullaniciAdi.setText(list.get(position).getKullanici());
        cinsiyet.setText(list.get(position).getCinsiyet());
        return convertView;
    }
}
