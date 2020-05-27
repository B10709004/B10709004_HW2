package com.example.b10709004_hw2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.b10709004_hw2.data.WaitlistContract;
import com.example.b10709004_hw2.data.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity {


    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private String[]z=new String[2];
    private RecyclerView waitlistRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllGuests();
        mAdapter = new GuestListAdapter(this, cursor);
        waitlistRecyclerView.setAdapter(mAdapter);
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("name")) {
            z = intent.getStringArrayExtra("name");
            addToWaitlist(z[0], z[1]);
            mAdapter.swapCursor(getAllGuests());
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final long id = (long) viewHolder.itemView.getTag();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("警告")
                        .setMessage("確定要刪除?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeGuest(id);
                                mAdapter.swapCursor(getAllGuests());
                            }
                        })
                        .show();
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView);
    }


    public void addToWaitlist(String name,String party)
    {
        if(name.length()==0||party.length()==0)
        {
            return;
        }
        int partysize=1;
        try{
            partysize = Integer.parseInt(party);
        }catch (NumberFormatException ex)
        {
            Log.e(LOG_TAG,"Failed to parse party size text to numer"+ex.getMessage());
        }
        addNewGuest(name,partysize);
        mAdapter.swapCursor(getAllGuests());
    }


    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }



    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }


    private boolean removeGuest(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent startSettingsActivity = new Intent(this, SettingsAdd.class);
            startActivity(startSettingsActivity);
            return true;
        }
        else if (id == R.id.action_setting) {
            Intent startSettingsActivity2 = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}