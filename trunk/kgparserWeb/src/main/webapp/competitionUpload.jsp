<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Загрузка файлов соревнования</title>
	<%@ include file="./headerInclude.jspf" %>
</head>
<body>
<div id="wrapper">
	<h2>Загрузка соревнования</h2>

	<form action="<%=request.getContextPath()%>/competitionUpload" method="post" enctype="multipart/form-data">
		<table>
			<tbody>

				<tr>
					<td class="label"><label for="name-field">Название соревнования <span class="required">*</span></label></td>
					<td class="field"><input style="width: 300px;" required="required" type="text" maxlength="255" name="name" id="name-field"/></td>
				</tr>

				<tr>
					<td class="label"><label for="link-field">Ссылка на страницу соревнования</label></td>
					<td class="field"><input style="width: 500px;" type="text" maxlength="255" name="link" id="link-field"/></td>
				</tr>

				<tr>
					<td class="label"><label for="comment-field">Комментарий</label></td>
					<td class="field"><input style="width: 500px;" type="text" maxlength="255" name="comment" id="comment-field"/></td>
				</tr>

				<tr>
					<td class="label"><label for="zipFile-field">Zip-файл с результатами <span class="required">*</span></label></td>
					<td class="field"><input required="required" type="file" accept="application/zip" name="zipFile" id="zipFile-field"/></td>
				</tr>

				<tr>
					<td class="label"><label for="exportScriptVersion-field">Версия скрипта voidmain</label></td>
					<td class="field">
						<select id="exportScriptVersion-field" required="required" name="exportScriptVersion">
							<option value="1.5">1.5</option>
							<option value="1.7">1.7</option>
							<option value="1.8" selected="selected">1.8</option>
						</select>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<div class="hint">Поля, помеченные как <span class="required">*</span>, обязательны для заполнения.</div>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<div class="buttonsContainer">
							<input type="submit" value="Загрузить"/>
						</div>
					</td>
				</tr>

			</tbody>
		</table>
	</form>

	<div class="bottomLinks">
		<a href="./competitionsList.jsp">Вернуться к списку соревнований</a>
	</div>
</div>
</body>
</html>