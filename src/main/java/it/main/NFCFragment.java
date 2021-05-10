package it.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class NFCFragment extends Fragment
{
    NfcAdapter adapter;
    PendingIntent intent;
    IntentFilter filter;

    public NFCFragment() { super(R.layout.nfc_fragment); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        adapter = NfcAdapter.getDefaultAdapter(getContext());
        if(adapter == null)
        {
            Toast.makeText(getContext(), "NFC not supported", Toast.LENGTH_SHORT);
            NavController nav = Navigation.findNavController(view);
            nav.navigate(R.id.action_nfc_fragment_to_registerFragment);
        }

        //readFromIntent(getActivity().getIntent());
        intent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }
}
