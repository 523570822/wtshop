package com.wtshop.controller.admin;

import java.util.Arrays;
import java.util.List;

import com.wtshop.Message;
import com.wtshop.util.ApiResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Specification;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.SpecificationService;

/**
 * Controller - 规格
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/specification")
public class SpecificationController extends BaseController {

	private SpecificationService specificationService = enhance(SpecificationService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);

	/**
	 * 添加
	 */
	public void add() {
		Long sampleId = getParaToLong("sampleId");
		setAttr("sample", specificationService.find(sampleId));
		setAttr("productCategoryTree", productCategoryService.findTree());
		render("/admin/specification/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Specification specification = getModel(Specification.class);
		Long productCategoryId = getParaToLong("productCategoryId");
		List<String> options = Arrays.asList(getParaValues("options"));
		
		specification.setOptionsConverter(options);
		CollectionUtils.filter(specification.getOptionsConverter(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		List<Specification> specification1=specificationService.findByName(null,specification.getName(),specification.getProductCategoryId());
		if(specification1.size()>0){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "该规格名称已经存在"));
			setAttr("productCategoryTree", productCategoryService.findTree());
			setAttr("specification", specification);
			render("/admin/specification/add.ftl");
			return;
		}
		specification.setProductCategoryId(productCategoryService.find(productCategoryId).getId());
		specification.setOptions(JSONArray.toJSONString(options));
		specificationService.save(specification);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("specification", specificationService.find(id));
		render("/admin/specification/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Specification specification = getModel(Specification.class);
		List<String> options = Arrays.asList(getParaValues("options"));
		
		specification.setOptionsConverter(options);
		CollectionUtils.filter(specification.getOptionsConverter(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		
		specification.setOptions(JSONArray.toJSONString(options));
	//	specification.remove("product_category_id");
		List<Specification> specification1=specificationService.findByName(specification.getId(),specification.getName(),specification.getProductCategoryId());
		if(specification1.size()>0){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "该规格名称已经存在"));
			setAttr("productCategoryTree", productCategoryService.findTree());
			setAttr("specification", specification);
			render("/admin/specification/edit.ftl");
			return;

		}
		specificationService.update(specification);
		addFlashMessage(SUCCESS_MESSAGE);
		render("/admin/specification/edit.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", specificationService.findPage(pageable));
		setAttr("pageable", pageable);
		render("/admin/specification/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		specificationService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}