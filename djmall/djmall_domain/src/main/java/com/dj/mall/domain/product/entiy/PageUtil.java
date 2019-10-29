package com.dj.mall.domain.product.entiy;

import java.io.Serializable;
import java.util.List;

public class PageUtil implements Serializable {

	/** 当前页*/
	private Long pageNo = 1L;
	
	/** 每页条数*/
	private Long pageSize = 2L;
	
	/** 总页数*/
	private Long totalPageNo;
	
	/** 总条数*/
	private Long totalPageNum;

	/** 返回数据*/
	private List<?> list;

	/** 开始位置*/
	/*private Integer start;*/

	public Long getPageNo() {
		return pageNo;
	}

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalPageNo() {
		return totalPageNo;
	}

	public void setTotalPageNo(Long totalPageNo) {
		this.totalPageNo = totalPageNo;
	}

	public Long getTotalPageNum() {
		return totalPageNum;
	}

	/**
	 * 设置总条数
	 * @param totalPageNum
	 */
	public void setTotalPageNum(Long totalPageNum) {
		this.totalPageNum = totalPageNum;
		totalPageNum = totalPageNum == null ? 0 : totalPageNum;
		pageSize = pageSize == 0 ? 10 : pageSize;
		// 总条数
		this.totalPageNo = (long) Math.ceil(totalPageNum * 1.0 / pageSize * 1.0);
	}

	/**
	 * 开始位置
	 * @return
	 */
	public Long getStart() {
		// 开始位置= (当前页 - 1) * 每页条数
		return (this.pageNo - 1) * this.pageSize;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
