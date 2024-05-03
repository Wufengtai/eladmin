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
package me.zhengjie.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.service.impl.GoodServiceImpl;
import me.zhengjie.modules.store.entity.Area;
import me.zhengjie.modules.store.entity.StoreEntity;
import me.zhengjie.modules.store.entity.vo.AreaQueryCriteria;
import me.zhengjie.modules.store.entity.vo.StoreQueryCriteria;
import me.zhengjie.modules.store.mapper.AreaMapper;
import me.zhengjie.modules.store.mapper.StoreMapper;
import me.zhengjie.modules.store.service.AreaService;
import me.zhengjie.modules.store.service.StoreService;
import me.zhengjie.modules.system.domain.Job;
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
@CacheConfig(cacheNames = "area")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    private final GoodServiceImpl goodService;
    private final AreaMapper areaMapper;
    private final RedisUtils redisUtils;
    private final UserMapper userMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public Area findById(Long id) {
        return  getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Area resources) {
        save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Area resources) {
        Area store = getById(resources.getId());
        resources.setId(store.getId());
        saveOrUpdate(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除属于该仓库的货品
        ids.forEach(item->{
            goodService.remove(new LambdaQueryWrapper<Good>().eq(Good::getStoreId, item));
        });
        // 删除缓存
        redisUtils.delByKeys(CacheKey.AREA_ID, ids);
    }

    @Override
    public PageResult<Area> queryAll(AreaQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(areaMapper.findAll(criteria, page));
    }

    @Override
    public List<Area> queryAll(AreaQueryCriteria criteria) {
        return areaMapper.findAll(criteria);
    }

    @Override
    public void download(List<Area> stores, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Area store : stores) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("区域名称", store.getName());
            map.put("仓库名称", store.getStoreName());
            map.put("创建日期", store.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
