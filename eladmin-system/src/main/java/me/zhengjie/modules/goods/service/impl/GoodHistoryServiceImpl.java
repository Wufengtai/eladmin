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
import me.zhengjie.modules.goods.entity.vo.GoodHistoryQueryCriteria;
import me.zhengjie.modules.goods.entity.vo.GoodQueryCriteria;
import me.zhengjie.modules.goods.mapper.GoodHistoryMapper;
import me.zhengjie.modules.goods.service.GoodHistoryService;
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
@CacheConfig(cacheNames = "goodHis")
public class GoodHistoryServiceImpl extends ServiceImpl<GoodHistoryMapper, History> implements GoodHistoryService {

    private final GoodHistoryMapper goodHistoryMapper;
    private final RedisUtils redisUtils;
    private final UserMapper userMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public History findById(Long id) {
        return  getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(History resources) {
        save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(History resources) {
        History store = getById(resources.getId());
        resources.setId(store.getId());
        saveOrUpdate(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.HISTORY_ID, ids);
    }

    @Override
    public PageResult<History> queryAll(GoodHistoryQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(goodHistoryMapper.findAll(criteria, page));
    }

    @Override
    public List<History> queryAll(GoodHistoryQueryCriteria criteria) {
        return goodHistoryMapper.findAll(criteria);
    }

    @Override
    public void download(List<History> stores, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (History store : stores) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("仓库名称", store.getStoreName());
            map.put("出入库类型", store.getType());
            map.put("货品名称", store.getGoodName());
            map.put("用户名称", store.getUserName());
            map.put("数量", store.getNum());
            map.put("创建日期", store.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
