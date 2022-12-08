package com.theflexproject.thunder.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.FileItemDialogAdapter;
import com.theflexproject.thunder.model.MyMedia;

import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class CustomFileListDialogFragment extends DialogFragment  {

    Context context;
    RecyclerView fileListInDialog;
    List<MyMedia> mediaList;
    FileItemDialogAdapter.OnItemClickListener listener;
    public OnInputListener mOnInputListener;
    View source;
    public CustomFileListDialogFragment(Context context,View source, List<MyMedia> mediaList) {
    this.context=context;
    this.mediaList=mediaList;
    this.source=source;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


//    @SuppressLint("RestrictedApi")
//    @Override
//    public void setupDialog(@NonNull Dialog dialog , int style) {
//        super.setupDialog(dialog , style);
//
//        if(getDialog() !=null){
//            setDialogPosition();
//        }
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
//        setDialogPosition();
        return inflater.inflate(R.layout.fragment_custom_file_list_dialog , container , false);
    }


    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        BlurView blurView;
        ViewGroup rootView;
        View decorView;

        blurView = view.findViewById(R.id.blurViewDialog);
        decorView =((Activity)view.getContext()).getWindow().getDecorView();
        rootView = decorView.findViewById(android.R.id.content);
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ((Activity) context).getWindow().setStatusBarColor(Color.TRANSPARENT);
        final float radius = 5f;
        final Drawable windowBackground = ((Activity) context).getWindow().getDecorView().getBackground();

        blurView.setupWith(rootView, new RenderScriptBlur(context))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);



        listener = (view1 , position) -> {
            mOnInputListener.sendInput(position);
            System.out.println("selected index number"+position);
            dismiss();
        };

        fileListInDialog = view.findViewById(R.id.fileListInDialog);
        fileListInDialog.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL , false));
        fileListInDialog.setHasFixedSize(true);
        FileItemDialogAdapter fileItemDialogAdapter = new FileItemDialogAdapter(mediaList,listener);
        fileListInDialog.setAdapter(fileItemDialogAdapter);



    }

    @Override public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            System.out.println("onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

//    private void setDialogPosition() {
//        if (source == null) {
//            return; // Leave the dialog in default position
//        }
//
//        // Find out location of source component on screen
//        // see https://stackoverflow.com/a/6798093/56285
//        int[] location = new int[2];
//        source.getLocationOnScreen(location);
//        int sourceX = location[0];
//        int sourceY = location[1];
//
//        Window window =((Activity) getActivity()).getWindow();
//
//        // set "origin" to top left corner
//        window.setGravity(Gravity.TOP| Gravity.START);
//
//        WindowManager.LayoutParams params = window.getAttributes();
//
//        // Just an example; edit to suit your needs.
//        params.x = sourceX - dpToPx(110); // about half of confirm button size left of source view
//        params.y = sourceY - dpToPx(80); // above source view
//
//        window.setAttributes(params);
//    }
//
//    public int dpToPx(float valueInDp) {
//        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
//    }

    public interface OnInputListener {
        void sendInput(int selection);
    }
}