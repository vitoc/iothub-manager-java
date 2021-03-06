// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.iothubmanager.services.models;

import com.microsoft.azure.iotsolutions.iothubmanager.services.helpers.HashMapHelper;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;

import java.util.*;

public final class DeviceTwinServiceModel {

    private String eTag;
    private String deviceId;
    private DeviceTwinProperties properties;
    private HashMap tags;
    private Boolean isSimulated;

    public DeviceTwinServiceModel () {}

    public DeviceTwinServiceModel(
        final String eTag,
        final String deviceId,
        final DeviceTwinProperties properties,
        final HashMap tags,
        final Boolean isSimulated) {

        this.eTag = eTag;
        this.deviceId = deviceId;
        this.properties = properties;
        this.tags = tags;
        this.isSimulated = isSimulated;
    }

    public DeviceTwinServiceModel(final DeviceTwinDevice device) {
        this(
            device.getETag(),
            device.getDeviceId(),
            new DeviceTwinProperties(
                HashMapHelper.setToHashMap(device.getDesiredProperties()),
                HashMapHelper.setToHashMap(device.getReportedProperties())
            ),
            HashMapHelper.setToHashMap(device.getTags()),
            isSimulated(HashMapHelper.setToHashMap(device.getTags()))
        );
    }

    public String getEtag() {
        return this.eTag;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceTwinProperties getProperties() {
        return this.properties;
    }

    public HashMap getTags() {
        return this.tags;
    }

    public Boolean getIsSimulated() {
        return isSimulated;
    }

    private static Boolean isSimulated(Map tags) {
        Set<String> keys = tags.keySet();
        return keys.contains("IsSimulated") && tags.get("IsSimulated") == "Y";
    }

    public DeviceTwinDevice toDeviceTwinDevice() {
        DeviceTwinDevice twinDevice = this.getDeviceId() == null || this.getDeviceId().isEmpty()
            ? new DeviceTwinDevice() : new DeviceTwinDevice(this.getDeviceId());

        if (this.getEtag() != null) {
            twinDevice.setETag(this.getEtag());
        }

        if (this.getTags() != null) {
            twinDevice.setTags(HashMapHelper.mapToSet(this.getTags()));
        }

        if (this.properties != null && this.properties.getDesired() != null)
            twinDevice.setDesiredProperties(HashMapHelper.mapToSet(this.properties.getDesired()));

        return twinDevice;
    }
}
