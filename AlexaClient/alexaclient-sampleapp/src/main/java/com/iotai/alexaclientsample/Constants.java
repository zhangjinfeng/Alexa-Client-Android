/**
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * You may not use this file except in compliance with the License. A copy of the License is located the "LICENSE.txt"
 * file accompanying this source. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.iotai.alexaclientsample;

import android.os.Environment;

public class Constants {
    // Constant Keys
    public static final String SESSION_ID = "sessionId";

    public static final String CLIENT_ID = "clientId";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String AUTH_CODE = "authCode";

    public static final String CODE_CHALLENGE = "codeChallenge";
    public static final String CODE_CHALLENGE_METHOD = "codeChallengeMethod";
    public static final String DSN = "dsn";
    public static final String PRODUCT_ID = "AlexaClient";
    public static final String PRODUCT_INSTANCE_ATTRIBUTES = "productInstanceAttributes";
    public static final String DEVICE_SERIAL_NUMBER = "deviceSerialNumber";

    // Log related information
    public static final String LOG_TAG = "StudyEnglishWord";
    public static final boolean IS_DEBUG_ENABLED = true;
    public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory() + "/.studyenglishword";
}
