package it.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment
{
    public LoginFragment() { super(R.layout.login_fragment); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = -1;

        EditText email = view.findViewById(R.id.email);
        EditText pwd = view.findViewById(R.id.pwd);
        TextView qui = view.findViewById(R.id.qui);
        Button button = view.findViewById(R.id.login);

        qui.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.106:4200/register"));
            startActivity(browserIntent);
        });

        button.setOnClickListener(v -> {
            if(email.getText().toString().equals(""))
                email.requestFocus();
            else if(pwd.getText().toString().equals(""))
                pwd.requestFocus();
            else
            {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                    {
                        boolean logged = false;
                        for(QueryDocumentSnapshot doc: task.getResult())
                        {
                            if(doc.getData().get("email").equals(email.getText().toString())
                                    || doc.getData().get("pwd").equals(pwd.getText().toString()))
                            {
                                logged = true;
                                MainActivity.auth = (boolean) doc.getData().get("auth");
                                break;
                            }
                        }

                        if(logged)
                        {
                            NavController nav = Navigation.findNavController(view);
                            nav.navigate(R.id.action_login_fragment_to_auth_fragment);
                        }
                        else
                        {
                            TextView error = view.findViewById(R.id.error);
                            error.setVisibility(View.VISIBLE);
                            Animation fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                            fadeout.setAnimationListener(new Animation.AnimationListener()
                            {
                                @Override
                                public void onAnimationStart(Animation animation) {}

                                @Override
                                public void onAnimationEnd(Animation animation) { error.setVisibility(View.GONE); }

                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                            error.startAnimation(fadeout);
                        }
                    }
                });
            }
        });
    }
}
