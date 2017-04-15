package com.example.locato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{
    GoogleSignInOptions mGoogleSignInOptions;
    GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOptions)
                .build();

        findViewById(R.id.btn_si_signIn).setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_si_signIn:
                signIn();
                break;
        }
    }

    public void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            saveUserProfile(acct.getDisplayName(),acct.getEmail());

            User.setName(acct.getDisplayName());
            User.setEmail(acct.getEmail());

            Database database = new Database(this);
            database.createRecord();

            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(this,"Not success",Toast.LENGTH_LONG).show();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void saveUserProfile(String name,String email)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myfile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(getString(R.string.sign_in_status),true);
        editor.putString(getString(R.string.user_name),name);
        editor.putString(getString(R.string.user_email),email);
        editor.commit();
    }
}