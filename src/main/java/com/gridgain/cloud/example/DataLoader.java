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

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.IgniteClient;

import java.util.Random;

public class DataLoader {
    public static void main(String[] args) {
        try (IgniteClient igniteClient = Ignition.startClient(CloudConfig.getCloudConfiguration())) {

            // Create sensors table
            igniteClient.query(
                    new SqlFieldsQuery(
                            "CREATE TABLE IF NOT EXISTS Sensor " +
                                    "(id INT PRIMARY KEY, " +
                                    "name VARCHAR, " +
                                    "latitude DOUBLE, " +
                                    "longitude DOUBLE)" +
                                    " WITH \"template=partitioned, backups=1\""
                    ).setSchema("PUBLIC")).getAll();

            // Create temperature table
            igniteClient.query(
                    new SqlFieldsQuery(
                            "CREATE TABLE IF NOT EXISTS Temperature " +
                                    "(sensorId INT, " +
                                    "ts TIMESTAMP, " +
                                    "temp DOUBLE, " +
                                    "PRIMARY KEY(sensorId, ts))" +
                                    " WITH \"template=partitioned, backups=1, affinity_key=sensorId\""
                    ).setSchema("PUBLIC")).getAll();

            // Pre-load Sensors table
            Random rand = new Random();

            System.out.println("Created tables");

            for (int i = 0; i < 1000; i++) {
                double lat = (rand.nextDouble() * -180.0) + 90.0;
                double lon = (rand.nextDouble() * -360.0) + 180.0;

                String name = "sensor_" + i;

                igniteClient.query(new SqlFieldsQuery(
                        "INSERT INTO Sensor(id, name, latitude,longitude) VALUES(?, ?, ?, ?)"
                ).setArgs(i, name, lat, lon).setSchema("PUBLIC")).getAll();
            }

            System.out.println("Pre-loaded sensors data");
        } catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);

            e.printStackTrace();
        }
    }
}