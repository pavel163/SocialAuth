package com.ebr163.socialauth.vk.model;

public class VkProfile {

    public long id;
    public String accessToken;
    public String firstName;
    public String lastName;
    public int gender;
    public String birthday;
    public VkCity city;
    public String photo;

    public enum Gender {
        UNKNOWN(0), FEMALE(1), MALE(2);

        private int value;

        Gender(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Gender fromInt(int value) {
            switch (value) {
                case 0:
                    return UNKNOWN;
                case 1:
                    return FEMALE;
                case 2:
                    return MALE;
            }
            return UNKNOWN;
        }
    }
}
