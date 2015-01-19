package company.majid.behnam.toeflibt;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private TodoDB mDB;  // Our connection to the database.
    private SimpleCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDB = new TodoDB(this);
        mDB.open();
        List<String> uList = new ArrayList<String>();
        Cursor cursor = mDB.queryAll();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                uList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        //String[] countryArray = {"India", "Pakistan", "USA", "UK"};;
        ListView lv = (ListView)findViewById(R.id.usrList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, uList);
        lv.setAdapter(arrayAdapter);


        //
        Button button = (Button) findViewById(R.id.createAccButton);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startDetail(0, true);  // true = create new
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    // Placing this next to onCreate(), help to remember to mDB.close().
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDB.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Starts the detail activity, either edit existing or create new.
    public void startDetail(long rowId, boolean create) {
        Intent intent = new Intent(this, CreateAccount.class);
        // Our convention: add rowId to edit existing. To create add nothing.
        /*if (!create) {
            intent.putExtra(EXTRA_ROWID, rowId);
        }*/
        startActivity(intent);
        // Easy bug: remember to add to add a manifest entry for the detail activity
    }
}
