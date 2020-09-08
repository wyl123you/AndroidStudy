package com.example.study.demo.addressPicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.DialogAddressPickerBinding;
import com.example.study.demo.retrofit.BaseObserver;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddressPickerActivity extends BaseActivity<DialogAddressPickerBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_address_picker;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "AddressPickerActivity";
    }

    @Override
    protected void initViews() {

        HttpEngine.getApi()
                .getProvince("0", "89d4e0520954e2cd851909b1c5ae0e08")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<LocationResp>() {
                    @Override
                    public void onNext(LocationResp locationResp) {
                        super.onNext(locationResp);
                        ArrayList<Province> provinces = locationResp.getResult();
                        provinces.add(0, new Province());
                        provinces.add(0, new Province());
                        provinces.add(0, new Province());
                        provinces.add(new Province());
                        provinces.add(new Province());
                        provinces.add(new Province());

                        AddressPickerAdapter addressPickerAdapter = new AddressPickerAdapter(provinces, getApplicationContext());
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        mBinding.rvProvince.setAdapter(addressPickerAdapter);
                        mBinding.rvProvince.setLayoutManager(manager);
//                        PagerSnapHelper helper = new PagerSnapHelper();
//                        helper.attachToRecyclerView(mBinding.rvProvince);
                        LinearSnapHelper helper = new LinearSnapHelper();
                        helper.attachToRecyclerView(mBinding.rvProvince);

                        mBinding.rvProvince.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                RecyclerView.LayoutManager manager1 = recyclerView.getLayoutManager();
                                if (manager1 instanceof LinearLayoutManager) {
                                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager1;
                                    int first = linearLayoutManager.findFirstVisibleItemPosition();
                                    int last = linearLayoutManager.findLastVisibleItemPosition();
                                    int position = (last + first) / 2;
                                    mBinding.province.setText(provinces.get(position).getName());
                                }
                            }
                        });
                    }
                });
    }
}
