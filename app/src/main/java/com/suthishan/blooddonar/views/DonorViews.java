package com.suthishan.blooddonar.views;

public interface DonorViews {
    void showProgress();
    void hideProgress();
    void donorListSuccess(String response);
    void donorListError(String string);
}
