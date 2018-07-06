package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Review;
import com.wtshop.service.ReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wtshop.controller.wap.BaseController.STATUS;
import static com.wtshop.controller.wap.BaseController.SUCCESS;

/**
 * Controller - 评论
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/review")
public class ReviewController extends BaseController {

	private ReviewService reviewService = enhance(ReviewService.class);

	/**
	 * 回复
	 */
	public void reply() {
		Long id = getParaToLong("id");
		setAttr("review", reviewService.find(id));
		render("/admin/review/reply.ftl");
	}

	/**
	 * 回复
	 */
	public void replySubmit() {
		Long id = getParaToLong("id");
		String content = getPara("content");
		Review review = reviewService.find(id);
		if (review == null) {
			redirect(ERROR_VIEW);
			return;
		}
		Review replyReview = new Review();
		replyReview.setContent(content);
		replyReview.setIp(getRequest().getRemoteAddr());
		reviewService.reply(review, replyReview);

		addFlashMessage(SUCCESS_MESSAGE);
		redirect("reply.jhtml?id=" + id);
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
        Review review = reviewService.find(id);
        String images = review.getImages();
        setAttr("review", review);
		render("/admin/review/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Long id = getParaToLong("id");
		Boolean isShow = getParaToBoolean("isShow", false);
		Review review = reviewService.find(id);
		if (review == null) {
			redirect(ERROR_VIEW);
			return;
		}
		review.setIsShow(isShow);
		review.setStatus(true);
		reviewService.update(review);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		String typeName = getPara("type");
		Review.Type type = StrKit.notBlank(typeName) ? Review.Type.valueOf(typeName) : null;
		Pageable pageable = getBean(Pageable.class);
		if(pageable.getSearchProperty() != null && pageable.getSearchProperty().equals("status") && pageable.getSearchValue().equals("已处理")){
			pageable.setSearchValue("1");
		}else if(pageable.getSearchProperty() != null && pageable.getSearchProperty().equals("status") && pageable.getSearchValue().equals("待处理")){
			pageable.setSearchValue("0");
		}
		Page<Review> pageList = reviewService.findPageList(null, null, type, null, pageable);
		for(Review review : pageList.getList()){
			String images = review.getImages();
			if(images != null && images.contains("/")){
				review.setImages("1");
			}
		}
		setAttr("pageable", pageable);
		setAttr("type", type);
		setAttr("types", Review.Type.values());
		setAttr("page", pageList);
		render("/admin/review/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		for(Long id : ids){
			Review review = reviewService.find(id);
			review.setIsDelete(true);
			reviewService.update(review);
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 更改处理状态
	 */
	public void handle(){
		Long id = getParaToLong("id");
		Review review = reviewService.find(id);
		review.setStatus(true);
		reviewService.update(review);
		Map<String, String> map = new HashMap<String, String>();
		map.put(STATUS, SUCCESS);
		renderJson(map);
	}

}