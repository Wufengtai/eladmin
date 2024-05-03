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
package me.zhengjie.modules.goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.entity.History;
import me.zhengjie.modules.goods.entity.vo.GoodHistoryQueryCriteria;
import me.zhengjie.modules.goods.entity.vo.GoodQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 数据权限服务类
 */
public interface GoodHistoryService {

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    History findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(History resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(History resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<History> queryAll(GoodHistoryQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部数据
     * @param criteria /
     * @return /
     */
    List<History> queryAll(GoodHistoryQueryCriteria criteria);

    /**
     * 导出数据
     * @param stores 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<History> stores, HttpServletResponse response) throws IOException;

}
