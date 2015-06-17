package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfAgentActivity extends AppCompatActivity {

    ListView myListView;
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_agent);

        myListView = (ListView)findViewById(R.id.myListView);

        for(int i=1; i<11; i++)
        {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("First Line", "First line of text. Line No: " + i);
            datum.put("Second Line","Second line of text. Line No: " + i);
            data.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.simplerow,
                new String[] {"First Line", "Second Line" },
                new int[] {R.id.rowTextView, R.id.rowTextView2 });

        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Log.d("TAG", "value: " + data.get(position).get("First Line"));
                Log.d("TAG", "value: " + data.get(position).get("Second Line"));

                Intent i = new Intent(ListOfAgentActivity.this, ModifyAgentActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_agent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ListOfAgentActivity.this, AddAgentActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DashBoardActivity.class);
        finish();
        startActivity(i);
    }
}
