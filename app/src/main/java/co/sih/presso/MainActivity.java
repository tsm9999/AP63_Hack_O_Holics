package co.sih.presso;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btngoBack, btnconfirmLogout;
    boolean flag = true;
    FloatingActionButton startSpeech;
    ArrayList<String> resultSpeech = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    Boolean speechResultsFound = false;
    SpeechRecognizer userSpeech;


    SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSpeech = findViewById(R.id.startSpeechbtn);

        Toast.makeText(this, "Welcome  " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        startSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Button CLicked", Toast.LENGTH_SHORT).show();

                speechRecognizer = speechRecognizer.createSpeechRecognizer(getApplicationContext());
                Intent intent;
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(500000));
//                if ((flag = true))
//                    speechRecognizer.startListening(intent);
                startActivityForResult(intent, 5);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (resultCode == RESULT_OK && data != null) {
                resultSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inputSTTString = resultSpeech.get(0);
                Log.e("Result Speech array", resultSpeech.get(0));
                Intent intent = new Intent(getApplicationContext(), JsonifyActivity.class);
                Toast.makeText(this, inputSTTString, Toast.LENGTH_SHORT).show();
                intent.putExtra("inputSTTString", inputSTTString);
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                break;
            case R.id.profile:
                Intent h2 = new Intent(MainActivity.this, DoctorProfile.class);
                startActivity(h2);
                break;

            case R.id.contactUs:
                Intent h1 = new Intent(MainActivity.this, HelpUs.class);
                startActivity(h1);
                break;

            case R.id.logout:

                logout();
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void logout() {

        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(MainActivity.this);
        final View confirmLogoutV = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        logoutAlert.setView(confirmLogoutV);

        btngoBack = confirmLogoutV.findViewById(R.id.goBack);
        btnconfirmLogout = confirmLogoutV.findViewById(R.id.logoutConfirm);

        final AlertDialog dialog = logoutAlert.create();
        dialog.show();

        btnconfirmLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });

        btngoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}