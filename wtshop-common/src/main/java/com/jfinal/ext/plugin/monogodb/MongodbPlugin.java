/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.monogodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.apache.commons.lang3.StringUtils;

public class MongodbPlugin implements IPlugin {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAUL_PORT = 27017;

    protected final Log logger = Log.getLog(getClass());

    private MongoClient client;
    private String host;
    private int port;
    private String hosts;
    private String ports;
    private String database;

    public MongodbPlugin(String database) {
        this.host = DEFAULT_HOST;
        this.port = DEFAUL_PORT;
        this.database = database;
    }

    public MongodbPlugin(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public MongodbPlugin(String hosts, String ports, String database) {
        this.hosts = hosts;
        this.ports = ports;
        this.database = database;
    }

    @Override
    public boolean start() {

        if (StringUtils.isNotBlank(hosts) && StringUtils.isNotBlank(ports)) {
            //  集群环境
            try {
                List<ServerAddress> serverAddresses = new ArrayList<>();
                String[] serverAddressHosts = hosts.split(",");
                String[] serverAddressPorts = ports.split(",");
                for (int i = 0; i < serverAddressHosts.length; i++) {
                    ServerAddress serverAddress = new ServerAddress(serverAddressHosts[i], Integer.valueOf(serverAddressPorts[i]));
                    serverAddresses.add(serverAddress);
                }
                client = new MongoClient(serverAddresses);
            } catch (UnknownHostException e) {
                throw new RuntimeException("can't connect mongodb, please check the host and port:" + host + "," + port, e);
            }

        } else {
            try {
                client = new MongoClient(host, port);
            } catch (UnknownHostException e) {
                throw new RuntimeException("can't connect mongodb, please check the host and port:" + host + "," + port, e);
            }
        }
        MongoKit.init(client, database);
        return true;
    }

    @Override
    public boolean stop() {
        if (client != null) {
            client.close();
        }
        return true;
    }

}
