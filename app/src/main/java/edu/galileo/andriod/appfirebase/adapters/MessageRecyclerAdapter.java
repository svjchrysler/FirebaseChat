package edu.galileo.andriod.appfirebase.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.andriod.appfirebase.R;
import edu.galileo.andriod.appfirebase.Util;
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
        String token = item.getToken();

        if (token.equals(Util.TOKEN_USER)) {
            holder.txtUser.setText(username);
            Picasso.with(this.context).load(Uri.parse(photo)).into(holder.imgAvatar);
            holder.txtMessage.setText(message);

            holder.linearTwo.setVisibility(View.GONE);
        } else {
            holder.txtUser2.setText(username);
            Picasso.with(this.context).load(Uri.parse(photo)).into(holder.imgAvatar2);
            holder.txtMessage2.setText(message);

            holder.linearOne.setVisibility(View.GONE);
        }
    }


    class ToDoItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txtUser)
        TextView txtUser;

        @BindView(R.id.imgAvatar2)
        CircleImageView imgAvatar2;
        @BindView(R.id.txtMessage2)
        TextView txtMessage2;
        @BindView(R.id.txtUser2)
        TextView txtUser2;

        @BindView(R.id.linearOne)
        LinearLayout linearOne;
        @BindView(R.id.linearTwo)
        LinearLayout linearTwo;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

