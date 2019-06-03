package com.wtshop.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.model.Brand;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.Promotion;
import com.wtshop.service.BrandService;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.PromotionService;

/**
 * Controller - 商品分类
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/product_category")
public class ProductCategoryController extends BaseController {

	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private BrandService brandService = enhance(BrandService.class);
	private PromotionService promotionService = enhance(PromotionService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		render("/admin/product_category/add.ftl");
	}

	/**
	 * 保存
	 * 
	 */
	public void save() {
		ProductCategory productCategory = getModel(ProductCategory.class);
		Long parentId = getParaToLong("parentId");
		Long[] brandIds = getParaValuesToLong("brandIds");
		Long[] promotionIds = getParaValuesToLong("promotionIds");
		ProductCategory pProductCategory = productCategoryService.find(parentId);
		if (pProductCategory != null) {
			productCategory.setParentId(pProductCategory.getId());
		}
		productCategory.setBrands(new ArrayList<Brand>(brandService.findList(brandIds)));
		productCategory.setPromotions(new ArrayList<Promotion>(promotionService.findList(promotionIds)));
		
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setGoods(null);
		productCategory.setParameters(null);
		productCategory.setAttributes(null);
		productCategory.setSpecifications(null);
		productCategoryService.save(productCategory);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 * 
	 */
	public void edit() {
		Long id = getParaToLong("id");
		ProductCategory productCategory = productCategoryService.find(id);
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		setAttr("productCategory", productCategory);
		setAttr("children", productCategoryService.findChildren(productCategory, true, null));
		render("/admin/product_category/edit.ftl");
	}

	/**
	 * 更新
	 * 
	 */
	public void update() {
		ProductCategory productCategory = getModel(ProductCategory.class);
		Long parentId = getParaToLong("parentId");
		Long[] brandIds = getParaValuesToLong("brandIds");
		Long[] promotionIds = getParaValuesToLong("promotionIds");
		ProductCategory pProductCategory = productCategoryService.find(parentId);
		if (pProductCategory != null) {
			productCategory.setParentId(pProductCategory.getId());
		}
		productCategory.setBrands(new ArrayList<Brand>(brandService.findList(brandIds)));
		productCategory.setPromotions(new ArrayList<Promotion>(promotionService.findList(promotionIds)));
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				redirect(ERROR_VIEW);
				return;
			}
			List<ProductCategory> children = productCategoryService.findChildren(parent, true, null);
			if (children != null && children.contains(parent)) {
				redirect(ERROR_VIEW);
				return;
			}
		}
		productCategory.remove("tree_path", "grade", "children", "goods", "parameters", "attributes", "specifications");
		productCategoryService.update(productCategory);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		setAttr("productCategoryTree", productCategoryService.findTree());
		render("/admin/product_category/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("id");
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			redirect(ERROR_VIEW);
			return;
		}
		List<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			renderJson(Message.error("admin.productCategory.deleteExistChildrenNotAllowed"));
			return;
		}
		List<Goods> goods = productCategory.getGoods();
		if (goods != null && !goods.isEmpty()) {
			renderJson(Message.error("admin.productCategory.deleteExistProductNotAllowed"));
			return;
		}



		productCategoryService.delete(id);
		renderJson(SUCCESS_MESSAGE);
	}

}