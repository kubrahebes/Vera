package com.example.mac.vera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.vera.models.YeniNot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotDetayActivity extends AppCompatActivity {

    @BindView(R.id.txt_baslik)
    TextView textBaslik;
    @BindView(R.id.txt_icerik)
    TextView textIcerik;
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

    String selectedItem;
    String userNAme;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button btnOpenDialog;
    TextView textInfo;
    String goalId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    YeniNot value,value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_detay);
        ButterKnife.bind(this);

        pref = NotDetayActivity.this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
       // userNAme = getUser().getUserName();
        Intent intent = getIntent();
        selectedItem = intent.getStringExtra("selectId");

        /**
         *Firebase connection
         */
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("database").child("notlar");
        myRef2 = database.getReference("database");
        getdata();
    }

    /**
     * get data from the firebase
     */
    public void getdata() {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot verigetir : dataSnapshot.getChildren()) {

                    try {
                        value = verigetir.getValue(YeniNot.class);

                        if (selectedItem.equals(value.getKey())) {
                            setdata(value);
                            value2 = value;
                        }

                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        System.out.println("Error " + e.getMessage());
                    }


                    //  Toast.makeText(FinanceGoalDetail.this, ""+value.getId().equals(selectedItem), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(NotDetayActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    /**
     * Set Adapter
     */
    public void setdata(YeniNot obje) {

        textBaslik.setText(obje.getBaslik());
        textIcerik.setText(obje.getIcerik());
        txtBildirim.setText(obje.getBildirim());
        txtKonum.setText(obje.getKonum_name());
        txtOnem.setText(obje.getOnem_renk());
        txtSaat.setText(obje.getSaat());
        txtTarih.setText(obje.getTarih());
    }

}
