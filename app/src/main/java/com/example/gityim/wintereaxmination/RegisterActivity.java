package com.example.gityim.wintereaxmination;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gityim.wintereaxmination.DataBase.MyDataBaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userAccount;
    EditText userPassWord;
    EditText userName;
    private MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.R_register_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("注册");
        userAccount = findViewById(R.id.R_user_account);

        userPassWord = findViewById(R.id.R_user_password);
        userName = findViewById(R.id.R_user_name);

        dbHelper = new MyDataBaseHelper(this, "ZhiHuDaily.db", null, 3);
        dbHelper.getWritableDatabase();
        Button button = findViewById(R.id.R_register);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.R_register:
                Log.d("闪退1", "onCreate: ");
                if (userRegister()) {
                    Log.d("闪退2", "onCreate: ");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private Boolean userRegister() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String account = userAccount.getText().toString();
        String passWord = userPassWord.getText().toString();
        String name = userName.getText().toString();
        if (checkUserAccount(account, passWord)) {
            Toast.makeText(this, "该账号已被注册", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("account", account);
            values.put("password", passWord);
            db.insert("User", null, values);
            return true;
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean checkUserAccount(String account, String password) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("User", null, null, null, null, null, null);

        if (account.equals("") || password.equals("")) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
        }

        if (cursor.moveToFirst()) {

            do {
                String seekAccount = cursor.getString(cursor.getColumnIndex("account"));
                if (account.equals(seekAccount) ) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
}
