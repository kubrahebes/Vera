package com.example.mac.vera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.vera.models.Kullanicilar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaydolActivity extends BaseActivity {

    @BindView(R.id.edit_text_kullanici_adi)
    MaterialEditText editTextKullaniciAdi;
    @BindView(R.id.edit_text_e_posta)
    MaterialEditText editTextEPosta;
    @BindView(R.id.edit_text_sifre)
    MaterialEditText editTextSifre;
    @BindView(R.id.edit_text_sifre_tekrar)
    MaterialEditText editTextSifreTekrar;
    @BindView(R.id.btn_kaydol)
    Button btnKaydol;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private Toast toast = null;

    public boolean flag = false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DatabaseReference myRef;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);
        ButterKnife.bind(this);
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("database");
        mAuth = FirebaseAuth.getInstance();

        //Progress dialog for logging screen. Shows the user a progress while he is logging.
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Registering...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(true);
        mProgress.setIndeterminate(true);
    }


    public void getusers() {
        mAuth.createUserWithEmailAndPassword(editTextEPosta.getText().toString(), editTextSifre.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(KaydolActivity.this, "Signup Success!", Toast.LENGTH_SHORT).show();

                            //create a table in the firebase and add the new user information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Kullanicilar yeniKullanici = new Kullanicilar(user.getUid(), editTextKullaniciAdi.getText().toString(), user.getEmail());
                            myRef.child("kullanicilar").child(user.getUid()).setValue(yeniKullanici);
                           saveUser(yeniKullanici);
                            //set the shared Preference for user who can login
                            editor.putBoolean("IS_LOGIN", true);
                            editor.putString("uID", user.getUid());
                            editor.commit();
                            Toast.makeText(KaydolActivity.this, pref.getString("uID",null), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(KaydolActivity.this, MainActivity.class));


                            finish();
                        } else if (!task.isSuccessful() && flag) {
                            mProgress.dismiss();
                            Toast.makeText(KaydolActivity.this, "Email address wrong or your password is too easy please change your password.", Toast.LENGTH_SHORT).show();
                        }


                    }

                });

    }

    @OnClick(R.id.btn_kaydol)
    public void onViewClicked() {

        mProgress.show();  //Shows the progress dialog.
        isonline();//Checking if the user is online or not.
        getusers();

    }


    //Method for checking if there is an internet or not.
    public void isonline() {

        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            mProgress.hide();
            //Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, It appears you don't have Internet connection!");
            alert.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alert.show();

        } else {
            flag = true;
        }

    }

}
