package io.github.code10ftn.monumentum.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import io.github.code10ftn.monumentum.R;

@EViewGroup(R.layout.view_new_comment_dialog)
public class NewCommentDialogView extends FrameLayout {

    @ViewById
    EditText input;

    public NewCommentDialogView(@NonNull Context context) {
        super(context);
    }

    public EditText getInput() {
        return input;
    }
}
