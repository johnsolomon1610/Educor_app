package com.example.educor_app.Classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.Authentications.Dashboard;
import com.example.educor_app.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapter_classroom extends RecyclerView.Adapter<RecyclerAdapter_classroom.MyViewHolder> {

    Context context;
    ArrayList<classroom_data> data;
    FirebaseDatabase firebaseDatabase;
    public static DatabaseReference reference;
    FirebaseUser users;
    public  static int location;

    // List<Color> colors=new ArrayList<Color>();

    public RecyclerAdapter_classroom(Context c , ArrayList<classroom_data> p)
    {
        context = c;
        data= p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_classroom,parent,false));
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(data.get(position).getName());
        holder.create_name.setText(data.get(position).getCreate_Name());

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu( holder.mImageButton,position);
            }
        });

      //  colors.add(Color.valueOf(R.color.c1));
        //colors.add(Color.valueOf(R.color.c2));
        //colors.add(Color.valueOf(R.color.c3));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name,create_name;
        ImageButton mImageButton;
        CardView cardView;
        @SuppressLint("UseCompatLoadingForDrawables")


        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.class_name);
            create_name=itemView.findViewById(R.id.name);
            mImageButton= itemView.findViewById(R.id.imageButton2);
            cardView=itemView.findViewById(R.id.cardview1);

         //   cardView.setCardBackgroundColor(getcolor());



            itemView.setOnClickListener(this);

        }


      /*  public int getcolor(){
            Random random=new Random();
            return Color.argb(255,random.nextInt(235),random.nextInt(22),random.nextInt(220));
        }*/

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();
            location=position;
            Intent intent=new Intent(context, Dashboard.class);
            intent.putExtra("name",data.get(position).getName());
            intent.putExtra("key",data.get(position).getKey());
            intent.putExtra("Teacher",data.get(position).getCreate_Name());
            context.startActivity(intent);
        }


    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu_class, popup.getMenu());
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
            switch (menuItem.getItemId()) {

                case R.id.unenrol:
                    reference = FirebaseDatabase.getInstance().getReference("Class").child("Create");
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,data.size());
                    return true;
                case R.id.report:
                    Intent intent=new Intent(context, Classroom_ReportActivity.class);
                    intent.putExtra("name",data.get(position).getName());
                    context.startActivity(intent);
                    return true;
                default:
            }
            return false;
        }
    }
}
