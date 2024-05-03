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
package me.zhengjie.modules.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.entity.History;
import me.zhengjie.modules.goods.entity.vo.GoodHistoryQueryCriteria;
import me.zhengjie.modules.goods.entity.vo.GoodQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* 仓库mapper
*/
@Mapper
public interface GoodHistoryMapper extends BaseMapper<History> {

    List<History> findAll(@Param("criteria") GoodHistoryQueryCriteria criteria);

    IPage<History> findAll(@Param("criteria") GoodHistoryQueryCriteria criteria, Page<Object> page);

    @Select("select id from qbs_goods_history where name = #{name}")
    History findByName(@Param("name") String name);
}