package com.suthishan.blooddonar.model;

import java.util.List;

public class DonorModel {

    private List<Donor_data> donor_data;
    private String message;
    private boolean status;

    public List<Donor_data> getDonor_data() {
        return donor_data;
    }

    public void setDonor_data(List<Donor_data> donor_data) {
        this.donor_data = donor_data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Donor_data {
        private String password;
        private String status;
        private String mobile;
        private String email;
        private String longitude;
        private String latitude;
        private String gender;
        private String bloodgroup;
        private String age;
        private String dname;
        private int d_id;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBloodgroup() {
            return bloodgroup;
        }

        public void setBloodgroup(String bloodgroup) {
            this.bloodgroup = bloodgroup;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public int getD_id() {
            return d_id;
        }

        public void setD_id(int d_id) {
            this.d_id = d_id;
        }
    }
}
