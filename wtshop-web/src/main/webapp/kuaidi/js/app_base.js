var appid = GetQueryString("appid"), openid = GetQueryString("openid").replace(/%3D/g, ""),
    unionid = GetQueryString("unionid"), token = sessionStorage.getItem("TOKEN") || "",
    coname = void 0 !== coname && coname ? coname : GetQueryString("coname"), hdisplay = GetQueryString("hdisplay"),
    nonce = GetQueryString("nonce"), timeStamp = GetQueryString("timeStamp"), kd100sign = GetQueryString("kd100sign"),
    PLATFORM = "MWWW", query = "",
    queryArr = ["coname", "hdisplay", "nonce", "timeStamp", "kd100sign", "appid", "openid", "unionid"],
    ua = window.navigator.userAgent.toLowerCase(), fromClient = GetQueryString("kdfrom") || GetQueryString("from"),
    isWechat = "micromessenger" == ua.match(/MicroMessenger/i), isYzj = ua.match(/Qing\/.*;(iOS|iPhone|Android).*/i),
    isMiniProgram = "miniProgram" == fromClient, isQuickApp = "quickApp" == fromClient,
    isApp = "kdios" == fromClient || "kdandroid" == fromClient || "app" == fromClient || -1 < ua.indexOf("kuaidi100"),
    isBaidu = /aladdin|alading|alading|openv|baidu_ala/.test(fromClient);
for (var i in rqWxAuth(), queryArr) window[queryArr[i]] && (query += 0 == i ? queryArr[i] + "=" + window[queryArr[i]] : "&" + queryArr[i] + "=" + window[queryArr[i]]);

function getcookie(e) {
    var t = "";
    if (document.cookie && "" != document.cookie) for (var i = document.cookie.split(";"), n = 0; n < i.length; n++) {
        var o = i[n].replace(/(^\s*)|(\s*$)/g, "");
        if (o.substring(0, e.length + 1) == e + "=") {
            t = unescape(o.substring(e.length + 1));
            break
        }
    }
    return t
}

function setcookie(e, t) {
    var i = new Date, n = parseInt(i.getTime()), o = 86400 - 3600 * i.getHours() - 60 * i.getMinutes() - i.getSeconds();
    i.setTime(n + 1e6 * (o - 60 * i.getTimezoneOffset())), document.cookie = escape(e) + "=" + escape(t) + ";expires=" + i.toGMTString() + "; domain=.kuaidi100.com;path=/"
}

function delcookie(e) {
    var t = new Date;
    t.setTime(t.getTime() - 1);
    var i = getcookie(e);
    document.cookie = escape(e) + "=" + escape(i) + "; expires=" + t.toGMTString() + "; path=/"
}

function resetcookie(e) {
    setcookie(e, "")
}

function getStorage(e) {
    var t = null;
    try {
        t = JSON.parse(localStorage.getItem(e))
    } catch (e) {
    }
    return t
}

function setStorage(e, t) {
    try {
        localStorage.setItem(e, JSON.stringify(t))
    } catch (e) {
    }
}

function getSession(e) {
    var t = null;
    try {
        t = JSON.parse(sessionStorage.getItem(e))
    } catch (e) {
    }
    return t
}

function setSession(e, t) {
    try {
        sessionStorage.setItem(e, JSON.stringify(t))
    } catch (e) {
    }
}

function addlog(e) {
    $.ajax({url: "/apicenter/courier.do?method=addlog&logtype=" + e})
}

function GetQueryString(e) {
    var t = new RegExp("(^|&)" + e + "=([^&]*)(&|$)", "i"), i = window.location.search.substr(1).match(t);
    return null != i ? decodeURIComponent(i[2]) : ""
}

function appLogin(t, i) {
    var e = sessionStorage.getItem("TOKEN"), n = getcookie("TOKEN");
    if (e || n) token = e || n, isFunc(t) && t(); else if (coname && appid && openid || openid && isWechat && !isMiniProgram) {
        var o = appid ? {
            openid: openid,
            appid: appid,
            nonce: nonce,
            timeStamp: timeStamp,
            kd100sign: kd100sign
        } : {openid: openid};
        $.ajax({
            url: "/apicenter/xcx.do?method=getTokenByOpenid", data: o, success: function (e) {
                200 == e.status && e.data ? (token = e.data, coname && appid && openid || !coname ? sessionStorage.setItem("TOKEN", token) : setcookie("TOKEN", token), isFunc(t) && t()) : isFunc(i) && i()
            }, error: function () {
                isFunc(i) && i()
            }
        })
    } else isYzj ? $.ajax({
        url: "/apicenter/xcx.do?method=getTokenByYZJTicket",
        data: {ticket: GetQueryString("ticket")},
        success: function (e) {
            200 == e.status && e.token ? (setcookie("TOKEN", token = e.token), isFunc(t) && t()) : isFunc(i) && i()
        },
        error: function () {
            isFunc(i) && i()
        }
    }) : isFunc(i) && i()
}

function request(t, i, n) {
    var e = (i = i || {}).data || {}, o = {
        url: t,
        type: i.method || "POST",
        dataType: "json",
        timeout: i.timeout || 1e4,
        beforeSend: isFunc(i.before) ? i.before : function () {
        },
        success: function (e) {
            if (200 == e.status) isFunc(i.success) && i.success(e); else if (403 == e.status) {
                if (!1 === i.handleLogin) return;
                coname && appid && openid || openid && isWechat ? appLogin(function () {
                    request(t, i, n)
                }, function () {
                    "undefined" != typeof goLogin && isFunc(goLogin) ? goLogin() : login()
                }) : "undefined" != typeof goLogin && isFunc(goLogin) ? goLogin() : login()
            } else !1 !== i.handleFail && (isFunc(i.fail) ? i.fail(e) : tips(e.message))
        },
        error: function (e, t) {
            (!0 === i.handleError || isFunc(i.error)) && (isFunc(i.error) ? i.error(e, t) : tips("网络错误，请检查您的网络设置"))
        },
        complete: function (e) {
            isFunc(i.complete) && i.complete(e)
        }
    };
    if (!e.token && (e.token = token), !e.openid && openid && (e.openid = openid), !e.appid && appid && (e.appid = appid), e.platform = PLATFORM, !e.nonce && nonce && (e.nonce = nonce), !e.coname && coname && (e.coname = coname), !e.timeStamp && timeStamp && (e.timeStamp = timeStamp), !e.kd100sign && kd100sign && (e.kd100sign = kd100sign), o.data = e, n && (o.cache = !1, o.contentType = !1, o.processData = !1), t) return $.ajax(o)
}

function login() {
    var e = isWechat || coname ? "https://m.kuaidi100.com/sso/login.jsp?" + query : "https://sso.kuaidi100.com/user/";
    location.href = e
}

function rqWxAuth() {
    var e = null, t = "";
    Date.now();
    if (!isWechat || isMiniProgram) return !1;
    if ("undefined" != typeof weixinredirect && !1 === weixinredirect) return !1;
    try {
        e = sessionStorage.getItem("wxSign"), sessionStorage.setItem("wxSign", Date.now()), openid && e || (t = location.href.replace("http:", "https:").replace(/&?(openid|unionid|subscribe)=[^&]*/g, ""), $.ajax({
            url: "/weixin/mapping.do?method=buildMapping",
            method: "POST",
            dataType: "json",
            async: !1,
            data: {url: t},
            success: function (e) {
                "200" == e.status ? location.replace("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx567c983270be6319&redirect_uri=http%3A%2F%2Fwx.kuaidi100.com%2Foauth.do&response_type=code&scope=snsapi_base&state=" + e.key + "#wechat_redirect") : location.replace("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx567c983270be6319&redirect_uri=http%3A%2F%2Fwx.kuaidi100.com%2Foauth.do&response_type=code&scope=snsapi_base&state=" + t + "#wechat_redirect")
            },
            error: function () {
                location.replace("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx567c983270be6319&redirect_uri=http%3A%2F%2Fwx.kuaidi100.com%2Foauth.do&response_type=code&scope=snsapi_base&state=" + t + "#wechat_redirect")
            }
        }))
    } catch (e) {
    }
}

function isFunc(e) {
    return "function" == typeof e
}

function tips(e, t) {
    if (e) {
        var i = $('<div style="position:fixed;width:100%;top:0;bottom:0;text-align:center;z-index:99999;transition:all .3s;padding:0 3.625rem;box-sizing:border-box;"><div style="display:inline-block;vertical-align:middle;background: rgba(0,0,0,.8);color:#FFF;border-radius: 4px;font-size: .875rem;padding: .5rem 1rem;line-height: 1.5em;box-sizing:border-box;">' + e + '</div><span style="display:inline-block;height: 100%;vertical-align:middle;"></span></div>');

        function n() {
            i.css("opacity", 0), setTimeout(function () {
                i.remove()
            }, 500)
        }

        $("body").append(i), i.click(function (e) {
            n()
        }), setTimeout(function () {
            n()
        }, t || 1e3 + (e.length - 10) / 10 * 1e3)
    }
}

function dialog(n, o, a) {
    function c() {
        e.hide(), setTimeout(function () {
            e.remove(), haveDialog = !1
        }, 100)
    }

    haveDialog = !0;
    var e = $('    <div class="global-dialog">      <div class="dialog-body"' + (!0 === n.autoWidth ? "" : ' style="width:70%;"') + '>        <div class="dialog-head">提示</div>        <div class="dialog-content">' + (n.content || "") + '</div>        <div class="dialog-foot">          <div class="dialog-btn" handle="confirm">' + (n.confirmText || "确定") + "</div>        </div>      </div>    </div>");
    return "confirm" != n.type && (n.reverse ? e.find(".dialog-foot").append('<div class="dialog-btn" handle="cancel">' + (n.cancelText || "取消") + "</div>") : e.find(".dialog-foot").prepend('<div class="dialog-btn" handle="cancel">' + (n.cancelText || "取消") + "</div>")), e.appendTo($("body")), e.on("click", ".dialog-btn", function (e) {
        e.preventDefault();
        var t = $(this).is("[handle=confirm]"), i = $(this).is("[handle=cancel]");
        !1 !== n.autoHide && c(), t && "function" == typeof o && o(), i && "function" == typeof a && a()
    }), {hide: c}
}

function cominfo(t, i) {
    var e = localStorage.getItem("com_" + t);
    null == e || "" == e ? $.ajax({
  /*      type: "post",
        url: "/company.do",
        data: "method=companyjs&number=" + t,
        dataType: "json",
        success: function (e) {
            localStorage.setItem("com_" + t, JSON.stringify(e)), isFunc(i) && i(e)
        }*/
       type: "post",
        url: "../api/login/company.jhtml",
        data: "method=companyjs&number=" + t,
        dataType: "json",
        success: function (e) {
            localStorage.setItem("com_" + t, JSON.stringify(e)), isFunc(i) && i(e)
        }
    }) : isFunc(i) && i(JSON.parse(e))
}

function downApp() {
    -1 != ua.indexOf("micromessenger") ? location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.Kingdee.Express&ckey=CK1413979100250" : -1 != ua.indexOf("iphone") || -1 != ua.indexOf("ipad") ? location.href = "http://itunes.apple.com/app/id458270120?ls=1&mt=8" : -1 != ua.indexOf("android") ? location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.Kingdee.Express&ckey=CK1413979100250" : location.href = "//m.kuaidi100.com/app/"
}

function openApp(e, i) {
    var n = -1 != ua.indexOf("iphone") || -1 != ua.indexOf("ipad"), o = -1 != ua.indexOf("android");
    if (e = e || "kuaidi100://", -1 != ua.indexOf("micromessenger")) location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.Kingdee.Express&ckey=CK1413979100250&" + (n ? "ios_scheme=" : "android_schema=") + encodeURIComponent(e); else if (n) {
        window.location.href = e;
        var a = +new Date;
        setTimeout(function () {
            !window.document.webkitHidden && setTimeout(function () {
                +new Date - a < 2e3 && (window.location = i ? "https://itunes.apple.com/app/apple-store/id458270120?pt=117859344&ct=" + i.toLowerCase() + "&mt=8" : "http://itunes.apple.com/app/id458270120?ls=1&mt=8")
            }, 400)
        }, 500)
    } else if (o) {
        var c = document.createElement("script");
        c.async = "async", c.src = "https://127.0.0.1:10100", c.onload = function () {
            window.location.href = e, clearTimeout(t)
        }, document.getElementsByTagName("head")[0].appendChild(c), t = setTimeout(function () {
            location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.Kingdee.Express&ckey=CK1413979100250&android_schema=" + encodeURIComponent(e)
        }, 1e3)
    }
}

function setHref() {
    var e = document.querySelectorAll("a"), t = e.length, i = "", n = [], o = "";
    if (t) for (var a = 0; a < t; a++) if (i = e[a].getAttribute("href")) {
        if (0 === i.indexOf("#") || 0 === i.indexOf("tel:")) continue;
        n = i.split("?"), o = i.split("#")[1] || "", n[1] = n[1] ? n[1].replace("#" + o, "") : "", i = (n[0] + "?" + query).replace(/&+$/, "") + (n[1] ? "&" + n[1] : "") + (o ? "#" + o : ""), e[a].setAttribute("href", i.replace(/\?$/, ""))
    }
}

function to(e) {
    location.href = e + (query ? "?" + query : "")
}

function badge(t, i) {
    var e = new Date, n = e.getFullYear() + "_" + e.getMonth() + "_" + e.getDate(),
        o = localStorage.getItem(token + "_lastSignin"), a = 0 == location.pathname.indexOf("/message");
    "unread" != localStorage.getItem(token + "_newflag") && token ? request("/mobile/messageapi.do?method=checkNewMessage", {
        data: {
            applicationType: "H5",
            version: 1
        }, handleFail: !1, handleLogin: !1, handleError: !1, success: function (e) {
            200 == e.status && e.newestflag && (isFunc(t) && t(), !a && localStorage.setItem(token + "_newflag", "unread"))
        }
    }) : (isFunc(t) && t(), a && localStorage.removeItem(token + "_newflag")), n != o && (token ? request("/apicenter/creditmall.do?method=checksignin", {
        handleFail: !1,
        handleLogin: !1,
        handleError: !1,
        success: function (e) {
            201 == e.status ? i() : 200 == e.status && localStorage.setItem(token + "_lastSignin", n)
        }
    }) : isFunc(i) && i())
}

function goBack() {
    document.referrer ? history.back() : to(coname ? "/app/" : "/")
}

function tglMenu(e) {
    tglItem = document.getElementById("menuList"), tglItem && (e ? (tglItem.querySelector("ul").classList.remove("down"), setTimeout(function () {
        tglItem.style.display = "none"
    }, 300)) : (tglItem.style.display = "block", setTimeout(function () {
        tglItem.querySelector("ul").classList.add("down")
    }, 1)))
}

function jumpQuickApp(e) {
    var t = /Android/i.test(ua), i = 0 <= ua.indexOf("build/huawei"), n = null, o = +getStorage("quickAppTime");
    if (!(o && +o + 864e5 > Date.now()) && (setStorage("quickAppTime", Date.now()), !isBaidu && t && !isApp && !isWechat && !isYzj && !isQuickApp && !isMiniProgram && (!appid || !coname))) try {
        (n = document.createElement("script")).type = "text/javascript", n.src = i ? "//appimg.dbankcdn.com/hwmarket/files/fastapp/router.fastapp.js" : "//statres.quickapp.cn/quickapp/js/routerinline.min.js", n.onload = n.onreadystatechange = function () {
            this.readyState && "loaded" !== this.readyState && "complete" !== this.readyState || (appRouter("com.application.kuaidi100", (e || "/index") + "?channel=H5", {channel: "H5"}, "快递100小助手"), n.onload = n.onreadystatechange = null)
        }, document.getElementsByTagName("head")[0].appendChild(n)
    } catch (e) {
    }
}

function showDownload(e, t, i, n) {
    var o = new Date;
    try {
        if (isMiniProgram || isApp || isQuickApp || coname) return;
        var a = null, c = $("#main").size() ? $("#main") : $("body");
        if (i = void 0 === i ? "快递100 APP，查询寄件更轻松" : i, "nav" == (e = e || "nav")) {
            if (getStorage("nav_download_time") == "" + o.getFullYear() + o.getMonth() + o.getDate()) return;
            a = $('<div class="download-nav" role="container">                <div class="close" role="close"></div>                <div class="ico"></div>                <div class="text">' + i + '</div>                <div class="bt" role="download">打开APP</div>              </div>')
        } else if ("dialog" == e) a = $('<div class="m-mask flex center" role="container">                <div class="download-dialog">                  <div class="ico"></div>                  <div class="close" role="close"></div>                  <div class="title">' + i + '</div>                  <div class="text">此功能需要访问客户端才能使用</div>                  <div class="bt-txt" role="close">继续使用</div>                  <div class="bt" role="download">使用APP打开</div>                </div>              </div>'); else {
            if ("modal" != e) return;
            a = $('<div class="m-mask flex center" role="container">                <div class="download-modal">                  <div class="close" role="close"></div>                  <div class="text">快递100 APP</div>                  <div class="title">在线寄件更方便</div>                  <div class="bt" role="download">打开APP</div>                </div>              </div>')
        }
        a.on("click", "[role=close]", function (e) {
            var t = new Date;
            e.preventDefault(), $(this).closest(".download-nav").size() && setStorage("nav_download_time", "" + t.getFullYear() + t.getMonth() + t.getDate()), a.remove()
        }).on("click", "[role=download]", function (e) {
            e.preventDefault(), addlog("m_download_click_" + (t || "normal")), openApp(n || "", t)
        }), c.prepend(a), addlog("m_download_view_" + (t || "normal"))
    } catch (e) {
    }
}

function gloadCss(e) {
    var t = document, i = t.createElement("style"), n = t.createTextNode(e), o = t.getElementsByTagName("head");
    i.setAttribute("type", "text/css"), i.appendChild(n), o.length ? o[0].appendChild(i) : t.documentElement.appendChild(i)
}

fromClient && (query += query ? "&from=" + fromClient : "from=" + fromClient), setHref(), window.addEventListener("pageshow", function (e) {
    !e.persisted || !isWechat || openid && document.referrer || location.reload()
});