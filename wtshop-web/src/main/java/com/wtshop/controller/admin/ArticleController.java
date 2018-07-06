package com.wtshop.controller.admin;

import java.util.ArrayList;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Article;
import com.wtshop.model.Tag;
import com.wtshop.service.ArticleCategoryService;
import com.wtshop.service.ArticleService;
import com.wtshop.service.TagService;

/**
 * Controller - 文章
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/article")
public class ArticleController extends BaseController {

	private ArticleService articleService = enhance(ArticleService.class);
	private ArticleCategoryService articleCategoryService = enhance(ArticleCategoryService.class);
	private TagService tagService = enhance(TagService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("articleCategoryTree", articleCategoryService.findTree());
		setAttr("tags", tagService.findList(Tag.Type.article));
		render("/admin/article/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Article article = getModel(Article.class);
		Long articleCategoryId = getParaToLong("articleCategoryId");
		Long[] tagIds = getParaValuesToLong("tagIds");
		Boolean isPublication = getParaToBoolean("isPublication", false);
		Boolean isTop = getParaToBoolean("isTop", false);
		article.setIsPublication(isPublication);
		article.setIsTop(isTop);
		article.setArticleCategoryId(articleCategoryService.find(articleCategoryId).getId());
		article.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));
		
		
		article.setHits(0L);
		article.setGenerateMethod(Article.GenerateMethod.eager.ordinal());
		articleService.save(article);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("articleCategoryTree", articleCategoryService.findTree());
		setAttr("tags", tagService.findList(Tag.Type.article));
		setAttr("article", articleService.find(id));
		render("/admin/article/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Article article = getModel(Article.class);
		Long articleCategoryId = getParaToLong("articleCategoryId");
		Long[] tagIds = getParaValuesToLong("tagIds");
		Boolean isPublication = getParaToBoolean("isPublication", false);
		Boolean isTop = getParaToBoolean("isTop", false);
		article.setIsPublication(isPublication);
		article.setIsTop(isTop);
		article.setArticleCategoryId(articleCategoryService.find(articleCategoryId).getId());
		article.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));
		article.remove("hits", "generate_method");
		
		articleService.update(article);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", articleService.findPage(pageable));
		render("/admin/article/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		articleService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}