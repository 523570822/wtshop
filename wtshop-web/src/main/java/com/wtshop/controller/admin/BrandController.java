package com.wtshop.controller.admin;

import org.apache.commons.lang3.StringUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Brand;
import com.wtshop.model.Brand.Type;
import com.wtshop.model.Goods;
import com.wtshop.service.BrandService;
import com.wtshop.service.GoodsService;
import java.util.List;
import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * Controller - 品牌
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/brand")
public class BrandController extends BaseController {

	private BrandService brandService = enhance(BrandService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("types", Brand.Type.values());
		render("/admin/brand/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Brand brand = getModel(Brand.class);
		String typeName = getPara("type");
		Brand.Type type = StrKit.notBlank(typeName) ? Type.valueOf(typeName) : null;
		if (type != null) {
			brand.setType(type.ordinal());
		}
		if (Brand.Type.text.ordinal() == brand.getType()) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			redirect(ERROR_VIEW);
			return;
		}
		brand.setGoods(null);
		brand.setProductCategories(null);
		brandService.save(brand);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/brand/list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("types", Brand.Type.values());
		setAttr("brand", brandService.find(id));
		render("/admin/brand/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Brand brand = getModel(Brand.class);
		String typeName = getPara("type");
		Brand.Type type = StrKit.notBlank(typeName) ? Type.valueOf(typeName) : null;
		if (type != null) {
			brand.setType(type.ordinal());
		}
		if (Brand.Type.text.ordinal() == (brand.getType())) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			redirect(ERROR_VIEW);
			return;
		}
		brandService.update(brand);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/brand/list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", brandService.findPage(pageable));
		LogKit.info(">" + pageable.getPageNumber());
		setAttr("pageable", pageable);
		render("/admin/brand/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {

		String[] values = StringUtils.split(getPara("ids"), ",");
		Long[] idList = values == null ? null :convertToLong(values);
		if (idList != null) {
			for (Long id : idList) {
				List<Goods> goodsList = goodsService.findByBrandId(id);
				if (goodsList != null &&  goodsList.size() > 0) {
					for(Goods goods : goodsList){
						renderJson(Message.error("admin.brand.deleteExistNotAllowed", goods.getName()));
						return;
					}

				}
			}
			brandService.delete(idList);
		}
		renderJson(SUCCESS_MESSAGE);
	}

}