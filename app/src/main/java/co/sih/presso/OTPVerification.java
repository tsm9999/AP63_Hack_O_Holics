package co.sih.presso;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.concurrent.TimeUnit;

import co.sih.presso.Models.UserDetails;

public class OTPVerification extends AppCompatActivity {

    TextInputEditText otp;
    Button submitOTP;
    TextView resendOTP;
    MKLoader mkLoader;
    String phoneNumber, name, email, id;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    CollectionReference cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        db = FirebaseFirestore.getInstance();
        cr = db.collection("UserDetails");

        otp = findViewById(R.id.otp);
        submitOTP = findViewById(R.id.btnSubmitOTP);
        resendOTP = findViewById(R.id.resend);
        mkLoader = findViewById(R.id.loader);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        name = getIntent().getStringExtra("nameR");
        email = getIntent().getStringExtra("emailR");

        firebaseAuth = FirebaseAuth.getInstance();
        sendOTP();

        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otp.getText().toString()))
                    Toast.makeText(OTPVerification.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                else if (otp.getText().toString().replace(" ", "").length() != 6)
                    Toast.makeText(OTPVerification.this, "Incorrect OTP format", Toast.LENGTH_SHORT).show();
                else {
                    mkLoader.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp.getText().toString().replace(" ", ""));
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });
    }

    private void sendOTP() {

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                resendOTP.setText("" + l / 1000);
                resendOTP.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resendOTP.setText("RESEND");
                resendOTP.setEnabled(true);
            }

        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OTPVerification.this.id = id;

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPVerification.this, "Verification Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        mkLoader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            UserDetails userDetails = new UserDetails(name, email, phoneNumber);
                            cr.document(name).set(userDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(OTPVerification.this, MainActivity.class));
                                            finish();
                                            FirebaseUser user = task.getResult().getUser();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OTPVerification.this, "User Registration Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // ...
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(OTPVerification.this, "Verification Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}