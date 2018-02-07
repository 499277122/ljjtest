var Base = {};

var Aop = {
	init : function(name, f) {
		for ( var i in name) {
			Aop.before(name[i], f);
		}
	},
	before : function(name, f) {
		var thisObj = this;
		var orign = eval(name);
		var func = function() {
			var success = 0;
			for ( var i in f) {
				if (f[i].apply(this, arguments)) {
					success++;
				}
			}
			if (success > 0) {
				return orign.apply(this, arguments);
			}
		};
		eval(name + " = func;");
	}
};

Base = {
	ajax : function(url, type, data, async, dataType, callBack) {
		var index;
		$.ajax({
			type : type,
			url : url,
			data : data,
			async : async,
			dataType : dataType,
			beforeSend : function() {
				index = layer.load(1, {
					shade : [ 0.1, '#fff' ]
				});
			},
			success : function(data) {
				layer.close(index);
				callBack(data);
			}
		});
	},
	postAsync : function(url, data, dataType, callBack) {
		Base.ajax(url, "POST", data, true, dataType, callBack);
	},
	postSync : function(url, data, dataType, callBack) {
		Base.ajax(url, "POST", data, false, dataType, callBack);
	},
	getAsync : function(url, data, dataType, callBack) {
		Base.ajax(url, "GET", data, true, dataType, callBack);
	},
	getSync : function(url, data, dataType, callBack) {
		Base.ajax(url, "GET", data, false, dataType, callBack);
	},
	// 公共utils操作
	utils : {
		isIE : function() {
			return $.browser.msie;
		},
		isIE6 : function() {
			return window.VBArray && !window.XMLHttpRequest;
		},
		basePath : function() {
			return location.href.substring(0, location.href.lastIndexOf('/'));
		},
		// 空字符串判断
		isEmpty : function(v) {
			return v == null || v == undefined || v == "";
		},
		// 空对象判断
		isNull : function(obj) {
			if (typeof obj == "undefined" || obj == null) return true;
			else return false;
		}
	},
	model : {
		close : function(divId) {
			$("#" + divId).find(".modal").removeClass("in").hide();
			$(".modal-backdrop").remove();
			$('body').removeClass("modal-open").removeAttr("style");
		}
	},
	validate : {
		extend : function() {
			// 邮政编码验证
			jQuery.validator.addMethod("isZipCode", function(value, element) {
				var tel = /^[0-9]{6}$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入有效的邮政编码");
			// 身份证验证
			jQuery.validator.addMethod("isCard", function(value, element) {
				var tel = /^\d{15}|\d{18}$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入有效的身份证号码");
			// 电话号码验证 正确格式为："xxx-XXXXXXX"、"XXXX-XXXXXXXX"、"xxx-XXXXXXX"、"xxx-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX"。
			jQuery.validator.addMethod("isPhone", function(value, element) {
				var tel = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,11}$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入有效的电话号码 例:区号-号码(020-0000000)");
			// 身份证验证
			jQuery.validator.addMethod("isMobile", function(value, element) {
				var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$|(^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$)/;
				return this.optional(element) || (tel.test(value));
			}, "请输入有效的手机号码");
			// 浮点数验证
			jQuery.validator.addMethod("decimal", function(value, element) {
				var tel = /^(?:0\.[1-9]{1,2}|[1](?:\.0)?)$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入正确数字");
			// 正数双精度浮点数验证不可为0
			jQuery.validator.addMethod("precision", function(value, element) {
				var tel = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入正确数字");
			// 正数双精度浮点数验证可为0
			jQuery.validator.addMethod("precisionCouldZero", function(value, element) {
				var tel = /^(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入正确数字");
			// 正数验证不为0
			jQuery.validator.addMethod("notZero", function(value, element) {
				var tel = /^[1-9]\d*$/;
				return this.optional(element) || (tel.test(value));
			}, "请输入正确数字");
			// 数字或英文
			jQuery.validator.addMethod("numberAndEnglish", function(value, element) {
				var tel = /^[A-Za-z0-9\-]+$/;
				return this.optional(element) || (tel.test(value));
			}, "数字或英文字符");
		}
	},
	cookie : {
		// 删除cookie
		delCookie : function(name) {
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval = getCookie(name);
			if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
		},
		// 获取cookie
		getCookie : function(c_name) {
			if (document.cookie.length > 0) {
				var c_start = document.cookie.indexOf(c_name + "=");
				if (c_start != -1) {
					c_start = c_start + c_name.length + 1;
					var c_end = document.cookie.indexOf(";", c_start);
					if (c_end == -1) {
						c_end = document.cookie.length;
					}
					return unescape(decodeURIComponent(document.cookie.substring(c_start, c_end)));
				}
			}
			return "";
		},
		// 设置cookie
		setCookie : function(c_name, value, expiredays) {
			var exdate = new Date();
			exdate.setDate(exdate.getDate() + expiredays);
			document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
		},
		// 判对cookie是否存在
		isCookie : function(name) {
			var username = getCookie(name);
			if (Base.utils.isEmpty(username)) {
				return false;
			}
			return true;
		}
	},
	isLogin : function() {
		var flag = false;
		if (ifCheck == 'false') {
			return true;
		}
		var token = Base.cookie.getCookie("TM_TOKEN");
		var loginName = Base.cookie.getCookie("TM_LoginName");
		if (!Base.utils.isEmpty(token) && !Base.utils.isEmpty(loginName)) {
			$.ajax({
				type : "POST",
				url : jsCtx + "/isLogin",
				data : {
					"token" : token,
					"loginName" : loginName
				},
				async : false,
				dataType : "json",
				/*
				 * dataType : "jsonp", jsonp : "jsonpcallback",
				 */
				success : function(data) {
					if (data.flag) {
						flag = true;
					} else {
						layer.msg(data.msg, {
							icon : 5
						});
						flag = false;
					}
				}
			});
		} else {
			layer.msg("该登录用户会话过期，请重新登录！", {
				icon : 5
			});
			flag = false;
		}
		return flag;
	},
	dateFormat : function(datetime) {
		var date = new Date(datetime);
		Y = date.getFullYear() + '-';
		M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		D = date.getDate() + ' ';
		h = date.getHours();
		m = date.getMinutes();
		s = date.getSeconds();
		if (D.toString().length == 1) {
			D = "0" + D;
		}
		if (h.toString().length == 1) {
			h = "0" + h;
		}
		if (m.toString().length == 1) {
			m = "0" + m;
		}
		if (s.toString().length == 1) {
			s = "0" + s;
		}
		return Y + M + D + "&nbsp;" + h + ":" + m + ":" + s;
	}
};
Base.validate.extend();