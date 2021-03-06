package gachon.mpclass.pearth;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class RecordActivity extends AppCompatActivity {
    public String context;
    private FloatingActionButton sendbt;
    private TextView userID;
    public String user;
    int cnt=0;
    int water=0;
    int elec=0;
    int plant=0;
    int air=0;
    int volun=0;
    int zero=0;
    int harm=0;
    int eat=0;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    DatabaseReference mDB=FirebaseDatabase.getInstance().getReference().child("Tag");
    private ListView listView;
    ArrayList<ListViewItem> item=new ArrayList<ListViewItem>();
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setTitle("?????? ?????????");

        //????????? ???????????? ???????????? ??????
        sendbt = (FloatingActionButton) findViewById(R.id.button2);

        //????????? ????????? ????????? ??? ?????? ???????????? ?????????

        sendbt.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivityForResult(intent,1);
        });


        listView = (ListView) findViewById(R.id.listviewmsg);
        initDatabase();

        ListViewAdapter adapter=new ListViewAdapter(item);
        listView.setAdapter(adapter);

        mReference=mDatabase.getReference("board");
        //????????? ?????? ??????,,
        mReference.addChildEventListener(new ChildEventListener() {
            //?????? ????????? ?????? ??? ValueListener??? ????????? ?????? ???????????? ???????????? ?????? ?????? ???
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //?????? ????????? ?????????(??? : MessageItem??????) ????????????
                ListViewItem listViewItem= dataSnapshot.getValue(ListViewItem.class);

                //????????? ???????????? ???????????? ???????????? ?????? ArrayList??? ??????
                item.add(listViewItem);
                G.keyList.add(dataSnapshot.getKey());
                if (listViewItem.getTag().indexOf("#?????????") >= 0) {
                    water = water + 1;
                }
                if (listViewItem.getTag().indexOf("#????????????") >= 0) {
                    elec = elec + 1;
                }
                if (listViewItem.getTag().indexOf("#????????????") >= 0) {
                    plant = plant + 1;
                }
                if (listViewItem.getTag().indexOf("#???????????????") >= 0) {
                    air = air + 1;
                }
                if (listViewItem.getTag().indexOf("#??????????????????") >= 0) {
                    volun = volun + 1;
                }
                if (listViewItem.getTag().indexOf("#??????????????????") >= 0) {
                    zero = zero + 1;
                }
                if (listViewItem.getTag().indexOf("#????????????????????????") >= 0) {
                    harm = harm + 1;
                }
                if (listViewItem.getTag().indexOf("#??????????????????") >= 0) {
                    eat = eat + 1;
                }
                mDB.child("water").setValue(water);
                mDB.child("electricity").setValue(elec);
                mDB.child("plant").setValue(plant);
                mDB.child("air").setValue(air);
                mDB.child("volunteer").setValue(volun);
                mDB.child("zero-waste").setValue(zero);
                mDB.child("harm").setValue(harm);
                mDB.child("eat").setValue(eat);
                //??????????????? ??????
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1); //??????????????? ????????? ????????? ????????? ?????? ??????
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = G.keyList.indexOf(dataSnapshot.getKey());
                item.remove(index);
                G.keyList.remove(index);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void initDatabase () {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }


    //???????????? ?????? ???????????? ?????? ??????
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //????????? ?????????
    private void hideActionBar () {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_record) {
            Intent homeIntent = new Intent(this, RecordActivity.class);
            startActivity(homeIntent);
        }
        if (id == R.id.action_analysis) {
            Intent settingIntent = new Intent(this, Analysis.class);
            startActivity(settingIntent);
        }
        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(this, Shareboard.class);
            startActivity(shareIntent);
        }



        return super.onOptionsItemSelected(item);
    }
}
