package com.dj.mall.admin.web.product;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.api.order.OrderService;
import com.dj.mall.api.order.product.ProductService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.domain.product.vo.ProductVo;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.util.POIUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 商品列表展示
     * @param pageNo  当前页
     * @param product  传参
     * @return 返回数据
     */
    @PostMapping("list")
    public ResultModel list(Integer pageNo, Product product, HttpSession session){
        User user = (User) session.getAttribute("user1");
        product.setUserId(user.getId());
        product.setUserLever(user.getLever());
        Map<String, Object> map = new HashMap<>();
        try {
            Page<Product> page = new Page<>();
            //设置当前页
            page.setCurrent(pageNo);
            //每页条数
            page.setSize(SystemConstant.PAGESIZE_NUMBER);
            Page<Product> pageInfo = productService.findProductAll(page, product);
            //分页结果
            //查询订单表  商品对应的订单量
            List<Product> list = pageInfo.getRecords();
            for (Product p : list) {
                Integer count = productService.getOrderByProductIdCount(p.getId());
                p.setOrderNumber(count);
            }


            map.put("list", list);
            map.put("totalPage", pageInfo.getPages());
            map.put("pageNo", pageNo);

            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 添加修改去重
     * @param productName   根据 productName去重
     * @return
     */
    @RequestMapping("uniq")
    public Boolean uniq(String  productName){
        try{
            //添加对商品去重
            Product product = productService.findProductByProductNameToUnique(productName);
            if(null != product){
                return false;
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  添加商品
     * @param   file  图片
     * @param
     * @return
     */
    @RequestMapping("add")
    public ResultModel add(MultipartFile file, ProductVo productVo, HttpSession session){
             User user = (User) session.getAttribute("user1");
        try {
            Product product = new Product();
            BeanUtils.copyProperties(productVo, product);
            //上传图片  dubbo 不支持文件上传  可以换成hessian 协议  或者将file file.getBytes(); 到service
            byte[] bytes = file.getBytes();
            //赋值 用户等级 用户id
            product.setUserId(user.getId());
            product.setUserLever(user.getLever());
            product.setCreateTime(new Date());
            //添加
            productService.insertProductToAdd(bytes,product);
            //增量索引
            Integer code = productService.makeSolr("delta");
            if(code == SystemConstant.SUCCESS_CODE ){
                return new ResultModel().success("success flush");
            }
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }





    /**
     * 修改上架 下架
     * @param id  根据id修改
     * @return
     */
    @PostMapping("status/{id}")
    public ResultModel status(@PathVariable("id") Integer id){
        try {
            //查询状态是否  1 上架  2 下架
            Product product = productService.findProductById(id);
            if(product.getProductStatus() == SystemConstant.LEVER_NUMBER_ONE){
                product.setProductStatus(SystemConstant.STATUS_NUMBER_TWO);
            } else {
                product.setProductStatus(SystemConstant.LEVER_NUMBER_ONE);
            }
            //修改
            productService.updateProductStatusById(product);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 商品修改
     * @param file  图片
     * @param productVo  传递参数
     * @return
     */
    @RequestMapping("update")
    public ResultModel update(MultipartFile file, ProductVo productVo){
        try {
            Product product = new Product();
            BeanUtils.copyProperties(productVo, product);
            //将file转成流的形式 传到serviceImpl层
            byte[] bytes = null;
            if(!file.isEmpty()){
                 bytes = file.getBytes();
            }
            //修改
            productService.updateProduct(bytes, product);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     *  商品对应 添加属性 属性值  展示列表
     * @param productCode  通过商品code查询
     * @return
     */
    @RequestMapping("attrList/{productCode}")
    public ResultModel attrList(@PathVariable("productCode") String  productCode){
             Map<String, Object> map = new HashMap<>();
        try {
            List<ProductSkuGm> proAttValueList = productService.findProductAttrValueByProductCode(productCode);
            for (ProductSkuGm productSkuGm : proAttValueList) {
                productSkuGm.setAttrValueShowArr(productSkuGm.getAttrValueShow().split(","));
                productSkuGm.setAttrValueIdArr(productSkuGm.getAttrValueId().split(","));
            }
            map.put("proAttValueList", proAttValueList);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 修改sku下架
     * @param skuStatus  根据id 修改状态  前台传来的状态
     * @return
     */
    @RequestMapping("updateStatus/{id}")
    public ResultModel updateStatus(@PathVariable("id") Integer id, Integer skuStatus){
        try {
            //判断是否已经为默认  默认的不能下架  1 代表默认  0 代表否
            ProductSku productSku = productService.toUpdateById(id);
            if(productSku.getIsDefault().equals(SystemConstant.STRING_ONE)){
                return new ResultModel().error("默认不能设置为下架");
            }
            productService.updateProductSkuStatus(id, skuStatus);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     *
     */

    /**
     * 设置为默认  将之前的默认修改为否
     * @param skuDefault 前台传来的参数  修改   id 根据id修改
     * @return
     */
    @RequestMapping("updateDefault/{id}")
    public ResultModel updateDefault(@PathVariable("id") Integer id, String  skuDefault){
        try {
            //判断是否已经为默认
            ProductSku productSku = productService.toUpdateById(id);
            if(productSku.getIsDefault().equals(SystemConstant.STRING_ONE)){
                return new ResultModel().error("已经被设置为默认");
            }
            //0  是下架   下架不能设置为默认
            if(productSku.getSkuStatus() == SystemConstant.NUMBER_ZERO){
                return new ResultModel().error("下架不能设置为默认");
            }

            //修改默认 将之前默认的一条数据改为否  通过 sku   的id 进行查询   1 为默认  0 否
            ProductSku productSku1 = productService.updateIsDefaultAndToNotDefault(productSku.getProductId(), "1");
            if( null == productSku1){
                return new ResultModel().error("没有已经默认的该条信息");
            }
            //数据封装   批量修改 list
            List<ProductSku> list = new ArrayList<>();
            //将之前的默认改为否
            ProductSku productSku2 = new ProductSku();
            productSku2.setId(productSku1.getId());
            productSku2.setIsDefault(SystemConstant.STRING_ZERO);
            //修改为默认
            ProductSku productSku3 = new ProductSku();
            productSku3.setId(id);
            productSku3.setIsDefault(skuDefault);
            list.add(productSku2);
            list.add(productSku3);
            //将之前的默认改为否    修改为默认
            productService.updateProductSkuIsDefault(list);
            //将之前的默认改为否
           // productService.updateProductSkuIsDefault(productSku1.getId(), "0");
            //修改为默认
          //  productService.updateProductSkuIsDefault(id, skuDefault);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 修改库存
     * @return
     */
    @RequestMapping("updateCount")
    public ResultModel updateCount(ProductSku productSku){
        try {
            productService.updateProductSkuCount(productSku);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }






    /**
     * 管理员可见  刷新索引
     * @return
     */
    @RequestMapping("makeSolrNew")
    public ResultModel makeSolrNew(){
        try {
            //新建索引
            Integer code = productService.makeSolr("full");
            if( code == 200){ //SystemConstant.SUCCESS_CODE
                return new ResultModel().success("刷新成功");
            }
            return new ResultModel().error("刷新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



    /**
     * 评论展示页面    //页面传来1 2 3 4
     *  1 所有评论  2 好评  3 中评 4 差评
     * @return
     */
    @RequestMapping("commonList")
    public ResultModel commonList(Common common, Integer pageNo){
        Map<String, Object> map = new HashMap<>();
        try {
            Page<Common> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
            Page<Common> pageInfo = productService.findCommonByProductId(page, common);
            List<Common> commonList = pageInfo.getRecords();

            //查询评论 根据product id 查询
            for (Common common1 : commonList) {
                //字符串表示 时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                common1.setCreateTimeShow(df.format(common1.getCreateTime()));
                List<Reply> replyList = common1.getReplyList();
                for (Reply reply : replyList) {
                    reply.setCreateTimeShow(df.format(reply.getCreateTime()));
                }
            }
            map.put("list", commonList);
            map.put("pageNo", pageInfo.getCurrent());
            map.put("totalPage", pageInfo.getPages());

            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 商家去回复    评论id  回复内容               回复类型  2
     * @return
     */
    @RequestMapping("reply")
    public ResultModel reply(Reply reply){
        try {
            reply.setCreateTime(new Date());
            //保存
            productService.insertReply(reply);
            return new ResultModel().success("回复成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }




    /**
     * 用户下载 导入ex工作表模板
     * @return
     */
    @GetMapping("uploadTemp")
    public void uploadTemp(HttpServletResponse response) {
        try {
//
            //创建工作表
            Workbook workbook = new XSSFWorkbook();
            //创建工作表
            Sheet sheet = workbook.createSheet();
            //设置表头
            Row row = sheet.createRow(SystemConstant.ZERO);
            row.createCell(SystemConstant.ZERO).setCellValue("商品名称名称");
            row.createCell(SystemConstant.ONE).setCellValue("邮费");
            row.createCell(SystemConstant.TWO).setCellValue("描述");
            row.createCell(SystemConstant.THREE).setCellValue("商品类型名称");
            row.createCell(SystemConstant.FOUR).setCellValue("商品Sku属性名");
            row.createCell(SystemConstant.FIVE).setCellValue("商品Sku属性值(多个属性值用逗号分开)");
            row.createCell(SystemConstant.SIX).setCellValue("商品Sku属性");
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("商品导入模板表.xlsx").getBytes(), "iso-8859-1"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 导入商品表
     * @param file
     * @param response
     * @return
     */
    @PostMapping("importPro")
    public ResultModel<Object> importPro(MultipartFile file, HttpServletResponse response, HttpSession session){
        try {
            if (file.isEmpty()) {
                return new ResultModel().error("导入文件不能为空");
            }
            //从session中获取当前登录的用户类型
            User user = (User) session.getAttribute("user1");
            //获取用户ID
            Integer userId = user.getId();
            //获取输入流
            InputStream inputStream = file.getInputStream();
            //解析excel 并打开表
            Workbook workbook = POIUtil.getExcelWorkBook(inputStream, file.getOriginalFilename());
            List<Product> productList = new ArrayList<>();
            //获取sheet
            Sheet sheet = workbook.getSheetAt(SystemConstant.ZERO);
            //用 sheet总行数 遍历
            for (int j = SystemConstant.ONE; j < sheet.getPhysicalNumberOfRows(); j++) {
                //跳过表头
                Row row = sheet.getRow(j);
                //组装商品信息
                Product product = new Product();
                product.setProductName(POIUtil.getStringCellValue(row.getCell(SystemConstant.ZERO)));
                product.setFreightId(SystemConstant.FIVE);
                product.setProductDescribe(POIUtil.getStringCellValue(row.getCell(SystemConstant.TWO)));
                product.setDictionaryCode(POIUtil.getStringCellValue(row.getCell(SystemConstant.THREE)));
                product.setUserLever(SystemConstant.ONE);
                product.setCreateTime(new Date());
                product.setUserId(userId);
                //放入集合
                productList.add(product);
            }
            if (productService.addProAll(productList)) {
                return new ResultModel().success("success");
            }
            return new ResultModel().error("导入失败");
        } catch (Exception e){
            e.printStackTrace();
            return new ResultModel().error("导入失败" + e.getMessage());
        }
    }




    /**
     * 导出商品订单
     * @return
     */
    @GetMapping("exportOrder")
    public void exportOrder(HttpSession session, HttpServletResponse response) {
        try {
            //从session中获取当前登录的用户类型
            User user = (User) session.getAttribute("user1");
            //获取用户ID
            Integer userId = user.getId();
            //获取用户类型
            Integer lever = user.getLever();
            //创建工作表
            Workbook workbook = new XSSFWorkbook();
            //创建工作表
            Sheet sheet = workbook.createSheet();
            //设置表头
            Row row = sheet.createRow(SystemConstant.ZERO);
            row.createCell(SystemConstant.ZERO).setCellValue("订单号");
            row.createCell(SystemConstant.ONE).setCellValue("商品名称");
            row.createCell(SystemConstant.TWO).setCellValue("购买数量");
            row.createCell(SystemConstant.THREE).setCellValue("折扣");
            row.createCell(SystemConstant.FOUR).setCellValue("付款金额");
            row.createCell(SystemConstant.FIVE).setCellValue("支付方式");
            row.createCell(SystemConstant.SIX).setCellValue("邮费");
            row.createCell(SystemConstant.SEVEN).setCellValue("收货人信息");
            row.createCell(SystemConstant.EIRHT).setCellValue("下单人");
            row.createCell(SystemConstant.NINE).setCellValue("下单人电话");
            row.createCell(SystemConstant.TEN).setCellValue("下单时间");
            row.createCell(SystemConstant.ELEVEN).setCellValue("付款时间");
            row.createCell(SystemConstant.TWELVE).setCellValue("订单状态");
            //查询要导入的数据集合
            List<Order> orderList = userService.findOrderAll(lever, userId);
            for (int i = SystemConstant.ZERO; i < orderList.size(); i++) {
                //设置表头
                Row row1 = sheet.createRow(i + SystemConstant.ONE);
                row1.createCell(SystemConstant.ZERO).setCellValue(orderList.get(i).getOrderSonNum());
                row1.createCell(SystemConstant.ONE).setCellValue(orderList.get(i).getProductName() + ":" + orderList.get(i).getSkuAttrName());
                row1.createCell(SystemConstant.TWO).setCellValue(orderList.get(i).getProNum());
                row1.createCell(SystemConstant.THREE).setCellValue(orderList.get(i).getRate().equals(SystemConstant.ZERO) ? "无" : orderList.get(i).getRate() + "%");
                row1.createCell(SystemConstant.FOUR).setCellValue(orderList.get(i).getTotalMoney().toString());
                row1.createCell(SystemConstant.FIVE).setCellValue(orderList.get(i).getPayStatus());
                row1.createCell(SystemConstant.SIX).setCellValue(orderList.get(i).getFreightMoney().equals(SystemConstant.ZERO) ? "包邮" : "平邮, " + orderList.get(i).getFreightMoney());
                row1.createCell(SystemConstant.SEVEN).setCellValue(orderList.get(i).getAddress());
                row1.createCell(SystemConstant.EIRHT).setCellValue(orderList.get(i).getUsername());
                row1.createCell(SystemConstant.NINE).setCellValue(orderList.get(i).getPhone());
                row1.createCell(SystemConstant.ELEVEN).setCellValue(orderList.get(i).getDeliveryTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH：mm：ss").format(orderList.get(i).getDeliveryTime()));
                row1.createCell(SystemConstant.TEN).setCellValue(orderList.get(i).getPayTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH：mm：ss").format(orderList.get(i).getPayTime()));
                String status = "";
                if (orderList.get(i).getOrderSonStatus() == SystemConstant.FOUR) {
                    status = "已取消";
                } else if (orderList.get(i).getOrderSonStatus() == SystemConstant.ONE){
                    status = "待付款";
                } else if (orderList.get(i).getOrderSonStatus() == SystemConstant.TWO) {
                    status = "待收货";
                } else {
                    status = "已完成";
                }
                row1.createCell(SystemConstant.TWELVE).setCellValue(status);
            }
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("订单统计表.xlsx").getBytes(), "iso-8859-1"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 待发货
     * 弹框消息提醒
     * @param userId
     * @param message
     * @return
     */
    @RequestMapping("findOrderAdmin")
    public ResultModel<Object> findOrderAdmin(Integer userId, Integer[] message, Integer orderSonStatus){
        try {
            List<Order> orderList = orderService.findOrderAdmin(userId, message, orderSonStatus);
            if (orderList.size() != SystemConstant.NUMBER_ZERO) {
                return new ResultModel<>().success("OK");
            }
            return new ResultModel<>().success(SystemConstant.NUMBER_ONE_HOUNDER, "OK");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error(e.getMessage());
        }
    }

}
