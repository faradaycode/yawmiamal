package com.magentamedia.yaumiamal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.magentamedia.yaumiamal.fragments.WelcomeDialog;
import com.magentamedia.yaumiamal.models.RegisterFeedback;
import com.magentamedia.yaumiamal.models.yawmiServerAPI;
import com.magentamedia.yaumiamal.models.yawmiClient;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 787;

    private SharedPreferences sp;

    private YawmiMethodes me;

    // UI references.

    @BindView(R.id.linear_login_container)
    LinearLayout logintxtcon;

    @BindView(R.id.login_btn_cont)
    LinearLayout loginbtncon;

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.sign_in_button)
    CardView signinme;

    @BindView(R.id.registering_button)
    CardView regisbtn;

    @OnClick(R.id.registering_button)
    public void registerme(View view) {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.sign_in_button)
    public void loginme(View view) {
        attemptLogin();
    }

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        me = new YawmiMethodes();

        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (sp.getBoolean("statusLogin", false)) {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        final String passme = mPasswordView.getText().toString();
        final String mail = mEmailView.getText().toString();

        mEmailView.setError(null);
        mPasswordView.setError(null);

        if (!isEmailValid(mEmailView.getText().toString())) {

            mEmailView.requestFocus();
            mEmailView.setError("Format Email Tidak Valid");

            return;
        }

        if (!passme.equals("")) {
            if (isPasswordValid(mail)) {
                mPasswordView.requestFocus();
                mPasswordView.setError("Password Tidak Boleh Kurang Dari 6 Karakter");

                return;
            }

        } else {
            mPasswordView.requestFocus();
            mPasswordView.setError("Password Tidak Boleh Kosong");

            return;
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener( LoginActivity.this,
                        new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();

                                yawmiServerAPI api = yawmiClient.getRetrofit().create(yawmiServerAPI.class);
//                                Call<RegisterFeedback> call = api.registerAcc(namaku, mail, passme, newToken);

//                                call.enqueue(new Callback<RegisterFeedback>() {
//                                    @Override
//                                    public void onResponse(Call<RegisterFeedback> call,
//                                                           Response<RegisterFeedback> response) {
//                                        Log.i("RESP", ""+response.body().getMessage());
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<RegisterFeedback> call, Throwable t) {
//                                        Log.e("Throw", t.getLocalizedMessage());
//                                    }
//                                });
                            }
                        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(this.getPackageName(), "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if success get auth
                        if (task.isSuccessful()) {

                            final FirebaseUser user = mAuth.getCurrentUser();

                            final String passme = user.getDisplayName()
                                    .replaceAll("\\s","").toLowerCase();

                            insertAccountToDB(user.getDisplayName(), user.getEmail(), passme);

                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }

    private void insertAccountToDB(final String myname, final String email, final String password) {

        //get token and save to server
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener( LoginActivity.this,
                        new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {

                                String newToken = instanceIdResult.getToken();

                                yawmiServerAPI api = yawmiClient.getRetrofit()
                                        .create(yawmiServerAPI.class);
                                Call<RegisterFeedback> call = api
                                        .registerAcc(
                                                myname,
                                                email,
                                                password,
                                                newToken);

                                call.enqueue(new Callback<RegisterFeedback>() {
                                    @Override
                                    public void onResponse(Call<RegisterFeedback> call,
                                                           Response<RegisterFeedback> response) {
                                        int rescode = response.body().getValue();

                                        if (rescode == 1) {
                                           showDialogWelcome(me.capitalizem("selamat datang "
                                                   + myname +", " + "password akunmu adalah: " +
                                                   password).toString());

                                           sharedPrefIn(myname, email);
                                        }

                                        if (rescode == 2) {
                                            showDialogWelcome(me.capitalizem("selamat datang " +
                                                    "kembali "+ myname+", selamat beraktivitas")
                                                            .toString());

                                            sharedPrefIn(myname, email);
                                        }

                                        if (rescode == 0) {
                                            assert response.body() != null;
                                            showDialogWelcome(me.capitalizem(response.body().getMessage())
                                                            .toString());
                                        }

                                        if (rescode == -1) {
                                            showDialogWelcome(me.capitalizem("server sedang " +
                                                    "mengalami masalah, silahkan coba lagi beberapa" +
                                                            " saat").toString());
                                        }

                                        Log.i("RESP", ""+response.body().getMessage());
                                    }

                                    @Override
                                    public void onFailure(Call<RegisterFeedback> call, Throwable t) {
                                        String REGEX = "(?i).*?\\b(Failed to connect)\\b.*?";

                                        if (t.getMessage().matches(REGEX)) {
                                            me.onToast(getApplicationContext(), "Koneksi " +
                                                    "Ke Server Sedang Bermasalah");
                                        }

                                        Log.e("Throw", ""+t.getMessage());
                                    }
                                });

                                Log.i("newtoken", newToken);
                            }
                        });
    }

    private void showDialogWelcome(String words) {
        DialogFragment df = new WelcomeDialog();

        //passing
        Bundle param = new Bundle();
        param.putString("text_val", words);

        df.setArguments(param);
        df.show(getSupportFragmentManager(), "dayPicker");
    }

    private void sharedPrefIn(String nama, String email) {
        sp.edit().putString("name_user", nama).apply();
        sp.edit().putString("email", email).apply();
        sp.edit().putBoolean("statusLogin", true).apply();
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                me.onToast(this, e.getMessage());
            }
        }
    }
}

