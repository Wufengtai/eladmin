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
package me.zhengjie.modules.goods.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.goods.entity.History;
import me.zhengjie.modules.goods.entity.vo.GoodHistoryQueryCriteria;
import me.zhengjie.modules.goods.entity.vo.GoodQueryCriteria;
import me.zhengjie.modules.goods.service.GoodHistoryService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
* 仓库api
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：仓库管理")
@RequestMapping("/api/goodHis")
public class GoodHistoryController {

    private final GoodHistoryService goodHistoryService;
    private static final String ENTITY_NAME = "goodHis";

    @ApiOperation("导出仓库数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('goodHis:list')")
    public void exportJob(HttpServletResponse response, GoodHistoryQueryCriteria criteria) throws IOException {
        goodHistoryService.download(goodHistoryService.queryAll(criteria), response);
    }

    @ApiOperation("查询仓库")
    @GetMapping
    @PreAuthorize("@el.check('goodHis:list','user:list')")
    public ResponseEntity<PageResult<History>> queryJob(GoodHistoryQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(goodHistoryService.queryAll(criteria, page),HttpStatus.OK);
    }

    @Log("新增仓库")
    @ApiOperation("新增仓库")
    @PostMapping
    @PreAuthorize("@el.check('goodHis:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody History resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        goodHistoryService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改仓库")
    @ApiOperation("修改仓库")
    @PutMapping
    @PreAuthorize("@el.check('goodHis:edit')")
    public ResponseEntity<Object> updateJob(@Validated(History.Update.class) @RequestBody History resources){
        goodHistoryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除仓库")
    @ApiOperation("删除仓库")
    @DeleteMapping
    @PreAuthorize("@el.check('goodHis:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids){
        goodHistoryService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}