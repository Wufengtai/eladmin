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
package me.zhengjie.modules.order.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.goods.entity.Good;
import me.zhengjie.modules.order.entity.Order;
import me.zhengjie.modules.order.entity.vo.OrderQueryCriteria;
import me.zhengjie.modules.order.service.OrderService;
import me.zhengjie.utils.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* 订单api
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：订单管理")
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private static final String ENTITY_NAME = "order";

    @ApiOperation("导出订单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('order:list')")
    public void exportOrder(HttpServletResponse response, OrderQueryCriteria criteria) throws IOException {
        orderService.download(orderService.queryAll(criteria), response);
    }

    @ApiOperation("获取货品")
    @GetMapping(value = "/getGood/{ic}")
    public ResponseEntity<PageResult<Order>> getGoodByIc(@PathVariable("ic") String ic){
        return new ResponseEntity(orderService.getGoodByIc(ic), HttpStatus.OK);
    }

    @ApiOperation("查询订单")
    @GetMapping(value = "/{username}")
    @PreAuthorize("@el.check('order:list','user:list')")
    public ResponseEntity<PageResult<Order>> queryOrder(@PathVariable("username") String username, OrderQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(orderService.queryAll(username, criteria, page),HttpStatus.OK);
    }

    @Log("新增订单")
    @ApiOperation("新增订单")
    @PostMapping
    @PreAuthorize("@el.check('order:add')")
    public ResponseEntity<Object> createOrder(@Validated @RequestBody Order resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        orderService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("预约订单")
    @ApiOperation("预约订单")
    @PostMapping(value = "/appoint")
    public ResponseEntity<Object> appointOrder(@Validated @RequestBody Order resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        orderService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改订单")
    @ApiOperation("修改订单")
    @PutMapping
    @PreAuthorize("@el.check('order:edit')")
    public ResponseEntity<Object> updateOrder(@Validated(Order.Update.class) @RequestBody Order resources){
        orderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除订单")
    @ApiOperation("删除订单")
    @DeleteMapping
    @PreAuthorize("@el.check('order:del')")
    public ResponseEntity<Object> deleteOrder(@RequestBody Set<Long> ids){
        orderService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}