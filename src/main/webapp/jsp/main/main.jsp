<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Maven SpringMVC</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ux/TabCloseMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/common.js"></script>

<style type="text/css">
	.no-icon{display:none;}
</style>

<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var store = Ext.create('Ext.data.TreeStore', {
			fields : [ 
			           {name:'internal', type:'string'},
			           {name:'text', type:'string'},
			           {name:'actionUrl', type:'string'},
			           {name:'id', type:'int'},
			           {name:'modefyTimestamp', type:'date', dateFormat : 'time'}
			         ],
			root : {
				display : '根节点',
				text : 'root',
				id : 1,
				expanded : true
			},
			// 数据代理
			proxy : {
				type : 'ajax',// 请求方式
				url : "${pageContext.request.contextPath}/menu/child.do"
			}
		});
		
		var centerPanel = window.centerPanel = new Ext.TabPanel({
			region : 'center',
			id : 'mainTabPanel',
			activeTab:2,
			border:false,
			plugins: [new Ext.create('Ext.ux.TabCloseMenu',{
                closeTabText: '关闭面板',
                closeOthersTabsText: '关闭其他',
                closeAllTabsText: '关闭所有'
            }),new Ext.create('Ext.ux.dbClickCloseTab',{})],
			items : [{
				title : '首页',
				id : 'home',
				border:false,
				html : '<iframe style="width:100%;height:100%;border:0px solid #ccc;" src="${pageContext.request.contextPath}/index.jsp"></iframe>'
			}]
		});
		
		var navigatorPanel = new Ext.Panel({
			region : 'west',
			collapsible : true,
			title : '左侧导航',
			xtype : 'panel',
			width : 180,
			autoScroll : true,
			layout: 'accordion',
			items : [{
				title : '面板2',
				xtype : "panel",
				items : [{
					tools : [{
						type : 'expand',
						tooltip : 'expand',
						handler : function() {
							Ext.getCmp('menu1').expandAll();
						}
					}, {
						type : 'collapse',
						tooltip : 'collapse',
						handler : function() {
							Ext.getCmp('menu1').collapseAll();
						}
					},{
						type : 'refresh',
						tooltip : 'refresh and expand',
						handler : function(){
							var tree = Ext.getCmp('menu1');
							tree.getStore().load({
					    		callback : function(records, operation, success){
									tree.expandAll();//load之后才能展开
					    		}
					    	});
						}
					}],
					xtype : 'treepanel',
					id : 'menu1',
					store : store,
					expanded : true,
					autoHeight:true,
					height: 300,
					autoScroll : true,
                    containerScroll : true,
					rootVisible: false,
					listeners: {
						itemclick: function (view, record, item, index, e, eOpts) {
	                        if (record.get('leaf') && record.get('actionUrl') != '') { //叶子节点
	                        	com.test.comm.utils.showTab(record.get('internal'),record.get('text'),"${pageContext.request.contextPath}/" + record.get('actionUrl'));
	                        }
	                    }
	                }
				}]
			}, {
				id : 'panel3',
				title : '面板3',
				xtype : "panel",
				html : "子元素3"
			} ]
		});
		
		
		Ext.create('Ext.container.Viewport', {
			layout : 'border',
			items : [new Ext.Toolbar({
				region:'north',
				height:40,
				border:false,
				items : [{
					text : '首页',
					handler:function(){
						Ext.getCmp('home').show();
					}
				}]
			}), navigatorPanel, centerPanel]
		});

	});

</script>
</head>
<body>



</body>
</html>