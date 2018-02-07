/**
 * describ：表格数据的复制粘贴功能整合
 * author：_yys_yys_
 * time：2017.04.16
 * **/

(function($){
    var defaults = {
        cjtable: "cjtable",
        cjtd: "cjtd"
    };
    
    //定义一个点对象
    function Point(){
        this.x = 0;
        this.y = 0;
    }
    
    //初始化表格
    $.fn.createEditTable = function(){
        var tb = this;
        this.addClass(defaults.cjtable);
        this.find("td").addClass(defaults.cjtd);
        $(document).bind("mousedown", docDownhandle);
        //处理鼠标按下事件
        this.find("tbody tr td").mousedown(function(event){
            event.stopPropagation();
            if(event.button != 2){
                var colIndex = $(this).index();
                if($(this).find("input").get().length > 0){
                    return;
                }
                endEdit();
                tb.find("td").removeClass("selectClass").attr("startMouseDown","no");
                $(this).attr("startMouseDown","yes");
                if(!$(this).is(".notChecked")){
                    $(this).addClass("selectClass");
                }
                tb.find("tbody td").each(function(){
                    var colIndex = $(this).index();
                    $(this).bind("mousemove",cellMove);
                });
                $(document).bind("mouseup",uphandle);
            }
        }).bind("dblclick",dblclick);

        $(document).bind("keydown",nt);

        //鼠标移动的处理事件
        function cellMove(){
            setSelectRegion.call(this);
        }

        function docDownhandle(){
            tb.find("td").removeClass("selectClass").attr("startMouseDown","no");
            endEdit();
        }
        
        //鼠标抬起的处理事件
        function uphandle(tempTd){
            tb.find("tbody td").unbind("mousemove",cellMove);
            // $(tempTd).parent().parent().find("td").unbind("mousemove",cellMove);
            $(this).unbind("mouseup",uphandle);
        }
        
        //设置选中区域
        function setSelectRegion(){
            var spoint = new Point();
            var epoint = new Point();
            var startCells = $(this).parent().parent().find("td[startMouseDown='yes']").get();
            if(startCells.length == 0){
                return;
            }
            var startCell = startCells[0];
            endCell = this;
            if(startCell == endCell){
                return;
            }
            $(this).parent().parent().find("td").removeClass("selectClass");
            spoint.x = $(startCell).index() < $(endCell).index() ? $(startCell).index() : $(endCell).index();
            spoint.y = $(startCell).parent().index() < $(endCell).parent().index() ? $(startCell).parent().index() : $(endCell).parent().index();
            epoint.x = $(startCell).index() > $(endCell).index() ? $(startCell).index() : $(endCell).index();
            epoint.y = $(startCell).parent().index() > $(endCell).parent().index() ? $(startCell).parent().index() : $(endCell).parent().index();
            for(var i = spoint.y; i <= epoint.y; i++){
                for(var j = spoint.x; j <= epoint.x; j++){
                    tb.find("tbody tr").eq(i).children().eq(j).addClass("selectClass");
                }
            }
            tb.find(".selectClass").each(function(){
                if($(this).is(".notChecked")){
                    $(this).removeClass("selectClass");
                }
            })
        }
        //从单元格中获取值
        function getValueFromCell(){
            return $(this).text();
        }
        //处理双击编辑事件
        function dblclick(event){
            startEdit.call(this);
        }
        //开始编辑
        function startEdit(startEditValue){
            if($(this).find("input").get().length > 0){
                return;
            }
            endEdit();
            var cellValue = $.trim(getValueFromCell.call(this));
            if(startEditValue != null){
                cellValue = startEditValue;
            }
            $(this).attr("isEdit", "yes");
            $(this).parent().parent().find("td").removeClass("selectClass");
            //控制input只允许输入数字和小数点
            var inp;
            if(!$(this).is(".notChecked")){
            	if($(this).index()==0){
                    inp = $("<input type='text' class='form-control text-align-left'>");
                    inp.on("keyup",function(event){
                        var $inp = $(this);
                        //响应鼠标事件，允许左右方向键移动 
                        event = window.event || event;
                        if(event.keyCode == 37 | event.keyCode == 39){
                            return;
                        }
                    });
                }else if($(this).is(".isFirstTd")){
                    inp = $("<input type='text' class='form-control text-align-left'>");
                    inp.on("keyup",function(event){
                        var $inp = $(this);
                        //响应鼠标事件，允许左右方向键移动 
                        event = window.event || event;
                        if(event.keyCode == 37 | event.keyCode == 39){
                            return;
                        }
                    });
                }else{
                    inp = $("<input type='text' class='form-control text-align-center'>");
                    inp.on("keyup",function(event){
                        var $inp = $(this);
                        //响应鼠标事件，允许左右方向键移动 
                        event = window.event || event;
                        if(event.keyCode == 37 | event.keyCode == 39){
                            return;
                        }
                        //先把非数字的都替换掉，除了数字和.
    	            	$inp.val($inp.val().replace(/[^\d.]/g,""));
    	        	    //保证只有出现一个.而没有多个.
    	            	$inp.val($inp.val().replace(/\.{2,}/g,"."));
    	        	    //必须保证第一个为数字而不是.
    	            	$inp.val($inp.val().replace(/^\./g,""));
    	        	    //保证.只出现一次，而不能出现两次以上
    	            	$inp.val($inp.val().replace(".","$#$").replace(/\./g,"").replace("$#$","."));
    	        	    //最多只能输入三个小数
    	            	$inp.val($inp.val().replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$1$2.$3'));
    	        	    //没有小数点时数字首位不能为0
    	        	    if($inp.val().indexOf(".")<0 && $inp.val()!=""){
    	        	    	$inp.val(parseFloat($inp.val()));
    	        	    }
                    });
                }
            }
            $("input").blur(function(){
                var $inp = $(this);
                $inp.val($inp.val().replace(/\.$/g, ""));
            });
            inp.val(cellValue);
            $(this).html(inp);
        }
        //结束编辑
        function endEdit(){
            var currentEditCells = tb.find("tbody td[isEdit='yes']").get();
            var attr = $(currentEditCells).attr('attr-valid');
        	var currentEditCell;
        	if(currentEditCells.length > 0){
        		currentEditCell = currentEditCells[0];
        		var ivalue = "";
        		if($(currentEditCell).find("input").get().length == 0){
        			ivalue = $(currentEditCell).text();
        		}else{
        			ivalue = $(currentEditCell).find("input").val();
        		}
        		$(currentEditCell).html("");
        		$(currentEditCell).append(ivalue);
        		$(currentEditCell).attr("isEdit",'no');
        		if(attr == "area"){
        			transportCommonMdl.areaRepeatValid(currentEditCells);
        		}
        	}
        }
        //先把要粘贴的数据放入textArea控件中
        function dealwithData(event){
            var ss = document.getElementsByClassName("tableCPtextArea")[0];
            var reg = /[\s]+/g;
            var arr = ss.value.split(reg);
//          ss.blur();
            dealCtrlV(arr);
        }
        //处理ctrl+v
        function dealCtrlV(arr){
            var arrtd = tb.find(".selectClass").get();
            for(var i = 0; i < arrtd.length; i++){
                if(i < arr.length && arr[i] != ""){
                    $(arrtd[i]).html("");
                    $(arrtd[i]).append(arr[i]);
                    $(arrtd[i]).attr("isEdit", 'no');
//                  $(arrtd[i]).html(arr[i]);
                }
            }
            tb.find(".selectClass").each(function(){
                if(isNaN($(this).html()) && !$(this).is(".isFirstTd") && $(this).index()!=0){
                    $(this).html("");
                }
            })
        }
        //处理键盘上下左右移动
        function nt(event){
            if(event.ctrlKey && event.keyCode == 86){
                var ss = document.getElementsByClassName("tableCPtextArea")[0];
//              $(ss).focus();
                $(ss).select();
                //等50毫秒，keyPress事件发生了再去处理数据 
                setTimeout(dealwithData,50);
            }else{
                var currentEditCells = tb.find("tbody td[isEdit='yes']").get();
                var currentEditCell;
                if(currentEditCells.length > 0){
                    currentEditCell = currentEditCells[0];
                }
                var arrtd = tb.find("tbody td.selectClass").get();
                if(arrtd.length == 1 || currentEditCell != null){
                    var currentSelectCell;
                    if(currentEditCell != null){
                        currentSelectCell = currentEditCell;
                    }else{
                        currentSelectCell = arrtd[0];
                    }
                    var myColIndex = $(currentSelectCell).index();
                    var myRowIndex = $(currentSelectCell).parent().index();
                    if(event.keyCode == 37){ //LEFT左
                        event.stopPropagation();
                        event.preventDefault();
                        endEdit.call(currentSelectCell);
                        tb.find("td").removeClass('selectClass');
                        if(myColIndex == 1){
                            myColIndex = 0;
                        }
                        $(currentSelectCell).parent().find('td:eq(' + (--myColIndex) + ')').addClass('selectClass');
                    }else if(event.keyCode == 39){ //RIGHT右
                        event.stopPropagation();
                        event.preventDefault();
                        endEdit.call(currentSelectCell);
                        tb.find("td").removeClass('selectClass');
                        if($(currentSelectCell).parent().find('td:eq(' + (++myColIndex) + ')').get().length == 0){
                            $(currentSelectCell).parent().find('td:eq(1)').addClass('selectClass');
                        }else{
                            $(currentSelectCell).parent().find('td:eq(' + (myColIndex) + ')').addClass('selectClass');
                        }
                    }else if(event.keyCode == 38){ //UP上
                        event.stopPropagation();
                        event.preventDefault();
                        endEdit.call(currentSelectCell);
                        tb.find("td").removeClass('selectClass');
                        $(currentSelectCell).parent().parent().find('tr:eq(' + (myRowIndex - 1) + ')').find('td:eq(' + (myColIndex) + ')').addClass('selectClass');
                    }else if(event.keyCode == 40){ //DOWN下
                        event.stopPropagation();
                        event.preventDefault();
                        endEdit.call(currentSelectCell);
                        tb.find("td").removeClass('selectClass');
                        if($(currentSelectCell).parent().parent().find('tr:eq(' + (myRowIndex + 1) + ')').get().length == 0) {
                            $(currentSelectCell).parent().parent().find('tr:eq(0)').find('td:eq(' + (myColIndex) + ')').addClass('selectClass');
                        }else{
                            $(currentSelectCell).parent().parent().find('tr:eq(' + (myRowIndex + 1) + ')').find('td:eq(' + (myColIndex) + ')').addClass('selectClass');
                        }
                    }else if(event.keyCode == 8){ //Backspace后退
                        event.stopPropagation();
                        event.preventDefault();
                        endEdit.call(currentSelectCell);
                        $(currentSelectCell).html("");
                    }
                    else{
                        var re = /[0-9,a-z,A-Z]$/;
                        var str = String.fromCharCode(event.keyCode);
                        if(re.test(str)){
                            if(currentEditCell == null && currentSelectCell != null){
                                startEdit.call(currentSelectCell,"");
                                //alert(re.test(str));
                            }
                        }
                    }
                }
            }
        }
    }
})(jQuery);