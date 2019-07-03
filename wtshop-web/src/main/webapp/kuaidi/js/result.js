var vm = new Vue({
    el: "#main",
    data: {
        num: GetQueryString("nu").replace(/\W/g, ""),
        com: GetQueryString("com").replace(/\W/g, ""),
        selectCom: GetQueryString("com").replace(/\W/g, ""),
        showHistory: !1,
        history: [],
        autos: [],
        lists: [],
        alllists: [],
        cominfo: {},
        desc: !0,
        showAll: !1,
        loading: !1,
        check: !1,
        errors: {message: "", title: "", type: "empty"},
        checkCode: {show: !1, value: ""},
        scanable: window.scanable,
        fromApp: "appshare" == GetQueryString("kdfrom") || "appshare" == GetQueryString("from") || "singlemessage" == GetQueryString("from")
    },
    methods: {
        getHistory: function () {
            this.showHistory = !0, this.history = [], token ? request("/apicenter/xcx.do?method=myquerylist", {
                success: function (t) {
                    vm.history = t.items || []
                }, handleLogin: !1, handleFail: !1
            }) : this.getLocalHistory()
        }, getLocalHistory: function () {
            this.history = getStorage("queryHistoryList") || []
        }, toggleHistory: function (t) {
            setTimeout(function () {
                this.showHistory = t
            }.bind(this), 0), t && this.getHistory()
        }, validate: function () {
            return /^\w+$/.test(this.num)
        }, doCheckCode: function () {
            this.checkCode.value.length < 4 ? tips("请输入正确的手机后四位", 1500) : this.query()
        }, closeCheckCode: function () {
            this.getError("empty", "查询顺丰单号需要验证手机号", "请输入收件或者寄件人后四位手机号码"), this.checkCode.show = !1
        }, historyQuery: function (t, e) {
            this.num = t, this.com = e, this.query()
        }, query: function () {
            console.info("dddddddd");
            console.info(this.com);
            var e = this;
            if (this.num) if (this.validate()) if (this.com) {
                if (this.checkCode.value = this.checkCode.value || getStorage("checkCode_" + this.num) || "", "shunfeng" == this.com && !this.checkCode.value) return this.checkCode.show = !0, void(this.loading = !1);
                this.loading = !0, this.alllists = [], this.errors.type = "", cominfo(this.com, function (t) {
                    e.cominfo.name = t.name, e.cominfo.tel = t.contactTel
                }), request("../api/login/query.jhtml", {
                    data: {
                        postid: this.num,
                        id: 1,
                        valicode: "",
                        temp: Math.random(),
                        type: this.com,
                        phone: this.checkCode.value
                    }, success: function (t) {
                        vm.showList(t)
                    }, fail: function (t) {
                        "408" == t.status ? tips("手机后四位输入错误") : vm.getError("empty", "快递100查无记录", "请核对你的公司和单号是否正确")
                    }, complete: function (t) {
                        vm.loading = !1, "408" != t.status && "404" != t.status && vm.checkCode.value && "shunfeng" === vm.com && setStorage("checkCode_" + vm.num, vm.checkCode.value), "408" != t.status && (vm.checkCode.value = "", vm.checkCode.show = !1)
                    }, error: function () {
                        vm.loading = !1, vm.getError("network", "网络错误", "请检查您的网络，稍后刷新页面重试")
                    }
                })
            } else this.auto(); else tips("请输入正确的单号")
        }, getError: function (t, e, i) {
            this.errors.type = t, this.errors.title = e, this.errors.message = i
        }, auto: function () {
            this.loading = !0, this.autos = [], this.com = "", $.ajax({
                url: "../api/login/kdquerytools.jhtml?method=autoComNum&text=" + this.num,
                dataType: "json",
                success: function (t) {
                    t && t.auto && t.auto.length ? (vm.autos = t.auto, vm.autos.push({
                        comCode: "other",
                        name: "其他快递公司"
                    }), vm.com = vm.autos[0].comCode, vm.query()) : (this.getError("none", "快递100识别不了单号的公司", "请核对单号是否正确，或手动选择快递公司"), vm.loading = !1)
                },
                error: function () {
                    vm.loading = !1, vm.getError("network", "网络错误", "请检查您的网络，稍后刷新页面重试")
                }
               /* url: "/apicenter/kdquerytools.do?method=autoComNum&text=" + this.num,
                dataType: "json",
                success: function (t) {
                    t && t.auto && t.auto.length ? (vm.autos = t.auto, vm.autos.push({
                        comCode: "other",
                        name: "其他快递公司"
                    }), vm.com = vm.autos[0].comCode, vm.query()) : (this.getError("none", "快递100识别不了单号的公司", "请核对单号是否正确，或手动选择快递公司"), vm.loading = !1)
                },
                error: function () {
                    vm.loading = !1, vm.getError("network", "网络错误", "请检查您的网络，稍后刷新页面重试")
                }*/
            })
        }, showList: function (t) {
            this.alllists = t.data, "FL00" === t.condition && this.alllists.unshift({
                context: "快递100发放寄件福利，点击立即领取!",
                ftime: this.alllists[0].time,
                type: "gift"
            }), this.desc && this.lists.reverse(), this.showHistory = !1, this.saveHistory()
        }, saveHistory: function () {
            var e = getStorage("queryHistoryList") || [];
            if (token) request("/apicenter/xcx.do?method=savemyquery", {
                data: {com: this.com, nu: this.num},
                handleFail: !1,
                handleLogin: !1
            }); else {
                for (var t = 0; t < e.length; t++) if (e[t].kuaidiNum == this.num && e[t].kuaidiCom == this.com) {
                    e.splice(t, 1);
                    break
                }
                cominfo(this.com, function (t) {
                    e.unshift({
                        kuaidiCom: this.com,
                        kuaidiNum: this.num,
                        comName: t.shortName,
                        lastModify: Date.now()
                    }), e = e.slice(0, 15), setStorage("queryHistoryList", e)
                }.bind(this))
            }
        }, goDispatchCoupon: function () {
            location.href = "https://m.kuaidi100.com/activity/dispatchCoupon.jsp"
        }, scan: function () {
            this.scanable && "function" == typeof window.scan ? scan(function (t) {
                vm.com = "", vm.num = t, vm.query()
            }) : isWechat && !isMiniProgram && "undefined" != typeof wx ? (this.com = "", wx.scanQRCode({
                desc: "请扫描快递单上的条形码",
                needResult: 1,
                scanType: ["qrCode", "barCode"],
                success: function (t) {
                    var e = t.resultStr.split(","), i = 1 < e.length ? e[1] : e[0];
                    /^\w+$/.test(i) ? (vm.num = i, vm.query()) : tips("没有扫到有效的单号")
                },
                error: function (t) {
                    0 < t.errMsg.indexOf("function_not_exist") && tips("版本过低请升级")
                }
            })) : showDownload("dialog", "indexScanDialog", "扫码查快递更轻松")
        }, download: function () {
            var t = "kuaidi100://ilovegirl/" + (-1 != ua.indexOf("iphone") || -1 != ua.indexOf("ipad") ? "express" : "query") + "?num=" + this.num + "&com=" + this.com;
            openApp(t, "appshareResult")
        }
    },
    computed: {
        historyList: function () {
            for (var t = [], e = 0; e < this.history.length; e++) -1 < this.history[e].kuaidiNum.indexOf(this.num) && t.push(this.history[e]);
            return t.slice(0, 15)
        }
    },
    filters: {
        date: function (t) {
            return t.substring(0, 10).replace(/-/g, ".")
        }, time: function (t) {
            return t.substring(11, 16)
        }, context: function (t) {
            return t.replace(/(1\d{10})/g, '<a href="tel:$1">$1</a>')
        }
    },
    watch: {
        num: function (t) {
            this.num = t.replace(/\s/g, "")
        }, "checkCode.value": function (t) {
            this.checkCode.value = t.replace(/\D/g, "").slice(0, 4)
        }, desc: function () {
            this.lists.reverse()
        }, alllists: function (t) {
            this.lists = this.fromApp || !this.showAll ? t.slice(0, 3) : t.slice(0)
        }, showAll: function (t) {
            this.lists = t ? this.alllists.slice(0) : this.alllists.slice(0, 3)
        }, selectCom: function (t) {
            "other" == t ? (sessionStorage.setItem("lastQueryNum", this.num), window.location.replace(coname ? "/app/query/all.jsp?" + query : "/all/?" + query)) : "auto" == t ? (this.com = "", this.query()) : t && (this.com = t, this.query())
        }
    },
    mounted: function () {
        appLogin(), this.query(), this.com && cominfo(this.com, function (t) {
            this.autos.push({comCode: t.number, name: t.shortName}, {
                comCode: "auto",
                name: "识别快递公司"
            }, {comCode: "other", name: "其他快递公司"})
        }.bind(this))
    }
});