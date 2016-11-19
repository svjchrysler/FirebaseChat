package edu.galileo.andriod.appfirebase.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.galileo.andriod.appfirebase.R;
import edu.galileo.andriod.appfirebase.models.Chat;

public class MessageRecyclerAdapter extends FirebaseRecyclerAdapter<Chat,
        MessageRecyclerAdapter.ToDoItemViewHolder> {

    public MessageRecyclerAdapter(int modelLayout, DatabaseReference ref) {
        super(Chat.class, modelLayout, MessageRecyclerAdapter.ToDoItemViewHolder.class, ref);
    }

    @Override
    public ToDoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);

        return new ToDoItemViewHolder(view);
    }


    @Override
    protected void populateViewHolder(ToDoItemViewHolder holder, Chat item, int position) {
        //String itemDescription = item.getItem();
        String username = item.getUsername();
        String message = item.getMessage();
        String photo = item.getPhoto();

        holder.txtUser.setText(username);
        holder.txtMessage.setText(message);

    }

    class ToDoItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.txtUser) TextView txtUser;
        @BindView(R.id.txtMessage) TextView txtMessage;
        /* falta imagen */

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Chat currentItem = (Chat) getItem(position);
            DatabaseReference reference = getRef(position);

            //boolean completed = !currentItem.isCompleted();
            /*currentItem.setCompleted(completed);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("completed", completed);
            reference.updateChildren(updates);*/
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

