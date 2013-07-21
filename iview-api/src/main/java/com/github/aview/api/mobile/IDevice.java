
package com.github.aview.api.mobile;

/*
 * #%L
 * iview-api
 * %%
 * Copyright (C) 2013 The aview authors
 * %%
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */


import com.github.aview.api.Profile;

/**
 * @author aview
 * 
 */
public enum IDevice {
    IPHONE21("iPhone2,1", Profile.LIVE_BASE, DeviceType.IPHONE), IPHONE31("iPhone3,1", Profile.LIVE_HIGH,
            DeviceType.IPHONE), IPHONE33("iPhone3,3", Profile.LIVE_HIGH, DeviceType.IPHONE), IPHONE41("iPhone4,1",
            Profile.LIVE_HIGH, DeviceType.IPHONE), IPHONE51("iPhone5,1", Profile.LIVE_HIGH, DeviceType.IPHONE), IPHONE52(
            "iPhone5,2", Profile.LIVE_HIGH, DeviceType.IPHONE), IPOD31("iPod3,1", Profile.LIVE_BASE, DeviceType.IPOD), IPOD41(
            "iPod4,1", Profile.LIVE_HIGH, DeviceType.IPOD), IPOD51("iPod5,1", Profile.LIVE_HIGH, DeviceType.IPOD), IPAD11(
            "iPad1,1", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD21("iPad2,1", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD22(
            "iPad2,2", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD23("iPad2,3", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD24(
            "iPad2,4", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD31("iPad3,1", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD32(
            "iPad3,2", Profile.LIVE_HIGH, DeviceType.IPAD), IPAD33("iPad3,3", Profile.LIVE_HIGH, DeviceType.IPAD);

    private String     deviceId;
    private Profile    defaultProfile;
    private DeviceType deviceType;

    IDevice(String deviceId, Profile defaultProfile, DeviceType deviceType) {
        this.deviceId = deviceId;
        this.defaultProfile = defaultProfile;
        this.deviceType = deviceType;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @return the profile
     */
    public Profile getDefaultProfile() {
        return defaultProfile;
    }

    /**
     * @return the deviceType
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public static IDevice fromString(String name) {
        try {
            return IDevice.valueOf(name);
        } catch (Exception e) {
            return IDevice.IPHONE21;
        }
    }
}
