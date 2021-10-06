package com.example.educor_app.quiz.Results_section;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educor_app.quiz.Model.User;
import com.example.educor_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ResultsAdminDetailed extends AppCompatActivity {

    private DatabaseReference myRef;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    //private MaterialButton button;
    private CardView button;
    private ResultsAdminDetailed.TestAdapter testAdapter;
    ArrayList<TestResults> result=new ArrayList<>();
    private String testName;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String class_name;
    private int lastPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        button = findViewById(R.id.download_card);

        user=FirebaseAuth.getInstance().getCurrentUser();
        testName=getIntent().getStringExtra("test");
        class_name=getIntent().getStringExtra("class_name");

        databaseReference=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String qualification=snapshot.child("qualification").getValue().toString();
                    if(qualification.equals("Teacher")){

                        button.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
        /*
        button for admin to see report in excel files
        */

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference();
        ListView listView = findViewById(R.id.test_listview);
        testAdapter=new ResultsAdminDetailed.TestAdapter(ResultsAdminDetailed.this,result);
        listView.setAdapter(testAdapter);
        this.setTitle(testName);
        getResults();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if available and not read only
                if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    Log.e("TAG", "Storage not available or read only");
                    return;
                }
                saveToExcel(testName.concat(".xls"));
            }
        });

    }

    public void saveToExcel(String fileName) {
        getResults();
        //New Workbook
        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();

        Sheet createSheet = hSSFWorkbook.createSheet(this.testName);
        //create row
        Row createRow = createSheet.createRow(0);
        createRow.createCell(0).setCellValue("Name");
        createRow.createCell(1).setCellValue("Score");

        int i = 0;
        while (i < this.testAdapter.dataList.size()) {
            int i2 = i + 1;
            Row createRow2 = createSheet.createRow(i2);
            createRow2.createCell(0).setCellValue(this.testAdapter.dataList.get(i).user.getUsername());
            createRow2.createCell(1).setCellValue(this.testAdapter.dataList.get(i).score);
            i = i2;
        }

        //save the file under download folder of android
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);

         try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            hSSFWorkbook.write(fileOutputStream);
            StringBuilder sb = new StringBuilder();
            sb.append("Writing file");
            sb.append(fileName);
            Log.w("FileSaver",sb.toString());
            fileOutputStream.close();
            Toast.makeText(ResultsAdminDetailed.this, "File is saved under Download folder", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ResultsAdminDetailed.this, "Can't be saved", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageReadOnly() {
        return "mounted_ro".equals(Environment.getExternalStorageState());
    }

    public boolean isExternalStorageAvailable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public void getResults(){

        myRef.child("Quiz").child(class_name).child("Results").child(testName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TestResults t=new TestResults();
                    t.userID=snapshot.getKey();
                    t.score= Objects.requireNonNull(snapshot.getValue()).toString();
                    result.add(t);
                }

                Collections.sort(result, new Comparator<TestResults>() {
                    @Override
                    public int compare(TestResults o1, TestResults o2) {
                        return o2.score.compareTo(o1.score);
                    }
                });

                getDetails();
                Log.e("The read success: " ,"su"+result.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }


    private void getDetails(){
        myRef.child("signup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i=0;i<result.size();i++){
                    if(dataSnapshot.child(result.get(i).userID).exists())
                        result.get(i).user=dataSnapshot.child(result.get(i).userID).getValue(User.class);
                }
                testAdapter.dataList=result;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
            }
        });
    }


    class TestResults{

        public String userID,score;
        public  User user;
    }


    class TestAdapter extends ArrayAdapter<TestResults> {
        private Context mContext;
        ArrayList<TestResults> dataList;

        public TestAdapter( Context context,ArrayList<TestResults> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item,parent,false);
            ((ImageView)listItem.findViewById(R.id.item_imageView))
                    .setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ranking));
            if(dataList.get(position).user!=null)
                ((TextView)listItem.findViewById(R.id.item_textView)).setText(dataList.get(position).user.getUsername());
            else {
                ((TextView) listItem.findViewById(R.id.item_textView)).setText("Details not added yet");
            }

            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;
            ((Button)listItem.findViewById(R.id.item_button)).setText(dataList.get(position).score);
            return listItem;
        }
    }
}
