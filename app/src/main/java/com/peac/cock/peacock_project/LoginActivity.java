package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private EditText login_EditTextEmail;
    private EditText login_EditTextPassword;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private ArrayList<String> uidList = new ArrayList<>();

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backPressCloseHandler= new BackPressCloseHandler(this);

        mAuth = FirebaseAuth.getInstance();

        Intent intent1 = getIntent();
        uidList = intent1.getStringArrayListExtra("uidList");

        TextView joinTextView = findViewById(R.id.login_layout_textView_join);
        ImageButton loginOkButton = findViewById(R.id.login_layout_loginOk_imgButton);
        login_EditTextEmail = findViewById(R.id.login_layout_editText_email);
        login_EditTextPassword = findViewById(R.id.login_layout_editText_passWord);
        SignInButton googleLoginButton = findViewById(R.id.login_layout_googleLogin_imgButton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginButton.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        mAuthListener = firebaseAuth -> {
            boolean isEmail = false;
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                for (String s : uidList) {
                    if (s.equals(user.getEmail())) {
                        isEmail = true;
                    }
                }
                if (isEmail) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MemberInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        joinTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EmailJoinActivity.class);
            startActivity(intent);
            finish();
        });

        loginOkButton.setOnClickListener(v -> {
            if (login_EditTextEmail.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                login_EditTextEmail.requestFocus();
                return;
            }
            if (login_EditTextPassword.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Password를 입력하세요!", Toast.LENGTH_SHORT).show();
                login_EditTextPassword.requestFocus();
                return;
            }
            loginUser(login_EditTextEmail.getText().toString(), login_EditTextPassword.getText().toString());
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        loginUser(email, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "이메일 로그인이성공!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "구글 로그인이성공!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signOut();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
