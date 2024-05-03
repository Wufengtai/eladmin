package me.zhengjie.modules.store.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.store.entity.Area;
import me.zhengjie.modules.store.entity.vo.AreaQueryCriteria;
import me.zhengjie.modules.store.service.AreaService;
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
@Api(tags = "系统：仓库管理")
@RequestMapping("/api/area")
public class AreaController {

    private final AreaService areaService;
    private static final String ENTITY_NAME = "area";

    @ApiOperation("导出仓库数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('area:list')")
    public void exportJob(HttpServletResponse response, AreaQueryCriteria criteria) throws IOException {
        areaService.download(areaService.queryAll(criteria), response);
    }

    @ApiOperation("查询仓库")
    @GetMapping
    @PreAuthorize("@el.check('area:list','user:list')")
    public ResponseEntity<PageResult<Area>> queryJob(AreaQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(areaService.queryAll(criteria, page),HttpStatus.OK);
    }

    @Log("新增仓库")
    @ApiOperation("新增仓库")
    @PostMapping
    @PreAuthorize("@el.check('area:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody Area resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        areaService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改仓库")
    @ApiOperation("修改仓库")
    @PutMapping
    @PreAuthorize("@el.check('area:edit')")
    public ResponseEntity<Object> updateJob(@Validated(Area.Update.class) @RequestBody Area resources){
        areaService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除仓库")
    @ApiOperation("删除仓库")
    @DeleteMapping
    @PreAuthorize("@el.check('area:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids){
        areaService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}