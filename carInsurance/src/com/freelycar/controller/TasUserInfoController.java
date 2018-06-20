package com.freelycar.controller;

import com.freelycar.entity.Client;
import com.freelycar.entity.TasUserInfo;
import com.freelycar.service.TasUserInfoService;
import com.freelycar.util.TasConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 唐炜
 */
@RestController
@RequestMapping(value = "/tasUserInfo")
public class TasUserInfoController {
    @Autowired
    private TasUserInfoService tasUserInfoService;

    @RequestMapping(value = "/updateUserOpenIds", method = RequestMethod.GET)
    public int updateUserOpenIds() {
        return tasUserInfoService.updateUserOpenIds();
    }

    @Test
    @RequestMapping(value = "/sendMessageToUser", method = RequestMethod.GET)
    public void sendMessageToUser() {
        tasUserInfoService.sendMessageToUser();
    }
}
