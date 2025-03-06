package me.vickychijwani.spectre.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.Date;

import me.vickychijwani.spectre.R;
import me.vickychijwani.spectre.analytics.AnalyticsService;
import me.vickychijwani.spectre.databinding.ActivityPostConflictResolutionBinding;
import me.vickychijwani.spectre.event.SavePostEvent;
import me.vickychijwani.spectre.model.entity.Post;
import me.vickychijwani.spectre.util.DateTimeUtils;
import me.vickychijwani.spectre.util.log.Log;

public class PostConflictResolutionActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPostConflictResolutionBinding binding;
  /*  @BindView(R.id.conflict_explanation)                TextView mExplanationView;
    @BindView(R.id.conflict_choice)                     RadioGroup mChoiceGroup;
    @BindView(R.id.conflict_choice_use_device_copy)     RadioButton mChoiceUseDeviceCopyBtn;
    @BindView(R.id.conflict_choice_use_server_copy)     RadioButton mChoiceUseServerCopyBtn;
//    @BindView(R.id.conflict_decide_later_btn)           View mDecideLaterBtn;
    @BindView(R.id.conflict_accept_btn)                 View mAcceptBtn;

    @BindView(R.id.conflict_post_preview_status)        TextView mPostPreviewStatusView;
    @BindView(R.id.conflict_post_preview_title)         TextView mPostPreviewTitleView;
    @BindView(R.id.conflict_post_preview_markdown)      TextView mPostPreviewMarkdownView;
*/
    private Post mLocalPost = null;
    private Post mServerPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostConflictResolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mLocalPost = getIntent().getParcelableExtra(BundleKeys.LOCAL_POST);
        mServerPost = getIntent().getParcelableExtra(BundleKeys.SERVER_POST);

        String explanation = getString(R.string.conflict_explanation, mLocalPost.getTitle());
        binding.conflictExplanation.setText(Html.fromHtml(explanation));

        // passing null for date to avoid showing wrong updatedAt date for the local post
        // since we don't actually set updatedAt every time a post is edited in the app
        String strUseDeviceCopy = formatChoiceString(R.string.conflict_choice_use_device_copy, null);
        binding.conflictChoiceUseDeviceCopy.setText(Html.fromHtml(strUseDeviceCopy));
        binding.conflictChoiceUseDeviceCopy.setOnClickListener(this);

        String strUseServerCopy = formatChoiceString(R.string.conflict_choice_use_server_copy, mServerPost.getUpdatedAt());
        binding.conflictChoiceUseServerCopy.setText(Html.fromHtml(strUseServerCopy));
        binding.conflictChoiceUseServerCopy.setOnClickListener(this);

        binding.conflictAcceptBtn.setOnClickListener(this);
//        mDecideLaterBtn.setOnClickListener(this);

        binding.conflictChoiceUseDeviceCopy.performClick();

        AnalyticsService.logConflictFound();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.conflict_choice_use_device_copy) {
            showPostPreview(mLocalPost);
        } else if (id == R.id.conflict_choice_use_server_copy) {
            showPostPreview(mServerPost);
        } else if (id == R.id.conflict_accept_btn) {
            if (binding.conflictChoice.getCheckedRadioButtonId() == R.id.conflict_choice_use_device_copy) {
                resolveConflict(new Post(mLocalPost));
            } else if (binding.conflictChoice.getCheckedRadioButtonId() == R.id.conflict_choice_use_server_copy) {
                resolveConflict(new Post(mServerPost));
            } else {
                Log.exception(new IllegalStateException("No choice selected in conflict resolution UI!"));
            }
            //            case R.id.conflict_decide_later_btn:
//                break;
        }
    }

    private void showPostPreview(@NonNull Post post) {
        String postStatus = "unknown";
        if (post.isDraft()) {
            postStatus = getString(R.string.draft);
        } else if (post.isPublished()) {
            postStatus = getString(R.string.published);
        } else if (post.isScheduled()) {
            postStatus = getString(R.string.scheduled);
        }
        String postStatusLine = getString(R.string.conflict_post_preview_status, postStatus.toUpperCase());
        binding.conflictPostPreviewStatus.setText(Html.fromHtml(postStatusLine));
        binding.conflictPostPreviewTitle.setText(post.getTitle());
        binding.conflictPostPreviewMarkdown.setText(post.getMarkdown());
    }

    private void resolveConflict(@NonNull Post acceptedPost) {
        acceptedPost.setUpdatedAt(mServerPost.getUpdatedAt());
        acceptedPost.setConflictState(Post.CONFLICT_NONE);
        getBus().post(new SavePostEvent(acceptedPost, false));
        AnalyticsService.logConflictResolved();
        finish();
    }

    private String formatChoiceString(@StringRes int titleId, @Nullable Date date) {
        String title = getString(titleId);
        if (date != null) {
            String dateStr = DateTimeUtils.formatAbsolute(date, this);
            String subtitle = getString(R.string.conflict_last_update_at, dateStr);
            return String.format("<big>%1$s</big><br/><small>%2$s</small>", title, subtitle);
        } else {
            return String.format("<big>%1$s</big>", title);
        }
    }

}
