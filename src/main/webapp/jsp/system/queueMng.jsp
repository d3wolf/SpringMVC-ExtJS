<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Queue Manage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
Ext.onReady(function () {
	var location = (window.location+'').split('/');  
	var basePath = location[0]+'//'+location[2]+'/'+location[3]; 
	
    var store = Ext.create('Ext.data.Store', {
        fields: ["name", "enabled", "entryCount", "queueState"],
        pageSize: 20,  //页容量5条数据
        //是否在服务端排序 （true的话，在客户端就不能排序）
        remoteSort: false,
        remoteFilter: true,
        proxy: {
            type: 'ajax',
            url: '${pageContext.request.contextPath}/queue/getQueues.do',
            reader: {   //这里的reader为数据存储组织的地方，下面的配置是为json格式的数据，例如：[{"total":50,"rows":[{"a":"3","b":"4"}]}]
                type: 'json', //返回数据类型为json格式
                root: 'rows',  //数据
                totalProperty: 'total' //数据总条数
            }
        },
        sorters: [{
            //排序字段。
            property: 'name',
            //排序类型，默认为 ASC 
         //   direction: 'desc'
        }],
        autoLoad: true  //即时加载数据
    });

    var grid = Ext.create('Ext.grid.Panel', {
	    renderTo: Ext.getBody(),
	    store: store,
	    layout : 'fit',
	    selModel: {  
	    	selType : 'checkboxmodel',
	        mode : 'MULTI' //"SINGLE"/"SIMPLE"/"MULTI"
	    },   
	    columns: [                    
	                  { text: '名称', dataIndex: 'name', align: 'left', width: 200},
	                  { text: '启用', dataIndex: 'enabled', width: 40},
	                  { text: '等待条目', dataIndex: 'entryCount', align: 'left'},
	                  { text: '状态', dataIndex: 'queueState', align: 'left' }
	               ],
	    bbar: [{
	        xtype: 'pagingtoolbar',
	        store: store,
	    //    displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	        emptyMsg: "没有数据",
	 //       beforePageText: "当前页",
	  //      afterPageText: "共{0}页",
	        displayInfo: true                 
	    }],
	     listeners: {
	    	 itemcontextmenu:function(view, record, item, index, e, eOpts){
		        	//禁用浏览器的右键相应事件
					e.preventDefault();
					e.stopEvent();
					new Ext.menu.Menu({
						//控制右键菜单位置 
						float:true,
						items:[{
							text:"查看信息",
							icon : '${pageContext.request.contextPath}/images/icon-info.gif',
							handler : function(){
								this.up("menu").hide();
								var id = record.getId();
								showDetail(id);
							}
						}, {
							text:"执行队列",
							icon : '${pageContext.request.contextPath}/images/execute.gif',
							handler : function(){
								this.up("menu").hide();
								var id = record.getId();
								processQueue(id);
							}
						}, {
							text:"删除队列及条目",
							icon : '${pageContext.request.contextPath}/images/delete.gif',
							handler : function(){
								this.up("menu").hide();
								var id = record.getId();
								deleteQueueAndEntries(id);
							}
						}]
					}).showAt(e.getXY());//让右键菜单跟随鼠标位置 
				}
	    },
	     tbar:[
	     {text:'新建' ,handler : createQueue},"-",
	     {text:'停用/启用' },"-",
	     "->",{ iconCls:"a_search",text:"搜索",handler:showAlert}], 
	});

    function createQueue(){
    	Ext.create("Ext.window.Window", {
    	    id: "newQueueWin",
    	    title: "新建队列",
    	    width: 500,
    	    height: 300,
    	    layout: "fit",
    	    items: [{
    	            xtype: "form",
    	            defaultType: 'textfield',
    	            id : 'createQueueForm',
    	            defaults: {
    	                anchor: '100%',
    	            },
    	            fieldDefaults: {
    	                labelWidth: 80,
    	                labelAlign: "left",
    	                flex: 1,
    	                margin: 5
    	            },
    	            items: [
    	                {
    	                    xtype: "container",
    	                    items: [
    	                        { xtype: "textfield", name: "name", fieldLabel: "名称", allowBlank: false },
    	                    ]
    	                }
    	            ]
    	        }
    	    ],
    	    buttons: [
    	        { xtype: "button", text: "确定", handler: btnsubmitclick },
    	        { xtype: "button", text: "取消", handler: function () { this.up("window").close(); } }
    	    ]
    	}).show();
    }
    
  //提交按钮处理方法
	var btnsubmitclick = function () {
	  var form = Ext.getCmp('createQueueForm');
		if (form.getForm().isValid()) {
			form.getForm().submit({
				waitTitle: "请稍候",
				waitMsg: '正在创建...',
				url: '${pageContext.request.contextPath}/queue/createQueue.do',
				method: 'post',
				success: function(form, action) {
				//	var msg = action.result.msg;
				//	debugger;
				//	Ext.Msg.alert('提示1', action.result.msg);
					Ext.getCmp('newQueueWin').close();
					store.load();
				},
				failure: function(form, action) {
					if (action.result == undefined) {
						Ext.Msg.alert('提示2', "系统出错...请联系管理员");
						form.items.items[1].reset();
					}else {
						var msg = action.result;
				//		debugger;
						Ext.Msg.alert('提示3', action.result.msg);
						form.items.items[1].reset();
					}
				}
			}); 
		}
	}
    
	function showAlert(){
		var selectedData=grid.getSelectionModel().getSelection()[0].data;
		
		Ext.MessageBox.alert("标题",selectedData.cataId);
	}
	
	function showDetail(id){
		window.parent.location.href=basePath  + '/#' + "queue/detail.do?id="+id;
	}
	
	function deleteQueueAndEntries(id){
		Ext.MessageBox.confirm('注意', '将会删除队列及其所有条目，确认继续?', function(e){
			if('yes' != e){
				return;
			}
			Ext.Ajax.request({
			    url: "${pageContext.request.contextPath}/queue/deleteQueue.do",
			    method : 'post',
			    params : {
			    	id : id
			    },
			    success: function(response){
			    	var rpText = Ext.decode(response.responseText);
			    	Ext.Msg.alert('提示3', rpText.msg);
			    	store.load();
			   
			    },
			    failure: function(response, opts) {
			        Ext.MessageBox.alert("错误", "失败:\n" + response.responseText);
			    }
			})
		}); 
	}
	
	//执行队列
	function processQueue(id){
		Ext.Ajax.request({
		    url: "${pageContext.request.contextPath}/queue/processQueue.do",
		    method : 'post',
		    params : {
		    	id : id
		    }
		});
	}
	
	Ext.EventManager.onWindowResize(function(){
		grid.getView().refresh();
	}) 
}); 
	
</script>
</head>
<body>
	<div id="divTree"></div>
</body>
