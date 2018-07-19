package com.example.mac.vera;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mac.vera.models.YeniNot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YeniNotEkleActivity extends AppCompatActivity implements OnMapReadyCallback {
    String secilen_renk;
    RadioGroup radioGroup;
    String select_location;
    @BindView(R.id.edit_text_yeni_baslik)
    EditText editTextYeniBaslik;

    @BindView(R.id.btn_onem_derecesi)
    Button btnOnemDerecesi;
    @BindView(R.id.btn_konum_sec)
    Button btnKonumSec;
    @BindView(R.id.btn_tarih_sec)
    Button btnTarihSec;
    @BindView(R.id.btn_bildirim_sec)
    Button btnBildirimSec;
    @BindView(R.id.edit_text_yeni_icerik)
    EditText editTextYeniIcerik;
    @BindView(R.id.btn_saat_sec)
    Button btnSaatSec;
    @BindView(R.id.btn_kaydet)
    Button btnKaydet;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String tarih, saat, nbildirim;
    @BindView(R.id.txt_onem)
    TextView txtOnem;
    @BindView(R.id.txt_konum)
    TextView txtKonum;
    @BindView(R.id.txt_tarih)
    TextView txtTarih;
    @BindView(R.id.txt_saat)
    TextView txtSaat;
    @BindView(R.id.txt_bildirim)
    TextView txtBildirim;
    Double x, y;
    String uID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @BindView(R.id.image_onem)
    ImageView imageOnem;
    @BindView(R.id.image_konum)
    ImageView imageKonum;
    @BindView(R.id.image_tarih)
    ImageView imageTarih;
    @BindView(R.id.image_saat)
    ImageView imageSaat;
    @BindView(R.id.image_bildirim)
    ImageView imageBildirim;
    @BindView(R.id.btn_kaydetme)
    Button btnKaydetme;
    private GoogleMap mMap;
    //Button ara;
    EditText search_edt;
    //private FusedLocationClient mFusedLocationClient;
    LatLng search_locasyon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_not_ekle);
        ButterKnife.bind(this);

        // Intent intent = getIntent();
        //select_location = intent.getStringExtra("select_locasyon");

        // Toast.makeText(this, select_location, Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("database");

        pref = getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        uID = pref.getString("uID", null);
        Toast.makeText(this, uID, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.btn_onem_derecesi, R.id.btn_konum_sec, R.id.btn_tarih_sec,
            R.id.btn_bildirim_sec, R.id.btn_saat_sec, R.id.btn_kaydet})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_onem_derecesi:
                OnemSec();
                break;
            case R.id.btn_konum_sec:
                KonumSec();
                break;
            case R.id.btn_tarih_sec:
                tarih();
                break;
            case R.id.btn_bildirim_sec:
                // showAddProductDialogBildirim();
                openDialog();
                break;
            case R.id.btn_saat_sec:
                saat();
                break;
            case R.id.btn_kaydet:
                Kaydet();
                break;

        }
    }


    public void tarih() {


        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
        int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

        DatePickerDialog datePicker;//Datepicker objemiz
        datePicker = new DatePickerDialog(YeniNotEkleActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                monthOfYear++;
                // Toast.makeText(startActivity.this, dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_SHORT).show();
                tarih = dayOfMonth + "/" + monthOfYear + "/" + year;
                txtTarih.setVisibility(View.VISIBLE);
                imageTarih.setVisibility(View.VISIBLE);
                txtTarih.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                // TODO Auto-generated method stub
                // tarihTextView.setText(dayOfMonth + "/" + monthOfYear + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            }
        }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
        datePicker.setTitle("Tarih Seçiniz");
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

        datePicker.show();

    }

    public void saat() {
        Calendar mcurrentTime = Calendar.getInstance();//
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
        int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
        TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

        //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
        timePicker = new TimePickerDialog(YeniNotEkleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // Toast.makeText(startActivity.this, selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                saat = selectedHour + ":" + selectedMinute;
                txtSaat.setVisibility(View.VISIBLE);
                imageSaat.setVisibility(View.VISIBLE);
                txtSaat.setText(saat);
                // saatTextView.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
            }
        }, hour, minute, true);//true 24 saatli sistem için
        timePicker.setTitle("Saat Seçiniz");
        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
        timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

        timePicker.show();

    }


    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(YeniNotEkleActivity.this);
        final View subView = inflater.inflate(R.layout.bildirim_dialog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Notun için En Uygun Bildirim Türü Hangisi ?");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        radioGroup = subView.findViewById(R.id.radio_group);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int secilen_radio_btn_id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = subView.findViewById(secilen_radio_btn_id);
                nbildirim = radioButton.getText().toString();
                txtBildirim.setVisibility(View.VISIBLE);
                imageBildirim.setVisibility(View.VISIBLE);
                txtBildirim.setText(nbildirim);
                //  Toast.makeText(startActivity.this, secilen_renk, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(YeniNotEkleActivity.this, "Sizin İçin Uygun Bildirim Türünü Seçmediniz !!", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }


    private void OnemSec() {
        LayoutInflater inflater = LayoutInflater.from(YeniNotEkleActivity.this);
        final View subView = inflater.inflate(R.layout.onem_dialog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Notun Önemini Seç ");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        radioGroup = subView.findViewById(R.id.radio_group);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int secilen_radio_btn_id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = subView.findViewById(secilen_radio_btn_id);
                imageOnem.setVisibility(View.VISIBLE);
                secilen_renk = radioButton.getText().toString();
                // Toast.makeText(startActivity.this, secilen_renk, Toast.LENGTH_SHORT).show();
                txtOnem.setVisibility(View.VISIBLE);
                txtOnem.setText(secilen_renk);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(YeniNotEkleActivity.this, "Uygun Bildirim Türünü Seçmediniz !!", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private static View subView;

    private void KonumSec() {


        LayoutInflater inflater = LayoutInflater.from(YeniNotEkleActivity.this);
        if (subView != null) {
            ViewGroup parent = (ViewGroup) subView.getParent();
            if (parent != null)
                parent.removeView(subView);
        } else {
            subView = inflater.inflate(R.layout.activity_maps, null);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        Button ara = subView.findViewById(R.id.ara_btn);
        search_edt = subView.findViewById(R.id.ara_edt);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(YeniNotEkleActivity.this);


        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();

            }
        });
        if (YeniNotEkleActivity.this.mMap != null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions marker = new MarkerOptions().position(
                            latLng)
                            .title("Hello Maps ");
                    // marker.icon(BitmapDescriptorFactory
                    //  .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mMap.addMarker(marker);
                    Toast.makeText(YeniNotEkleActivity.this, search_locasyon.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtKonum.setVisibility(View.VISIBLE);
                imageKonum.setVisibility(View.VISIBLE);
                txtKonum.setText(search_edt.getText().toString());
                //  mapFragment.onDetach();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(YeniNotEkleActivity.this, "Uygun Bildirim Türünü Seçmediniz !!", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(38, 35);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(sydney).zoom(10).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }


    //Arama sonucunun konumunu getiren kod
    private void geoLocate() {

        String searchString = search_edt.getText().toString();


        Geocoder geocoder = new Geocoder(YeniNotEkleActivity.this);

        List<Address> list = new ArrayList<>();

        try {

            list = geocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {


        }


        if (list.size() > 0) {

            Address address = list.get(0);


            search_locasyon = new LatLng(address.getLatitude(), address.getLongitude());
            x = address.getLatitude();
            y = address.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(search_locasyon));
            final Marker newMaker = mMap.addMarker(new MarkerOptions().position(search_locasyon).title("deneme"));

            // CameraPosition cameraPosition = CameraPosition.builder()
            //       .target(search_locasyon).zoom(10).build();
            // mMap.animateCamera(CameraUpdateFactory
            //       .newCameraPosition(cameraPosition));

            /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    newMaker.remove();
                }
            }, 15000);

*/

            address.getAddressLine(0);

        }


    }

    public void Kaydet() {


        AlertDialog.Builder builder = new AlertDialog.Builder(YeniNotEkleActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Kaydet");
        builder.setMessage("Bu notu katydetmek istiyormusunuz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextYeniBaslik.getText().toString().isEmpty() || editTextYeniIcerik.getText().toString().isEmpty() || secilen_renk.isEmpty() ||
                        search_locasyon.toString().isEmpty() || tarih.isEmpty() | saat.isEmpty() || nbildirim.isEmpty()) {

                    Toast.makeText(YeniNotEkleActivity.this, "Bütün Alanları Doldurdugunuzdan Emin Olun !!", Toast.LENGTH_SHORT).show();
                } else {
                    String key = myRef.child("notlar").push().getKey();
                    YeniNot not = new YeniNot(
                            uID,
                            key,
                            editTextYeniBaslik.getText().toString(),
                            editTextYeniIcerik.getText().toString(),
                            secilen_renk,
                            x,
                            y,
                            tarih,
                            search_edt.getText().toString(),
                            saat,
                            nbildirim);
                    myRef.child("notlar").child(key).setValue(not);
                    Intent intent = new Intent(YeniNotEkleActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        })
                .setNegativeButton("Hayır", null)
                .show();

    }

    @OnClick(R.id.btn_kaydetme)
    public void onViewClicked() {
        startActivity(new Intent(YeniNotEkleActivity.this, MainActivity.class));
    }
}