/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.wifi;

import com.android.settings.R;
import com.android.settingslib.wifi.AccessPoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

class WifiDialog extends AlertDialog implements WifiConfigUiBase {
    /* SPRD: add for cmcc wifi feature @{ */
    //static final int BUTTON_SUBMIT = DialogInterface.BUTTON_POSITIVE;
    static int BUTTON_SUBMIT = 0;
    static int BUTTON_DISCONNECT = 0;
    /* @} */
    static final int BUTTON_FORGET = DialogInterface.BUTTON_NEUTRAL;

    private final boolean mEdit;
    private final boolean mModify;
    private final DialogInterface.OnClickListener mListener;
    private final AccessPoint mAccessPoint;

    private View mView;
    private WifiConfigController mController;
    private boolean mHideSubmitButton;
    private boolean mHideForgetButton;

    public WifiDialog(Context context, DialogInterface.OnClickListener listener,
            AccessPoint accessPoint, boolean edit, boolean modify,
            boolean hideSubmitButton, boolean hideForgetButton) {
        this(context, listener, accessPoint, edit, modify);
        mHideSubmitButton = hideSubmitButton;
        mHideForgetButton = hideForgetButton;
    }

    public WifiDialog(Context context, DialogInterface.OnClickListener listener,
            AccessPoint accessPoint, boolean edit, boolean modify) {
        super(context);
        mEdit = edit;
        mModify = modify;
        mListener = listener;
        mAccessPoint = accessPoint;
        mHideSubmitButton = false;
        mHideForgetButton = false;
    }

    @Override
    public WifiConfigController getController() {
        return mController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mView = getLayoutInflater().inflate(R.layout.wifi_dialog, null);
        setView(mView);
        setInverseBackgroundForced(true);
        mController = new WifiConfigController(this, mView, mAccessPoint, mEdit, mModify);
        super.onCreate(savedInstanceState);

        if (mHideSubmitButton) {
            mController.hideSubmitButton();
        } else {
            /* During creation, the submit button can be unavailable to determine
             * visibility. Right after creation, update button visibility */
            mController.enableSubmitIfAppropriate();
        }

        if (mHideForgetButton) {
            mController.hideForgetButton();
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            mController.updatePassword();
    }

    @Override
    public boolean isEdit() {
        return mEdit;
    }

    @Override
    public Button getSubmitButton() {
        return getButton(BUTTON_SUBMIT);
    }

    @Override
    public Button getForgetButton() {
        return getButton(BUTTON_FORGET);
    }

    @Override
    public Button getCancelButton() {
        return getButton(BUTTON_NEGATIVE);
    }

    @Override
    public void setSubmitButton(CharSequence text) {
        /* SPRD: add for cmcc wifi feature @{ */
        BUTTON_SUBMIT = DialogInterface.BUTTON_POSITIVE;
        BUTTON_DISCONNECT = 0;
        /* @} */
        setButton(BUTTON_SUBMIT, text, mListener);
    }

    @Override
    public void setForgetButton(CharSequence text) {
        setButton(BUTTON_FORGET, text, mListener);
    }

    @Override
    public void setCancelButton(CharSequence text) {
        setButton(BUTTON_NEGATIVE, text, mListener);
    }

    /* SPRD: add for cmcc wifi feature @{ */
    public void setDisconnectButton(CharSequence text) {
        BUTTON_SUBMIT = 0;
        BUTTON_DISCONNECT = DialogInterface.BUTTON_POSITIVE;
        setButton(BUTTON_DISCONNECT, text, mListener);
    }
    public Button getDisconnectButton() {
        return getButton(BUTTON_DISCONNECT);
    }
    /* @} */
}
