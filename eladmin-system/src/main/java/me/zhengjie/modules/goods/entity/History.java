package me.zhengjie.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;

// 货品出入库记录实体类
@Data
@TableName("qbs_goods_history")
public class History extends BaseEntity {
    @TableId(value="id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField(value = "store_id")
    @ApiModelProperty(value = "仓库id")
    private Long storeId;

    @TableField(value = "store_name")
    @ApiModelProperty(value = "仓库姓名")
    private String storeName;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @TableField(value = "area_id")
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    @TableField(value = "area_name")
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @TableField(value = "good_id")
    @ApiModelProperty(value = "货品id")
    private Long goodId;

    @TableField(value = "good_name")
    @ApiModelProperty(value = "货品姓名")
    private String goodName;

    @NotBlank
    @ApiModelProperty(value = "出入库类型")
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "货品数量")
    private Integer num;

}
