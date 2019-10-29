package com.dj.mall.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.product.ProductSkuService;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.mapper.product.ProductSkuMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>  implements ProductSkuService {



}
