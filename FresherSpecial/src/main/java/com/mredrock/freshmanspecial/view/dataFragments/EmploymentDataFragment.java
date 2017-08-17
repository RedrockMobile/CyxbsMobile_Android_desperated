package com.mredrock.freshmanspecial.view.dataFragments;


import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmploymentDataFragment extends Fragment {
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
