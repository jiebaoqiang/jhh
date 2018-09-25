package com.jhh.dc.baika.manage.service.impl;

import com.jhh.dc.baika.manage.service.common.ExportConcurrentService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Create by Jxl on 2017/9/25
 */
@Service
public class ExportConcurrentServiceImpl implements ExportConcurrentService {
    private static BlockingQueue<Token> blockingQueue = new LinkedBlockingQueue<>();
    private static String token = "EXPORT_ABLE";
    private long fill_interval = 3 * 60 * 1000;
    private long last_fill_time;
    private int fill_volume = 5;

    private static final Logger logger = LogManager.getLogger(ExportConcurrentServiceImpl.class);

    @PostConstruct
    public void postC() {
        fill2Full();
    }

    @Override
    public boolean getExportToken() {
        Token exportToken = blockingQueue.poll();
        if (exportToken != null) {
            return true;
        }
        tryFillQueue();
        exportToken = blockingQueue.poll();
        return exportToken != null;
    }

    @Override
    public String setFillVolume(int volume) {
        this.fill_volume = volume;
        return "设置导出令牌容器大小为:" + volume;
    }

    @Override
    public String setFillInterval(long interval) {
        this.fill_interval = interval;
        return "设置导出令牌容器填满间隔为:" + interval + "(毫秒!)";
    }

    private void tryFillQueue() {
        synchronized (blockingQueue) {
            if (System.currentTimeMillis() > (fill_interval + last_fill_time)) {
                fill2Full();
            }
        }
    }

    private void fill2Full() {
        for (int i = 0; i < fill_volume; i++) {
            blockingQueue.add(new Token(token));
        }
        last_fill_time = System.currentTimeMillis();
    }


    class Token {
        public Token(String token) {
            super();
            this.token = token;
        }

        String token;
    }


}
