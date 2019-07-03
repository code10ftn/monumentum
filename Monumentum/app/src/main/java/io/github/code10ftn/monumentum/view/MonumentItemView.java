package io.github.code10ftn.monumentum.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.Monument;

@EViewGroup(R.layout.view_monument_item)
public class MonumentItemView extends LinearLayout {

    @ViewById
    TextView name;

    @ViewById
    TextView location;

    @ViewById
    ImageView monumentImage;

    @ViewById
    RatingBar rating;

    public MonumentItemView(Context context) {
        super(context);
    }

    public void bind(Monument monument) {
        name.setText(monument.getName());
        location.setText(monument.getLocation());
        Glide.with(getContext())
                .load(monument.getPhotoUri())
                .into(monumentImage);
        rating.setRating((float) monument.getAverageRating());
    }
}
