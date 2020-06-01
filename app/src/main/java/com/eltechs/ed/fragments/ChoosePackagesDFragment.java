package com.eltechs.ed.fragments;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.app.AlertDialog.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.eltechs.ed.*;
import com.kdt.eltechsaxs.*;
import java.util.*;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ChoosePackagesDFragment extends DialogFragment {
    OnPackagesSelectedListener mListener;
    List<ContainerPackage> mSelectedItems;

    public interface OnPackagesSelectedListener {
        void onPackagesSelected(List<ContainerPackage> list);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnPackagesSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnPackagesSelectedListener");
            throw new ClassCastException(sb.toString());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mSelectedItems = new ArrayList<ContainerPackage>();
        Builder builder = new Builder(getContext());
        builder.setTitle((CharSequence) "Select packages").setAdapter(new ArrayAdapter(getContext(), R.layout.multichoice_list_item, ContainerPackage.LIST), null).setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChoosePackagesDFragment.this.mListener.onPackagesSelected(ChoosePackagesDFragment.this.mSelectedItems);
                ChoosePackagesDFragment.this.dismiss();
            }
        }).setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChoosePackagesDFragment.this.dismiss();
            }
        });
        AlertDialog create = builder.create();
        create.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                ((AlertDialog) ChoosePackagesDFragment.this.getDialog()).getButton(-1).setEnabled(!ChoosePackagesDFragment.this.mSelectedItems.isEmpty());
            }
        });
        create.getListView().setChoiceMode(2);
        create.getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ContainerPackage containerPackage = (ContainerPackage) adapterView.getItemAtPosition(i);
                if (((CheckedTextView) view).isChecked()) {
                    ChoosePackagesDFragment.this.mSelectedItems.add(containerPackage);
                } else {
                    ChoosePackagesDFragment.this.mSelectedItems.remove(containerPackage);
                }
                ((AlertDialog) ChoosePackagesDFragment.this.getDialog()).getButton(-1).setEnabled(!ChoosePackagesDFragment.this.mSelectedItems.isEmpty());
            }
        });
        return create;
    }
}
