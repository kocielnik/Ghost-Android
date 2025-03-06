package me.vickychijwani.spectre.view.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import me.vickychijwani.spectre.R;
import me.vickychijwani.spectre.databinding.FragmentGhostAuthBinding;
import me.vickychijwani.spectre.databinding.FragmentGhostV0ErrorBinding;

public class GhostV0ErrorFragment extends BaseFragment {

private FragmentGhostV0ErrorBinding binding;
   /* @BindView(R.id.error_title)     TextView mErrorTitle;
    @BindView(R.id.error_content)   TextView mErrorContent;
    @BindView(R.id.error_tip)       TextView mErrorTip;
    @BindView(R.id.error_legacy)    TextView mErrorLegacy;
*/
    public static GhostV0ErrorFragment newInstance() {
        GhostV0ErrorFragment fragment = new GhostV0ErrorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GhostV0ErrorFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGhostV0ErrorBinding.inflate(inflater, container, false);
        String appName = getString(R.string.app_name);
        binding.errorTitle.setText(Html.fromHtml(binding.errorTitle.getText().toString()));
        binding.errorContent.setText(Html.fromHtml(binding.errorContent.getText().toString()));
        binding.errorTip.setText(Html.fromHtml(binding.errorTip.getText().toString()));
        binding.errorLegacy.setText(Html.fromHtml(String.format(binding.errorLegacy.getText().toString(), appName)));
        binding.errorTitle.setMovementMethod(LinkMovementMethod.getInstance());
        binding.errorContent.setMovementMethod(LinkMovementMethod.getInstance());
        binding.errorTip.setMovementMethod(LinkMovementMethod.getInstance());
        binding.errorLegacy.setMovementMethod(LinkMovementMethod.getInstance());
        return binding.getRoot();

    }

}
