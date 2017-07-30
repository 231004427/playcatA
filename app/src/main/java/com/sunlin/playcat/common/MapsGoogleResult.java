package com.sunlin.playcat.common;

/**
 * Created by sunlin on 2017/7/7.
 */

public class MapsGoogleResult {
    private MapsGoogleResultAddress[] address_components;

    public void setAddress_components(MapsGoogleResultAddress[] address_components) {
        this.address_components = address_components;
    }

    public MapsGoogleResultAddress[] getAddress_components() {
        return address_components;
    }
}
