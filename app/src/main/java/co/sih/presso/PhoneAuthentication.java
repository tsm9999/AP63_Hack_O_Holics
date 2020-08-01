package co.sih.presso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;

public class PhoneAuthentication extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputEditText phoneNumber;
    Button verifyOTP, gotoLoginPage;
    String name, email;

    public static boolean isNumberValid(String number) {

        Pattern ptr = Pattern.compile("^((\\\\(\\\\d{1,3}\\\\))|\\\\d{1,3})[- .]?\\\\d{3,4}[- .]?\\\\d{4}$");
        return ptr.matcher(number).matches() ? true : false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        countryCodePicker = findViewById(R.id.countryCodePicker);
        phoneNumber = findViewById(R.id.phoneNumber);
        verifyOTP = findViewById(R.id.btnVerifyOTP);
        gotoLoginPage = findViewById(R.id.gotoLoginPageFromPhonePage);
        name = getIntent().getStringExtra("nameR");
        email = getIntent().getStringExtra("emailR");

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        gotoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhoneAuthentication.this, Login.class));
                finish();
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                boolean check1 = isNumberValid(phoneNumber.getText().toString());

                if (phoneNumber.getText().toString().isEmpty())
                    Toast.makeText(PhoneAuthentication.this, "Incorrect Number Format", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(PhoneAuthentication.this, OTPVerification.class);
                    intent.putExtra("phoneNumber", countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                    intent.putExtra("nameR", name);
                    intent.putExtra("emailR", email);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}