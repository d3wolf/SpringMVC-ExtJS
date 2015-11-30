<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Queue Manage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
Ext.onReady(function () {
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
            property: 'ordeId',
            //排序类型，默认为 ASC 
            direction: 'desc'
        }],
        autoLoad: true  //即时加载数据
    });

    var grid = Ext.create('Ext.grid.Panel', {
	    renderTo: Ext.getBody(),
	    store: store,
	    width : 600,
	    height : 500,
	    selModel: { selType: 'checkboxmodel' },   //选择框
	    columns: [                    
	                  { text: '名称', dataIndex: 'name', align: 'left', maxWidth: 80 },
	                  { text: '启用', dataIndex: 'enabled',  maxWidth: 120 },
	                  { text: '等待条目', dataIndex: 'entryCount', align: 'left', minWidth: 80 },
	                  { text: '状态', dataIndex: 'queueState', maxWidth: 80, align: 'left' }
	               ],
	    bbar: [{
	        xtype: 'pagingtoolbar',
	        store: store,
	        displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	        emptyMsg: "没有数据",
	        beforePageText: "当前页",
	        afterPageText: "共{0}页",
	        displayInfo: true                 
	    }],
	     listeners: { 'itemclick': function (view, record, item, index, e) {
	       Ext.MessageBox.alert("标题",record.data.cataId);
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

}); 
	
</script>
</head>
<body>
	<div id="divTree"></div>
</body>
