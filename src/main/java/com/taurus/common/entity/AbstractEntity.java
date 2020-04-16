package com.taurus.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taurus.api.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 实体/表 基类
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fdId;

	/**
	 * 1:启用 0:禁用
	 */
	@Column(columnDefinition = "int(2)", nullable = false)
	@JsonIgnore
	private StatusEnum fdStatus;

	@Column(length = 256)
	private String fdRemark;

	@Column(name = "create_by", length = 8)
	private String fdCreateBy;

	@Column(name = "create_time")
	private Date fdCreateTime;

	@Column(name = "update_by", length = 8)
	private String fdUpdateBy;

	@Column(name = "update_time")
	private Date fdUpdateTime;

}
