package com.freelycar.controller;

import com.freelycar.entity.Reciver;
import com.freelycar.service.ReciverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping(value = "/reciver")
public class ReciverController {
    /********** 注入ReciverService ***********/
    @Autowired
    private ReciverService reciverService;


    //增加一个Reciver
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> saveReciver(@RequestBody Reciver reciver) {
        return reciverService.saveReciver(reciver);
    }

    @RequestMapping(value = "/getReciverByOpenId", method = RequestMethod.GET)
    public Map<String, Object> getReciverByOpenId(String openId) {
        return reciverService.getReciverByOpenId(openId);
    }


    //查询所有的Reciver
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> listReciver(Reciver reciver, int page, int number) {
        return reciverService.listReciver(reciver, page, number);
    }

    //根据id删除Reciver
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public Map<String, Object> removeReciverById(String id) {
        return reciverService.removeReciverById(id);
    }

    //更新Reciver
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateReciver(@RequestBody Reciver reciver) {
        return reciverService.updateReciver(reciver);
    }

    /**
     * 根据orderId查询Reciver
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/getReciverByOrderId", method = RequestMethod.GET)
    public Reciver getReciverByOrderId(@RequestParam(value = "orderId") String orderId) {
        return reciverService.getReciverByOrderId(orderId);
    }

}