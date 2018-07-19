package com.example.mac.vera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_e_posta)
    MaterialEditText editTextEPosta;
    @BindView(R.id.edit_text_sifre)
    MaterialEditText editTextSifre;
    @BindView(R.id.btn_giris)
    Button btnGiris;
    @BindView(R.id.btn_kaydol)
    TextView btnKaydol;

    DatabaseReference myRef;
    FirebaseDatabase database;
    EditText sinInUserName;
    EditText sinUInPassword;
    @BindView(R.id.txt_sifre_unuttum)
    TextView txtSifreUnuttum;
    private FirebaseAuth mAuth;
    Unbinder unbinder;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    /**
     * get user ont he firebas for login
     *
     * @param mail
     * @param password
     */

    private void LoginUser(String mail, final String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            editor.putBoolean("IS_LOGIN", true);
                            editor.putString("uID", user.getUid());
                            editor.commit();
                            Toast.makeText(LoginActivity.this, "Login Succsess!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Validate control for the email and password EditText
     *
     * @return
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = editTextEPosta.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEPosta.setError("Required.");
            valid = false;
        } else {
            editTextEPosta.setError(null);
        }
        String password = editTextSifre.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextSifre.setError("Required.");
            valid = false;
        } else {
            editTextSifre.setError(null);
        }
        return valid;
    }


    /**
     * Dialog for  Forget password
     */

    private void showAddProductDialog() {
        ForgotPassDialog dialog = new ForgotPassDialog();
        dialog.show(getFragmentManager(), "example");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_giris, R.id.btn_kaydol, R.id.txt_sifre_unuttum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_giris:
                LoginUser(editTextEPosta.getText().toString(), editTextSifre.getText().toString());
                // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.btn_kaydol:
                startActivity(new Intent(LoginActivity.this, KaydolActivity.class));
                break;
            case R.id.txt_sifre_unuttum:
                showAddProductDialog();
                break;
        }
    }
}
