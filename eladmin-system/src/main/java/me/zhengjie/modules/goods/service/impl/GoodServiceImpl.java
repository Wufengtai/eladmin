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
package me.zhengjie.modules.goods.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.entity.History;
import me.zhengjie.modules.goods.entity.vo.GoodQueryCriteria;
import me.zhengjie.modules.goods.mapper.GoodHistoryMapper;
import me.zhengjie.modules.goods.mapper.GoodMapper;
import me.zhengjie.modules.goods.service.GoodHistoryService;
import me.zhengjie.modules.goods.service.GoodService;
import me.zhengjie.modules.system.mapper.UserMapper;
import me.zhengjie.utils.*;
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
@CacheConfig(cacheNames = "good")
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements GoodService {

    private final GoodHistoryService goodHistoryService;
    private final GoodMapper goodMapper;
    private final RedisUtils redisUtils;
    private final UserMapper userMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public Good findById(Long id) {
        return  getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Good resources) {
        // 新增货品同时创建入库记录
        save(resources);
        History history = new History();
        history.setStoreId(resources.getStoreId());
        history.setStoreName(resources.getStoreName());
        history.setGoodId(resources.getId());
        history.setGoodName(resources.getName());
        history.setUserId(resources.getUserId());
        history.setUserName(resources.getUserName());
        history.setNum(resources.getNum());
        history.setAreaId(resources.getAreaId());
        history.setAreaName(resources.getAreaName());
        history.setType(0);  // 新增的默认入库
        goodHistoryService.create(history);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Good resources) {
        Good store = getById(resources.getId());
        resources.setId(store.getId());
        saveOrUpdate(resources);
        History history = new History();
        history.setStoreId(resources.getStoreId());
        history.setStoreName(resources.getStoreName());
        history.setGoodId(resources.getId());
        history.setGoodName(resources.getName());
        history.setUserId(resources.getUserId());
        history.setUserName(resources.getUserName());
        history.setAreaId(resources.getAreaId());
        history.setAreaName(resources.getAreaName());
        // 如果数量变更则响应的出入库记录
        if(store.getNum() > resources.getNum()){
            //出库
            history.setNum(store.getNum() - resources.getNum());
            history.setType(1);  // 新增的默认入库
            goodHistoryService.create(history);
        }else if(store.getNum() < resources.getNum()){
            //入库
            history.setNum(resources.getNum() - store.getNum());
            history.setType(0);  // 新增的默认入库
            goodHistoryService.create(history);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.Good_ID, ids);
    }

    @Override
    public PageResult<Good> queryAll(GoodQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(goodMapper.findAll(criteria, page));
    }

    @Override
    public List<Good> queryAll(GoodQueryCriteria criteria) {
        return goodMapper.findAll(criteria);
    }

    @Override
    public void download(List<Good> goods, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Good good : goods) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("货品名称", good.getName());
            map.put("仓库名称", good.getStoreName());
            map.put("区域名称", good.getAreaName());
            map.put("用户名称", good.getUserName());
            map.put("数量", good.getNum());
            map.put("价格", good.getPrice());
            map.put("状态", good.getStatus());
            map.put("预警数", good.getWarningNum());
            map.put("创建日期", good.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
