package company.majid.behnam.toeflibt;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateAccount extends Activity {
    private TodoDB mDB;
    private Long mRowId;

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mDB = new TodoDB(this);
        mDB.open();

        Button button = (Button) findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addAccount();
                finish();  // same as "back" .. either way we get onPause() to save
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_account, menu);
        return true;
    }
    protected void onDestroy() {
        super.onDestroy();
        mDB.close();
    }
    public void addAccount(){
        ContentValues cv = mDB.createAccount("salam", "khoubi");
        mDB.createRow(cv);
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
}
