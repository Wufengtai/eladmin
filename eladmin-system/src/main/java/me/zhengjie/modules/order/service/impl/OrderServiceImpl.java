/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.service.impl.GoodServiceImpl;
import me.zhengjie.modules.order.entity.Order;
import me.zhengjie.modules.order.entity.vo.OrderQueryCriteria;
import me.zhengjie.modules.order.mapper.OrderMapper;
import me.zhengjie.modules.order.service.OrderService;
import me.zhengjie.modules.store.service.impl.AreaServiceImpl;
import me.zhengjie.modules.system.mapper.UserMapper;
import me.zhengjie.utils.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 仓库功能实现类
 **/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "store")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final GoodServiceImpl goodService;
    private final AreaServiceImpl areaService;
    private final OrderMapper orderMapper;
    private final RedisUtils redisUtils;
    private final UserMapper userMapper;

    @Override
    public List<Order> getGoodByIc(String ic) {
        List<Order> orders = orderMapper.getGoodByIc(ic);
        orders.forEach(item->{
            item.setProgress(0);
            orderMapper.updateById(item);
        });
        return orders;
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public Order findById(Long id) {
        return  getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Order resources) {
        save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        Order store = getById(resources.getId());
        resources.setId(store.getId());
        saveOrUpdate(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.ORDER_ID, ids);
    }

    @Override
    public PageResult<Order> queryAll(String username, OrderQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(orderMapper.findAll(username, criteria, page));
    }

    @Override
    public List<Order> queryAll(OrderQueryCriteria criteria) {
        return orderMapper.findAll(criteria);
    }

    @Override
    public void download(List<Order> orders, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Order order : orders) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", order.getId());
            map.put("订单进度", order.getProgress());
            map.put("地址", order.getAddress());
            map.put("数量", order.getNum());
            map.put("是否锁单", order.getStatus());
            map.put("价格", order.getPrice());
            map.put("联系方式", order.getPhone());
            map.put("用户名称", order.getUserName());
            map.put("仓库名称", order.getStoreName());
            map.put("区域名称", order.getAreaName());
            map.put("货品名称", order.getGoodName());
            map.put("创建日期", order.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
