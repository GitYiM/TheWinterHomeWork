/*package com.example.gityim.wintereaxmination;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ToolBarActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public void setToolbar(String title,int type){
        toolbar =findViewById(R.id.head_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            switch(type){
                case 1:
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
                    break;
                case 2:
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    break;
                default:
            }
        }


    }
}*/
