package org.hm.com.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationScreen extends AppCompatActivity {

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration Screen");

        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // register();
            }
        });

    }

    private void register() {

        RequestQueue queue = Volley.newRequestQueue(this);
            try {
                String URL = "";
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("email", "abc@abc.com");
                jsonBody.put("password", "");
                jsonBody.put("user_type", "");
                jsonBody.put("company_id", "");
                jsonBody.put("status", "");

                JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        onBackPressed();

                    }
                }) {
                    //Use it if you have headers
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final Map<String, String> headers = new HashMap<>();
                        //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                        return headers;
                    }
                };
                queue.add(jsonOblect);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }


    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}
