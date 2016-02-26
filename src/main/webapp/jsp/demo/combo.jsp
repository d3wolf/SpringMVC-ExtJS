<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<title>Combo</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all-debug.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/examples/ux/TabCloseMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.6.min.js"></script>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();

		var pStore = new Ext.data.JsonStore({
			fields : [ 'id', 'name' ],
			autoLoad:true,
			proxy : {
				type : 'ajax',
				url : "${pageContext.request.contextPath}/demo/province.do",
				autoLoad : true,
				reader : {
					type : 'json',
					root : 'province'
				}
			},
		});
		
		var cStore = new Ext.data.JsonStore({
			fields : [ 'id', 'name' ],
			autoLoad : false,
			proxy : {
				type : 'rest',
				url : "${pageContext.request.contextPath}/demo/city.do",
				reader : {
					type : 'json',
					root : 'city'
				}
			},
			listeners : {
				load : function(records) {
				//	alert(records[0].get("name"));
				}
			}
		});
		cStore.load();
		
		var dStore = new Ext.data.JsonStore({
			fields : [ 'id', 'name' ],
			autoLoad:true,
			proxy : {
				type : 'ajax',
				url : "${pageContext.request.contextPath}/demo/district.do",
				autoLoad : false,
				reader : {
					type : 'json',
					root : 'district'
				}
			},
		});

		var province = new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			queryMode : 'local',
			id : "province",
			renderTo : 'province',
			editable : true,
			store : pStore,
			valueField : 'id',
			displayField : 'name',
			editable: false,
			listeners : {
				change : function() {
					city.clearValue();
					district.clearValue();
					cStore.load({params : {pid : province.getValue()}});
				//	city.setValue(1);
				}
			}
		});
		
		var city = new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			queryMode : 'local',
			id : "city",
			renderTo : 'city',
			editable : true,
			store : cStore,
			valueField : 'id',
			displayField : 'name',
			editable: false,
			listeners : {
				select : function() {
					district.setValue('');
					dStore.load({params : {cid : city.getValue()}});
				}
			}
		});
		
		var district = new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			queryMode : 'local',
			id : "district",
			renderTo : 'district',
			editable : true,
			store : dStore,
			valueField : 'id',
			displayField : 'name',
			editable: false,
			listeners : {
				change : function(combox, newValue, oldValue) {
				//	alert(newValue);
				}
			}
		});
	});
	
</script>
</head>
<body>


	<fieldset class="x-fieldset x-form-label-left" id="fieldSet" style="width: 500px;">
			<legend >
				PCD
			</legend>
			<div id='div1'>
			<div id='province'></div>
			<div id='city'></div>
			<div id='district'></div>
		</div>
	</fieldset>

	<div id='extFieldSet'></div>





</body>
