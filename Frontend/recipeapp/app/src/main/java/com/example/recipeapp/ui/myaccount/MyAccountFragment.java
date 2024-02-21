package com.example.recipeapp.ui.myaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipeapp.databinding.FragmentMyaccountBinding;

public class MyAccountFragment extends Fragment {

    private FragmentMyaccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyAccountViewModel myaccountViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);

        binding = FragmentMyaccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyaccount;
        myaccountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
