package me.zhengjie.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;

// 仓库实体类
@Data
@TableName("qbs_goods")
public class Good extends BaseEntity {
    @TableId(value="id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "货品名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "货品类型")
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "货品状态")
    private Boolean status;

    @NotBlank
    @ApiModelProperty(value = "单价")
    private Integer price;

    @NotBlank
    @ApiModelProperty(value = "规格")
    private String specifications;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @TableField(value = "store_id")
    @ApiModelProperty(value = "仓库编号")
    private Long storeId;

    @TableField(value = "store_name")
    @ApiModelProperty(value = "仓库名称")
    private String storeName;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @TableField(value = "area_id")
    @ApiModelProperty(value = "区域编号")
    private Long areaId;

    @TableField(value = "area_name")
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @TableField(value = "warning_num")
    @ApiModelProperty(value = "预警数")
    private Integer warningNum;

}
