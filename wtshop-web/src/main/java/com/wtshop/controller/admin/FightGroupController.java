package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.api.common.result.TuanGouGoodsMessageResult;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ReadProper;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrFeng on 2019/2/18
 * 团购管理
 */
@ControllerBind(controllerKey = "/admin/fightGroup")
public class FightGroupController extends BaseController {
    private FightGroupService fuDaiService = enhance(FightGroupService.class);
    private static final int PAGE_SIZE = 10;
    private FootPrintService footPrintService= enhance(FootPrintService.class);
    private ConsultationService consultationService = enhance(ConsultationService.class);
  private GroupBuyService groupBuyService = enhance(GroupBuyService.class);

    private GoodsService goodsService = enhance(GoodsService.class);
    private AreaService areaService = enhance(AreaService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
    private ReviewService reviewService = enhance(ReviewService.class);
    private OrderService orderService = enhance(OrderService.class);
    private MemberService memberService = enhance(MemberService.class);
    private  FightGroupService fightGroupService= enhance(FightGroupService.class);
    private ProductService productService = enhance(ProductService.class);
    private ActIntroduceService actIntroduceService = enhance(ActIntroduceService.class);
    private CertificatesService certificatesService= enhance(CertificatesService.class);
    public void list() {



        Pageable pageable = getBean(Pageable.class);
        pageable.setOrderProperty("orders");
        pageable.setOrderDirection("desc");
        setAttr("pageable", pageable);
        setAttr("page", fuDaiService.findPage(pageable));
        render("/admin/fightGroup/list.ftl");
    }

    //去添加页面
    public void add() throws ParseException {
        Long fightGroupL = 23L;
        Long tuanGouId = 23L;
        List<Order> order = orderService.findByfightgroupId(fightGroupL);
        FightGroup fightGroup = fightGroupService.find(fightGroupL);
        Map<String, Object> map = new HashedMap();
        map.put("goods", fightGroup.getProduct().getGoods());
        map.put("fightGroup",fightGroup);
        map.put("order",order);
        renderJson(ApiResult.success(map));

       /* Long fightGroupL = 23L;

        Long tuanGouId = 23L;

        FightGroup fightGroup = fightGroupService.find(fightGroupL);
      //  groupBuy.getProduct();
        List<Order> order = orderService.findByfightgroupId(fightGroupL);
        Map<String, Object> map = new HashedMap();
        map.put("goods", fightGroup.getProduct().getGoods());
        map.put("fightGroup",fightGroup);
        map.put("order",order);

        renderJson(ApiResult.success(map));*/

/*     setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        render("/admin/groupBuy/add.ftl");*/
    }

    //保存福袋信息
    public void save() {

        FightGroup fuDai = getModel( FightGroup.class);


        Long productId = getParaToLong("productId");

        fuDaiService.save(fuDai);
        FudaiProduct fudaiProduct = new FudaiProduct(productId, fuDai.getId(), 1);
        redirect("/admin/groupBuy/list.jhtml");
    }

    //去修改页面
    public void toEdit() {
        Long fuDaiId = getParaToLong("id");
        FightGroup group = fuDaiService.find(fuDaiId);
        setAttr("groupBuy", group);

        render("/admin/groupBuy/edit.ftl");
    }

    //修改福袋信息
    public void edit() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        FightGroup fuDai = getModel( FightGroup.class);




        Long productId = getParaToLong("productId");









        fuDaiService.update(fuDai);

        redirect("/admin/groupBuy/list.jhtml");
    }

    //删除福袋
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        fuDaiService.delete(ids);


        renderJson("type", "success");
    }

    public void status() {
        Long id = getParaToLong("fudaiId");
        int status = getParaToInt("status");
        FightGroup fuDai = fuDaiService.find(id);
        fuDai.setStatus(status);
        fuDaiService.update(fuDai);
        renderJson("1");
    }

    public void addGoods() {
        Long fuDaiId = getParaToLong("id");
        List list = fuDaiService.queryByFuDaiId(fuDaiId);
        FightGroup fd = fuDaiService.find(fuDaiId);
        setAttr("fuDaiId", fuDaiId);
        setAttr("indexNum", list.size());
        setAttr("fuDaiProductList", list);
        setAttr("fd", fd);
        render("/admin/groupBuy/addGoods.ftl");
    }

    public void saveGoogs() {
        List<FightGroup> list = getModels(FightGroup.class);
        Long fuDaiId = getParaToLong("fuDaiId");
        fuDaiService.saveOrUpdate(list, fuDaiId);
        redirect("/admin/groupBuy/list.jhtml");
    }

    public void imgList() {
        long fudaiId = getParaToLong("id");
        setAttr("fudaiId", fudaiId);

        render("/admin/groupBuy/imglist.ftl");

    }



    public void delImg() {

        renderJson(ApiResult.success());
    }

    /**
     * 禁用福袋
     */
    public void disabled() {
        Long fudaiId = getParaToLong("id");
        FightGroup fuDai = fuDaiService.find(fudaiId);
        fuDai.setStatus(FuDai.State_UnActive);
        fuDaiService.update(fuDai);
        redirect("list.jhtml");
    }


    /**
     * 启用福袋
     */
    public void publish() {
        Long fudaiId = getParaToLong("id");
        FightGroup fuDai = fuDaiService.find(fudaiId);
        List<FightGroup> list = fuDaiService.findSubListByFudaiId(fuDai.getId());
/*        if (CollectionUtils.isEmpty(list) || list.size() <= fuDai.getNum()) {
            addFlashMessage(Message.errMsg("福袋副产品数量需要大于福袋要抽取的副产品数量"));
            redirect("list.jhtml");
            return;
        }*/
        fuDai.setStatus(FuDai.State_Active);
        fuDaiService.update(fuDai);
        redirect("list.jhtml");
    }


}
