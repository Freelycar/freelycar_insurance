package com.freelycar.service;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.dao.ClientDao;
import com.freelycar.dao.OrderDao;
import com.freelycar.dao.TasUserInfoDao;
import com.freelycar.entity.Client;
import com.freelycar.entity.InsuranceOrder;
import com.freelycar.entity.TasUserInfo;
import com.freelycar.util.TasConfig;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 公众号用户信息表Service层
 *
 * @author 唐炜
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TasUserInfoService {
    private final static Logger log = Logger.getLogger(TasUserInfoService.class);

    /********** 注入Dao ***********/
    @Autowired
    private TasUserInfoDao tasUserInfoDao;

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private OrderDao orderDao;


    public String getTasOpenId(String unionId) {
        TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);
        if (null != tasUserInfo) {
            return tasUserInfo.getOpenId();
        }
        return null;
    }

    /**
     * 更新用户的公众号openId记录
     */
    public int updateUserOpenIds() {
        //调用微信接口查询关注了公众号的用户列表集合（集合1）
        List<String> allTasOpenIds = TasConfig.getTasUsers(null);

        //查询tasUserInfo表中所有有openId记录的openId集合（集合2）
        List<String> existingOpenIds = tasUserInfoDao.queryAllOpenIds();

        //集合1与集合2比较，得到一个新的集合（集合4）
        List<String> nonexistentOpenIds = new ArrayList<>();
        for (String tasOpenId : allTasOpenIds) {
            if (!existingOpenIds.contains(tasOpenId)) {
                nonexistentOpenIds.add(tasOpenId);
            }
        }

        //调用接口遍历获取集合4包含的openId对应的用户的全部信息，放入新集合中（集合5）
        List<JSONObject> userInfoJOs = new ArrayList<>();
        if (nonexistentOpenIds.size() > 0) {
            String accessToken = TasConfig.getTasAccessToken();
            for (String openId : nonexistentOpenIds) {
                JSONObject userInfoJO = TasConfig.getUserInfo(accessToken, openId, TasConfig.LANG_CN);
                userInfoJOs.add(userInfoJO);
            }
        }

        //遍历集合5，将openId和unionId存入数据库
        int updateCount = 0;
        for (JSONObject userInfoJO : userInfoJOs) {
            String unionId = userInfoJO.getString("unionid");
            String tasOpenId = userInfoJO.getString("openid");

            TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);
            if (null != tasUserInfo) {
                tasUserInfo.setOpenId(tasOpenId);
            }else{
                tasUserInfo = new TasUserInfo();
                tasUserInfo.setOpenId(tasOpenId);
                tasUserInfo.setUnionId(unionId);
            }
            tasUserInfoDao.saveOrUpdate(tasUserInfo);
            System.out.println(tasUserInfo);
            updateCount++;
        }


        return updateCount;
    }

    @Test
    public void sendMessageToUser() {
        int orderId = 126;
        InsuranceOrder order = orderDao.getOrderById(orderId);
        Client client = clientDao.getClientByOpenIdAndLicenseNumber(order.getOpenId(), order.getLicenseNumber());
        if (null != client) {
            String unionId = client.getUnionId();
            TasUserInfo tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);

            if (null != tasUserInfo) {
                String tasMessageSendResult = TasConfig.pushOrderUnpaidInfo(order, tasUserInfo.getOpenId());
                System.out.println(tasMessageSendResult);
            } else {
                //后面还得加一个逻辑，tasUserInfo找不到时，需要调用更新的方法去更新tasUserInfo表
                int updateCount = 0;
                if (!StringUtils.isEmpty(unionId)) {
                    updateCount = this.updateUserOpenIds();
                }
                if (updateCount == 0) {
                    System.out.println("用户没有关注公众号，无法向其推送消息！");
                } else {
                    tasUserInfo = tasUserInfoDao.getTasUserInfoByUnionId(unionId);
                    if (null != tasUserInfo) {
                        String tasMessageSendResult = TasConfig.pushOrderUnpaidInfo(order, tasUserInfo.getOpenId());
                        System.out.println(tasMessageSendResult);
                    }
                }
            }
        }
    }
}


