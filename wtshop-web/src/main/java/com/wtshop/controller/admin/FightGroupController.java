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
       Map<String, String[]> ss = getParaMap();
        Long fuDaiId =23L;
                GroupBuy fuDai = groupBuyService.find(fuDaiId);
        //可拼团认
        List<FightGroup> fightgroupList=fightGroupService.findByProductId(fuDai.getProductId());
        Product p = fuDai.getProduct();
        Goods goods = p.getGoods();
        Map<String, Object> map = new HashedMap();

        Long id = goods.getId();
        String type = getPara("type"); //是否喵币商品
        Member perosn=memberService.getCurrent();
        Pageable pageable = new Pageable(1, 20);
        Boolean favorite = false;

        if (goods == null) {
            return;
        }
        List<Area> areas = areaService.findParents(goods.getArea(), true, null);
        goods.setAttributeValue0(areas.get(0).getName());

        RequestContextHolder.setRequestAttributes(getRequest());
        Member m=memberService.getCurrent();
        if (goods.getFavoriteMembers().contains(m)) {
            favorite = true;
        }
        Page<Consultation> consultationPages = consultationService.findPage(null, goods, true, pageable);
        Page<Review> reviewPages = reviewService.findPageList(null, goods, null, true, pageable);
        List<Review> list = reviewPages.getList();
        for(Review review:list){
            Date dd = review.getModifyDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(dd);

            Date date =  sdf.parse(s);
            review.setModifyDate(date);
            Member member = memberService.find(review.getMemberId());
            String nickname = member.getNickname();
            if(review.getIsAnonymous() != null && review.getIsAnonymous()){
                if(StringUtils.isNotBlank(nickname)){
                    String first = nickname.substring(0,1);
                    String end = nickname.substring(nickname.length()-1);
                    review.setOrderContent(first+"**"+end);
                }else {
                    review.setOrderContent("**");
                }
            }else {
                review.setOrderContent(nickname);
            }
        }
        Long reviewCount = reviewService.count(null, goods, null, true);//全部评论
        Long positiveCount = reviewService.count(null, goods, Review.Type.positive, true);
        Long moderateCount = reviewService.count(null, goods, Review.Type.moderate, true);
        Long negativeCount = reviewService.count(null, goods, Review.Type.negative, true);
        Long imagescount = 0L;
        List<Review> reviews = reviewService.count(null, goods, true);
        for(Review review : reviews){
            String images = review.getImages();
            if(images.contains("/")){
                imagescount += 1;
            }
        }
        List<Product> productList = productService.findProductList(id);
        String name = goods.getName();
        List<Tag> tags = goodsService.finTagList(goods.getId());



        //购物须知&正品保障&关税
        Setting setting = SystemUtils.getSetting();
        String settingShoppingCopyUrl = setting.getShoppingCopyUrl();
        String certifiedCopyUrl = setting.getCertifiedCopyUrl();
        String taxExplainUrl = setting.getTaxExplainUrl();
        Product byGoodsId = productService.findByGoodsId(goods.getId());
        Integer stock = byGoodsId.getStock();


        //插入会员足迹
        if (m !=null){
            boolean isHas = footPrintService.findByTime(goods.getId(),perosn.getId());
            if(!isHas){
                Footprint footprint=new  Footprint();
                footprint.setGoodsId(goods.getId());
                footprint.setMemberId(perosn.getId());
                footPrintService.save(footprint);
            }
        }
        //收货地址
        Receiver aDefault = receiverService.findDefault(perosn);

        //商品配送
        String receiveTime = null;
        if(aDefault != null){
            AreaDescribe areaDescribe = areaDescribeService.findByAreaId(aDefault.getAreaId());
            //判断本级地区是否填写
            if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
                receiveTime = areaDescribe.getReceivingBegintime();
            }else {
                AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(aDefault.getAreaId()).getParentId());
                if(areaDescribes !=null){
                    receiveTime = areaDescribes.getReceivingBegintime();
                }

            }
        }
        String 	freeMoney= RedisUtil.getString("freeMoney");
        String freMon;
        if(freeMoney==null||freeMoney.trim().equals("")||freeMoney.trim().equals("null")||freeMoney.trim().equals("0")){
            freMon=	"包邮";
        }else{
            freMon="订单满"+freeMoney+"元包邮";
        }


        TuanGouGoodsMessageResult goodsMessageResult = new TuanGouGoodsMessageResult(stock,goods,name, favorite, consultationPages, reviewPages, reviewCount,positiveCount,moderateCount,negativeCount,imagescount,tags,productList,settingShoppingCopyUrl,certifiedCopyUrl,taxExplainUrl,aDefault,receiveTime,freMon,fuDai,fightgroupList);
        renderJson(ApiResult.success(goodsMessageResult));



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
