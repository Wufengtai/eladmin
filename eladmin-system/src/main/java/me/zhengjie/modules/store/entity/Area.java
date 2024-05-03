package me.zhengjie.modules.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;

// 区域实体类
@Data
@TableName("qbs_area")
public class Area extends BaseEntity {
    @TableId(value="id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "仓库名称")
    private String name;

    @TableField(value = "store_id")
    @ApiModelProperty(value = "仓库id")
    private Long storeId;

    @TableField(value = "store_name")
    @ApiModelProperty(value = "仓库姓名")
    private String storeName;

}
