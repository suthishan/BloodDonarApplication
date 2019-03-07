package com.suthishan.blooddonar.interactor;

public interface SeekerInteractor {
    void getSeekerList();
    void requestBlood(String latitude, String longitude, String s_id, String bloodGroup);

    void sendRequest(String d_id, String s_id);
}
