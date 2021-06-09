package it.main;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AuthFragment extends Fragment
{
    public static String email;

    private static void setAdmin(FragmentActivity a)
    {
        TextView authText = a.findViewById(R.id.authText);
        authText.setText("Utente riconosciuto. Ora sei un amministratore!");
    }

    public AuthFragment() { super(R.layout.auth_fragment); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = 1;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        if(!MainActivity.auth)
        {
            String text = "";
            if(adapter == null)
                text = "NFC non disponibile";
            else if(!adapter.isEnabled())
                text = "Abilita l'NFC dalle impostazioni";
            if(!text.equals(""))
                Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_LONG).show();
        }
        else
            setAdmin(getActivity());
    }

    public static void elaborate(String tag, FragmentActivity a)
    {
        String data[] = tag.split("&");
        if(data.length == 10)
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("comuni").whereEqualTo("nome", "Agrigento").get().addOnCompleteListener(task -> {
                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                for(String s: (ArrayList<String>) doc.getData().get("auth"))
                {
                    if(s.equals(data[1]))
                    {
                        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task1 -> {
                            task1.getResult().getDocuments().get(0).getReference().update("auth", true);
                        });
                        MainActivity.auth = true;
                        setAdmin(a);
                        break;
                    }
                }
            });
        }
    }
}
