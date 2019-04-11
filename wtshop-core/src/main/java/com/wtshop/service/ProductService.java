package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.ProductDao;
import com.wtshop.dao.StockLogDao;
import com.wtshop.model.Admin;
import com.wtshop.model.Goods;
import com.wtshop.model.Product;
import com.wtshop.model.StockLog;
import com.wtshop.util.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.nlpcn.commons.lang.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Service - 商品
 * 
 * 
 */
public class ProductService extends BaseService<Product> {

	/**
	 * 构造方法
	 */
	public ProductService() {
		super(Product.class);
	}
	
	private ProductDao productDao = Enhancer.enhance(ProductDao.class);
	private StockLogDao stockLogDao = Enhancer.enhance(StockLogDao.class);
	private InformationService informationService = Enhancer.enhance(InformationService.class);

	/**
	 * 判断最后一件商品是否是满减
	 */
	public Product findByCartId(Long cartId){
		return productDao.findByCartId(cartId);
	}

	/**
	 * 根据商品goodsId获取规格product
	 */
	public List<Product> findProductList(Long id){
		return productDao.findProductList(id);
	}

	/**
	 * 判断编号是否存在
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	public boolean snExists(String sn) {
		return productDao.snExists(sn);
	}

	/**
	 * 根据福袋号获取商品 fudaiproductId
	 */
	public Product findProductByFudaiId(Long id){
		return productDao.findProductByFudaiId(id);
	}


	/**
	 * 根据编号查找商品
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	public Product findBySn(String sn) {
		return productDao.findBySn(sn);
	}
	
	/**
	 * 根据货品查找商品
	 * 
	 * @param goodsId
	 * @return 商品，若不存在则返回null
	 */
	public Product findByGoodsId(Long goodsId) {
		return productDao.findByGoodsId(goodsId);
	}

	/**
	 * 通过编号、名称查找商品
	 * 
	 * @param type
	 *            类型
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除商品
	 * @param count
	 *            数量
	 * @return 商品
	 */
	public List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count) {
		return productDao.search(type, keyword, excludes, count);
	}

	/**
	 * 增加库存
	 * 
	 * @param product
	 *            商品
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 * @param memo
	 *            备注
	 */
	public void addStock(Product product, int amount, StockLog.Type type, Admin operator, String memo) {
		Assert.notNull(product);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		Assert.notNull(product.getStock());
		Assert.state(product.getStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setStock(product.getStock() + amount);
		productDao.update(product);

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager.ordinal());
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy.ordinal());
			}
		}

		if(product.getStock() == 0 && amount > 0){
			try{
				informationService.myFavoriteMessage(goods);
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		StockLog stockLog = new StockLog();
		stockLog.setType(type.ordinal());
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator);
		stockLog.setMemo(memo);
		stockLog.setProductId(product.getId());
		stockLogDao.save(stockLog);
	}

	/**
	 * 增加已分配库存
	 * 
	 * @param product
	 *            商品
	 * @param amount
	 *            值
	 */
	public void addAllocatedStock(Product product, int amount) {
		Assert.notNull(product);

		if (amount == 0) {
			return;
		}

		Assert.notNull(product.getAllocatedStock());
		Assert.state(product.getAllocatedStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setAllocatedStock(product.getAllocatedStock() + amount);
		productDao.update(product);

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager.ordinal());
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy.ordinal());
			}
		}
	}

	/**
	 * 商品过滤
	 * 
	 * @param products
	 *            商品
	 */
	public void filter(List<Product> products) {
		CollectionUtils.filter(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getStock() != null;
			}
		});

	
	}
	public Page findByPage(Pageable page){
		return  productDao.findByPage(page);
	}

	public Page findReverseAuctionByPage(Pageable page, String flag){
		return productDao.findReverseAuctionByPage(page, flag);
	}

	public Page findFuDaiGoodsByPage(Pageable page){
		return productDao.findFuDaiGoodsByPage(page);
	}

    public  List<Product> findBySpvalue(String categoryId,long goodid) {
		//List<String> category = StringUtil.matcherAll(",", categoryId);
		List<String> category = Arrays.asList(categoryId.split(","));
		return productDao.findBySpvalue(category,goodid);
    }
}