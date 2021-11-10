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
import com.airbnb.lottie.LottieAnimationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogCustom extends Dialog {
    private View rootView;
    private Context context;
    private String title;
    private String message;
    private String mainTextButton;
    private View.OnClickListener mainActionButton;
    private Integer resAnimLottie;
    private Integer resImg;

    @Bind(R.id.tvTitleCustomDialog)
    TextView tvTitle;

    @Bind(R.id.tvMessageCustomDialog)
    TextView tvMessage;

    @Bind(R.id.btnMainCustomDialog)
    Button btnMain;

    @Bind(R.id.lavCustomDialog)
    LottieAnimationView lavAnimation;

    @Bind(R.id.imgCustomDialog)
    ImageView imgCustomDialog;

    public DialogCustom(Context context, String title, String message, String mainTextButton, View.OnClickListener mainActionButton) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.title = title;
        this.message = message;
        this.mainTextButton = mainTextButton;
        this.mainActionButton = mainActionButton;
    }

    public DialogCustom(Context context, String title, String message, String mainTextButton, View.OnClickListener mainActionButton, Integer resImg) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
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
        setContentView(R.layout.dialog_custom);
        ButterKnife.bind(this);
        configurateDialog();
        rootView = findViewById(R.id.root_view);
    }

    private void configurateDialog() {
        tvTitle.setText(title);
        tvMessage.setText(message);
        btnMain.setText(mainTextButton);

        if (resImg != 0) {
            imgCustomDialog.setImageResource(resImg);
            imgCustomDialog.setVisibility(View.VISIBLE);
        } else
            imgCustomDialog.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnMainCustomDialog)
    public void acceptClick(View view) {
        dismiss();
        mainActionButton.onClick(view);
    }

    @Override
    public void show() {
        super.show();
        rootView.setAlpha(0);
        ViewPropertyAnimator anim = rootView.animate().alpha(1).setDuration(650);
        anim.start();
    }

    @Override
    public void dismiss() {
        if (rootView != null) {
            ViewPropertyAnimator anim = rootView.animate().alpha(0).setDuration(650);
            anim.setListener(new AbstractAnimListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        DialogCustom.super.dismiss();
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
