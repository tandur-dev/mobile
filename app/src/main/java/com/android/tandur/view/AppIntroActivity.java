package com.android.tandur.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.tandur.MainActivity;
import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.LoginResponse;
import com.android.tandur.preference.AppPreference;
import com.android.tandur.view.signup.BiodataActivity;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppIntroActivity extends AppIntro {
    private ApiInterface apiInterface;

    private FirebaseUser firebaseUser;
    private AuthMethodPickerLayout authMethodPickerLayout;
    private List<AuthUI.IdpConfig> providers;

    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient();

        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.sign_in)
                .setGoogleButtonId(R.id.materialButtonGoogle)
                .setFacebookButtonId(R.id.materilaButtonFacebook)
                .build();

        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.slide_intro_1));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.slide_intro_2));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.slide_intro_3));

//        // Fade Transition
//        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);
//
        // Show/hide status bar
        showStatusBar(true);

        setSkipText("Lewati");
        setColorSkipButton(Color.parseColor("#BABABA"));
        setNextArrowColor(Color.parseColor("#7CBD1E"));
        setDoneText("Selesai");
        setColorDoneText(Color.parseColor("#7CBD1E"));

        //Enable/disable page indicators
        setIndicatorColor(
                Color.parseColor("#7CBD1E"),
                Color.parseColor("#BABABA")
        );
        setIndicatorEnabled(true);
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
//        doSignIn();
        AppPreference.saveSlideIntro(this, true);
        finish();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
//        doSignIn();
        AppPreference.saveSlideIntro(this, true);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Log.e("emailResponse", response.getEmail());
            login(response.getEmail());
        }
    }

    public void doSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .build(),
                RC_SIGN_IN);
    }

    public void login(String email) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("updateToken", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();
                Log.d("token", token);

                apiInterface.login(
                        email,
                        token
                ).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.status) {
                                if (loginResponse.data.isVerifUser.equalsIgnoreCase("0")) {
                                    AppPreference.saveUser(AppIntroActivity.this, loginResponse.data);
                                    startActivity(new Intent(AppIntroActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(AppIntroActivity.this, "Akun anda sedang dalam verifikasi, silahkan coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Intent intent = new Intent(AppIntroActivity.this, BiodataActivity.class);
                                intent.putExtra("EMAIL", email);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("login", t.getMessage());
                    }
                });
            }
        });
    }
}
