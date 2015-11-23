<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Menu Manage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
	function treeNodeAsync() {//根据不同的节点值,获取不同的加载数据(可以是节点ID\节点文本等等)
		Ext.tip.QuickTipManager.init();
	
		var tb = new Ext.Toolbar({
			width : 300,
			items : [{
				text : '展开',
				handler : function(btn) {
					//	Ext.MessageBox.alert(btn.text, btn.id);
					tree.expandAll();
				}
			}, {
				text : '折叠',
				handler : function(btn) {
					tree.collapseAll();
				}
			}, {
				text : '刷新',
				handler : function(btn) {
					tree.getStore().load({
			    		callback : function(records, operation, success){
							tree.expandAll();//load之后才能展开
			    		}
			    	});
				}
			} ]
		});

		var store = Ext.create('Ext.data.TreeStore', {
			fields : [ 
			           {name:'internal', type:'string'},
			           {name:'text', type:'string'},
			           {name:'actionUrl', type:'string'},
			           {name:'id', type:'int'},
			           {name:'modefyTimestamp', type:'date', dateFormat : 'time'}
			         ],// TODO:属性域
			root : {
				internal : 'root',
				text : '根节点',
				id : 1,
				expanded : true
			},
			// 数据代理
			proxy : {
				type : 'ajax',// 请求方式
				url : "${pageContext.request.contextPath}/menu/child.do"
			},
			listeners : {
				load : function(){
			//		tree.expandAll();
				}
			}
		});
		
		
		
		var column = [ {
			xtype : "treecolumn", 
			text : "display",
			dataIndex : "text",
			width : 200,
			editor:{  
                allowBlank : false
            }
		}, {
			text : "internal",
			dataIndex : "internal",
			editor:{  
                allowBlank : false
            }
		}, {
			text : "actionUrl",
			dataIndex : "actionUrl",
			editor:{  
                allowBlank : false
            }
		}, {
			text : "id",
			dataIndex : "id"
		}, {
			text : "modify",
			dataIndex : "modefyTimestamp",
			renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')
		}, {
		     xtype : 'actioncolumn',
		     width : 30,
		     items : [{
		     icon : '${pageContext.request.contextPath}/images/save.gif', // Use a URL in the icon config
		     tooltip : 'save',
		     handler : function(grid, rowIndex, colIndex) {
		    	var record=grid.getStore().getAt(rowIndex);
		    	saveNodeAttribute(grid, record);
		     }
		   	}]
		}];
			 	
	var tree = Ext.create('Ext.tree.Panel', {
		//	title : 'Menu1',
			id : 'testTree',
			columnLines : true,
			border : true,
			store : store,
			height : 500,
			useArrows : true,
			renderTo : "divTree",
			forceFit: true, //
			tbar : tb,
			animate : true,
			columns : column,
			plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1
			}) ],
			
			listeners:{
				itemcontextmenu:function(view, record, item, index, e, eOpts){
		        	//禁用浏览器的右键相应事件
					e.preventDefault();
					e.stopEvent();
					new Ext.menu.Menu({
						//控制右键菜单位置 
						float:true,
						items:[{
							text:"增加子节点",
							handler:function(){
								//当点击时隐藏右键菜单 
								this.up("menu").hide();
								var id = record.get('id');
								addNewMenu(tree, id);
							}
						},{
							text:"保存节点属性",
							handler:function(){
								this.up("menu").hide();
								saveNodeAttribute(tree, record);
							}
						},{
							text:"移除节点及子节点",
							handler:function(){
								this.up("menu").hide();
								var id = record.get('id');
								removeMenu(tree, id);
							}
						}] 
					}).showAt(e.getXY());//让右键菜单跟随鼠标位置 
				}
			}
		});

		var myMask = new Ext.LoadMask(tree, {
			msg : "加载中..."
		});
		tree.on('beforeload', function(node) {
			myMask.show();
		});

		tree.on('load', function(node) {
			myMask.hide();
		//	tree.expandAll();
		});
	}
	
	function addNewMenu(tree,id){
		Ext.Ajax.request({
		    url: '${pageContext.request.contextPath}/menu/add.do',
		    method : 'post',
		    timeout: 30000,
		    params: {
		    	id : id
		    },
		    success: function(response){
		    	var rpText = response.responseText;
		    	tree.getStore().load({
		    		callback : function(records, operation, success){
						tree.expandAll();//load之后才能展开
		    		}
		    	});
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "新增失败，请重试！" + response.responseText);
		    }
		});
	}
	
	function saveNodeAttribute(tree, record){
		var id = record.get('id');
		var text = record.isModified('text')?record.get('text'):'';
		var internal = record.isModified('internal')?record.get('internal'):'';
		var actionUrl = record.isModified('actionUrl')?record.get('actionUrl'):'';
		
/* 		record.load(id,
  			{
				callback: function(record, operation) {
						alert('a');
  				}
		});   */
		if(!record.isModified('text') && !record.isModified('internal') && !record.isModified('actionUrl')){
			Ext.MessageBox.alert("注意", "没有更改数据,未做保存");
			return;
		}
		Ext.Ajax.request({
		    url: '${pageContext.request.contextPath}/menu/save.do',
		    method : 'post',
		    timeout: 30000,
		    params: {
		    	id : id,
		    	text : text,
		    	internal : internal,
		    	actionUrl : actionUrl
		    },
		    success: function(response){
		    //	alert(tree.getSelectionModel().getSelection()[0]);
		    	record.load(id,
		      			{
		    				callback: function(record, operation) {
		    						alert('a');
		      				}
		    		}); 
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "保存失败，请重试！" + response.responseText);
		    }
		});
	}
	
	function removeMenu(tree, id){
		Ext.Ajax.request({
		    url: '${pageContext.request.contextPath}/menu/remove.do',
		    method : 'post',
		    timeout: 30000,
		    params: {
		    	id : id
		    },
		    success: function(response){
		    	tree.getStore().load({
		    		callback : function(records, operation, success){
						tree.expandAll();//load之后才能展开
		    		}
		    	});
		    },
		    failure: function(response, opts) {
		        Ext.MessageBox.alert("错误", "移除失败，请重试！" + response.responseText);
		    }
		});
		
		// 浏览器大小表格自适应
		Ext.EventManager.onWindowResize(function() {
			Ext.getCmp("testTree").setWidth(document.documentElement.clientWidth)
		//	var height = document.documentElement.clientHeight;
		//	var tempHeight = height - 238;
		//	Ext.getCmp("ProductCategoriesGridID").setHeight(tempHeight);
		});
	}

	Ext.onReady(treeNodeAsync);
	
</script>
</head>
<body>
	<div id="divTree"></div>
</body>
