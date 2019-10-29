package com.dj.mall.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSkuMapper extends BaseMapper<ProductSku> {



}
