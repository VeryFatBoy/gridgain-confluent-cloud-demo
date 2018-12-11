/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gridgain.cloud.example;

import org.apache.ignite.client.SslMode;
import org.apache.ignite.client.SslProtocol;
import org.apache.ignite.configuration.ClientConfiguration;

public class CloudConfig {
    public static ClientConfiguration getCloudConfiguration() {
        String thinClient = "d.6qqqnugd31wbpanzson5is.gridgain.cloud:9248"; // Provide actual thinClient address from the "Cluster Info" dialog.
        String username = "ignite";                                         // This is a pre-defined username provided by GG Cloud.
        String pwd = "3swnRzAncM";                                          // Provide actual cluster password from the "Cluster Info" dialog.
        String sslPwd = "ZfLNkdj4VCSbrmCgwXsf";                             // Provide actual SSL password from the "Cluster Info" dialog.

        ClassLoader classLoader = new CloudConfig().getClass().getClassLoader();

        // Provide the path where the file is stored after downloading from GG Cloud.
        String store = classLoader.getResource("keyStore.jks").getPath();

        ClientConfiguration cfg = new ClientConfiguration()
                .setAddresses(thinClient)
                .setUserName(username)
                .setUserPassword(pwd)
                .setSslMode(SslMode.REQUIRED)
                .setSslClientCertificateKeyStorePath(store)
                .setSslClientCertificateKeyStoreType("JKS")
                .setSslClientCertificateKeyStorePassword(sslPwd)
                .setSslTrustCertificateKeyStorePath(store)
                .setSslTrustCertificateKeyStoreType("JKS")
                .setSslTrustCertificateKeyStorePassword(sslPwd)
                .setSslKeyAlgorithm("SunX509")
                .setSslTrustAll(false)
                .setSslProtocol(SslProtocol.TLS);

        return cfg;
    }
}
