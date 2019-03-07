package com.suthishan.blooddonar.views;

public interface SeekerViews {
    void showProgress();
    void hideProgress();
    void seekerListSuccess(String response);
    void seekerListError(String string);

    void requestbloodSuccess(String response);
    void requestbloodError(String string);

    void sendRequestSuccess(String response);
    void sendReqeustError(String error);
}
