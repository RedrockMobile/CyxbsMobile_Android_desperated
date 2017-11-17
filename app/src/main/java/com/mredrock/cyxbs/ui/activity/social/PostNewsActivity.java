package com.mredrock.cyxbs.ui.activity.social;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.multi_image_selector.MultiImageSelectorActivity;
import com.mredrock.cyxbs.component.widget.TopicEditText;
import com.mredrock.cyxbs.component.widget.ninelayout.NineGridlayout;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.model.social.UploadImgResponse;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PostNewsActivity extends BaseActivity implements View.OnClickListener,TopicEditText.OnTopicEditListener {
    public static final String TAG = "PostNewsActivity";
    private final static String ADD_IMG = "file:///android_asset/add_news.jpg";
    public static final String EXTRA_TOPIC_ID = "extra_topic_id";
    public static final String EXTRA_TOPIC_TITLE = "extra_topic_title";
    private final static int REQUEST_IMAGE = 0001;
    @BindView(R.id.toolbar_title)
    TextView mTitleText;
    @BindView(R.id.toolbar_save)
    TextView mSend;
    @BindView(R.id.add_news_edit)
    TopicEditText mAddNewsEdit;
    @BindView(R.id.iv_ngrid_layout)
    NineGridlayout mNineGridlayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_add_topic)
    AppCompatImageView mIvAddTopic;
    private List<Image> mImgList;
    private User mUser;
    private int mTopicId = -1;

    @OnClick(R.id.toolbar_save)
    public void onClick(View view) {
        if (mTopicId == -1) {
            sendDynamic("标题我该打什么才好？？", mAddNewsEdit.getText().toString(), BBDDNews.BBDD);
        } else {
            sendTopicArticle("标题我该打什么才好？？", mAddNewsEdit.getText().toString(), mTopicId);
        }
    }

    private void sendTopicArticle(String title, String content,int topicId) {
        Observable<String> observable;
        List<Image> currentImgs = new ArrayList<>();
        currentImgs.addAll(mImgList);
        currentImgs.remove(0);

        if (currentImgs.size() > 0)
            observable = uploadWithImg(currentImgs, title, content, BBDDNews.TOPIC_ARTICLE, topicId);
        else observable = uploadWithoutImg(title, content, BBDDNews.TOPIC_ARTICLE, topicId);

        observable.subscribe(new SimpleObserver<>(this, true, false, new SubscriberListener<Object>() {
            @Override
             public void onComplete() {
                super.onComplete();
                Intent intent = new Intent();
                intent.putExtra(TopicArticleActivity.EXTRA_POST_SUCCESS, true);
                PostNewsActivity.this.setResult(TopicArticleActivity.RESULT_CODE, intent);
                showUploadSuccess(content);
            }

            @Override
            public boolean onError(Throwable e) {
                finish();
                Toast.makeText(PostNewsActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                return super.onError(e);
            }
        }));
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PostNewsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        ButterKnife.bind(this);
        mUser = BaseAPP.getUser(this);
        init();
        initToolbar();
        Intent intent = getIntent();
        mTopicId = intent.getIntExtra(EXTRA_TOPIC_ID, -1);
        String topicTitle = intent.getStringExtra(EXTRA_TOPIC_TITLE);
        if (!(topicTitle == null || "".equals(topicTitle))) {
            mAddNewsEdit.setTopicText("#" + topicTitle + "#  ");
            mIvAddTopic.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(
                    v -> this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void init() {
        RxPermissions rxPermissions = new RxPermissions(this);
        mAddNewsEdit.setTopicEditListener(this);
        mImgList = new ArrayList<>();
        mImgList.add(new Image(ADD_IMG, Image.TYPE_ADD));
        mNineGridlayout.setImagesData(mImgList);
        mNineGridlayout.setOnAddImagItemClickListener((v, position) ->
                rxPermissions
                        .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                // All requested permissions are granted
                                Intent intent = new Intent(PostNewsActivity.this, MultiImageSelectorActivity.class);
                                // 是否显示调用相机拍照
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                // 最大图片选择数量
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                // 默认选择图片,回填选项(支持String ArrayList)
                                ArrayList<String> results = new ArrayList<>();
                                for (Image i : mImgList) {
                                    if (i.getType() != Image.TYPE_ADD)
                                        results.add(i.url);
                                }

                                if (mImgList.size() != 0)
                                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, results);
                                startActivityForResult(intent, REQUEST_IMAGE);
                            } else {
                                Utils.toast(this, "没有赋予权限哦");
                            }
                        }));

        mNineGridlayout.setOnClickDeletecteListener((v, position) -> {
            mImgList.remove(position);
            mNineGridlayout.setImagesData(mImgList);
        });
    }

    private void sendDynamic(String title, String content, int type) {
        if (content == null || content.equals("")) {
            Toast.makeText(PostNewsActivity.this, getString(R.string.noContent), Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<String> observable;
        List<Image> currentImgs = new ArrayList<>();
        currentImgs.addAll(mImgList);
        currentImgs.remove(0);

        if (currentImgs.size() > 0)
            observable = uploadWithImg(currentImgs, title, content, type, null);
        else observable = uploadWithoutImg(title, content, type, null);

        observable.subscribe(new SimpleObserver<>(this, true, false, new SubscriberListener<Object>() {
            @Override
             public void onComplete() {
                super.onComplete();
                showUploadSuccess(content);
            }

            @Override
            public boolean onError(Throwable e) {
                finish();
                Toast.makeText(PostNewsActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                return super.onError(e);
            }
        }));
    }

    private Observable<String> uploadWithImg(List<Image> currentImgs, String title, String content, int type, @Nullable Integer topicId) {
        return Observable.fromIterable(currentImgs)
                .observeOn(Schedulers.io())
                .map(image -> image.url)
                .flatMap(url -> RequestManager.getInstance().uploadNewsImg(mUser.stuNum, url))
                .buffer(currentImgs.size())
                .flatMap(responseList -> {
                    String tUrl = "";
                    String pUrl = "";
                    for (UploadImgResponse.Response response : responseList) {
                        pUrl += response.photoSrc.split("/")[6] + ",";
                        tUrl += response.thumbnailSrc.split("/")[7] + ",";
                    }
                    pUrl = pUrl.substring(0, pUrl.length() - 1);
                    tUrl = tUrl.substring(0, tUrl.length() - 1);
                    if (type == BBDDNews.BBDD) {
                        return RequestManager.getInstance().sendDynamic(type, title, content, tUrl, pUrl, mUser.id, mUser.stuNum, mUser.idNum);
                    } else {
                        return RequestManager.getInstance().sendTopicArticle(topicId, title, content, tUrl, pUrl, mUser.stuNum, mUser.idNum);
                    }
                });
    }

    private Observable<String> uploadWithoutImg(String title, String content, int type,@Nullable Integer topicId) {
        if (type == BBDDNews.BBDD) {
            return RequestManager.getInstance()
                    .sendDynamic(type, title, content, " ", " ", mUser.id, mUser.stuNum, mUser.idNum);
        } else {
            return RequestManager.getInstance().sendTopicArticle(topicId, title, content, "", "", mUser.stuNum, mUser.idNum);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (Image image : mImgList) {
                    for (int i = 0; i < pathList.size(); i++) {
                        if (image.url.equals(pathList.get(i))) pathList.remove(i);
                    }
                }
                // 处理你自己的逻辑 ....
                if (mImgList.size() + pathList.size() > 10) {
                    Utils.toast(this, "最多只能选9张图");
                    return;
                }
                Observable.fromIterable(pathList)
                        .map(s -> new Image(s, Image.TYPE_NORMAL))
                        .map(image -> {
                            mImgList.add(image);
                            return mImgList;
                        })
                        .subscribe(new SimpleObserver<>(this, new SubscriberListener<List<Image>>() {
                            @Override
                            public void onNext(List<Image> list) {
                                super.onNext(list);
                                mNineGridlayout.setImagesData(list);
                            }
                        }));
            }
        }
    }

    private void showUploadSuccess(String content) {
        RxBus.getDefault().post(new HotNews(content, mImgList));
        PostNewsActivity.this.finish();
    }


    @OnClick(R.id.iv_add_topic)
    public void onViewClicked() {
        Intent intent = new Intent(this, TopicActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTopic() {
        mTitleText.setText("参与话题");
    }

    @Override
    public void onNoTopic() {
        mTitleText.setText("发表动态");
        mTopicId = -1;
        mIvAddTopic.setVisibility(View.VISIBLE);
    }
}
