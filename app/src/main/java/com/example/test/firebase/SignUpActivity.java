package com.example.test.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // SignUpActivity 함수를 사용하기 위한 객체선언
    public static Context suContext;

    // 비밀번호 정규식
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    // 닉네임 정규식
    public static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣]{4,16}$");

    String email = "";
    String password1 = "";
    String password2 = "";
    String nickname = "";

    Button button;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPassword2;
    EditText editTextNickname;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        suContext = this;

        // 뒤로가기 버튼
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextEmail = (EditText)findViewById(R.id.edittext_email);
        editTextPassword = (EditText)findViewById(R.id.edittext_password);
        editTextPassword2 = (EditText)findViewById(R.id.edittext_password2);
        editTextNickname = (EditText)findViewById(R.id.edittext_nickname);
        button = (Button)findViewById(R.id.button_signup);

        button.setOnClickListener(this);
    }

    // 뒤로가기 버튼 눌렀을때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // 버튼을 눌렀을때의 동작
    @Override
    public void onClick(View v) {
        if(v == button) {
            email = editTextEmail.getText().toString();
            password1 = editTextPassword.getText().toString();
            password2 = editTextPassword2.getText().toString();
            nickname = editTextNickname.getText().toString();

            if(isValidEmail() && isValidPassword() && isValidNickname()) {
                ((MainActivity)MainActivity.mContext).createUser(email, password1, nickname);
            }
        }
    }

    // 다이얼로그 리스너
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog == alertDialog && which == DialogInterface.BUTTON_POSITIVE) {
                // 메인 화면으로 이동
                Toast.makeText(SignUpActivity.this, "메인 화면으로 이동합니다", Toast.LENGTH_SHORT).show();
            } else if(dialog == alertDialog && which == DialogInterface.BUTTON_NEGATIVE) {
                // 로그인 화면으로 이동
                finish();
            }
        }
    };

    // 회원가입 완료 다이얼로그 띄우기
    public void completeSignUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입 완료");
        builder.setMessage("회원가입이 완료되었습니다. 바로 로그인하시겠습니까?");
        builder.setPositiveButton("OK", dialogListener);
        builder.setNegativeButton("Cancel", dialogListener);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
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
        if (password1.isEmpty() || password2.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password1).matches() || !PASSWORD_PATTERN.matcher(password2).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(this, "비밀번호 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!(password1.equals(password2))) {
            // 입력한 두 비밀번호 불일치
            Toast.makeText(this, "동일한 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 닉네임 유효성 검사
    public boolean isValidNickname() {
        if (nickname.isEmpty()) {
            // 닉네임 공백
            Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            // 닉네임 형식 불일치
            Toast.makeText(this, "닉네임 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
