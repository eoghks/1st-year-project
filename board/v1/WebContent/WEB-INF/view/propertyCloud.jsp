<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/static/css/propertyCloud.css" rel="stylesheet">
<title><spring:message code="message.title.property" /></title>
</head>
<body>
	<div>
		<label class="required"><spring:message code="message.cloud.hostName" /></label> 
		<input class="i1" type='text' id="host_name" required />
	</div>

	<div class="parent">
		<div class="child">
			<label>Sycros Agent<br>&nbsp;&nbsp;<spring:message code="message.cloud.installed" /></label>
		</div>
		<div class="child">
			<input class="i2" type="checkbox" id="scan_type" />
		</div>
	</div>
	<div>
		<dl>
			<dt id="dt1" class="dt1">&nbsp;&nbsp;&nbsp;<spring:message code="message.cloud.accountInfo" /></dt>
			<dd class="dd1">
				<div>
					<label class="required"><spring:message code="message.cloud.scanKey" /></label> 
					<input class="i3" type="text" id="scan_key" />
				</div>
				<div>
					<label class="required"><spring:message code="message.cloud.accessKeyId" /></label> 
					<input class="i3" type="text" id="access_key_id" required />
				</div>
				<div>
					<label class="required"><spring:message code="message.cloud.region" /></label>
					<select class="s1" id="region">
						<option value="seoul"><spring:message code="message.cloud.seoul" /></option>
						<option value="usa"><spring:message code="message.cloud.usa" /></option>
					</select>
				</div>
				<div class="parent2">
					<div>
						<label><spring:message code="message.cloud.scanInterval" /></label>
						<input class="i5" id="scan_interval" type="number" min="60" max="3600" value="300"/>
					</div>
					<div>
						<label><spring:message code="message.cloud.second" /></label>
					</div>
				</div>
			</dd>

			<dt id="dt1" class="dt2">&nbsp;<spring:message code="message.cloud.namespaceList" /></dt>
			<dd class="dd1 hidden">
				<div>
					<label><spring:message code="message.cloud.namespace" /></label>
					<select id="namespace" class="s2">
						<option value="null" selected>*<spring:message code="message.cloud.namespaceNotion" /></option>
						<option value="AWS/EC2">AWS/EC2</option>
						<option value="AWS/RDS">AWS/RDS</option>
						<option value="AWS/Lambda">AWS/Lambda</option>
					</select>
					<button class="b1" id="addRow">
						<spring:message code="message.cloud.add" />
					</button>
					<button class="b1" id="removeRow">
						<spring:message code="message.cloud.delete" />
					</button>

				</div>
				<div>
					<table id="AWS">
						<thead>
							<tr>
								<th><input type="checkbox" id="selectNamespace" class="i4" /></th>
								<th style="width: 420px;"><spring:message code="message.cloud.namespace" /></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</dd>

		</dl>
	</div>
	<div class="div2" align="right">
		<button class="b2" id="submit_check">
			<spring:message code="message.cloud.submit" />
		</button>
		<button class="b2" id="cancle">
			<spring:message code="message.cloud.cancle" />
		</button>
	</div>

	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script>
		<!--창 로드시-->
		$(document).ready(function(){
			if("${type}"==="update"){
				var property = ${property};
				$("#host_name").val(property.host_name);
				$("#host_name").prop("readonly",true);
				if(property.scan_type===1){
					$("#scan_type").prop("checked",true);
				}
				$("#scan_key").val(property.scan_key)
				$("#access_key_id").val(property.access_key_id)
				$("#region").val(property.region)
				$("#scan_interval").val(property.scan_interval)
				$.each(property.namespaces, function(index, namespace){
					var tr = addNamespace(namespace);
					$('#AWS > tbody').append(tr);
				})
			}
		})
		<!--취소 버튼-->
		$("#cancle").click(function(){
			window.close();
		})
		<!--checkbox 전체 선택/헤제-->
		$('#selectNamespace').click(function(){
			var checked = $('#selectNamespace').is(':checked');
			$('input[name=AWS]:checkbox').prop("checked", checked);
		})
		<!--Tap-->
		$("dt").click(function(){
			$('dd').each(function(dd, index){
				$(this).hide()
			})
			$(this).next().show()
			$('dt').each(function(dd, index){
				$(this).attr("class", "dt2")
			})
			$(this).attr("class","dt1")
		})
		<!--Namespace 추가-->
		$("#addRow").click(function(){
			var val=$("#namespace").val();
			if(val==="null")
				return;
			var check =false;
			$("[name=AWS]").each(function(AWS, index){
				if($(this).val()===val){
					check=true;
					alert("<spring:message code='submit.alert.sameNamespace'/>")
				}
			})
			if(check)
				return;
			var tr = addNamespace(val)
			$('#AWS > tbody').append(tr);
		})
		<!--Namespace 삭제-->
		$("#removeRow").click(function(){
			$("[name=AWS]:checked").each(function(row, index){
				$(this).closest('tr').remove();
			})
			$('#selectNamespace').prop("checked", false);
		})
		<!--확인-->
		$("#submit_check").click(function(){
			var host_name=$("#host_name").val()
			var scan_type=$('#scan_type').is(':checked');
			var scan_key=$("#scan_key").val()
			var access_key_id=$("#access_key_id").val();
			var region =$("#region").val();
			var scan_interval = $("#scan_interval").val();
			var namespaces=[];
			$("[name=AWS]").each(function(namesapce, index){
				namespaces.push($(this).val())
			})
			var type ="${type}"
			if(type==="new"){
				if(host_name===""){
					alert("<spring:message code='submit.alert.inputHost'/>");
					return
				}
				if(checkHost_name(host_name)){
					alert("<spring:message code='submit.alert.sameHost'/>");
					return;
				}
			}
			if(!scan_type && namespaces.length===0){
				alert("<spring:message code='submit.alert.inputSycrosAgent'/>");
				return;
			}
			if(scan_key===""){
				alert("<spring:message code='submit.alert.inputScanKey'/>");
				return
			}
			if(access_key_id===""){
				alert("<spring:message code='submit.alert.inputAccessKeyId'/>");
				return
			}
			if(region===""){
				alert("<spring:message code='submit.alert.inputRegion'/>");
				return
			}
			if(!(scan_interval>=60 && scan_interval <=3600)){
				alert("<spring:message code='submit.alert.inputScanInterval'/>");
				return
			}
			sendData(host_name, scan_type, scan_key, access_key_id, region, scan_interval, namespaces,type);
		})
		function checkHost_name(host_name){
			var result=false;
			$("[name=cloud]", opener.document ).each(function(){
				if(host_name===$(this).val()){
					result=true
				}
			})
			return result
		}
		
		<!--데이터 보내기-->
		function sendData(host_name, scan_type, scan_key, access_key_id, region, scan_interval, namespaces, submitType){
			if(scan_type)
				scan_type=1;
			else
				scan_type=0;
			scan_interval=Number(scan_interval);
			var obj={
				"host_name": host_name,
				"scan_type": scan_type,
				"scan_key" : scan_key,
				"access_key_id":access_key_id,
				"region":region,
				"scan_interval": scan_interval,
				"namespaces" : namespaces
			}
			var url =$(location).attr("origin");
			if(submitType==="new"){
				url = url +"/process?type=new";
			}else if(submitType==="update"){
				url = url +"/process?type=update";
			}
			$.ajax({
				url: url,
				type:"POST",
				async:false,
				contentType: "application/json",
				data: JSON.stringify(obj),
				success : function(response){
					if(response=="success"){
						opener.location.replace( $(location).attr("origin")+"?lang=${lang}");
						window.close();
					}else{
						if(submitType==="new"){
							alert("<spring:message code='error.createError'/>");
						}else{
							alert("<spring:message code='error.updateError'/>");
						}
					}
				},
				error : function(){
					if(submitType==="new"){
						alert("<spring:message code='error.createError'/>");
					}else{
						alert("<spring:message code='error.updateError'/>");
					}
				}
			
			})
			
		}
		
		function addNamespace(namespace){
			var tr= "<tr><td align='center'><input type='checkbox'' name='AWS'' value='"+namespace+"' class='i4'/></td><td>"+namespace+"</td></tr>";
			return tr;
		}
	</script>
</body>
</html>