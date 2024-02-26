<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
	<script src="https://kit.fontawesome.com/def66b134a.js"></script>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" integrity="sha512-xh6O/CkQoPOWDdYTDqeRdPCVd1SpvCA9XXcUnZS2FmJNp1coAFzvtCN9BmamE+4aHK8yyUHUSCcJHgXloTyT2A==" crossorigin="anonymous" referrerpolicy="no-referrer" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/js/all.min.js" integrity="sha512-naukR7I+Nk6gp7p5TMA4ycgfxaZBJ7MO5iC3Fp6ySQyKFHOGfpkSZkYVWV5R7u7cfAicxanwYQ5D1e17EfJcMA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<link href="/test/static/css/manageCloud.css" rel="stylesheet">
	<meta charset="UTF-8">
	<title><spring:message code="message.title.manage" /></title>
</head>
<body>

	<div class="d1">
		<select id="lang" class="s1">
			<option id="ko_KR" value="ko_KR"><spring:message code="lang.ko" /></option>
			<option id="en" value="en"><spring:message code="lang.en" /></option>
		</select>
	</div>

	<div class="container">
		<div>
			<button id="new" class="b1">
				<spring:message code="message.cloud.new" />
			</button>
			<button id="delete" class="b1">
				<spring:message code="message.cloud.delete" />
			</button>
		</div>

		<div>
			<select id="type" name="type" class="s1">
				<option value="hostName" selected><spring:message code="message.cloud.host" /></option>
				<option value="scanType"><spring:message code="message.cloud.scanType" /></option>
				<option value="scanKey"><spring:message code="message.cloud.scanKey" /></option>
				<option value="accessKeyId"><spring:message code="message.cloud.accessKeyId" /></option>
				<option value="region"><spring:message code="message.cloud.region" /></option>
				<option value="namespaces"><spring:message code="message.cloud.namespaces" /></option>
			</select> <input type="text" id="val1" class="i1"></input>
			<select id="val2" class="s1 hidden">
				<option value="1" selected><spring:message
						code="message.cloud.yes" /></option>
				<option value="0"><spring:message code="message.cloud.no" /></option>
			</select> 
			<select id="val3" class="s1 hidden">
				<option value="seoul" selected><spring:message
						code="message.cloud.seoul" /></option>
				<option value="usa"><spring:message code="message.cloud.usa" /></option>
			</select> 
			<input type="number" id="val4" class="i1 hidden" min="0" max="3" value="0"></input>
			
			<button id="search">
				<i class="fas fa-solid fa-magnifying-glass"></i>
			</button>
		</div>
	</div>

	<div style="background-color: #EFFBFB">
		<table id=cloudList>
			<thead>
				<tr>
					<th><input type="checkbox" id=selectCloud value="selectall" /></th>
					<th><spring:message code="message.cloud.host" /></th>
					<th><spring:message code="message.cloud.scanType" /></th>
					<th><spring:message code="message.cloud.scanKey" /></th>
					<th><spring:message code="message.cloud.accessKeyId" /></th>
					<th><spring:message code="message.cloud.region" /></th>
					<th><spring:message code="message.cloud.namespaces" /></th>
					<th width="20%"></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>

	<!-- JQuery -->
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script>
		<!--창 로드시-->
		$(document).ready(function(){
			var result = ${result};
			$.each(result.data, function(index, cloud){
				var tr = addRow(index,cloud);
				$('#cloudList > tbody').append(tr);
			});
			$(".popup").unbind("click").bind("click", click); 
			$("#lang").prop("value", "${lang}");
		})
		<!--언어 변경-->
		$('#lang').change(function(){
			var url =$(location).attr("origin")+"/test"+"?lang="+$(this).val();
			location.replace(url)
		})
		<!-- 체크 박스 전체 선택/해제-->
		$('#selectCloud').click(function(){
			var checked = $('#selectCloud').is(':checked');
			$('input[name=cloud]:checkbox').prop("checked", checked);
		})
		
		<!-- 검색 format 변경-->
		$('#type').change(function(){
			var type=$("#type").val();
			var val1=$("#val1");
			var val2=$("#val2");
			var val3=$("#val3");
			var val4=$("#val4");
			if(type==="scanType"){
				val1.hide();
				val2.show();
				val3.hide();
				val4.hide();
			}else if(type==="region"){
				val1.hide();
				val2.hide();
				val3.show();
				val4.hide();
			}else if(type==="namespaces"){
				val1.hide();
				val2.hide();
				val3.hide();
				val4.show();
			}else{
				val1.show();
				val2.hide();
				val3.hide();
				val4.hide();
			}
			
		})
		<!-- 삭제 -->
		$('#delete').click(function(){
			var cloud=$("[name=cloud]");
			var host_names=[];
			var ids=[];
			cloud.each(function(index,host_name){
				if($(host_name).is(":checked")){
					host_names.push($(host_name).val());
					ids.push($(host_name).attr("id"))
				}
			});
			var obj = {"host_names": host_names}
			var url =$(location).attr("origin")+"/test/delete";
			$.ajax({
				url: url,
				type:"POST",
				async:false,
				contentType: "application/json",
				data: JSON.stringify(obj),
				success : function(response){
					if(response===""){
						alert("<spring:message code='error.dbError'/>")
						location.replace( $(location).attr("origin"));
						return;
					}else{
						$.each($(".popup"),function(){
							$(this).closest("tr").remove()
						})
						$.each(response.data,function(index, el){
							var tr=addRow(index, el);
							$('#cloudList > tbody').append(tr);
							$(".popup").unbind("click").bind("click", click); 
						})
					}
				}
			})
		})
		<!-- 새로만들기 -->
		$('#new').click(function(){
			click("new");
		})
		
		<!--검색-->
		$("#search").click(function(){
			var type = $("#type").val();
			var val ;
			if(type==="scanType"){
				val = $("#val2").val();
			}else if(type==="region"){
				val = $("#val3").val();
			}
			else if(type==="namespaces"){
				
				if( $("#val4").val() < 0 ||  $("#val4").val() > 3 ){
					val = 0;
					$("#val4").val(0);
				}
				val = $("#val4").val()
			}else{
				val = $("#val1").val();
			}
			obj={
				"type":type,
				"val":val
			}
			var url =$(location).attr("origin")+"/test/search";
			$.ajax({
				url: url,
				type:"POST",
				async:false,
				contentType: "application/json",
				data: JSON.stringify(obj),
				success:function(response){
					if(response===""){
						alert("<spring:message code='error.dbError'/>")
						location.replace( $(location).attr("origin"));
						return;
					}else{
						$.each($(".popup"),function(){
							$(this).closest("tr").remove()
						})
						$.each(response.data,function(index, el){
							var tr=addRow(index, el);
							$('#cloudList > tbody').append(tr);
							$(".popup").unbind("click").bind("click", click); 
						})
					}
				}
			})
		})
		
		<!--속성-->
		function click(option) {
			var cw=510;
			var ch=480;
			
			var sw=window.screen.width ;
			var sw=sw+window.screenLeft;
			var sh=window.screen.height;
			
			var px=String((sw-cw)/2);
			var py=String((sh-ch)/2);
			
			host_name=$(this).text();
			var windowOption= 'left='+px+', top='+py+', width=510, height=480, location=no, status=no scrollbars=yes'
			var url ='/test/property?host_name='+encodeURIComponent(host_name)
			if(option==="new"){
				openWin=window.open(url +"&type=new", 'newCloud', windowOption);
			}else{
				openWin=window.open(url +"&type=update", 'updateCloud', windowOption);
			}
		}
		<!--테이블 행 추가 함수 -->
		function addRow(index,cloud){
			var tr="<tr><td><input type='checkbox' name='cloud' value='"+cloud.host_name+"' id='cloud"+index+"'/></td>";
			tr = tr+"<td><a class='popup' name='property' >"+cloud.host_name+"</a></td>";
			
			var scanType = "<spring:message code='message.cloud.no'/>";
			if(cloud.scan_type===1)
				scanType = "<spring:message code='message.cloud.yes'/>";
			tr = tr +"<td>"+scanType+"</td><td>"+cloud.scan_key+"</td>";
			var region ="<spring:message code='message.cloud.seoul'/>";
			if(cloud.region=="usa" || cloud.region=="미국")
				region="<spring:message code='message.cloud.usa'/>"
			tr = tr +"<td>"+cloud.access_key_id+"</td><td>"+region+"</td>";
			tr = tr +"<td>"+cloud.namespaces+"</td><td></td></tr>";
			return tr;
		}
		
	</script>
</body>
</html>
