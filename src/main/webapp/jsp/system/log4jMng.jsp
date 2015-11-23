<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Log Manage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
	function log4jManage() {
		Ext.tip.QuickTipManager.init();
		
		var levelStore = Ext.create('Ext.data.Store', {
		    fields: ['id', 'name'],
		    data : [
		        {"id":"OFF", "name":"OFF"},
		        {"id":"FATAL", "name":"FATAL"},
		        {"id":"ERROR", "name":"ERROR"},
		        {"id":"WARN", "name":"WARN"},
		        {"id":"INFO", "name":"INFO"},
		        {"id":"DEBUG", "name":"DEBUG"},
		        {"id":"TRACE", "name":"TRACE"},
		        {"id":"ALL", "name":"ALL"},
		    ]
		});
	
		var logPanel = Ext.create('Ext.panel.Panel', {
		    title: 'Log4j',
		    width: 900,
		    id : "logPanel",
		    bodyPadding: 10,
		    renderTo: Ext.getBody(),
		    layout : 'hbox',
		    items: [{
		    	xtype: 'textfield',
		    	fieldLabel : 'Logger',
		    	labelAlign : 'right',
		        id: 'logTarget',
		        width : 350,
		        allowBlank: false
		    },{
		    	xtype : 'combo',
		    	id : 'levelStr',
		    	store : levelStore,
		    	queryMode : 'local',
		    	displayField : 'name',
		    	valueField : 'id',
		    	fieldLabel : 'Level',
		    	labelAlign : 'right',
		    //	width : 100
		    },{
		    	xtype : 'button',
		    	text: '设置',
		    	margin : "0 10 0 10",
		    	handler : function(){
		    		var target = Ext.getCmp('logTarget').value;
		    		var levelStr = Ext.getCmp('levelStr').value;
		    		setTargetLevel(target, levelStr);
		    	}
		    },
		    {
		    	xtype : 'button',
		    	text: '显示',
		    	margin : "0 10 0 0",
		    	handler : function(){
		    		var value = Ext.getCmp('logTarget').value;
		    		getTargetLevel(value);
		    	}
		    },{
		    	xtype : 'label',
		    	id : 'showLevel',
		    	text : ''
		    }]
		});
	}
	

	function setTargetLevel(target, levelStr){
		Ext.Ajax.request({
		    url: "${pageContext.request.contextPath}/log/setLevel.do",
		    method : 'post',
		    params : {
		    	target : target,
		    	levelStr : levelStr
		    },
		    success: function(response){
		    	var rpText = Ext.decode(response.responseText);
		    //	if(rpText.success){
		    Ext.getCmp('showLevel').setText(rpText.msg);
		    //	}
		   
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "失败:\n" + response.responseText);
		    }
		});
	}
	
	function getTargetLevel(target){
		Ext.Ajax.request({
		    url: "${pageContext.request.contextPath}/log/getLevel.do",
		    method : 'post',
		    params : {
		    	target : target
		    },
		    success: function(response){
		    	var rpText = Ext.decode(response.responseText);
		    	Ext.getCmp('showLevel').setText(rpText.levelStr);
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "失败:\n" + response.responseText);
		    }
		});
	}

	Ext.onReady(log4jManage);
	
</script>
</head>
<body>
	<div id="divTree"></div>
</body>
