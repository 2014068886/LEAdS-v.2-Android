package com.ust.leadsv2.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class SigninActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView status,role,method;
    private Button button, register;

    String PasswordHolder, UsernameHolder;
    String finalResult ;
    String HttpURL = "http://192.168.1.14:80/LEAdS v.2/user/loginpost.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String Username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        usernameField =  (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.et_password);
        /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CheckEditTextIsEmptyOrNot();

                    if(CheckEditText){
                        UserLoginFunction(UsernameHolder, PasswordHolder);
                    }
                    else {
                        Toast.makeText(SigninActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                    }
                }
        }); */
    }

    public void CheckEditTextIsEmptyOrNot(){

        UsernameHolder = usernameField.getText().toString();
        PasswordHolder = passwordField.getText().toString();

        if(TextUtils.isEmpty(UsernameHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        } else {
            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String username, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(SigninActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equalsIgnoreCase("Data Matched")) {
                    finish();
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    intent.putExtra(Username,username);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SigninActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);
                hashMap.put("password",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(username,password);
    }

    public void forgotPassword (View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    public void goToSignUp (View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void goToMainScreen (View view) {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);

        CheckEditTextIsEmptyOrNot();

        if(CheckEditText){
            UserLoginFunction(UsernameHolder, PasswordHolder);
        }
        else {
            Toast.makeText(SigninActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
        }
    }
}
