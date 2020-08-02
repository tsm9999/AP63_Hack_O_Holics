package co.sih.presso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class JsonifyActivity extends AppCompatActivity {

//    public String test = "c";
    String test = "{\n" +
            "    \"Days\": [\n" +
            "        \" Two days\",\n" +
            "        \" 3 days\"\n" +
            "    ],\n" +
            "    \"Age\": 20,\n" +
            "    \"Medicines\": [\n" +
            "        [\n" +
            "            \"Traxol O 200mg Tablet\",\n" +
            "            \"Traxol O 100mg Tablet DT\",\n" +
            "            \"Taxclav 200 Tablet\",\n" +
            "            \"Tablo 200mg Tablet\",\n" +
            "            \"Trazonil 100mg Tablet\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"Cefbact 500mg Injection\",\n" +
            "            \"Cardiject 50mg Injection\",\n" +
            "            \"Cefact 750mg Injection\",\n" +
            "            \"Cimispect 500mg Injection\",\n" +
            "            \"Clot 5mg Injection\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"Dose\": [\n" +
            "        \" Before Lunch\",\n" +
            "        \" Before Lunch 3\"\n" +
            "    ]\n" +
            "}";
    TextView inputStringtv;
    TextView jsonConverttv;
    Button convertToJson, generatePrescriptionbtn;
    private String url = "http://" + "192.168.1.209" + ":" + 5005 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonify);

        convertToJson = findViewById(R.id.btnConvertToJson);
        inputStringtv = findViewById(R.id.inputStringtv);
        jsonConverttv = findViewById(R.id.outputJsontv);
        generatePrescriptionbtn = findViewById(R.id.btngeneratePrescription);


        Intent in = getIntent();
        String inputSTString = in.getStringExtra("inputSTTString");
        inputStringtv.setText(inputSTString);

        convertToJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequest("msg", url);
                jsonConverttv.setText(test);

                Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
                // Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
                intent.putExtra("jsonString", test);
                startActivity(intent);

            }
        });




//        jsonConverttv.setText(test);

        generatePrescriptionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JsonifyActivity.this, PrescriptionActivity.class));
            }
        });

    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull final Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JsonifyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            test = (response.body().string()).toString();
                            Toast.makeText(JsonifyActivity.this, test, Toast.LENGTH_LONG).show();
                            jsonConverttv.setText(test);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


    }
}