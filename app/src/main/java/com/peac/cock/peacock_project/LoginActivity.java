package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wee on 2017. 10. 30..
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private EditText login_EditTextEmail;
    private EditText login_EditTextPassword;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mDatabase;

    private ArrayList<String> uidList = new ArrayList<>();

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //툴바 설정
        Toolbar toolbar = findViewById(R.id.setting_layout_my_toolbar);
        setSupportActionBar(toolbar);


        //firebase Auth정보 get
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //before_intent_get
        intent = getIntent();
        uidList = intent.getStringArrayListExtra("uidList");

        //button 및 사용할 아이템
        TextView joinTextView = findViewById(R.id.login_layout_textView_join);
        ImageButton loginOkButton = findViewById(R.id.login_layout_loginOk_imgButton);
        login_EditTextEmail = findViewById(R.id.login_layout_editText_email);
        login_EditTextPassword = findViewById(R.id.login_layout_editText_passWord);
        SignInButton googleLoginButton = findViewById(R.id.login_layout_googleLogin_imgButton);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean isEmail = false;
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {//인증된 user 일시 보내는 화면
                    for (String s : uidList) {
                        if (s.equals(user.getEmail())) {
                            isEmail = true;
                        }
                    }
                    if (isEmail) {
                        Intent intent = new Intent(getApplicationContext(), AssetActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MemberInfoActivitiy.class);
                        startActivity(intent);
                        finish();
                    }
                } else {//비인증 유져라면 보내는 화면

                }
            }
        };

        /*이메일로 가입하기로 넘어가기 이벤트*/
        joinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailJoinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*로그인 하기 버튼 이벤트*/
        loginOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation Check
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
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    //firebase 이메일 검증시 사용
    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            loginUser(email, password);
                        } else {
                            Toast.makeText(LoginActivity.this, "이메일 로그인이성공!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    //firebase 구글인증시 사용
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                        } else {
                            Toast.makeText(LoginActivity.this, "구글 로그인이성공!!", Toast.LENGTH_SHORT).show();
                        }
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
    }
}
