package com.suthishan.blooddonar.interactor;

import com.suthishan.blooddonar.model.RegisterModel;
import com.suthishan.blooddonar.model.SeekerModel;

public interface RegisterInteractor {
    void registerValue(String registerUrl, RegisterModel registerModel);

    void seekerValue(String seekerUrl, SeekerModel seekerModel);


}
