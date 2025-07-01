package com.example.moodly.data;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodly.R;


public class MoodFragment extends DialogFragment {

    private ImageButton close, happy, sad;
    //
    private OnMoodSelectedListener moodSelectedListener;

//

    // Interface for communicating mood selection back to the parent fragment
    public interface OnMoodSelectedListener {
        void onMoodSelected(String mood);
    }
    //

    public MoodFragment() {
        // Required empty public constructor
    }

    //
    // Setter for the moodSelectedListener
    public void setMoodSelectedListener(OnMoodSelectedListener listener) {
        this.moodSelectedListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure that the listener is not null
        if (getActivity() instanceof OnMoodSelectedListener) {
            moodSelectedListener = (OnMoodSelectedListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString() + " must implement OnMoodSelectedListener");
        }
    }

    //

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Request no title for the dialog
        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        // Inflate the layout for this dialog fragment
        return inflater.inflate(R.layout.fragment_mood, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        close = view.findViewById(R.id.close);
        happy = view.findViewById(R.id.happy);
        sad = view.findViewById(R.id.sad);

        // Set up click listeners
        close.setOnClickListener(v -> dismiss()); // Close the dialog
        happy.setOnClickListener(v -> {
            if (moodSelectedListener != null) {
                moodSelectedListener.onMoodSelected("happy"); // Pass mood to parent
                Log.d("MoodFragment", "Happy button clicked");
            } else {
                Log.e("MoodFragment", "Mood listener is null. Cannot pass mood.");
            }
            dismiss();
        });
        sad.setOnClickListener(v -> {
            Log.d("MoodFragment", "Sad button clicked");
            if (moodSelectedListener != null) {
                moodSelectedListener.onMoodSelected("sad"); // Pass mood to parent
            } else {
                Log.e("MoodFragment", "MoodListener is null cannot pass mood");
            }
            dismiss();
        });
        
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
