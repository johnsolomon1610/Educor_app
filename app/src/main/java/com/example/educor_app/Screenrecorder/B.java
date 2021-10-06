package com.example.educor_app.Screenrecorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.educor_app.R;

import static com.example.educor_app.Screenrecorder.C.cmy;

public class B extends ArrayAdapter<A> {

    List<A> bl;
    Context bc;
    int br;
    File bf;
    public B(Context bc, int br, List<A> bl) {
        super(bc, br, bl);
        this.bc = bc;
        this.br = br;
        this.bl = bl;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(bc);
        View view = layoutInflater.inflate(br, null, false);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDuration = view.findViewById(R.id.txtDuration);
        TextView txtSize = view.findViewById(R.id.txtSize);
        final ImageView imgMoreVertical = view.findViewById(R.id.imgMoreVertical);
        imgMoreVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getContext(),imgMoreVertical);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.item_delete:
                                deleteFile(position);
                                return true;

                            case R.id.item_share:
                                shareFile(position);
                                return true;
                            case R.id.item_rename:
                                renameFile(position);
                                return true;
                        }

                        popupMenu.dismiss();

                        return true;
                    }
                });

                popupMenu.show();
            }
        });


        A A = bl.get(position);

        //adding values to the list item

        AssetManager assetManager = bc.getAssets();

        try {
            InputStream inputStream = assetManager.open(A.getaa());

            Drawable drawable = Drawable.createFromStream(inputStream,"");

            ImageView imgDrawablePlay = (ImageView) view.findViewById(R.id.imgDrawablePlay);

            imgDrawablePlay.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();
        }


        txtTitle.setText(A.getab());
        txtDuration.setText(A.getac());
        txtSize.setText(A.getad());




        return view;
    }

    private void renameFile(int position) {
        Toast.makeText(bc, "Renaming file" + position, Toast.LENGTH_SHORT).show();
    }

    private void shareFile(final  int position) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Screen Recording/"+cmy.get(position));
        Uri uri = FileProvider.getUriForFile(bc, bc.getPackageName() + ".provider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, cmy.get(position));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        bc.startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    private void deleteFile(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(bc);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want delete this?");

        builder.setIcon(R.drawable.delete_icon);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bf = new File(Environment.getExternalStorageDirectory() + "/Screen Recording/"+cmy.get(position));
                bf.delete();
                bl.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}