package com.ust.leadsv2.androidlogin;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SignupActivity extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;
    EditText username;
    EditText mobileNum;
    EditText password1;
    EditText password2;
    Button register;

    private ProgressDialog pDialog;
    String URL= "http://192.168.56.1/LEAdS%20v.2/user/index6.php";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname = (EditText) findViewById(R.id.et_fname);
        lname = (EditText) findViewById(R.id.et_lname);
        email = (EditText) findViewById(R.id.et_email);
        username = (EditText) findViewById(R.id.et_username);
        mobileNum = (EditText) findViewById(R.id.et_mobileNumber);
        password1 = (EditText) findViewById(R.id.et_password1);
        password2 = (EditText) findViewById(R.id.et_password2);

        register = (Button) findViewById(R.id.btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String emailAdd = email.getText().toString();
                String Mobile = mobileNum.getText().toString();
                String Username = username.getText().toString();
                String Password = password1.getText().toString();
                String confPassword = password2.getText().toString();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !Mobile.isEmpty() && !Username.isEmpty() && !emailAdd.isEmpty() && !Password.isEmpty() && !confPassword.isEmpty()) {
                        AsyncDataClass attemptLogin = new AsyncDataClass();
                        attemptLogin.execute(URL, Username, Password, emailAdd, Mobile, firstName, lastName);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }

                //AttemptLogin attemptLogin = new AttemptLogin();
                //attemptLogin.execute(fname.getText().toString(),lname.getText().toString(), email.getText().toString(), username.getText().toString(), mobileNum.getText().toString(), password1.getText().toString(), password2.getText().toString());
            }
        });

    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);

            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";

            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("username", params[1]));
                nameValuePairs.add(new BasicNameValuePair("password", params[2]));
                nameValuePairs.add(new BasicNameValuePair("email", params[3]));
                nameValuePairs.add(new BasicNameValuePair("mobile", params[4]));
                nameValuePairs.add(new BasicNameValuePair("fname", params[5]));
                nameValuePairs.add(new BasicNameValuePair("lname", params[6]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return jsonResult;

        }

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            System.out.println("Resulted Value: " + result);

            if(result.equals("") || result == null){

                Toast.makeText(SignupActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();

                return;

            }

            int jsonResult = returnParsedJsonObject(result);

            if(jsonResult == 0){

                Toast.makeText(SignupActivity.this, "Invalid username or password or email", Toast.LENGTH_LONG).show();

                return;

            }

            if(jsonResult == 1){
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);

            }

        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;

        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);

            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }

}