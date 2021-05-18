package co.sih.presso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String test = "{\"Medicines\": [[\"Ridoherp 200mg Tablet\", \"Biohep Tablet\", \"Risdone LS Tablet\", \"Ridone 4mg Tablet\", \"Ridone 3mg Tablet\"], [\"Cardepa Capsule\", \"Carpro Capsule\", \"Cebay Capsule\", \"Carnival Capsule\", \"Depidra Capsule\"], [\"Urotel 4mg Capsule XL\", \"Some 40mg Capsule\", \"Utreva 400mg Capsule\", \"Uterone 400mg Capsule\", \"Oslol 40mg Capsule TR\"], [\"Solzer 250mg/125mg Tablet\", \"Solzer 500mg/125mg Tablet\", \"Solzer 875mg/125mg Tablet\", \"Sonex 500mg/250mg Tablet\", \"Aticef 250 mg/125 mg Tablet\"]], \"Dose\": [\" Before Dinner\", \" Before Dinner\", \" Before Lunch After Dinner 2\", \" After Lunvh Dinner .\"], \"Days\": [\" 17 days\", \" 2 days\", \" 2 days\", \" 10 days\"], \"Symptoms\": [[\"Fall - collision/push/shove (finding)\", \"Fall - collision/push/shove (finding)\", \"Fall in home (finding)\", \"Fall in home (finding)\", \"Fall in home (finding)\"], [\"x\", \"a\", \"b\", \"c\", \"d\"]]}";
    //    String test = "{\"Medicines\": [[\"Risdone LS Tablet\", \"Ridone 4mg Tablet\", \"Ridone 3mg Tablet\", \"Ridone 2mg Tablet\", \"Ridone 1mg Tablet\"], [\"Cardepa Capsule\", \"Gabaxia Capsule\", \"Utipac Capsule\", \"Radlin Capsule\", \"Ephaci Capsule\"], [\"Urotel 4mg Capsule XL\", \"Duzela 40mg Capsule DR\", \"Some 40mg Capsule\", \"Qute 60mg Capsule\", \"Entazel D Capsule\"]], \"Dose\": [\" Before Dinner\", \" Before Dinner\", \" Before Lunch After Dinner 2\"], \"Days\": [\" 17 days\", \" 2 days\", \" 2 days\"]}";
//    String test = "";
    String inputSTString;
    TextView inputStringtv;
    TextView jsonConverttv;
    EditText localNetwork;
    Button convertToJson, generatePrescriptionbtn;
//    private String url = "http://" + "192.168.1.8:5005/";
    private String url;
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
        localNetwork = findViewById(R.id.LocalNetwork);

        url = "http://" + localNetwork.getText().toString() + ":5005/";

        Intent in = getIntent();
        inputSTString = in.getStringExtra("inputSTTString");
        inputStringtv.setText(inputSTString);

        convertToJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "http://" + localNetwork.getText().toString() + ":5005/";

                if(localNetwork.getText().toString().equals("")) {
                    Toast.makeText(JsonifyActivity.this, "Enter local network address to connect!", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(JsonifyActivity.this, url, Toast.LENGTH_SHORT).show();
                    postRequest(inputSTString, url);
                }


            }
        });


//        jsonConverttv.setText(test);

        generatePrescriptionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
                // Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
                intent.putExtra("jsonString", test);
                startActivity(intent);
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
                        Toast.makeText(JsonifyActivity.this, "Error", Toast.LENGTH_SHORT).show();
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