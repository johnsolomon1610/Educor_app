package com.example.educor_app.Assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.Assignment.Assignment_Submission.AssignmentAnswer;
import com.example.educor_app.Assignment.Comments.AssignmentComments;
import com.example.educor_app.Assignment.Assignment_Submission.AssignmentFiles;
import com.example.educor_app.R;
import com.example.educor_app.Assignment.Comments.ReportAbuse;
import com.example.educor_app.Assignment.Comments.comments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RecyclerAdapter_assignment extends RecyclerView.Adapter<RecyclerAdapter_assignment.MyViewHolder> {

    Context context;
    ArrayList<assignment_data> data1;
    DatabaseReference ref,ref3;

    public RecyclerAdapter_assignment(Context c , ArrayList<assignment_data> p)
    {
        context = c;
        data1= p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_assignment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText("Assignment : " + data1.get(position).getName());

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                ref3=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                ref3.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String qualification = Objects.requireNonNull(dataSnapshot.child("qualification").getValue()).toString();

                        if (qualification.equals("Teacher")) {
                            showPopupMenu( holder.mImageButton,position);
                        }
                        else {
                            showPopup( holder.mImageButton,position);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView name;
        CardView cardView;
        ImageButton Submit;
        EditText Comments;
        ImageButton mImageButton;
        DatabaseReference reference,ref2;
        public MyViewHolder(View itemView) {
            super(itemView);

            name =itemView.findViewById(R.id.class_name);
            cardView=itemView.findViewById(R.id.cardview1);
            Submit=itemView.findViewById(R.id.comments_submit);
            Comments=itemView.findViewById(R.id.comments);
            mImageButton= itemView.findViewById(R.id.imageButton3);

            final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int position=getAdapterPosition();
                    final String comments=Comments.getText().toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                    Date now = new Date();
                    final String date = formatter.format(now);

                    assert user != null;
                    ref2=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                    ref2.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String student_name= Objects.requireNonNull(snapshot.child("Username").getValue()).toString();

                            reference=FirebaseDatabase.getInstance().getReference("Assignment").child(data1.get(position).getClass_name())
                                    .child("comments").child(data1.get(position).getName()).child(user.getUid());
                            com.example.educor_app.Assignment.Comments.comments comment=new comments(comments,date,student_name);
                            reference.setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            ref=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String qualification= Objects.requireNonNull(snapshot.child("qualification").getValue()).toString();

                    if(qualification.equals("Teacher"))
                    {
                        int position=getAdapterPosition();
                        Intent intent=new Intent(context, AssignmentAnswer.class);
                        intent.putExtra("title",data1.get(position).getName());
                        intent.putExtra("due",data1.get(position).getDue_Time());
                        intent.putExtra("class_name",data1.get(position).getClass_name());
                        context.startActivity(intent);
                    }
                    else
                    {
                        int position=getAdapterPosition();
                        Intent intent=new Intent(context, AssignmentFiles.class);
                        intent.putExtra("due",data1.get(position).getDue_Time());
                        intent.putExtra("title",data1.get(position).getName());
                        intent.putExtra("description",data1.get(position).getDescription());
                        intent.putExtra("teacher_userid",data1.get(position).getUserid());
                        intent.putExtra("class_name",data1.get(position).getClass_name());
                        intent.putExtra("Url",data1.get(position).getUrl());
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_assignment, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public MyMenuItemClickListener(int positon) {
            this.position = positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.comments) {
                Intent intent1 = new Intent(context, AssignmentComments.class);
                intent1.putExtra("class_name", data1.get(position).getClass_name());
                intent1.putExtra("title", data1.get(position).getName());
                context.startActivity(intent1);
                return true;
            }
            return true;
        }
    }
    private void showPopup(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_assignment_student, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClick(position));
        popup.show();
    }

    class MyMenuItemClick implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public MyMenuItemClick(int positon) {
            this.position = positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.report) {
                Intent intent = new Intent(context, ReportAbuse.class);
                intent.putExtra("class_name",data1.get(position).getClass_name());
                intent.putExtra("Assignment_name",data1.get(position).getName());
                context.startActivity(intent);
                return true;
            }
            return true;
        }
    }
}
