package com.universl.hp.vehicle_sale_app.sub_activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.universl.hp.vehicle_sale_app.NewAdsPostActivity;
import com.universl.hp.vehicle_sale_app.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "abc";
    private LinearLayout mPhoneLayout;
    private LinearLayout mCodeLayout;

    private EditText mPhoneText;
    private EditText mCodeText;
    private CountryCodePicker countryCodePicker;

    private ProgressBar mPhoneBar;
    private ProgressBar mCodeBar;
    private TextView mErrorText;

    private Button sendButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    int buttontype = 0;
    Boolean isProblem = false;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mPhoneLayout = findViewById(R.id.phone_id);
        mCodeLayout = findViewById(R.id.lock_id);

        mPhoneText = findViewById(R.id.contact_number);
        mCodeText = findViewById(R.id.verification_code);

        mPhoneBar = findViewById(R.id.progressBar_call);
        mCodeBar = findViewById(R.id.progressBar_verify);
        countryCodePicker = findViewById(R.id.country_code);

        sendButton = findViewById(R.id.send_btn);
        mAuth = FirebaseAuth.getInstance();

        mErrorText = findViewById(R.id.errorText);
        countryCodePicker.setDefaultCountryUsingPhoneCode(+94);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<String> multiple_images = getIntent().getStringArrayListExtra("multiple_images");
                final ArrayList<String> single_images = getIntent().getStringArrayListExtra("single_images");
                final Boolean isMultiple = getIntent().getBooleanExtra("isMultiple",false);
                final Boolean isVanBusLorry = getIntent().getBooleanExtra("isVanBusLorryActivity",false);
                System.out.println("((((((((((((((((((((((((((((((((((((((("+ isVanBusLorry);
                if (isMultiple){
                    System.out.println("//////////////////>>>>>>>>>>>>>>" + multiple_images.size());
                }else {
                    System.out.println("/////////////////................" + single_images.size());
                }
                System.out.println("///////////////////-------------->" + isMultiple);
                if (buttontype == 0){
                    mPhoneBar.setVisibility(View.VISIBLE);
                    mPhoneText.setEnabled(false);
                    sendButton.setEnabled(false);

                    if (isProblem){
                        mErrorText.setVisibility(View.INVISIBLE);
                    }
                    String phoneNumber = "+"+countryCodePicker.getSelectedCountryCode() + mPhoneText.getText().toString();
                    System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}}" +  phoneNumber);
                    if (phoneNumber.isEmpty()){
                        mPhoneText.setError("කරුණාකර ඔබගේ දුරකථන අංකය ඇතුලත් කරන්න!");
                    }else {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,
                                60,
                                TimeUnit.SECONDS,
                                PhoneAuthActivity.this,
                                mCallbacks
                        );
                    }
                }else {
                    sendButton.setEnabled(false);
                    mCodeBar.setVisibility(View.VISIBLE);

                    String verificationCode = mCodeText.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mErrorText.setText("සත්යාපනය පිළිබඳ යම් දෝෂයක් ඇත");
                mErrorText.setVisibility(View.VISIBLE);
                mPhoneText.setEnabled(true);
                sendButton.setEnabled(true);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;

                buttontype = 1;

                mPhoneBar.setVisibility(View.INVISIBLE);
                mCodeLayout.setVisibility(View.VISIBLE);
                sendButton.setText("තහවුරැ කරන්න");

                sendButton.setEnabled(true);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final ArrayList<String> multiple_images = getIntent().getStringArrayListExtra("multiple_images");
                        final ArrayList<String> single_images = getIntent().getStringArrayListExtra("single_images");
                        final Boolean isMultiple = getIntent().getBooleanExtra("isMultiple",false);
                        final String district = getIntent().getStringExtra("district");
                        final String city = getIntent().getStringExtra("city");
                        final String brand = getIntent().getStringExtra("brand");
                        final String model = getIntent().getStringExtra("model");
                        final String trim = getIntent().getStringExtra("trim");
                        final String year = getIntent().getStringExtra("year");
                        final String mileage = getIntent().getStringExtra("mileage");
                        final int body_type = getIntent().getIntExtra("body_type",0);
                        final String transmission = getIntent().getStringExtra("transmission");
                        final String fuel = getIntent().getStringExtra("fuel");
                        final String capacity = getIntent().getStringExtra("capacity");
                        final String description = getIntent().getStringExtra("description");
                        final int condition = getIntent().getIntExtra("condition",0);
                        final String price = getIntent().getStringExtra("price");
                        final String title = getIntent().getStringExtra("title");
                        final String service_type = getIntent().getStringExtra("service_type");
                        final String vehicle_type = getIntent().getStringExtra("vehicle_type");
                        final Boolean isService_type = getIntent().getBooleanExtra("isSparePartService",false);
                        final Boolean isOtherVehicle = getIntent().getBooleanExtra("isOtherVehicle",false);
                        final Boolean isVanBusLorry = getIntent().getBooleanExtra("isVanBusLorryActivity",false);
                        final Boolean isMotorbikeScooter = getIntent().getBooleanExtra("isMotorbikeScooterActivity",false);
                        final Boolean isSelectCar = getIntent().getBooleanExtra("is_select_car",false);
                        final Boolean isVan = getIntent().getBooleanExtra("is_select_van_bus_lorry",false);
                        final Boolean isMotor = getIntent().getBooleanExtra("is_select_motorbike",false);
                        final Boolean isOther = getIntent().getBooleanExtra("is_select_other_vehicle",false);
                        final Boolean isService = getIntent().getBooleanExtra("is_select_service",false);
                        final String service = getIntent().getStringExtra("service");
                        final String name = getIntent().getStringExtra("name");
                        final String address = getIntent().getStringExtra("address");
                        if (task.isSuccessful()) {
                            isProblem = false;
                            FirebaseUser user = task.getResult().getUser();
                            if (isVanBusLorry){
                                Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);
                                intent.putExtra("district",district);
                                intent.putExtra("city",city);
                                if (isMultiple){
                                    intent.putStringArrayListExtra("multiple_images",multiple_images);
                                }else {
                                    intent.putStringArrayListExtra("single_images",single_images);
                                }
                                System.out.println(">>>>>>>>>>>>>>>"+isVan+"<<<<<<<<<<<<<<<<<");
                                intent.putExtra("is_select_van_bus_lorry",isVan);
                                intent.putExtra("brand",brand);
                                intent.putExtra("year",year);
                                intent.putExtra("mileage",mileage);
                                intent.putExtra("capacity",capacity);
                                intent.putExtra("description",description);
                                intent.putExtra("price",price);
                                intent.putExtra("condition",condition);
                                intent.putExtra("vehicle_type",vehicle_type);
                                intent.putExtra("contact",user.getPhoneNumber());
                                intent.putExtra("isMultiple",isMultiple);
                                intent.putExtra("isParseBoolean", true);
                                startActivity(intent);
                                finish();
                            }else if (isOtherVehicle){
                                Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);
                                intent.putExtra("district",district);
                                intent.putExtra("city",city);
                                if (isMultiple){
                                    intent.putStringArrayListExtra("multiple_images",multiple_images);
                                }else {
                                    intent.putStringArrayListExtra("single_images",single_images);
                                }
                                intent.putExtra("is_select_other_vehicle",isOther);
                                intent.putExtra("year",year);
                                intent.putExtra("mileage",mileage);
                                intent.putExtra("capacity",capacity);
                                intent.putExtra("description",description);
                                intent.putExtra("price",price);
                                intent.putExtra("condition",condition);
                                intent.putExtra("vehicle_type",vehicle_type);
                                intent.putExtra("contact",user.getPhoneNumber());
                                intent.putExtra("isMultiple",isMultiple);
                                intent.putExtra("isParseBoolean", true);
                                intent.putExtra("title",title);
                                startActivity(intent);
                                finish();
                            }else if (isService_type){
                                Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);
                                intent.putExtra("is_select_service",isService);
                                intent.putExtra("address",address);
                                intent.putExtra("name",name);
                                intent.putExtra("service",service);
                                intent.putExtra("contact",user.getPhoneNumber());
                                intent.putExtra("isParseBoolean", true);
                                intent.putExtra("title",title);
                                startActivity(intent);
                                finish();
                            } else if (isMotorbikeScooter){
                                Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);
                                intent.putExtra("district",district);
                                intent.putExtra("city",city);
                                if (isMultiple){
                                    intent.putStringArrayListExtra("multiple_images",multiple_images);
                                }else {
                                    intent.putStringArrayListExtra("single_images",single_images);
                                }
                                intent.putExtra("is_select_motorbike",isMotor);
                                intent.putExtra("brand",brand);
                                intent.putExtra("model",model);
                                intent.putExtra("year",year);
                                intent.putExtra("mileage",mileage);
                                intent.putExtra("capacity",capacity);
                                intent.putExtra("description",description);
                                intent.putExtra("price",price);
                                intent.putExtra("condition",condition);
                                intent.putExtra("trim",trim);
                                intent.putExtra("vehicle_type",vehicle_type);
                                intent.putExtra("contact",user.getPhoneNumber());
                                intent.putExtra("isMultiple",isMultiple);
                                intent.putExtra("isParseBoolean", true);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);
                                intent.putExtra("district",district);
                                intent.putExtra("city",city);
                                if (isMultiple){
                                    intent.putStringArrayListExtra("multiple_images",multiple_images);
                                }else {
                                    intent.putStringArrayListExtra("single_images",single_images);
                                }
                                intent.putExtra("is_select_car",isSelectCar);
                                intent.putExtra("brand",brand);
                                intent.putExtra("model",model);
                                intent.putExtra("trim",trim);
                                intent.putExtra("year",year);
                                intent.putExtra("mileage",mileage);
                                intent.putExtra("body_type",body_type);
                                intent.putExtra("transmission",transmission);
                                intent.putExtra("fuel",fuel);
                                intent.putExtra("capacity",capacity);
                                intent.putExtra("description",description);
                                intent.putExtra("price",price);
                                intent.putExtra("condition",condition);
                                intent.putExtra("vehicle_type",vehicle_type);
                                intent.putExtra("contact",user.getPhoneNumber());
                                intent.putExtra("isMultiple",isMultiple);
                                intent.putExtra("isParseBoolean", true);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            mErrorText.setText("කිසියම් දෝෂයක් තිබේ");
                            mErrorText.setVisibility(View.VISIBLE);
                            isProblem = true;
                            sendButton.setEnabled(true);
                            mPhoneText.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Boolean isService_type = getIntent().getBooleanExtra("isSparePartService",false);
        final Boolean isOtherVehicle = getIntent().getBooleanExtra("isOtherVehicle",false);
        final Boolean isVanBusLorry = getIntent().getBooleanExtra("isVanBusLorryActivity",false);
        final Boolean isMotorbikeScooter = getIntent().getBooleanExtra("isMotorbikeScooterActivity",false);

        if (isService_type){
            Intent intent = new Intent(PhoneAuthActivity.this,SparePartServiceActivity.class);

            startActivity(intent);
            finish();
        }else if (isMotorbikeScooter){
            Intent intent = new Intent(PhoneAuthActivity.this,MotorBikeScooterAdsActivity.class);

            startActivity(intent);
            finish();
        }else if (isOtherVehicle){
            Intent intent = new Intent(PhoneAuthActivity.this,OtherVehicleAdsActivity.class);

            startActivity(intent);
            finish();
        }else if (isVanBusLorry){
            Intent intent = new Intent(PhoneAuthActivity.this,VanBusLorryAdsActivity.class);

            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(PhoneAuthActivity.this,NewAdsPostActivity.class);

            startActivity(intent);
            finish();
        }
    }
}
