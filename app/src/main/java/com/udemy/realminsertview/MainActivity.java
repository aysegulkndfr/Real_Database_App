package com.udemy.realminsertview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Realm realm;
    EditText kullaniciAdi;
    EditText sifre;
    EditText isim;
    RadioGroup cinsiyetGrup;
    Button button;
    Button guncelleButton;
    Integer positionT = 0;
    String cinsiyetText, isimText, kullaniciAdiText, sifreText;
    RealmResults<KisiBilgileri> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RealmTanimla();
        tanimla();
        ekle();
        goster();
        pozisyonBul();
    }

    public void RealmTanimla() {

        realm = Realm.getDefaultInstance();
    }

    public void tanimla() {
        listView = findViewById(R.id.listView);
        kullaniciAdi = findViewById(R.id.editTextKullanici);
        sifre = findViewById(R.id.editTextSifre);
        isim = findViewById(R.id.editTextİsim);
        cinsiyetGrup = findViewById(R.id.CinsiyetRadio);
        button = findViewById(R.id.KayitolButton);
        guncelleButton = findViewById(R.id.GuncelleButton);
    }

    public void ekle() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BilgileriAl();
                yaz(cinsiyetText, isimText, kullaniciAdiText, sifreText);
                kullaniciAdi.setText("");
                isim.setText("");
                sifre.setText("");

                goster();

            }
        });

        guncelleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VeriTabanındanListeGetir();

                final KisiBilgileri kisi = list.get(positionT);

                BilgileriAl();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        kisi.setCinsiyet(cinsiyetText);
                        kisi.setKullanici(kullaniciAdiText);
                        kisi.setSifre(sifreText);
                        kisi.setIsim(isimText);
                    }
                });
                goster();


            }
        });
    }

    public void yaz(final String cinsiyet, final String isim, final String kullaniciAdi, final String sifre) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                KisiBilgileri kisiBilgileri = realm.createObject(KisiBilgileri.class);
                kisiBilgileri.setCinsiyet(cinsiyet);
                kisiBilgileri.setSifre(sifre);
                kisiBilgileri.setKullanici(kullaniciAdi);
                kisiBilgileri.setIsim(isim);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_LONG).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void goster() {
        VeriTabanındanListeGetir();
          /*for (KisiBilgileri k: kisiBilgileris){
            Log.i("Gelenler", k.toString());
        }*/
        if (list.size() > 0) {
            adapter adapter = new adapter(list, getApplicationContext());
            listView.setAdapter(adapter);
        }
    }

    public void pozisyonBul() {

        VeriTabanındanListeGetir();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("pozisyonGelen",""+position);
                //sil(position);//tıklandığında bu fon. çağır
                ac(position);
                kullaniciAdi.setText(list.get(position).getKullanici());
                sifre.setText(list.get(position).getSifre());
                isim.setText(list.get(position).getIsim());
                if (list.get(position).getCinsiyet().equals("Erkek")) {//0. item erkek

                    ((RadioButton) cinsiyetGrup.getChildAt(0)).setChecked(true);

                } else {//1. item kadın
                    ((RadioButton) cinsiyetGrup.getChildAt(1)).setChecked(true);
                }
                positionT = position;//Listview'ın hangi item'ına tıklandıysa onun pozisyonunu atadık.
            }
        });
    }

    public void sil(final int position) {
        // Log.i("name", "" + position);

        VeriTabanındanListeGetir();
        //Log.i("name2","Liste Eleman Sayısı"+gelenList.size());//kaç veri kayıtlı gösterir.
        Log.i("name2", "Liste Eleman Sayısı" + list.get(position).getKullanici());//tıklananın kullanıcı adını gösterir.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                KisiBilgileri kisi = list.get(position);
                kisi.deleteFromRealm();
                goster();
                kullaniciAdi.setText("");
                isim.setText("");
                sifre.setText("");
            }
        });
    }

    public void ac(final int position) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertlayout, null);

        Button evetButon = view.findViewById(R.id.evetButon);
        Button hayirButon = view.findViewById(R.id.hayirButon);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(false);//sadece hayır butonuna basınca alertdiaolog kapanır.

        final AlertDialog dialog = alert.create();
        evetButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sil(position);
                dialog.cancel();
            }
        });
        hayirButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void BilgileriAl() {

        Integer id = cinsiyetGrup.getCheckedRadioButtonId();//hangi id'ye tıklandı
        RadioButton radioButton = (RadioButton) findViewById(id); //tanımlaması yapıldı.
        cinsiyetText = radioButton.getText().toString();
        isimText = isim.getText().toString();
        kullaniciAdiText = kullaniciAdi.getText().toString();
        sifreText = sifre.getText().toString();
    }

    public void VeriTabanındanListeGetir() {
        list = realm.where(KisiBilgileri.class).findAll();
    }
}