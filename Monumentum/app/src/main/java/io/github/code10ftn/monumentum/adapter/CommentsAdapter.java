package io.github.code10ftn.monumentum.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.model.UserComment;
import io.github.code10ftn.monumentum.view.MonumentCommentView;
import io.github.code10ftn.monumentum.view.MonumentCommentView_;

@EBean
public class CommentsAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private List<UserComment> comments = new ArrayList<>();

    public void setComments(List<UserComment> comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonumentCommentView monumentCommentView;
        if (convertView == null) {
            monumentCommentView = MonumentCommentView_.build(context);
        } else {
            monumentCommentView = (MonumentCommentView) convertView;
        }

        monumentCommentView.bind((UserComment) getItem(position));
        return monumentCommentView;
    }
}
