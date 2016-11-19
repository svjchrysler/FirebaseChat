package edu.galileo.andriod.appfirebase.adapters;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.galileo.andriod.appfirebase.R;
import edu.galileo.andriod.appfirebase.models.Chat;

/**
 * Created by ykro.
 */

public class MessageRecyclerAdapter extends FirebaseRecyclerAdapter<Chat,
        MessageRecyclerAdapter.ToDoItemViewHolder> {

    public MessageRecyclerAdapter(int modelLayout, DatabaseReference ref) {
        super(Chat.class, modelLayout, MessageRecyclerAdapter.ToDoItemViewHolder.class, ref);
    }

    @Override
    public ToDoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ToDoItemViewHolder(view);
    }


    @Override
    protected void populateViewHolder(ToDoItemViewHolder holder, Chat item, int position) {

        String username = item.getUsername();
        String photo = item.getPhoto();
        String message = item.getMessage();

        holder.txtUser.setText(username);
        //holder.imgAvatar.setImageURI(Uri.parse(photo));
        holder.txtMessage.setText(message);

    }

    class ToDoItemViewHolder extends RecyclerView.ViewHolder
             {

        @BindView(R.id.txtMessage) TextView txtMessage;
        @BindView(R.id.imgAvatar) ImageView imgAvatar;
        @BindView(R.id.txtUser) TextView txtUser;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

