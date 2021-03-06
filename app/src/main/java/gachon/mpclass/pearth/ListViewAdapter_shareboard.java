package gachon.mpclass.pearth;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDate;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter_shareboard extends BaseAdapter {
    int cnt=0;
    Context context;
    ArrayList<ListViewItem_shareboard> data;
    int check = 0;
    DatabaseReference mDB=FirebaseDatabase.getInstance().getReference().child("report");
    DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("share");

    ListViewAdapter_shareboard(Context context){
        this.context=context;
    }
    public ListViewAdapter_shareboard(ArrayList<ListViewItem_shareboard> data) {
        this.data = data;

    }

    public void clear(){
        data.clear();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context=parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListViewItem_shareboard list=data.get(position);
        convertView=inflater.inflate(R.layout.shareboard,parent,false);
        TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        Button good=(Button)convertView.findViewById(R.id.good);
        Button report=(Button)convertView.findViewById(R.id.report);
        ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_preview);

        good.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context,"???????????? ???????????????.",Toast.LENGTH_SHORT).show();
            }
        });
        report.setOnClickListener(new View.OnClickListener(){
            String ref = G.keyList.get(position); //????????? ?????? ????????????
            public void onClick(View v){
                mDB.child("report"+cnt).setValue("Report : "+ref);
                Toast.makeText(context,"?????????????????????.",Toast.LENGTH_SHORT).show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://pearth-7ec20.appspot.com").child("images_share/" + list.getFileName());

            @Override
            public void onClick(View v) {
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid){
                        //?????? ?????? ??????

                    }
                });
                dbRef.child(G.keyList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (list.getImgUrl()==null)//????????? ?????????
        {
            textView1.setText(list.getTitle());
            textView2.setText(list.getContent());
        }
        else
        {
            textView1.setText(list.getTitle());
            textView2.setText(list.getContent());
            Glide.with(convertView)
                    .load(list.getImgUrl())
                    .into(imageView);
        }



        cnt++;
        return convertView;
    }
}