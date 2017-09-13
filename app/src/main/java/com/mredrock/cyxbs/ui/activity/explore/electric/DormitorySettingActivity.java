package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.fragment.explore.eletric.DialogRemindFragment;
import com.mredrock.cyxbs.util.ElectricRemindUtil;
import com.mredrock.cyxbs.util.KeyboardUtils;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DormitorySettingActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView titleText;
    @Bind(R.id.et_building_number)
    EditText buildingNumberEdit;
    @Bind(R.id.et_dormitory_number)
    EditText dormitoryNumberEdit;

    private static final String TAG = "DormitorySetting";
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog dialog;
    private int buildingNumber;


    public static final String BUILDING_KEY = "building_number";
    public static final String DORMITORY_KEY = "dormitory_number";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_setting);
        ButterKnife.bind(this);
        setResult(ElectricChargeActivity.REQUEST_NOT_SET_CODE);
        initView();

    }

    private void initView() {
        titleText.setText("设置寝室");
        buildingNumberEdit.setInputType(InputType.TYPE_NULL);

        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this)
                .inflate(R.layout.electric_bottom_dailog, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(recyclerView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        bottomSheetBehavior = BottomSheetBehavior.from((View) recyclerView.getParent());
        bottomSheetBehavior.setPeekHeight((int) Utils.dp2PxInt(this, 250));

        DormitoryAdapter adapter = new DormitoryAdapter();
        adapter.setOnItemClickListener(((position, text) -> {
            buildingNumberEdit.setText(text);
            buildingNumber = position + 1;
            dialog.dismiss();
        }));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String building = (String) SPUtils.get(APP.getContext(),BUILDING_KEY,"");
        if (building.isEmpty())
            return;
        buildingNumberEdit.setText(building+"栋");
        buildingNumber = Integer.parseInt(building);
        String dormitory = (String) SPUtils.get(APP.getContext(),DORMITORY_KEY,"");
        dormitoryNumberEdit.setText(dormitory);

    }

    @OnClick(R.id.et_building_number)
    public void onBuildingEditClick(View v){
        KeyboardUtils.hideInput(dormitoryNumberEdit);
        KeyboardUtils.hideInput(buildingNumberEdit);

        dialog.show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    @OnClick(R.id.toolbar_iv_left)
    public void onBackImageClick(){
        onBackPressed();
    }

    @OnClick(R.id.btn_dormitory_ok)
    public void onOkClick(){

        if (buildingNumber == 0 || dormitoryNumberEdit.getText().toString().length() < 3){
            //Toast.makeText(this,"信息不完整",Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            DialogRemindFragment dialogRemindFragment = new DialogRemindFragment();
            if (buildingNumber == 0 && dormitoryNumberEdit.getText().toString().isEmpty()) {
                bundle.putString("REMIND_TEXT", "你还没有填写哦");
            }else if(dormitoryNumberEdit.getText().toString().length() < 3 && dormitoryNumberEdit.getText().toString().length() > 0 ){
                bundle.putString("REMIND_TEXT", "请填写正确的寝室号");
            }else {
                bundle.putString("REMIND_TEXT","请将信息填写完整哦");
            }
            dialogRemindFragment.setArguments(bundle);
            dialogRemindFragment.show(getFragmentManager(),"DialogRemindFragment");
        }else {
            User user = APP.getUser(this);
            SPUtils.set(APP.getContext(),BUILDING_KEY,String.valueOf(buildingNumber));
            SPUtils.set(APP.getContext(),DORMITORY_KEY,dormitoryNumberEdit.getText().toString()+"");
            SPUtils.set(APP.getContext(), ElectricRemindUtil.SP_KEY_ELECTRIC_REMIND_TIME, System.currentTimeMillis() / 2);
            SimpleSubscriber<Object> subscriber = new SimpleSubscriber<Object>(this, true, new SubscriberListener<Object>() {
                @Override
                public void onCompleted() {
                    super.onCompleted();
                    setResult(ElectricChargeActivity.REQUEST_SET_CODE);
                    onBackPressed();
                }

                @Override
                public boolean onError(Throwable e) {
                    return super.onError(e);

                }
            });
            RequestManager.INSTANCE.bindDormitory(user.stuNum,user.idNum,buildingNumber + "-" + dormitoryNumberEdit.getText().toString(),subscriber);

        }

    }



    static class DormitoryAdapter extends RecyclerView.Adapter<DormitoryAdapter.Holder> {

        private OnItemClickListener mItemClickListener;

        public void setOnItemClickListener(OnItemClickListener li) {
            mItemClickListener = li;
        }

        @Override
        public DormitoryAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(final DormitoryAdapter.Holder holder, int position) {
            holder.tv.setText(String.format("%02d栋", position + 1));
            if(mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.getLayoutPosition(),
                                holder.tv.getText().toString());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 39;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_item_building);
            }
        }

        interface OnItemClickListener {
            void onItemClick(int position, String text);
        }
    }


}
