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
package me.zhengjie.modules.store.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.store.entity.StoreEntity;
import me.zhengjie.modules.store.entity.vo.StoreQueryCriteria;
import me.zhengjie.modules.store.service.StoreService;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.domain.vo.JobQueryCriteria;
import me.zhengjie.modules.system.service.JobService;
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
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;
    private static final String ENTITY_NAME = "store";

    @ApiOperation("导出仓库数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('store:list')")
    public void exportJob(HttpServletResponse response, StoreQueryCriteria criteria) throws IOException {
        storeService.download(storeService.queryAll(criteria), response);
    }

    @ApiOperation("查询仓库")
    @GetMapping
    @PreAuthorize("@el.check('store:list','user:list')")
    public ResponseEntity<PageResult<StoreEntity>> queryJob(StoreQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(storeService.queryAll(criteria, page),HttpStatus.OK);
    }

    @Log("新增仓库")
    @ApiOperation("新增仓库")
    @PostMapping
    @PreAuthorize("@el.check('store:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody StoreEntity resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        storeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改仓库")
    @ApiOperation("修改仓库")
    @PutMapping
    @PreAuthorize("@el.check('store:edit')")
    public ResponseEntity<Object> updateJob(@Validated(StoreEntity.Update.class) @RequestBody StoreEntity resources){
        storeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除仓库")
    @ApiOperation("删除仓库")
    @DeleteMapping
    @PreAuthorize("@el.check('store:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids){
        storeService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}