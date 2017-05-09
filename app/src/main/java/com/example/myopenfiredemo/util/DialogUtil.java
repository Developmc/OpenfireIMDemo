package com.example.myopenfiredemo.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

/**
 * ProgressDialog Util
 */
public class DialogUtil {
    public enum ButtonType {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }
    //Common dialog
    private static MaterialDialog commonDialog;
    //Loading dialog
    private static MaterialDialog loadingDialog;

    /**
     * 有"确定","取消"按钮的弹框 (无法点击弹框空白处关闭弹框)
     */
    public static void showCommonDialog(Context context, String title, String message,
        String positiveText, OnClickListener onPositiveListener, String negativeText,
        OnClickListener onNegativeListener) {
        commonDialog = new MaterialDialog.Builder(context).title(title)
            .content(message)
            .positiveText(positiveText)
            .onPositive(new ButtonCallBack(onPositiveListener))
            .negativeText(negativeText)
            .onNegative(new ButtonCallBack(onNegativeListener))
            .canceledOnTouchOutside(false)
            .show();
    }

    /**
     * 只有"确定"按钮的弹框
     */
    public static void showCommonDialog(Context context, String title, String message,
        String positiveText, OnClickListener onPositiveListener) {
        commonDialog = new MaterialDialog.Builder(context).title(title)
            .content(message)
            .positiveText(positiveText)
            .onPositive(new ButtonCallBack(onPositiveListener))
            .canceledOnTouchOutside(false)
            .show();
    }

    /**
     * 能否点击外部空白处就自动关闭弹框
     */
    public static void showCommonDialog(Context context, String title, String message,
        String positiveText, OnClickListener onPositiveListener, boolean canceledOnTouchOutside) {
        commonDialog = new MaterialDialog.Builder(context).title(title)
            .content(message)
            .positiveText(positiveText)
            .onPositive(new ButtonCallBack(onPositiveListener))
            .canceledOnTouchOutside(false)
            .show();
    }

    /**
     * 不需要重写响应事件的弹框
     */
    public static void showCommonDialog(Context context, String title, String message,
        String positiveText) {
        commonDialog = new MaterialDialog.Builder(context).title(title)
            .content(message)
            .positiveText(positiveText)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    hideCommonDialog();
                }
            })
            .canceledOnTouchOutside(false)
            .show();
    }

    /**
     * ListDialog
     */
    public static void showListDialog(Context context, String title, List items,
        OnItemClickListener onItemClickListener) {
        commonDialog = new MaterialDialog.Builder(context).title(title)
            .items(items)
            .itemsCallback(new ListCallback(onItemClickListener))
            .show();
    }

    public static void hideCommonDialog() {
        if (commonDialog != null && commonDialog.isShowing()) {
            commonDialog.dismiss();
            commonDialog = null;
        }
    }

    /**
     * 支持输入内容的弹框
     */
    public static void showInputDialog(Context context, String title, String content, int inputType,
        String hint, String preFill, OnInputClickListener onInputClickListener) {
        new MaterialDialog.Builder(context).title(title)
            .content(content)
            .inputType(inputType)
            .input(hint, preFill, new InputCallback(onInputClickListener))
            .show();
    }

    public static void showInputNumberDialog(Context context, String title, String content,
        String hint, String preFill, OnInputClickListener onInputClickListener) {
        new MaterialDialog.Builder(context).title(title)
            .content(content)
            .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
            .input(hint, preFill, new InputCallback(onInputClickListener))
            .show();
    }

    public static void showLoadingDialog(Context context, String message) {
        loadingDialog = new MaterialDialog.Builder(context).content(message)
            .progress(true, 0)
            .cancelable(false)
             .build();
        loadingDialog.show();
    }

    public static void showLoadingDialog(Context context, String message,
        DialogInterface.OnCancelListener onCancelListener) {
        loadingDialog = new MaterialDialog.Builder(context).content(message)
            .progress(true, 0)
            .cancelable(true)
            .cancelListener(onCancelListener)
            .build();
        loadingDialog.show();
    }

    public static void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 普通弹框的点击事件
     */
    public interface OnClickListener {
        void onClick(@NonNull Dialog dialog, @NonNull ButtonType which);
    }

    private static class ButtonCallBack implements MaterialDialog.SingleButtonCallback {
        private OnClickListener onClickListener;

        public ButtonCallBack(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            ButtonType buttonType;
            if (which == DialogAction.POSITIVE) {
                buttonType = ButtonType.POSITIVE;
            } else if (which == DialogAction.NEGATIVE) {
                buttonType = ButtonType.NEGATIVE;
            } else {
                buttonType = ButtonType.NEUTRAL;
            }
            if (onClickListener != null) {
                onClickListener.onClick(dialog, buttonType);
            }
        }
    }

    /**
     * 输入弹框的点击事件
     */
    public interface OnInputClickListener {
        void onInput(MaterialDialog dialog, CharSequence input);
    }

    private static class InputCallback implements MaterialDialog.InputCallback {
        private OnInputClickListener onInputClickListener;

        public InputCallback(OnInputClickListener onInputClickListener) {
            this.onInputClickListener = onInputClickListener;
        }

        @Override public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            if (onInputClickListener != null) {
                onInputClickListener.onInput(dialog, input);
            }
        }
    }

    /**
     * ListDialog的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(MaterialDialog dialog, View view, int which, CharSequence text);
    }

    private static class ListCallback implements MaterialDialog.ListCallback {
        private OnItemClickListener onItemClickListener;

        public ListCallback(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override public void onSelection(MaterialDialog dialog, View itemView, int which,
            CharSequence text) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dialog, itemView, which, text);
            }
        }
    }
}
