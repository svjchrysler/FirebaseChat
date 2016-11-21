package edu.galileo.andriod.appfirebase.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.andriod.appfirebase.R;
import edu.galileo.andriod.appfirebase.models.Chat;

public class MessageRecyclerAdapter extends FirebaseRecyclerAdapter<Chat,
        MessageRecyclerAdapter.ToDoItemViewHolder> {

    private Context context;

    public MessageRecyclerAdapter(int modelLayout, DatabaseReference ref, Context context) {

        super(Chat.class, modelLayout, MessageRecyclerAdapter.ToDoItemViewHolder.class, ref);
        this.context = context;
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
        Picasso.with(this.context).load(Uri.parse(photo)).into(holder.imgAvatar);
        holder.txtMessage.setText(message);

    }

    class ToDoItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txtUser)
        TextView txtUser;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

