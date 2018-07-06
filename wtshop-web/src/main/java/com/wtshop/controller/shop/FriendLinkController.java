package com.wtshop.controller.shop;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.FriendLink;
import com.wtshop.service.FriendLinkService;

/**
 * Controller - 友情链接
 * 
 * 
 */
@ControllerBind(controllerKey = "/friend_link")
@Before(ThemeInterceptor.class)
public class FriendLinkController extends BaseController {

	private FriendLinkService friendLinkService = enhance(FriendLinkService.class);

	/**
	 * 首页
	 */
	public void index() {
		setAttr("textFriendLinks", friendLinkService.findList(FriendLink.Type.text));
		setAttr("imageFriendLinks", friendLinkService.findList(FriendLink.Type.image));
		render("/shop/${theme}/friend_link/index.ftl");
	}

}