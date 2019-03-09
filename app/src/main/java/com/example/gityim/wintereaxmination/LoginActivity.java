package com.example.gityim.wintereaxmination;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gityim.wintereaxmination.DataBase.MyDataBaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userAccount;
    private EditText userPassword;
    private MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = findViewById(R.id.register);
        Button loginBtn = findViewById(R.id.login_Btn);
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        userAccount = findViewById(R.id.user_account);
        userPassword = findViewById(R.id.user_password);
        Log.d("郁闷3", "onClick: ");
        dbHelper = new MyDataBaseHelper(this, "ZhiHuDaily.db", null, 3);
        Log.d("郁闷4", "onClick: ");
        dbHelper.getWritableDatabase();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("登陆");
        Log.d("闪退0", "onCreate: ");

        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_Btn:

                if (checkUserAccount()) {

                    Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    Cursor cursor = db.query("User", new String[]{"name"}, "account = ? and password=?", new String[]{userAccount.getText().toString(), userPassword.getText().toString()}, null, null, null);
                    cursor.moveToFirst();
//                      Cursor cursor = db.query("User", null, "account = ?",new String[]{userAccount.getText().toString()}, null, null, null);
//                    Cursor cursor = db.rawQuery("SELECT name FROM User WHERE account=? AND password=? ", new String[]{userAccount.getText().toString(),userPassword.getText().toString()});
//                    Cursor cursor = db.rawQuery("SELECT name FROM User WHERE account=? AND password=? ", new String[]{userAccount.getText().toString(),userPassword.getText().toString()});
//                    cursor.moveToFirst();
                    Log.d("登陆1", "onCreate: " + "在这里");
//                    if (cursor.moveToFirst()) {
//                        do {
//                            String name = cursor.getString(cursor.getColumnIndex("name"));
//                            String password = cursor.getString(cursor.getColumnIndex("password"));
//                            String account = cursor.getString(cursor.getColumnIndex("account"));
//                            if (userAccount.getText().toString().equals(account) && userPassword.getText().toString().equals(password)) {
//                                cursor.close();
//                                intent1.putExtra("Username", name);
//                                Log.d("登陆3", "onCreate: " + name);
//                                startActivity(intent1);
//                                break;
//                            }
//                        }while(cursor.moveToNext());

//                    }
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    Log.d("登陆3", "onCreate: " + name);
                    cursor.close();
                    intent1.putExtra("Username", name);
                    startActivity(intent1);
                    Log.d("登陆2", "onClick: ");

                } else {
                    Toast.makeText(this, "请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Boolean checkUserAccount() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("User", null, null, null, null, null, null);

        String loginAccount = userAccount.getText().toString();

        String loginPassword = userPassword.getText().toString();

        if (loginAccount.equals("") || loginPassword.equals("")) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
        }

        if (cursor.moveToFirst()) {

            do {
                String seekAccount = cursor.getString(cursor.getColumnIndex("account"));
                String seekPassword = cursor.getString(cursor.getColumnIndex("password"));
                if (loginAccount.equals(seekAccount) && loginPassword.equals(seekPassword)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
