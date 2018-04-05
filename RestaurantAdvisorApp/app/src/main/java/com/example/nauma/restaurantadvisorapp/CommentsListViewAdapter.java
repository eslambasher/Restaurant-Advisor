package com.example.nauma.restaurantadvisorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nauma on 30/03/2018.
 */

public class CommentsListViewAdapter extends BaseAdapter {

    private Context context;

    private List<Comments> commentsList;

    public CommentsListViewAdapter(Context context, List commentsList)
    {
        this.context = context;
        this.commentsList = commentsList;
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_row, null);
        }

        Comments comments = commentsList.get(position);

        TextView textViewComment = (TextView) convertView.findViewById(R.id.comment);
        TextView textViewCommentUsername= (TextView) convertView.findViewById(R.id.commentusername);

        String user = "by: " + comments.getUsername();
        textViewComment.setText(comments.getComment());
        textViewCommentUsername.setText(user);

        return convertView;
    }
}
