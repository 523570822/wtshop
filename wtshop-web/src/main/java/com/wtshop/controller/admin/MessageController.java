package com.wtshop.controller.admin;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.entity.AreaResult;
import com.wtshop.model.Information;
import com.wtshop.model.MessageLink;
import com.wtshop.service.*;
import com.wtshop.util.RedisUtil;
import com.xiaoleilu.hutool.db.Page;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.Message;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.*;

import static com.wtshop.controller.wap.BaseController.STATUS;
import static com.wtshop.controller.wap.BaseController.SUCCESS;
import static com.wtshop.controller.wap.BaseController.convertToLong;

/**
 * Controller - 消息
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/message")
public class MessageController extends BaseController {

	private MessageService messageService = enhance(MessageService.class);
	private MemberService memberService = enhance(MemberService.class);
	private MemberAttributeService memberAttributeService = enhance(MemberAttributeService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private InformationService informationService = enhance(InformationService.class);
	private MessageLinkService messageLinkService = enhance(MessageLinkService.class);

	/**
	 * 检查用户名是否合法
	 */
	public void checkUsername() {
		String username = getPara("username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(memberService.usernameExists(username));
	}

	/**
	 * 发送
	 */
	public void send() {
		Long draftMessageId = getParaToLong("draftMessageId");
		Message draftMessage = messageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && draftMessage.getSender() == null) {
			setAttr("draftMessage", draftMessage);
		}
		render("/admin/message/send.ftl");
	}

	public void sendMember(){
		String goodList = getPara("idList");
		goodList = goodList.substring(1,goodList.length() -1).replace(" ", "");;
		String[] values = StringUtils.split(goodList, ",");
		Long[] idList = values == null ? null :convertToLong(values);
		List<Member> memberList = memberService.findMemberList(idList);
		Cache redis = Redis.use();
		ArrayList<Long> list = new ArrayList<Long>();
		for(Long id : idList){
			list.add(id);
		}
		redis.setex("PAGEALL",2*60, list);
		setAttr("members",memberList);
		render("/admin/message/send.ftl");

	}

	public void sendMembers(){
		String goodList = getPara("idList");
		String[] values = StringUtils.split(goodList, ",");
		Long[] idList = values == null ? null :convertToLong(values);
		List<Member> memberList = memberService.findMemberList(idList);
		Cache redis = Redis.use();
		ArrayList<Long> list = new ArrayList<Long>();
		for(Long id : idList){
			list.add(id);
		}
		redis.setex("PAGEALL",2*60, list);
		setAttr("members",memberList);
		render("/admin/message/send.ftl");

	}


	/**
	 * 发送信息
	 */
	public void sendSubmit() {
		Cache redis = Redis.use();
		Long[] ids = getParaValuesToLong("memberIds");
		String title = getPara("title");
		String content = getPara("content");
		String introduction = getPara("introduction");
		if(ids != null){
			for(Long id : ids){

				MessageLink messageLink = new MessageLink();
				messageLink.setTitle(title);
				messageLink.setContent(content);
				messageLink.setInformation(introduction);
				messageLink.setUuid(RandomStringUtils.randomAlphabetic(16));
				messageLinkService.save(messageLink);


				Information information = new Information();
				information.setContent(content);
				information.setTitle(title);
				information.setStatus(false);
				information.setType(Information.Type.none.ordinal());
				information.setIsDelete(false);
				information.setMemberId(id);
				information.setLink(messageLink.getUuid());
				information.setAction(Information.Action.none.ordinal());
				informationService.save(information);
			}
		}
		redis.del("PAGEALL");
		redirect("list.jhtml");
	}

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Information information = informationService.find(id);
		setAttr("adminMessage", information);
		render("/admin/message/view.ftl");
	}



	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Integer type = getParaToInt("typeName");
		setAttr("pageable", pageable);
		setAttr("page", informationService.findPageRecord( pageable ,type));
		render("/admin/message/list.ftl");
	}



	/**
	 * 删除
	 */
	public void handle(){
		Cache redis = Redis.use();
		Long id = getParaToLong("id");
		List<Long> idList = redis.get("PAGEALL");
		if(idList != null){
			for(Long idLists : idList){
				if(idLists.equals(id)){
					idList.remove(id);
					break;
				}

			}
			redis.setex("PAGEALL",2*60,idList);
		}

		List<Member> memberList = new ArrayList<Member>();
		memberList = memberService.findMemberByList(idList);
		setAttr("members",memberList);
		render("/admin/message/send.ftl");


	}

	/**
	 * 跳转选择会员页面
	 */
	public void memberSelect(){
		Cache redis = Redis.use();
		Pageable pageable = getBean(Pageable.class);
		Integer type = getParaToInt("typeName");
		pageable.setPageSize(10);
		String searchProperty = pageable.getSearchProperty();
		if(StringUtils.isEmpty(searchProperty)){
			com.jfinal.plugin.activerecord.Page<Member> memberPage = memberService.findPages(pageable, type);
			setAttr("memberRanks", memberRankService.findAll());
			setAttr("memberAttributes", memberAttributeService.findAll());
			setAttr("pageable", pageable);
			setAttr("page", memberPage);
			setAttr("idList", redis.get("PAGEALL"));
			render("/admin/message/messageSelect.ftl");
		}else {
			String searchValue = pageable.getSearchValue();
			com.jfinal.plugin.activerecord.Page<Member> memberPage = memberService.findPages(pageable, type);
			if(StringUtils.isEmpty(searchValue)){
				setAttr("searchValue", "全部会员");
			}else {
				String name = "";
				if("username".equals(searchProperty)){
					name="用户名: "+searchValue;
				}else if("nickname".equals(searchProperty)){
					name="昵称: "+searchValue;
				}else {
					name="E-mail: "+searchValue;
				}
				setAttr("searchValue", name);
			}
			int totalRow = memberPage.getTotalRow();
			setAttr("totalRow", totalRow);
			setAttr("list",memberPage.getList());
			render("/admin/message/send.ftl");

		}

	}

	/**
	 * 选中会员
	 */
	public void memberList(){
		Cache redis = Redis.use();
		String[] id = getPara("ids").split(",");
		List<Long> idList = redis.get("PAGEALL");
		List<Member> memberList = new ArrayList<Member>();
		if(idList == null){
			idList = new ArrayList<Long>();
		}

		for(String ids : id){
			if(StringUtils.isNotEmpty(ids)){
				if(!idList.contains(Long.parseLong(ids))){
					idList.add(Long.parseLong(ids));
				}
			}

		}
		redis.setex("PAGEALL",1*60,idList);
		memberList = memberService.findMemberByList(idList);
		setAttr("members",memberList);
		render("/admin/message/send.ftl");
	}




}