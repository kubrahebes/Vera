package com.example.mac.vera;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mac.vera.adapter.MainListAdapter;
import com.example.mac.vera.models.YeniNot;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mac.vera.R.drawable.mcv_action_next;

public class MainActivity extends AppCompatActivity {

    Drawer result;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.nav_drawer_menu)
    ImageView navDrawerMenu;
    @BindView(R.id.list_view_notlar)
    ListView listViewNotlar;
    ArrayList<YeniNot> kayitli_notlar;
    MainListAdapter adapter;
    YeniNot value;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String uID;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    String kayitliTarih, kayiliSaat;
    Double kayitliLatitude, kayitliLongitude;
    LatLng kayitliKonum;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        kayitli_notlar = new ArrayList<>();
        adapter = new MainListAdapter(this, new ArrayList<YeniNot>());
        listViewNotlar.setAdapter(adapter);

        calendarView.newState();
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        calendarView.setTopbarVisible(false);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(MainActivity.this, "" + date.getDay() + date.getMonth(), Toast.LENGTH_SHORT).show();
                bildirim();
            }
        });


        navigationDrawer();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("database").child("notlar");

        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        uID = pref.getString("uID", null);


        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getdata();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Kapat");
            builder.setMessage("İnternet Baglantınız yapılamadı Kapatmak istermisiniz ?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();                         //Stop the activity
                }

            })
                    .setNegativeButton("Hayır", null)
                    .show();
            // notConnectedText.setText(R.string.noconnection_String);
        }


    }

    public void tarihVeSaatKontrol() {
        LocalDate now = new LocalDate();
        LocalTime time = new LocalTime();
        if (now.equals(kayitliTarih) && time.equals(kayiliSaat)) {
            Toast.makeText(this, "Saat ve tarih uygunn", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "saat ve tarih uygun degil", Toast.LENGTH_SHORT).show();
        }

    }

    public void konumKontrol() {

    }

    public void kontrol(String kontrolText) {

        if (kontrolText.equals("Konum ve Tarih Uygunsa")) {

            tarihVeSaatKontrol();
            konumKontrol();

        } else if (kontrolText.equals("Konumun Uygun olması yeterli")) {

            konumKontrol();
        } else {
            tarihVeSaatKontrol();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bildirim() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    /**
     * get data from the firebase
     */
    public void getdata() {

        myRef.orderByChild("uid").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<YeniNot> NotList = new ArrayList<>();
                for (DataSnapshot verigetir : dataSnapshot.getChildren()) {
                    value = verigetir.getValue(YeniNot.class);

                    kayiliSaat = value.getSaat();

                    kayitliLatitude = value.getxKordinat();
                    kayitliLongitude = value.getyKordinat();
                    kayitliKonum = new LatLng(kayitliLatitude, kayitliLongitude);
                    kayitliTarih = value.getTarih();

                    kontrol(value.getBildirim());
                    NotList.add(value);
                }
                setdata(NotList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }


    /**
     * Set Adapter
     */
    public void setdata(final ArrayList<YeniNot> list) {
        if (list.isEmpty()) {
            Toast.makeText(MainActivity.this, "Empty ListView", Toast.LENGTH_SHORT).show();
        } else {
            adapter.clear();
            adapter.addAll(list);
            listViewNotlar.setAdapter(adapter);
            listViewNotlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this, NotDetayActivity.class);
                    intent.putExtra("selectId", list.get(i).getKey());

                    startActivity(intent);
                }
            });
        }
    }


    public void navigationDrawer() {

        // SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1)
        //       .withName("Home").withIcon(R.drawable.ic_home);
        //PrimaryDrawerItem primaryDrawerItem = new PrimaryDrawerItem().withIdentifier( 1 ).withName( "test" );
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withName("Eski Hatırlatmalar").withIcon(R.drawable.hat_rlatma);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withName("Yeni Not Ekle").withIcon(R.drawable.ekle);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3)
                .withName(" Ayarlar").withIcon(R.drawable.ayarlar);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(3)
                .withName(" Çıkış Yap");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.resim)
                .addProfiles(
                        new ProfileDrawerItem().withName("Ayse ").withEmail(
                                "Ayse@gmail.com")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })


                .build();


        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1, item2, item3,item4)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position) {

                            case 1:
                                bildirim();
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, YeniNotEkleActivity.class));
                                break;
                            case 3:

                                break;
                            case 4:
                                logout();
                                break;

                        }

                        return true;
                    }
                }).build();
        /*
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
*/
    }


    @OnClick(R.id.nav_drawer_menu)
    public void onViewClicked() {

        if (result != null) {
            result.openDrawer();
        }
    }


    public void logout() {
        editor.putBoolean("IS_LOGIN", false);
        editor.commit();
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
