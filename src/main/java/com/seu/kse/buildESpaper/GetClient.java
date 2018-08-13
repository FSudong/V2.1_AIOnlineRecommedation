package com.seu.kse.buildESpaper;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class GetClient {


    //设置集群名称为 默认名称：elasticsearch（如需修改需要更改配置文件）
    static Settings settings = Settings.builder().put("cluster.name", Configuration.ES_CLUSTER_NAME).build();

    //控制端：单例模式
    public static TransportClient transportClient;

    private GetClient(){}

    //返回 TransportClient
    public static TransportClient getTransportClient() throws UnknownHostException {
        if(transportClient == null){
            transportClient = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(Configuration.ES_HOST), 9300));
        }
        return transportClient;
    }
}
