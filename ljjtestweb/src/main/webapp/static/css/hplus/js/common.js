/**
 * describ：公共js文件
 * author：_yys_yys_
 * time：2017.06.05
 * **/

/*全局禁止input框number属性鼠标的滚动事件*/
//$(function(){
//    var isFF = /FireFox/i.test(navigator.userAgent);
//    if(!isFF){
//        //非火狐
//        $("input[type='number']").attr("onmousewheel","return false");
//    }
//});

//自定义函数方法
$.fn.extend({
    //numBaseCheck：检验input输入框(只能输入数字，保留两位小数，没有小数点数首字母不能为零)
    numBaseCheck:function(decimalNum){
        $(this).on("keyup",function(event){
            var $this = $(this);
            var decimalTime = "", decimalTime2 = ",'$1$2.$3'";
            for(var i = 0; i < decimalNum; i++){
                decimalTime += "\\d";
            }
            var nReg = new RegExp("^(\\-)*(\\d+)\\.(" + decimalTime + ").*$");
            //响应鼠标事件，允许左右方向键移动 
            event = window.event || event;
            if(event.keyCode == 37 | event.keyCode == 39){
                return;
            }
            //先把非数字的都替换掉，除了数字和.
            $this.val($this.val().replace(/[^\d.]/g,""));
            //保证只有出现一个.而没有多个.
            $this.val($this.val().replace(/\.{2,}/g,"."));
            //必须保证第一个为数字而不是.
            $this.val($this.val().replace(/^\./g,""));
            //保证.只出现一次，而不能出现两次以上
            $this.val($this.val().replace(".","$#$").replace(/\./g,"").replace("$#$","."));
            //保留*位小数
            $this.val($this.val().replace(nReg));
            console.log(nReg);
            //没有小数点时数字首位不能为0
            if($this.val().indexOf(".")<0 && $this.val()!=""){
                $this.val(parseFloat($this.val()));
            }
        });
    },
    
    //tableScroll：表格内容横向纵向滚动,传入的参数tableHeight为用户想要控制显示的tbody高度
    tableScroll:function(tableHeight){
        var sourceTable = $(this);//表格table的id
        $(sourceTable).addClass("table-one-row");
        $(".table-one-row th,.table-one-row td").css({
            "white-space": "nowrap",
            "padding-left": "8px",
            "padding-right": "8px"
        });
        var sourceTableHead = $(sourceTable).find("thead").attr({"id":"tableHead","class":"color-f5"}); //表格thead的id
        var headHeight = sourceTableHead.height(); //表格thead的高
        var sourceTableWidth = sourceTable.outerWidth(); //获取表格的外宽度（不包括margin宽）
        //fixContent为thead的克隆模块
        var fixContent = '<div class="table-out-div-first">'+
                            '<div id="fixedTableDiv">'+
                            '<table id="fixedTable"><thead></thead></table>'+
                            '</div>'+
                        '</div>';
        var targetHead = sourceTableHead.clone().attr("id", "fix_head");
        $(sourceTable).wrap('<div class="table-out-div"></div>');
        $(".table-out-div").css({
            "overflow": "auto",
            "width": "100%",
            "height": tableHeight + "px",
            "border-bottom": "1px solid #e7eaec"
        });
        $(".table-out-div").parent().prepend(fixContent);
        $("#fixedTable").attr("class",$(sourceTable).attr("class"));
        $(".table-out-div-first").css({
            "position": "relative",
            "width": "100%"
        })
        $("#fixedTableDiv").css({
            "height": headHeight + "px",
            "position": "absolute",
            "top": "0",
            "left": "0",
            "z-index": "2"
        });
        $("#fixedTable").css({
            'width': sourceTableWidth + "px"
        });
        targetHead.appendTo('#fixedTable thead');
        $("#tableHead th").each(function(index, value){
            var tempWidth = $(value).outerWidth();
            var tempHeight = $(value).outerHeight();
            $("#fixedTable th").eq(index).css({
                "width": tempWidth + "px",
                "height": tempHeight + "px"
            });
        });
        
//      if($(sourceTable).find("tfoot").length > 0){
//          console.log("t");
//          var sourceTableFoot = $(sourceTable).find("tfoot").attr({"id":"tableFoot","class":"color-f5"}); //表格tfoot的id
//          var footHeight = sourceTableFoot.height(); //表格tfoot的高
//      }else{
//          console.log("f");
//      }
        
        
        
        $("#fixedTableDiv").show();
        $(".table-out-div").scroll(function(){
            var distanceLeft = -Math.max($(".table-out-div").scrollLeft(), $(".table-out-div").scrollLeft());
            if(isNaN(distanceLeft)){
                distanceLeft = -$(".table-out-div").scrollLeft();
            }
            $("#fixedTableDiv").css("left", distanceLeft + "px");
        });
    },
    
    //imgBig: 鼠标移过去图片放大
    imgBig:function(){
        var x = 10;
        var y = 20;
        $(".table-center-img").mouseover(function(e){
            var tooltip = "<div id='tooltip'><img src='" + this.src + "' alt='预览图' style='max-width:300px;max-height:300px' /></div>";
            $("body").append(tooltip);
            $("#tooltip").css({
                "position": "absolute",
                "z-index": "999999999",
                "border": "2px solid #fff",
                "box-shadow": "1px 1px 5px #848484",
                "background-color": "#fff"
            }).show("fast"); //设置x坐标和y坐标，并且显示
            if(e.clientY + y + $("#tooltip img").height() < $(window).height()){
                $("#tooltip").css({
                    "left": (e.pageX + x) + "px",
                    "top": (e.pageY + y) + "px"
                }).show("fast");
            }else{
                $("#tooltip").css({
                    "left": (e.pageX + x + 10) + "px",
                    "top": (e.pageY - $("#tooltip img").height()) + "px"
                }).show("fast");
            }
        }).mouseout(function(){
            $("#tooltip").remove(); //移除 
        }).mousemove(function(e){
            $("#tooltip").css({
                "position": "absolute",
                "z-index": "999999999",
                "border": "2px solid #fff",
                "box-shadow": "1px 1px 5px #848484",
                "background-color": "#fff"
            }).show("fast"); //设置x坐标和y坐标，并且显示
            if(e.clientY + y + $("#tooltip img").height() < $(window).height()){
                $("#tooltip").css({
                    "left": (e.pageX + x) + "px",
                    "top": (e.pageY + y) + "px"
                }).show("fast");
            }else{
                $("#tooltip").css({
                    "left": (e.pageX + x + 10) + "px",
                    "top": (e.pageY - $("#tooltip img").height()) + "px"
                }).show("fast");
            }
        });
    },
    
    //combineTrTd：合并table相同数据的行和列
    //参数：tb(需要合并的表格ID)；colLength(需要对前几列进行合并)；缺省表示对全部列合并
    combineTrTd:function(tb,colLength){
        //检查表格是否规整 
        if(!checkTable(tb)){
            return;
        }
        var i = 0;
        var j = 0;
        var rowCount = $(tb).find("tbody tr").length;//行数
        var colCount = $(tb).find("tbody tr:eq(0)").find("td").length;//列数
        var obj1 = null;
        var obj2 = null;
        //为每个单元格命名
        for(i = 0;i < rowCount;i++){
            for(j = 0;j < colCount;j++){
                $(tb).find("tbody tr:eq("+i+")").find("td:eq("+j+")").attr("id","tb_" + i + "_" + j);
            }
        }
        //逐列检查合并 
        for(i = 0;i < colCount;i++){
            if(i == colLength){
                return;
            }
            obj1 = $("#tb_0_" + i);
            var rowspan = 1;
            for(j = 1; j < rowCount; j++) {
                obj2 = $("#tb_" + j + "_" + i);
                if(obj1.text() == obj2.text()){
                    rowspan++;
                    obj1.attr("rowspan",parseInt(rowspan));
                    $(obj2).remove();
                }else{
                    obj1 = $("#tb_" + j + "_" + i);
                }
            }
        }
        
        //功能：检查表格是否规整 
        function checkTable(tb){
            if($(tb).find("tbody tr").length == 0){
                return false;
            }
            if($(tb).find("tbody tr:eq(0)").find("td").length == 0){
                return false;
            }
            for(var i = 0; i < $(tb).find("tbody tr").length; i++){
                if($(tb).find("tbody tr:eq(0)").find("td").length != $(tb).find("tbody tr:eq("+i+")").find("td").length){
                    return false;
                }
            }
            return true;
        }
    },
})