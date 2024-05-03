package me.zhengjie.modules.store.entity;

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
@TableName("qbs_store")
public class StoreEntity extends BaseEntity {
    @TableId(value="id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "仓库名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "仓库地址")
    private String address;

    @NotBlank
    @ApiModelProperty(value = "仓库联系")
    private String phone;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @TableField(value = "type")
    @ApiModelProperty(value = "仓库类型")
    private Integer type;

    @TableField(value = "Police_info")
    @ApiModelProperty(value = "仓库警情")
    private Boolean policeInfo;



}
