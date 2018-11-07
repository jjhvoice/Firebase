package com.example.test.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // MainActivity 함수를 사용하기 위한 객체선언
    public static Context mContext;

    // 비밀번호 정규식
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    String email = "";
    String password = "";

    // 파이어베이스 인증 객체 생성
    FirebaseAuth firebaseAuth;

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignIn;
    TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText)findViewById(R.id.edittext_email);
        editTextPassword = (EditText)findViewById(R.id.edittext_password);
        buttonSignIn = (Button)findViewById(R.id.button_signin);
        textViewSignUp = (TextView)findViewById(R.id.textview_signup);

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    // 버튼을 눌렀을때의 동작
    @Override
    public void onClick(View v) {
        if(v == buttonSignIn) {
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();

            if(isValidEmail() && isValidPassword()) {
                loginUser(email, password);
            }
        } else if(v == textViewSignUp) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    // 이메일 유효성 검사
    public boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    public boolean isValidPassword() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(this, "비밀번호 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 회원가입
    public void createUser(final String email, String password, final String nickname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // 이메일 설정
                            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("email", "email : " + email);
                                    }
                                }
                            });

                            // 닉네임 설정
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nickname).build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("nickname", "nickname : " + nickname);
                                    }
                                }
                            });

                            /* 사용자 프로필 정보 가져올 때
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String email = user.getEmail();
                                    String name = user.getDisplayName();
                                 }
                            */

                            // 회원가입 성공
                            ((SignUpActivity)SignUpActivity.suContext).completeSignUp();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 로그인
    public void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
