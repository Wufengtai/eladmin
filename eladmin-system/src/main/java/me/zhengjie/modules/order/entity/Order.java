package me.zhengjie.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;

// 订单实体类
@Data
@TableName("qbs_order")
public class Order extends BaseEntity {
    @TableId(value="id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "数量")
    private Integer num;

    @NotBlank
    @ApiModelProperty(value = "总价格")
    private Integer price;

    @NotBlank
    @ApiModelProperty(value = "货品状态")
    private Boolean status;

    @NotBlank
    @ApiModelProperty(value = "联系")
    private String phone;

    @NotBlank
    @ApiModelProperty(value = "地址")
    private String address;

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
    @ApiModelProperty(value = "货品名称")
    private String goodName;

    @TableField(value = "progress")
    @ApiModelProperty(value = "进度")
    private Integer progress;

}
