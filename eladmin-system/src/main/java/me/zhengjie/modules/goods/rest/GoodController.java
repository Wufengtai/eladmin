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
import me.zhengjie.modules.goods.service.GoodService;
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
* 区域api
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：货品管理")
@RequestMapping("/api/good")
public class GoodController {

    private final GoodService goodService;
    private static final String ENTITY_NAME = "good";

    @ApiOperation("导出货品数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('good:list')")
    public void exportGood(HttpServletResponse response, GoodQueryCriteria criteria) throws IOException {
        goodService.download(goodService.queryAll(criteria), response);
    }

    @ApiOperation("查询货品")
    @GetMapping
    @PreAuthorize("@el.check('good:list','user:list')")
    public ResponseEntity<PageResult<Good>> queryGood(GoodQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(goodService.queryAll(criteria, page),HttpStatus.OK);
    }

    @Log("新增货品")
    @ApiOperation("新增货品")
    @PostMapping
    @PreAuthorize("@el.check('good:add')")
    public ResponseEntity<Object> createGood(@Validated @RequestBody Good resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        goodService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改货品")
    @ApiOperation("修改货品")
    @PutMapping
    @PreAuthorize("@el.check('good:edit')")
    public ResponseEntity<Object> updateGood(@Validated(Good.Update.class) @RequestBody Good resources){
        Boolean status= goodService.findById(resources.getId()).getStatus();
        if(status && resources.getStatus()){
            throw new BadRequestException("请先下架 "+ resources.getName() +" 货品再进行修改");
        }
        goodService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除货品")
    @ApiOperation("删除货品")
    @DeleteMapping
    @PreAuthorize("@el.check('good:del')")
    public ResponseEntity<Object> deleteGood(@RequestBody Set<Long> ids){
        goodService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}