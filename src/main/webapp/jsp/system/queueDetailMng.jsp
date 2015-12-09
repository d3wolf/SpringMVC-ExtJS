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
        fields : [ 
		           {name:'entryState', type:'string'},
		           {name:'targetClass', type:'string'},
		           {name:'targetMethod', type:'string'},
		           {name:'arguments', type:'string'},
		           {name:'message', type:'string'},
		           {name:'createTimestamp', type:'date', dateFormat : 'time'}
		         ],
        //是否在服务端排序 （true的话，在客户端就不能排序）
        remoteSort: false,
        remoteFilter: true,
        pageSize: 20, // 分页大小
        proxy: {
            type: 'ajax',
            url: '${pageContext.request.contextPath}/queue/getQueueEntries.do?id=${param.id}',
            reader: {   //这里的reader为数据存储组织的地方，下面的配置是为json格式的数据，例如：[{"total":50,"rows":[{"a":"3","b":"4"}]}]
                type: 'json', //返回数据类型为json格式
                root: 'rows',  //数据
                totalProperty: 'total' //数据总条数
            }
        },
        sorters: [{
            //排序字段。
            property: 'ordeId',
            //排序类型，默认为 ASC 
            direction: 'desc'
        }]
    });
    store.loadPage(1);

    var grid = Ext.create('Ext.grid.Panel', {
	    renderTo: Ext.getBody(),
	    store: store,
	    layout : 'fit',
	    selModel: { selType: 'checkboxmodel', mode : 'MULTI'},   //选择框
	    columns: [                    
	                  { text: '状态', dataIndex: 'entryState', align: 'left', width : 80},
	                  { text: '目标类', dataIndex: 'targetClass', width : 300},
	                  { text: '目标方法', dataIndex: 'targetMethod', align: 'left' },
	                  { text: '参数', dataIndex: 'arguments', align: 'left',  width : 300 },
	                  { text: '信息', dataIndex: 'message', align: 'left' , width : 300},
	                  { text: '创建时间', dataIndex: 'createTimestamp', renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'), width : 130}
	               ],
	    bbar: [{
	        xtype: 'pagingtoolbar',
	        store: store,
	        displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	        emptyMsg: "没有数据",
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
							handler : function(){
								this.up("menu").hide();
								var id = record.getId();
							}
						}]
					}).showAt(e.getXY());//让右键菜单跟随鼠标位置 
				}
	    },
	     tbar:[
	     {text:'新增',iconCls:'a_add',handler:showAlert},"-",
	     {text:'停用/启用',iconCls:'a_lock'},"-",
	     "->",{ iconCls:"a_search",text:"搜索",handler:showAlert}],
	});
    
	function showAlert(){
		var selectedData=grid.getSelectionModel().getSelection()[0].data;
		
		Ext.MessageBox.alert("标题",selectedData.cataId);
	}
	
	Ext.EventManager.onWindowResize(function(){
		grid.getView().refresh();
	}) 
}); 
	
</script>
</head>
<body>
</body>
