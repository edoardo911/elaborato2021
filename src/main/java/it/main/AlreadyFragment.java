package it.main;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlreadyFragment extends Fragment
{
    public AlreadyFragment() { super(R.layout.already_fragment); }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed() { requireActivity().finish(); }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
