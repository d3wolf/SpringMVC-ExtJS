<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Spider Main</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
	function setSpiderPanel() {//根据不同的节点值,获取不同的加载数据(可以是节点ID\节点文本等等)
		Ext.tip.QuickTipManager.init();
		
		var jokePanel = Ext.create('Ext.panel.Panel', {
		    title: 'joke spider[<a href="http://xiaohua.zol.com.cn/new/1.html" target="_blank">original data</a>]',
		    width: 400,
		    id : "jokePanel",
		    bodyPadding: 10,
		    renderTo: Ext.getBody(),
		    layout : 'hbox',
		    items: [{
		    	xtype: 'numberfield',
		        anchor: '100%',
		        id: 'urlIndex',
		        width : 80,
		        value: 1,
		        minValue: 1
		    },{
		    	xtype : 'button',
		    	text: '抓取指定页',
		    	style: {
		        //     marginBottom: '10px',//距底部高度
		             marginLeft:'10px',//距左边宽度
		             marginRight:'10px'//距右边宽度
		    	},
		    	handler : function(){
		    		var value = Ext.getCmp('urlIndex').value;
		    		fentchData("${pageContext.request.contextPath}/spider/joke.do",{urlIndex : value}, false);
		    	}
		    },
		    {
		    	xtype : 'button',
		    	text: '抓取前N页',
		    	handler : function(){
		    		var value = Ext.getCmp('urlIndex').value;
		    		fentchData("${pageContext.request.contextPath}/spider/jokes.do",{urlIndex : value}, true);
		    	}
		    }]
		});
	}
	
	function fentchData(url, params, needProcessBar){
		//执行抓取动作
    	Ext.Ajax.request({
		    url: url,
		    method : 'post',
		    params : params,
		    timeout : 3000000,
		    success: function(response){
		    	var rpText = response.responseText;
		    	if(!needProcessBar){
		    		Ext.MessageBox.alert("抓取信息",rpText);
		    	}
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "获取失败:\n" + response.responseText);
		    }
		});
    	
		//显示进度条  
		if(needProcessBar){
	        Ext.MessageBox.show({
	            title: '正在抓取',
	            width:240,
	            progress:true,
	            closable:false,
	         //   buttons: Ext.Msg.OK
	        }); 
	     //  	var processBar = Ext.MessageBox.progress("正在抓取", "", "0%");
			var timer = setInterval(function(){
		      Ext.Ajax.request({
		            url: "${pageContext.request.contextPath}/spider/process.do?t=" + new Date(),
		            method: 'get',
		            success: function(response, options){
				    	var obj =  Ext.decode(response.responseText);
		                if(obj.success){
		                    if(obj.finished){
		                        clearInterval(timer);
		                        Ext.MessageBox.updateProgress(obj.percentage, obj.msg);
		                   //     Ext.MessageBox.updateProgress(1, obj.msg, 'finished');
		                      	Ext.defer(function () { Ext.MessageBox.hide(); }, 800);
		                    }else{
		                        Ext.MessageBox.updateProgress(obj.percentage, obj.msg);
		                    }
		                }
		            },
		            failure: function(){
		                clearInterval(timer);
		                Ext.Msg.alert('错误', '发生错误了');
		            } 
		        });  
			}, 200);
		}
	}
	
	Ext.onReady(setSpiderPanel);
	
</script>
</head>
<body>
</body>
