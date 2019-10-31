package com.dj.mall.provider.task;

import com.dj.mall.api.order.OrderService;
import com.dj.mall.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ProductTask {

    @Autowired
    private OrderService orderService;

    /**
     * 定时增量
     */
    @Scheduled(cron = "0 0 0,3,6,9,12,15,18,21 * * ? ")
    public void flushIndexDelta() {
            try {
                HttpClientUtil.sendHttpRequest("http://localhost:8081/solr/slorCoreDJmallProduct/dataimport?command=delta-import", HttpClientUtil.HttpRequestMethod.GET, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    /**
     * 重做索引
     */
    @Scheduled(cron = "0 0 0,12 1/1 * ?")
    public void flushIndexFull() {
        try {
            HttpClientUtil.sendHttpRequest("http://localhost:8081/solr/slorCoreDJmallProduct/dataimport?command=full-import", HttpClientUtil.HttpRequestMethod.GET, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Scheduled(cron = "0/6 * * * * ?")
    public void test() {
        try {
            System.out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 45分钟 立即购买  加入购物车 确认订单 45 分钟  自动取消订单   每秒执行
     */
    @Scheduled(cron = "0/6 * * * * ?")
    public void calcleOrder() {
        try {
            orderService.updateStatusAndCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 15 天  已发货 自动从待收货 变成已完成  已发货  子集订单状态2  message状态是3 已发货
     */
    @Scheduled(cron = " 0 0 0 1/1 * ?")
    public void findOrderToSuccess() {
        try {
            orderService.updateFindOrderBySonNumStatusAndMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
