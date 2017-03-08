package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.network.exception.RedrockApiException;

import rx.functions.Func1;

/**
 * Created by ：AceMurder
 * Created on ：2017/2/28
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class ElectricQueryFunc implements Func1<ElectricCharge.ElectricChargeWrapper,ElectricCharge> {

    @Override
    public ElectricCharge call(ElectricCharge.ElectricChargeWrapper electricChargeWrapper) {
        if (electricChargeWrapper.getStatus() != Const.REDROCK_API_STATUS_SUCCESS || electricChargeWrapper.getElectricCharge() == null){
            throw new RedrockApiException("some thing wrong");
        }
        return electricChargeWrapper.getElectricCharge();
    }
}
