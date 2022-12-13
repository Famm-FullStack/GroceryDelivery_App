package com.example.grocerydeliveryapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerydeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView cardView, loginSignupCardView;
    private ImageButton loginSignupBtn;
    private TabLayout loginSignupTab;
    private LinearLayout loginLayout, signupLayout;
    private TextInputLayout emailTIL, passwordTIL;
    private TextInputLayout numberTIL,
            confirmPasswordTIL, referralCodeTIL;
    private TextView forgotPasswordTV;

    private ProgressBar loginSignupPB;

    private boolean loginTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            openHomeActivity();
        }

        loginSignupTab = findViewById(R.id.loginSignupTab);
        loginLayout = findViewById(R.id.loginLayout);
        signupLayout = findViewById(R.id.signupLayout);
        cardView = findViewById(R.id.cardView);
        loginSignupCardView = findViewById(R.id.loginSignupCardView);
        loginSignupBtn = findViewById(R.id.loginSignupBtn);


        emailTIL = findViewById(R.id.emailTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        numberTIL = findViewById(R.id.numberTIL);
        confirmPasswordTIL = findViewById(R.id.confirmPasswordTIL);
        referralCodeTIL = findViewById(R.id.referralCodeTIL);

        forgotPasswordTV = findViewById(R.id.resendVerificationTV);

        loginSignupPB = findViewById(R.id.loginSignupPB);

        loginSignupTab.addTab(loginSignupTab.newTab().setText("Login"));
        loginSignupTab.addTab(loginSignupTab.newTab().setText("Signup"));

        loginSignupTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showLoginTab();

                } else if (tab.getPosition() == 1) {
                    showSignupTab();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loginSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginTab) {
                    login();
                } else {
                    signup();
                }
            }
        });


    }

    private void showLoginTab() {
        int duration = 700;
        clearSignupForm();
        loginTab = true;
        loginLayout.setVisibility(View.VISIBLE);
        signupLayout.setVisibility(View.GONE);
        forgotPasswordTV.setVisibility(View.VISIBLE);

        TransitionManager.beginDelayedTransition(cardView,
                new AutoTransition().setDuration(duration));

        TransitionManager.beginDelayedTransition(loginSignupCardView,
                new AutoTransition().setDuration(duration));
    }

    private void showSignupTab() {
        int duration = 700;
        clearLoginForm();
        loginTab = false;
        forgotPasswordTV.setVisibility(View.GONE);
        signupLayout.setVisibility(View.VISIBLE);

        TransitionManager.beginDelayedTransition(cardView,
                new AutoTransition().setDuration(duration));

        TransitionManager.beginDelayedTransition(loginSignupCardView,
                new AutoTransition().setDuration(duration));
    }

    private void login() {
        if (!checkEmail()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }

        loginSignupPB.setVisibility(View.VISIBLE);
        loginSignupBtn.setVisibility(View.GONE);

        String email = emailTIL.getEditText().getText().toString();
        String password = passwordTIL.getEditText().getText().toString();

        loginUsingEmail(email, password);
    }

    private void loginUsingEmail(String email, String password) {
        loginSignupPB.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginRegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                            loginSignupPB.setVisibility(View.GONE);
                            loginSignupBtn.setVisibility(View.VISIBLE);
                        } else {

                            FirebaseUser user = mAuth.getCurrentUser();
                            loginSignupPB.setVisibility(View.GONE);
                            loginSignupBtn.setVisibility(View.VISIBLE);

                            if (user != null && user.isEmailVerified()) {
                                openHomeActivity();
                            } else {
                                Toast.makeText(LoginRegisterActivity.this, "Please verify email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void signup() {
        if (!checkEmail()) {
            return;
        }
        if (!checkNumber()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }
        if (!checkConfirmPassword()) {
            return;
        }

        loginSignupPB.setVisibility(View.VISIBLE);
        loginSignupBtn.setVisibility(View.GONE);

        String email = emailTIL.getEditText().getText().toString();
        String number = numberTIL.getEditText().getText().toString();
        String password = passwordTIL.getEditText().getText().toString();
        String referral = referralCodeTIL.getEditText().getText().toString();

        registerUsingEmail(email, password);

    }

    private void registerUsingEmail(String email, String password) {
        loginSignupPB.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegisterActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginRegisterActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                                    loginSignupPB.setVisibility(View.GONE);
                                    loginSignupBtn.setVisibility(View.VISIBLE);
                                    if (user != null && user.isEmailVerified()) {
                                        openHomeActivity();
                                    } else {
                                        loginSignupTab.selectTab(loginSignupTab.getTabAt(0));
                                        showLoginTab();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("sendEmailVerification", "failed " + e.getMessage());
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loginSignupPB.setVisibility(View.GONE);
                            loginSignupBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private boolean checkEmail() {
        if (emailTIL.getEditText().getText().length() > 0) {
            emailTIL.setErrorEnabled(false);
            return true;
        } else {
            emailTIL.setErrorEnabled(true);
            emailTIL.setError("Please enter Email address");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordTIL.getEditText().getText().length() > 0) {
            passwordTIL.setErrorEnabled(false);
            return true;
        } else {
            passwordTIL.setErrorEnabled(true);
            passwordTIL.setError("Please enter password");
            return false;
        }
    }

    private boolean checkNumber() {
        if (numberTIL.getEditText().getText().length() > 0) {
            numberTIL.setErrorEnabled(false);
            return true;
        } else {
            numberTIL.setErrorEnabled(true);
            numberTIL.setError("Please enter mobile number");
            return false;
        }
    }

    private boolean checkConfirmPassword() {
        String password = passwordTIL.getEditText().getText().toString();
        String confirmPassword = confirmPasswordTIL.getEditText().getText().toString();
        if (confirmPasswordTIL.getEditText().getText().length() > 0) {
            if (password.equalsIgnoreCase(confirmPassword)) {
                confirmPasswordTIL.setErrorEnabled(false);
                return true;
            } else {
                confirmPasswordTIL.setErrorEnabled(true);
                confirmPasswordTIL.setError("password not match");
                return false;
            }
        } else {
            confirmPasswordTIL.setErrorEnabled(true);
            confirmPasswordTIL.setError("Please enter password");
            return false;
        }
    }

    private void clearSignupForm() {
        emailTIL.getEditText().setText("");
        numberTIL.getEditText().setText("");
        passwordTIL.getEditText().setText("");
        confirmPasswordTIL.getEditText().setText("");
        referralCodeTIL.getEditText().setText("");

        emailTIL.setErrorEnabled(false);
        numberTIL.setErrorEnabled(false);
        passwordTIL.setErrorEnabled(false);
        confirmPasswordTIL.setErrorEnabled(false);
        referralCodeTIL.setErrorEnabled(false);
    }

    private void clearLoginForm() {
        emailTIL.getEditText().setText("");
        passwordTIL.getEditText().setText("");
        emailTIL.setErrorEnabled(false);
        passwordTIL.setErrorEnabled(false);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}