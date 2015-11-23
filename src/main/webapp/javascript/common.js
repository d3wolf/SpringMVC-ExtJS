Ext.namespace("com.test.comm");
/**
 * -----------------------------------------------------------------------------------------
 * @author       :   ex_zhangtao                                                            *
 * @discription  :   项目通用方法业务类                                                            *
 * @date         :   2015/05/13                                                             *
 * -----------------------------------------------------------------------------------------
 */
com.test.comm.utils = {
	discription : '公用对象js',
	author      : 'zhangtao'
};
/**
 * 
 * 开发：zhangtao 
 * 描述：表格渲染下拉框通用处理函数
 * 
 */
Ext.util.Format.comboRenderer = function(combo){
	return function(value){
		var record = combo.findRecord(combo.valueField,parseInt(value));
		return record ? record.get(combo.displayField) : combo.valueNotFoundText;
	}
};

/**
 * 对EXTJS4 Grid列排序支持中文排序
 */
Ext.data.Store.prototype.createComparator = function(sorters){  
    return function(r1, r2){  
        var s = sorters[0], f=s.property;  
        var v1 = r1.data[f], v2 = r2.data[f];  
          
        var result = 0;  
        if(typeof(v1) == "string"){  
            result = v1.localeCompare(v2);  
            if(s.direction == 'DESC'){  
                result *=-1;  
            }  
        } else {  
            result =sorters[0].sort(r1, r2);  
        }  
          
        var length = sorters.length;  
          
        for(var i = 1; i<length; i ++){  
            s = sorters[i];  
            f = s.property;  
            v1 = r1.data[f];  
            v2 = r2.data[f];  
            if(typeof(v1) == "string"){  
                result = result || v1.localeCompare(v2);  
                if(s.direction == 'DESC'){  
                    result *=-1;  
                }  
            } else {  
                result = result || s.sort.call(this, r1, r2);  
            }  
        }  
        return result;  
    };  
};



/**
 * @author:ex_zhangtao
 * @discription:新建TAB页
 * 
 */
com.test.comm.utils.showTab = function(id,title,src){
	var mainTab = Ext.getCmp('mainTabPanel');
	var newTab = Ext.getCmp(id);
	if(!newTab){
		newTab = mainTab.add({
			id : id,
			xtype : 'panel',
			border:false,
			title : title,
			closable : true,
			resizeTabs:true,
//			autoScroll:true,
			html : '<iframe name="mainFrame" id="mainFrame" style="width:100%;height:100%;border:0px solid #ccc;" src="'+src+'"></iframe>',
            listeners:{
                close:function(obj){
                    if(!!obj&&obj.hasOwnProperty('id')&&obj.id){
                        var theTab=Ext.fly(obj.id);
                        var iframe=theTab.select('iframe#mainFrame');
                        var hasElements=!!iframe
                            &&
                            iframe.hasOwnProperty('elements')
                            &&
                            iframe['elements']
                            &&
                            iframe['elements'].length>0;

                        if(hasElements&&iframe['elements'][0].src){
                            iframe['elements'][0].src='about:blank';
                            iframe['elements'][0].contentWindow.document.write('');
                            !!window.CollectGarbage&&CollectGarbage();
                        }
                    }
                }
            }
		});
	};
	newTab.show();
};

/**
 * @author:ex_zhangtao
 * @discription:iframe里新建TAB页
 * 
 */
com.test.comm.utils.addPanelToTab = function(id,title,src){
	var mainTabPanel = parent.window.centerPanel;
	var p = new parent.window.Ext.Panel({
		id : id,
		border:false,
		title : title,
		closable : true,
		resizeTabs:true,
	    html : '<iframe border="0" name="mainFrame" id="mainFrame" style="width:100%;height:100%;border:0px solid #ccc;" src="'+src+'"></iframe>'
	});
	mainTabPanel.add(p);
	mainTabPanel.setActiveTab(p);
};

/**
 * @author:ex_zhangtao
 * @discription:右键菜单（关闭当前页，关闭所有，关闭其他）TAB页
 * 
 */
Ext.ux.dbClickCloseTab = function(){
	var tabs, menu, ctxItem;
    this.init = function(tp){
        tabs = tp;
        tabs.on('dblclick', function(){
        	 tabs.remove(ctxItem);
        });
    }
	
};
Ext.ux.TabCloseMenu = function(){
    var tabs, menu, ctxItem;
    this.init = function(tp){
        tabs = tp;
        tabs.on('contextmenu', onContextMenu);
        tabs.on('dblclick', function(){
        	 tabs.remove(ctxItem);
        });
    }
    function onContextMenu(ts, item, e){
        if(!menu){ 
            menu = new Ext.menu.Menu([{
                id: tabs.id + '-close',
                text: '关闭当前标签',
                iconCls : 'btnno',
                handler : function(){
                    tabs.remove(ctxItem);
                }
            },{
                id: tabs.id + '-close-others',
                text: '关闭其他标签',
                iconCls : 'btnno',
                handler : function(){
                    tabs.items.each(function(item){
                        if(item.closable && item != ctxItem){
                            tabs.remove(item);
                        }
                    });
                }
            },{
                id: tabs.id + '-close-all',
                text: '关闭所有标签',
                iconCls : 'btnno',
                handler : function(){
                    tabs.items.each(function(item){
                        if(item.closable){
                           tabs.remove(item);
                        }
                    });
                }
            }]);
        }
        ctxItem = item;
        var items = menu.items;
        items.get(tabs.id + '-close').setDisabled(!item.closable);

        var disableOthers = true;
        tabs.items.each(function(){
            if(this != item && this.closable){
                disableOthers = false;
                return false;
            }
        });
        items.get(tabs.id + '-close-others').setDisabled(disableOthers);
        menu.showAt(e.getPoint());
       
        var disableAll = true;
        tabs.items.each(function(){
             if(this.closable){
                 disableAll = false;
                 return false;
             }
        });
        items.get(tabs.id + '-close-all').setDisabled(disableAll);
        menu.showAt(e.getPoint());
    }
};

/**
 * @author:ex_zhangtao
 * @discription:全屏
 * 
 */
com.test.comm.utils.fullScreen = function(btn) {
	  var status = btn.status;
	  var el = document.documentElement;
	  var rfs = el.requestFullScreen || el.webkitRequestFullScreen || 
	            el.mozRequestFullScreen || el.msRequestFullScreen;
	  if(typeof rfs != "undefined" && rfs) {
	    rfs.call(el);
	  } /*else if(typeof window.ActiveXObject != "undefined") {*/
	    //for IE，这里其实就是模拟了按下键盘的F11，使浏览器全屏
	  var wscript = new ActiveXObject("WScript.Shell");
	  if(wscript != null) {
        wscript.SendKeys("{F11}");
        btn.setText(status!='fullWindow'?"退出全屏":"全屏");
        btn.status  = status=='fullWindow'?"exitWindow":"fullWindow";
        btn.setIconClass(status!='fullWindow'?"exitWindow":"fullWindow");
      }
//	  }
};
 
/**
 * @author:ex_zhangtao
 * @discription:退出全屏
 * 
 */
com.test.comm.utils.exitFullScreen = function() {
    var el = document,
        cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen,
        wscript;
    if (typeof cfs != "undefined" && cfs) {
        cfs.call(el);
        return;
    }
 
    if (typeof window.ActiveXObject != "undefined") {
        wscript = new ActiveXObject("WScript.Shell");
        if (wscript != null) {
            wscript.SendKeys("{F11}");
        }
    }
};

/** 
 * @author：ex_zhangtao 
 * @discription：对指定的列进行重复单元格跨行合并 
 * @params：grid是gridPanel对象 
 *         cols是列序号数组，形如[1,2]，如果表格定义了rownumber列或check列，则列序号要相应递增 
 **/  
com.test.comm.utils.mergeCells = function(grid,cols){  
    var arrayTr=document.getElementById(grid.getId()+"-body").firstChild.firstChild.firstChild.getElementsByTagName('tr');    
    var trCount = arrayTr.length;  
    var arrayTd;  
    var td;  
    var merge = function(rowspanObj,removeObjs){ //定义合并函数  
       if(rowspanObj.rowspan != 1){  
            arrayTd =arrayTr[rowspanObj.tr].getElementsByTagName("td"); //合并行  
           td=arrayTd[rowspanObj.td-1];  
           td.rowSpan=rowspanObj.rowspan;  
           td.vAlign="middle";               
           Ext.each(removeObjs,function(obj){ //隐身被合并的单元格  
                arrayTd =arrayTr[obj.tr].getElementsByTagName("td");  
                arrayTd[obj.td-1].style.display='none';                           
           });  
        }     
    };    
    var rowspanObj = {}; //要进行跨列操作的td对象{tr:1,td:2,rowspan:5}      
    var removeObjs = []; //要进行删除的td对象[{tr:2,td:2},{tr:3,td:2}]  
    var col;  
    Ext.each(cols,function(colIndex){ //逐列去操作tr  
        var rowspan = 1;  
        var divHtml = null;//单元格内的数值          
        for(var i=1;i<trCount;i++){  //i=0表示表头等没用的行  
            arrayTd = arrayTr[i].getElementsByTagName("td");  
            var cold=0;  
            col=colIndex+cold;//跳过RowNumber列和check列  
            if(!divHtml){  
                divHtml = arrayTd[col-1].innerHTML;  
               rowspanObj = {tr:i,td:col,rowspan:rowspan}  
            }else{  
                var cellText = arrayTd[col-1].innerHTML;  
                var addf=function(){   
                   rowspanObj["rowspan"] = rowspanObj["rowspan"]+1;  
                    removeObjs.push({tr:i,td:col});  
                    if(i==trCount-1)  
                        merge(rowspanObj,removeObjs);//执行合并函数  
                };  
                var mergef=function(){  
                    merge(rowspanObj,removeObjs);//执行合并函数  
                    divHtml = cellText;  
                   rowspanObj = {tr:i,td:col,rowspan:rowspan}  
                    removeObjs = [];  
                };  
                if(cellText == divHtml){  
                    if(colIndex!=cols[0]){   
                        var leftDisplay=arrayTd[col-2].style.display;//判断左边单元格值是否已display  
                        if(leftDisplay=='none')  
                            addf();   
                       else  
                            mergef();                             
                    }else  
                        addf();                                           
                }else  
                    mergef();             
            }  
        }  
    });   
};    

/**
 * @author：ex_zhangtao 
 * @discription：新增 - 重置界面
 */
com.test.comm.utils.addToResetPage= function(paramObj){
//	try{
		for(var item in paramObj){
	   		var object = paramObj[item];
			for(var i = 0, len = object.length; i < len; i++) {
		   		var thisObj = Ext.getCmp(object[i]);
		   		if(thisObj&&typeof thisObj!='undefined') {
		   		   if(item == 'form'){
	   		   		  var basicForm = thisObj.getForm();
	   		   		  basicForm.reset();
		   		   }else{
	   		   		  var store = thisObj.getStore();
	   		   		  store.removeAll();
		   		   }
		   		}
		   	}
	   	}
//	}catch(e){
//		alert('解析异常！');
//	}
   	
};

/** 
 * 
 * @author：ex_zhangtao 
 * @discription：保存数据构造 
 * @params：传入的数据格式：
 *  var tempDataObjs = [{
			xtype:'form',
			formId:'routineForm'
	},{
			xtype:'grid',
			canEdit:true,
			editFeilds:['code','name'],
			gridId:'routineGrid'
	},{
			xtype:'grid',
			canEdit:false,
			gridId:'payPartGrid'
	}]
 **/  
com.test.comm.utils.saveDataObjects = function(tempDataObjs,saveURL,isRevise){
	var self = this;
	this.tempDataObjs = tempDataObjs;
	this.saveURL      = saveURL;
	this.isRevise     = isRevise;
	/**
	 * @ex_zhangtao
	 * @FORM表单合法性校验
	 */
	this.formValid = function(){
		var len = tempDataObjs.length;
		var seccessValid = true;
		for(var i=0; i<len; i++){
			var obj = tempDataObjs[i];
			var xtype = obj.xtype;
			if(xtype === 'form'){
				var formId = obj['formId'];
				var form = Ext.getCmp(formId);
				if(form && (typeof form!='undefined')){
					var basicForm = form.getForm();
					if(!basicForm.isValid()){
						seccessValid = false;
					}
				}
			}else if(xtype === 'grid'){
				var gridId  = obj['gridId'];
				var grid    = Ext.getCmp(gridId);
				var plugins = grid.plugins;
				if(plugins&&plugins.length > 0){
					plugins[0].completeEdit();
				}
			}
		}
		return seccessValid;
	};
	/**
	 * @ex_zhangtao
	 * @保存数据构造方法
	 */
	this.buildSavaData = function(){
		var saveJSONData = new Object();
		var len = tempDataObjs.length;
		for(var i=0; i<len; i++){
			var obj = tempDataObjs[i];
			var xtype = obj.xtype;
			if(xtype === 'form'){
				var formDataObj = new Object();
				var formId = obj['formId'];
				var form = Ext.getCmp(formId);
				if(form && (typeof form!='undefined')){
					var basicForm = form.getForm();
					var formValueMap = basicForm.getFieldValues();
					formDataObj = formValueMap;
					saveJSONData[formId] = formDataObj;
				}
			}else if(xtype === 'grid'){
				var gridData = new Object();
				var gridId   = obj['gridId'];
				var gridType = obj['gridType'];
				var grid = Ext.getCmp(gridId);
				if(grid && (typeof grid!='undefined')){
					var store = (gridType&&gridType!='tree')?grid.getStore():grid.getView().store;
					var gridJSON = new Array();
					store.each(function(record){
						var valueJsonObj = {};
						if(obj['canEdit']){
							var editFields = obj['editFeilds'],
							    flen = editFields.length;
							for(var j=0; j<flen; j++){
								var field = editFields[j];
								valueJsonObj[field] = record.get(field);
							}
						}else{
							var columnId = obj['columnId'];
							if((typeof columnId == 'undefined')||columnId == undefined){
								columnId = "id";
							}
							valueJsonObj[columnId] = record.get(columnId);
						}
						gridJSON.push(valueJsonObj);
					});
					saveJSONData[gridId] = gridJSON;
				}
			}else{
				continue;
			}
		}
		return saveJSONData;
	};
	/**
	 * 
	 * @ex_zhangtao
	 * @保存ajax方法体
	 */
	this.save = function(isMsg){
		var isSeccessValid = self.formValid();
		if(!isSeccessValid){
			Ext.Msg.alert('提示','表单合法性校验不通过，请检查后重新保存！');
			return;
		}
		var saveMask = new Ext.LoadMask(Ext.getBody(),{msg:"正在处理中，请稍后..."});
		saveMask.show();
		Ext.Ajax.request({
		    url: self.saveURL,
		    method:'POST',
		    params: {
		        paramData: Ext.encode(self.buildSavaData()),
		        isRevise : isRevise
		    },
		    success: function(response){
		    	saveMask.hide();
		    	if(response.responseText!=''){
		    		try{
		    			var msgBean = Ext.decode(response.responseText);
				        if(msgBean.success||msgBean.success=='true'){
				        	if(!isMsg){
				        		Ext.Msg.alert('提示','操作成功！');
				        	}
				        	for(var i=0,len=tempDataObjs.length; i<len;i++){
				        		var object = tempDataObjs[i];
				        		if(object.xtype == 'grid'){
				        			var gridId   = object['gridId'];
				        			var isReload = object['isReload'];
				        			    isReload = (typeof isReload!='undefined')?isReload:true;
				    				var grid = Ext.getCmp(gridId);
				    				if(grid&&isReload){
				    					grid.getStore().reload();
				    				}
				        		}
				        	}
				        }else{
				        	Ext.Msg.alert('提示','操作失败！'+msgBean.msg);
				        }
				       
		    		}catch(e){
		    			Ext.Msg.alert('提示','系统异常，请联系管理员！');
		    		}
		    	}
		    	self.finishedCallback(response.responseText);
		    },
		    failure: function(response){
		    	saveMask.hide();
		        var text = response.responseText;
		        self.finishedCallback(text);
		        Ext.Msg.alert('提示','系统异常，请联系管理员！');
		        
		    }
		});
	};
	/**
	 * @ex_zhangtao
	 * @请求执行完成后的回掉返回
	 */
	this.finishedCallback = function(msgBean){};
//	this.save();
    return this;
	 
};

/** 
 * @author：ex_zhangtao 
 * @discription：未检出设定界面不可用 
 * @params:lockItems = [{
 *     xtype:'from',
 *     formId:'basicForm'
 * },{
 *     xtype:'grid',
 *     gridId:'basicGrid'
 * }]
 **/  
com.test.comm.utils.lockOrUnLockTab = function(lockItems,isLock){
	try{
		for(var i=0,len=lockItems.length; i<len; i++ ){
			var item  = lockItems[i];
			var xtype = item['xtype'];
			if(xtype==='form'){
				var basicForm = Ext.getCmp(item['formId']).getForm();
				if(basicForm && typeof basicForm != 'undefined'){
					var fields   = basicForm.getFields();
					var fieldMap = fields.map;
					for(var fieldKey in fieldMap){
						var field = fieldMap[fieldKey];
						var isDisable = field.isDisable||false;
						if(field){
						   if(isDisable){
							   continue;
						   }
						   field.setReadOnly(isLock);
						   if(isLock){
							   field.addClass('x-item-disabled');
						   }else{
							   field.removeCls('x-item-disabled');
						   }
						}
					}
				}
			}else if(xtype==='grid'){
				var grid = Ext.getCmp(item['gridId']);
				grid.status = isLock;
				if(grid && typeof grid!='undefined'){
					grid.on('beforeedit',function(g){
					    return !grid.status;
					});
				}
			}else if(xtype==='buttonGroup'){
				var buttonGroup = item['buttonGroup'];
				for(var j=0,btnLen=buttonGroup.length;j<btnLen; j++){
					var buttonId = buttonGroup[j];
					var button   = Ext.getCmp(buttonId);
					if(button && typeof button != 'undefined'){
					   button.setDisabled(isLock);
					}
				}
			}else{
				continue;
			}
		}
	}catch(e){
	    alert('处理异常，请联系系统管理员！');	
	}
};


/**
 * @author：ex_zhangtao 
 * @discription：同步请求函数
 */
com.test.comm.utils.synchronizeRequest = function(config){
	this.url = config.url||'';
	this.method = config.method||'GET';
	this.setHttpRequest = function(){
		var self = this;
		var xmlhttp 
	    if (window.ActiveXObject)       //isIE   =   true; 
	    { 
	         xmlhttp  =  new XMLHttpRequest(); 
	    } 
	    else if (window.XMLHttpRequest) //isIE   =   false; 
	    { 
	         xmlhttp  =  new ActiveXObject("Microsoft.XMLHTTP"); 
	    } 
	    xmlhttp.open(this.url, this.method, true); 
	    xmlhttp.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded;charset=utf-8");   
	    xmlhttp.onreadystatechange = function() {
	    	if(xmlhttp.readyState==4){
	    		if(xmlhttp.status==200){
	    			self.success(xmlhttp.responseText);
	    		}else{
	    			self.error(xmlhttp.responseText);
	    		}
	    	}
        }
	    xmlhttp.send(null); 
	};
	this.success     = function(successMsg){
		config.success(successMsg);
	};
	this.error       = function(errorMsg){
		config.error(errorMsg);
	};
	this.setHttpRequest();
	 
};
/**
 * @author：ex_zhangtao 
 * @discription：外部界面链接进入新系统界面
 */
com.test.comm.utils.linkObjectToPage = function(urlObject){
	var linkMap = [
		{model:'matchingLibrary',name:'选配库',url:'basicData/matchingLibrary.jsp'},
		{model:'ItemChange',name:'选配库批量变更',url:'basicData/ItemChange.jsp'},
		{model:'productConfig',name:'产品配置表',url:'basicData/productConfiguration.jsp'},
		{model:'ItemChangeProductConfig',name:'产品配置表批量变更',url:'basicData/ItemChangeProductConfig.jsp'},
		{model:'configrationSchemeWrite',name:'配置方案编制',url:'configrationScheme/configrationSchemeWrite.jsp'},
		{model:'temporarySchemeWrite',name:'临时方案编制',url:'configrationScheme/temporarySchemeWrite.jsp'},
		{model:'reduceCostsSchemeWrite',name:'降成本方案编制',url:'configrationScheme/reduceCostsSchemeWrite.jsp'},
		{model:'schemeBatchChange',name:'方案批量变更',url:'configrationScheme/schemeBatchChange.jsp'},
		{model:'orderBomConfig',name:'订单BOM编制',url:'doOrder/orderBomConfig.jsp'},
		{model:'partOrderConfig',name:'零配件订单编制',url:'doOrder/partOrderConfig.jsp'},
		{model:'empiricalAccumulationWrite',name:'经验积累编制',url:'configrationScheme/empiricalAccumulationWrite.jsp'},
		{model:'configrationSchemeCheck',name:'配置方案校验',url:'configrationScheme/configrationSchemeCheck.jsp'},
		{model:'printListApply',name:'印刷品清单申请',url:'print/printListApply.jsp'},
		{model:'printPlanComfirm',name:'印刷品设计确认',url:'print/printPlanComfirm.jsp'},
		{model:'printChangeApply',name:'印刷品变更申请',url:'print/printChangeApply.jsp'},
		{model:'printChangeComfirm',name:'印刷品变更确认',url:'print/printChangeComfirm.jsp'},
		{model:'itemApply',name:'订单编码申请',url:'itemApply/itemApply.jsp'},
		{model:'quotationSpareParts',name:'报价单散件编制',url:'crmQuotation/quotationSpareParts.jsp'},
		{model:'itemPrice',name:'选配物料价格维护',url:'itemPrice/itemPrice.jsp'}
	];
	var linkRootPath = rootPath + 'netmarkets/jsp/midea/cto/';
	for(var i=0,len=linkMap.length; i<len; i++){
		var model = linkMap[i]['model'];
		var name  = linkMap[i]['name'];
		var url   = linkMap[i]['url'];
		var linkURL = linkRootPath+url+'?1=1'; 
		var index = 0;
		if(urlObject.model == model){
			for(var o in urlObject){
				index++;
				if(index > 1) linkURL += '&'+o+'='+urlObject[o];
			}
			com.test.comm.utils.showTab(model,name,linkURL); 
		}
	}
};

/**
 * 获取url链接中的参数值
 * @param {} key
 * @return {}
 */
com.test.comm.utils.getUrlParamsByKey= function (key) { 
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null){
		return unescape(r[2]);
	}
	return null; 
};

/**
 * @author：ex_zhangtao 
 * @discription：进入组织切换界面
 */
com.test.comm.utils.openSelectOrg = function(author){
    window.open(rootPath+"netmarkets/jsp/midea/org/selectOrg.jsp?stype=select&author="+author);	
};

/**
 * @author：ex_zhangtao 
 * @discription：浏览器最大化
 */
com.test.comm.utils.browserMax = function(){
	if (window.screen) {//判断浏览器是否支持window.screen判断浏览器是否支持screen 
		var myw = screen.availWidth; //定义一个myw，接受到当前全屏的宽 
		var myh = screen.availHeight; //定义一个myw，接受到当前全屏的高 
		window.moveTo(0, 0); //把window放在左上脚 
		window.resizeTo(myw, myh); //把当前窗体的长宽跳转为myw和myh 
	}
};

com.test.comm.XTriggerFiled = Ext.extend(Ext.form.field.Trigger,{
	getTriggerMarkup: function() {
        var me = this,
            i = 0,
            hideTrigger = (me.readOnly || me.hideTrigger),
            triggerCls,
            triggerBaseCls = me.triggerBaseCls,
            triggerConfigs = [];
        if (!me.trigger1Cls) {
            me.trigger1Cls = me.triggerCls;
        }
        for (i = 0; (triggerCls = me['trigger' + (i + 1) + 'Cls']) || i < 1; i++) {
            triggerConfigs.push({
                tag: 'td',
                valign: 'top',
                cls: Ext.baseCSSPrefix + 'trigger-cell',
                style: 'width:' + me.triggerWidth + (hideTrigger ? 'px;display:none' : 'px'),
                cn: {
                    cls: [Ext.baseCSSPrefix + 'trigger-index-' + i, triggerBaseCls, triggerCls].join(' '),
                    role: 'button'
                }
            },{
                tag: 'td',
                valign: 'top',
                style: 'padding-top:3px;width:' + me.triggerWidth + (hideTrigger ? 'px;display:none' : 'px'),
                cn: {
                    cls: [Ext.baseCSSPrefix + 'trigger-index-' + i, me.triggerClearCls, triggerCls].join(' '),
                    role: 'button'
                }
            });
        }
        triggerConfigs[i - 1].cn.cls += ' ' + triggerBaseCls + '-last';

        return Ext.DomHelper.markup(triggerConfigs);
    },
    cleanTriggerValue:function(){
    	
    },
    onTriggerWrapClick: function() {
        var me = this,
            targetEl, match,
            triggerClickMethod,
            event;

        event = arguments[me.triggerRepeater ? 1 : 0];
        var tagType = event.target.tagName;
        if (event && !me.readOnly && !me.disabled) {
                targetEl = event.getTarget('.' + me.triggerBaseCls, null);
                match = targetEl && targetEl.className.match(me.triggerIndexRe);

            if (match) {
                triggerClickMethod = me['onTrigger' + (parseInt(match[1], 10) + 1) + 'Click'] || me.onTriggerClick;
                if (triggerClickMethod) {
                    triggerClickMethod.call(me, event);
                }
            }else{
            	if(tagType!='INPUT'){
            		me.cleanTriggerValue();
            	}
            }
        }
    }
});


/**
 * @author：ex_zengxj 
 * @discription：导出，下载导出报表(零配件，印刷品)
 * @param ida2a2 对象id
 * @param objectType 对象类型
 */
com.test.comm.utils.exportReport = function(ida2a2, objectType){
	if(Ext.isEmpty(ida2a2) || Ext.isEmpty(objectType)){
		return;
	}
	var exportMask = new Ext.LoadMask(Ext.getBody(),{msg:"正在导出，请稍后..."});
	exportMask.show();
	Ext.Ajax.request({
        url:rootPath+'servlet/midearest/gplm/print/productOrder/doExport',
        params:{
		    ida2a2:ida2a2,
		    objectType : objectType
	    },
	    timeout: 30*60*1000, //30分钟
        method : 'POST',  
        success : function(request) {
        	exportMask.hide();
            var result = Ext.decode(request.responseText);
            if(result.success){
                var path = result.filePath;
                location.href =  'http://'+window.location.host
                    + '/Windchill/netmarkets/jsp/midea/exp/downloadCheckResult.jsp?downloadPath=' + path;
            } else {
            	if(!Ext.isEmpty(result.msg)){
            		Ext.Msg.alert('提示',result.msg);
            	} else {
            		Ext.Msg.alert('提示','下载失败！');
            	}
            }
        },
        failure : function() {
        	exportMask.hide();
            Ext.Msg.show({
                title : '错误提示',
                msg : '暂时无法下载，请稍候重试!',
                buttons : Ext.Msg.OK,
                icon : Ext.Msg.ERROR
            });
        }
    });
}
