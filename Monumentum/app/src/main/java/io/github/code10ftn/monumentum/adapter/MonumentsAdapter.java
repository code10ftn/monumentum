package io.github.code10ftn.monumentum.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.activity.MonumentDetailsActivity_;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.view.MonumentItemView;
import io.github.code10ftn.monumentum.view.MonumentItemView_;

@EBean
public class MonumentsAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private List<Monument> monuments = new ArrayList<>();

    public void setMonuments(List<Monument> monuments) {
        this.monuments = monuments;
    }

    @Override
    public int getCount() {
        return monuments.size();
    }

    @Override
    public Object getItem(int position) {
        return monuments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return monuments.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MonumentItemView monumentItemView;
        if (convertView == null) {
            monumentItemView = MonumentItemView_.build(context);
        } else {
            monumentItemView = (MonumentItemView) convertView;
        }

        final Monument monument = (Monument) getItem(position);

        monumentItemView.setOnClickListener(v -> MonumentDetailsActivity_.intent(context).monumentId(monument.getId()).start());

        monumentItemView.bind(monument);
        return monumentItemView;
    }
}
