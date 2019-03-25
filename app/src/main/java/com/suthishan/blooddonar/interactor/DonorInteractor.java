package com.suthishan.blooddonar.interactor;

public interface DonorInteractor {
    void getDonorList();

    void donorAcceptDecline(String d_id, String message);
}
