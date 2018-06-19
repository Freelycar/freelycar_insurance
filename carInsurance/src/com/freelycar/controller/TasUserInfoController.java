package com.freelycar.controller;

import com.freelycar.service.TasUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
