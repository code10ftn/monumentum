package io.github.code10ftn.monumentum.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.UserComment;

@EViewGroup(R.layout.view_monument_comment)
public class MonumentCommentView extends LinearLayout {

    @ViewById
    TextView name;

    @ViewById
    TextView text;

    @ViewById
    SimpleDraweeView userImage;

    @ViewById
    TextView timestamp;

    public MonumentCommentView(Context context) {
        super(context);
    }

    public void bind(UserComment comment) {
        name.setText(comment.getUser().getName());
        text.setText(comment.getComment());

        final RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        userImage.getHierarchy().setRoundingParams(roundingParams);

        timestamp.setText(DateFormat.getDateInstance(DateFormat.LONG).format(comment.getTimestamp()));
    }
}
