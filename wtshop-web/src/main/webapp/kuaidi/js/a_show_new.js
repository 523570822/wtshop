var platform = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ? "m" : "www",
    from = GetQueryString("from"), adArr = [], sets = [],
    isCoper = "aladdin" == from || "alading" == from || "openv" == from || "baidu_ala" == from;

function removeAd(o) {
    o ? $("[role-slot=" + o + "]").remove() : $("[role-slot]").remove()
}

function sortAds() {
    for (var o = {}, e = 0, a = adArr.length; e < a; e++) (o[adArr[e].pos] || (o[adArr[e].pos] = [])).push(adArr[e]);
    return o
}

function loadAds(o) {
    $.ajax({
        type: "post",
        url: "../api/login/ext.jhtml",
        data: "method=mainprofile&platform=" + platform + "&pos=" + o,
        dataType: "json",
        success: function (o) {
            if (200 == o.status && 0 < o.adslist.length) {
                for (var e = 0; e < o.adslist.length; e++) sets.push(o.adslist[e].pos), adArr.push(o.adslist[e]);
                showAds(sortAds())
            } else removeAd()
        },
        error: function () {
            removeAd()
        }
      /*  type: "post",
        url: "/assets/ext",
        data: "method=mainprofile&platform=" + platform + "&pos=" + o,
        dataType: "json",
        success: function (o) {
            if (200 == o.status && 0 < o.adslist.length) {
                for (var e = 0; e < o.adslist.length; e++) sets.push(o.adslist[e].pos), adArr.push(o.adslist[e]);
                showAds(sortAds())
            } else removeAd()
        },
        error: function () {
            removeAd()
        }*/
    })
}

function showAds(o) {
    if (o) {
        for (var e = 0; e < allpos.length; e++) -1 == $.inArray(allpos[e], sets) && $("[role-slot=" + allpos[e] + "]").remove();
        for (var a = 0, t = allpos.length; a < t; a++) {
            var r = allpos[a], s = $("[role-slot=" + r + "]");
            if (s.size()) {
                var l = o[r] ? o[r].length : 0;
                l && pushAd(s.eq(0), o[r][Math.floor(Math.random() * l)])
            }
        }
    } else removeAd()
}

function pushAd(o, e) {
    var a = "";
    e ? ("img" == e.type ? (o.data({
        aid: e._id,
        url: e.url,
        pos: e.pos
    }), a = '<a target="_blank" href="' + e.url + '"><img src="' + e.bgimage + '"></a>', a += "m_result_redpacket" == e.pos || "活动" == e.showType ? "" : '<div class="close">广告</div>') : a = "imgJavascript" == e.type ? (o.data({
        aid: e._id,
        url: e.url,
        pos: e.pos
    }), '<img src="' + e.bgimage + '">' + e.content.replace("${id}", e.pos)) : e.content, o.css("opacity", 1).empty().html(a), $.post("/mainapi.do?method=vieweventads", {
        source: platform,
        adlocation: e.pos,
        adurl: encodeURIComponent(e.url) || "UNKNOWN",
        showorlink: "show",
        _id: e._id
    })) : o.remove()
}

$(function () {
    var o = JSON.parse(localStorage.getItem("adRefuseDate")) || {}, e = Date.now() - 864e5;
    for (var a in $("[role-slot]").each(function (o, e) {
        $(e).attr("id", $(e).attr("role-slot"))
    }), o) +o[a] > e && $("[role-slot=" + a + "]").remove();
    if ("undefined" == typeof allpos || !allpos || !$("[role-slot]").size()) return $("[role-slot]").remove(), !1;
    loadAds(allpos.join(","))
}), $(function () {
    $("body").on("click", "[role-slot] a", function (o) {
        var e = $(this).closest("[role-slot]"), a = e.data("url"), t = e.data("pos"), r = e.data("aid");
        $.post("/mainapi.do?method=vieweventads", {
            source: platform,
            adlocation: t,
            adurl: encodeURIComponent(a),
            showorlink: "click",
            _id: r
        })
    }).on("touchend", "[role-slot] .close", function (o) {
        o.preventDefault(), o.stopPropagation();
        var e = $(this).closest("[role-slot]"), a = JSON.parse(localStorage.getItem("adRefuseDate")) || {};
        a[e.attr("role-slot")] = Date.now(), localStorage.setItem("adRefuseDate", JSON.stringify(a)), e.remove(), $.post("/mainapi.do?method=vieweventads", {
            source: platform,
            adlocation: e.data("pos"),
            adurl: encodeURIComponent(e.data("url")),
            showorlink: "close",
            _id: e.data("aid")
        })
    })
});