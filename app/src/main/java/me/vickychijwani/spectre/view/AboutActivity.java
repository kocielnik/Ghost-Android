package me.vickychijwani.spectre.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import me.vickychijwani.spectre.R;
import me.vickychijwani.spectre.databinding.ActivityAboutBinding;
import me.vickychijwani.spectre.util.AppUtils;

public class AboutActivity extends BaseActivity {

    public static final String URL_GITHUB_CONTRIBUTING = "https://github.com/tryghost/ghost-android/blob/main/CONTRIBUTING.md#reporting-bugs";
    public static final String URL_TRANSLATE = "https://hosted.weblate.org/engage/ghost/en/";
    public static final String URL_COMMUNITY = "https://forum.ghost.org/";
    private ActivityAboutBinding binding;
  /*  @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.about_version) TextView mVersionView;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_about);

        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String version = AppUtils.getAppVersion(this);
        binding.aboutVersion.setText(version);
    }

/*
    @OnClick(R.id.about_open_source_libs)
*/
    public void onOpenSourceLibsClicked(View v) {
        Intent intent = new Intent(this, OpenSourceLibsActivity.class);
        startActivity(intent);
    }

/*
    @OnClick(R.id.about_report_bugs)
*/
    public void onReportBugsClicked(View v) {
        AppUtils.openUri(this, URL_GITHUB_CONTRIBUTING);
    }

/*
    @OnClick(R.id.about_translate)
*/
    public void onTranslateClicked(View v) {
        AppUtils.openUri(this, URL_TRANSLATE);
    }

/*
    @OnClick(R.id.about_play_store)
*/
    public void onRateOnPlayStoreClicked(View v) {
        final String appPackageName = getPackageName();
        try {
            AppUtils.openUri(this, "market://details?id=" + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            AppUtils.openUri(this, "https://play.google.com/store/apps/details?id="
                    + appPackageName);
        }
    }

/*
    @OnClick(R.id.about_help)
*/
    public void onHelpClicked(View v) {
        AppUtils.openUri(this, URL_COMMUNITY);
    }

}
