package com.iori.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iori.bean.Order;
import com.iori.bean.OrderItem;
import com.iori.client.SkuFeignClient;
import com.iori.mapper.OrderMapper;
import com.iori.service.CartService;
import com.iori.service.OrderItemService;
import com.iori.service.OrderService;
import com.iori.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private SkuFeignClient skuFeignClient;

    @Transactional
    @Override
    public boolean create(OrderVo orderVo, String username) {

        //创建订单对象
        Order order = new Order();
        order.setId(IdWorker.getIdStr());
        order.setPayType(orderVo.getPayType());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setUsername(username);
        order.setBuyerMessage(orderVo.getPayType());
        order.setReceiverContact(orderVo.getContact());
        order.setReceiverMobile(orderVo.getMobile());
        order.setReceiverAddress(orderVo.getAddress());
        order.setSourceType("1");
        order.setOrderStatus("0");
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setPayType(orderVo.getPayType());


        int totalNum = 0;
        int totalMoney = 0;

        String[] ids = orderVo.getIds();
        for (String id : ids) {
            OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps("cart:" + username).get(id);
            //设置订单编号
            orderItem.setOrderId(order.getId());
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
            //修改库存
            skuFeignClient.updateNum(orderItem.getNum(),orderItem.getSkuId());
            //保存购物车信息数据库
            orderItemService.save(orderItem);
        }

        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        //将订单数据加入到数据库
        baseMapper.insert(order);

        //删除购物车选中的数据
        cartService.remove(ids,username);

        return true;
    }

}