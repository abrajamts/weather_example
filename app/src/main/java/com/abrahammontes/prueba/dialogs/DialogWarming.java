package com.abrahammontes.prueba.dialogs;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abrahammontes.prueba.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogWarming extends Dialog {
    @Bind(R.id.imgIconDialogWarming)
    ImageView imgIcon;

    @Bind(R.id.tvTitleDialogWarming)
    TextView tvTitle;

    @Bind(R.id.tvDescriptionDialogWarming)
    TextView tvDescription;

    @Bind(R.id.btnMainDialogWarming)
    Button btnMain;

    private View rootView;
    private String title;
    private String message;
    private String mainTextButton;
    private View.OnClickListener mainActionButton;
    private Integer resImg;

    public DialogWarming(Context context, String title, String message, String mainTextButton, View.OnClickListener mainActionButton) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.message = message;
        this.mainTextButton = mainTextButton;
        this.mainActionButton = mainActionButton;
    }

    public DialogWarming(Context context, String title, String message) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.message = message;
    }

    public DialogWarming(Context context, String title, String message, Integer resImg) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.message = message;
        this.resImg = resImg;
    }

    public DialogWarming(Context context, String title, String message, String mainTextButton, View.OnClickListener mainActionButton, Integer resImg) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.message = message;
        this.mainTextButton = mainTextButton;
        this.mainActionButton = mainActionButton;
        this.resImg = resImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_warming);
        ButterKnife.bind(this);
        configurateDialog();
        rootView = findViewById(R.id.root_view);
    }

    private void configurateDialog() {
        tvTitle.setText(title);
        tvDescription.setText(message);

        if (resImg != null) {
            imgIcon.setImageResource(resImg);
        }

        if (mainTextButton != null) {
            btnMain.setVisibility(View.VISIBLE);
            btnMain.setText(mainTextButton);
        } else
            btnMain.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnMainDialogWarming)
    public void acceptClick(View view) {
        dismiss();
        if (mainActionButton != null)
            mainActionButton.onClick(view);
    }

    @Override
    public void show() {
        super.show();
        rootView.setAlpha(0);
        ViewPropertyAnimator anim = rootView.animate().alpha(1).setDuration(500);
        anim.start();
    }

    @Override
    public void dismiss() {
        if (rootView != null) {
            ViewPropertyAnimator anim = rootView.animate().alpha(0).setDuration(500);
            anim.setListener(new DialogWarming.AbstractAnimListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        DialogWarming.super.dismiss();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            anim.start();
        }
    }

    /**
     * Current Class avoid implementing unnecessary methods for current view.
     */
    private static abstract class AbstractAnimListener implements Animator.AnimatorListener{
        @Override
        public void onAnimationStart(Animator animation, boolean isReverse) {

        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {

        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
