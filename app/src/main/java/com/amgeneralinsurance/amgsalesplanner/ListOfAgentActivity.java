package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.utils.ReuseableClass;

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

        SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
        Cursor cur = db.rawQuery("SELECT * FROM plan_tbl", null);

        if(cur.moveToNext()) {
            ((TextView)findViewById(R.id.textViewPlanTitle)).setVisibility(View.INVISIBLE);
            do {
                Map<String, String> datum = new HashMap<String, String>(9);
                datum.put("First Line", cur.getString(2));
                datum.put("Second Line","Meeting Scheduled on: " + cur.getString(6) + " Time: " + cur.getString(7) + "~" + cur.getString(8));
                datum.put("plan_id", cur.getString(1));
                datum.put("agency_name", cur.getString(2));
                datum.put("date", cur.getString(6));
                datum.put("start_time", cur.getString(7));
                datum.put("end_time", cur.getString(8));
                datum.put("purpose", cur.getString(9));
                datum.put("objective", cur.getString(10));
                datum.put("outcome", cur.getString(11));
                datum.put("completed", cur.getString(12));
                datum.put("action_required", cur.getString(13));
                datum.put("lat", cur.getString(4));
                datum.put("lng", cur.getString(5));
                data.add(datum);
            }while (cur.moveToNext());
        }
        cur.close();
        db.close();

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
                i.putExtra("plan_id", data.get(position).get("plan_id"));
                i.putExtra("agency_name", data.get(position).get("agency_name"));
                i.putExtra("date", data.get(position).get("date"));
                i.putExtra("start_time", data.get(position).get("start_time"));
                i.putExtra("end_time", data.get(position).get("end_time"));
                i.putExtra("purpose", data.get(position).get("purpose"));
                i.putExtra("objective", data.get(position).get("objective"));
                i.putExtra("outcome", data.get(position).get("outcome"));
                i.putExtra("completed", data.get(position).get("completed"));
                i.putExtra("action_required", data.get(position).get("action_required"));
                i.putExtra("lat", data.get(position).get("lat"));
                i.putExtra("lng", data.get(position).get("lng"));
                i.putExtra("coming_from", "list_of_agent_activity");
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
