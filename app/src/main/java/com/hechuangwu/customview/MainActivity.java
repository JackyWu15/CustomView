package com.hechuangwu.customview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        HorizontalView hlv_view = findViewById( R.id.hlv_view );
        LayoutInflater layoutInflater = getLayoutInflater();
        int widthPixels = getScreenMetrics( this ).widthPixels;
        int heightPixels = getScreenMetrics( this ).heightPixels;
        for (int i = 0; i < 3; i++) {
            ViewGroup inflate = (ViewGroup) layoutInflater.inflate( R.layout.content_layout, hlv_view, false );
            inflate.getLayoutParams().width = widthPixels;
            TextView title =  inflate.findViewById( R.id.title );
            title.setText( i+"page" );
            inflate.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(inflate);
            hlv_view.addView( inflate );
        }
    }

    private void createList(ViewGroup inflate) {
        ListView listView = (ListView) inflate.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


    public  DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i( "data", "dispatchTouchEvent: >>>" +getClass());
        return super.dispatchTouchEvent( ev );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i( "data", "onTouchEvent: >>>ACTION_DOWN>>>>>>>"+getClass() );
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i( "data", "onTouchEvent: >>>ACTION_MOVE>>>>>>>"+getClass() );
                break;
            case MotionEvent.ACTION_UP:
                Log.i( "data", "onTouchEvent: >>>ACTION_UP>>>>>>>"+getClass() );
                break;
        }
        return super.onTouchEvent( event );
    }
}
