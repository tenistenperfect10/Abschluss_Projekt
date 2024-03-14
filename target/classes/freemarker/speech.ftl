<!DOCTYPE html>
<html>
<head>
    <title>meeting management</title>
    <#include "common.ftl" />
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }

        h1 {
            text-align: center;
            color: #fff;
        }

        form {
            margin-top: 20px;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .input-group {
            display: flex;
            justify-content: space-between;
            justify-content: space-around;
            margin-bottom: 20px;
        }

        .date-box {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-right: 20px;
        }

        input[type="datetime-local"] {
            margin-top: 5px;
            padding: 8px;
            width: 200px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"], button[type="reset"] {
            padding: 10px;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button[type="reset"] {
            background-color: #ccc;
            margin-left: 10px;
        }
        .header {
            display: flex;
            background-color: #ddd; /* 灰色背景 */
            font-weight: bold; /* 加粗字体 */
            margin-bottom: 10px;
        }
        #directory, .header {
            width: 70%;
            margin: 0 auto;
            padding: 10px;
        }
        ul {
            list-style: none;
            padding-left: 20px;
        }

        .folder {
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            cursor: pointer;
            userImpl-select: none;
            font-weight: bold;
        }
        .pdl20 {
            padding-left: 20px;
        }
        .speaker {
            width: 120px;
        }
        .title {
            width: 200px;
        }
        li {
            display: flex;
        }
        .file {
            display: flex;
        }
        .startTime {
            width: 120px;
            color: #4CAF50;
        }
        .endTime {
            width: 120px;
            color: darkred;
        }
        .place {
            width: 80px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }
        .link {
            width: 150px;
            text-decoration: underline;
        }
        .flex-box {
            display: flex;
            align-items: center;
        }
        .date-box {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .queryLine {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 20px;
            padding: 10px 0;
            border-bottom: 1px solid #ccc;
        }

        .queryLine input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            margin-right: 10px;
            border: none;
            border-radius: 5px;
            background-color: #c9d7e4;
            box-sizing: border-box;
        }

        .queryLine button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .queryLine button:hover {
            background-color: #0056b3;
        }
    </style>
    <script>
        $(document).ready(function() {
            $('.folder').next('ul').hide(); // 隐藏所有的ul
            $("#directory").show();

            $('.folder').click(function() {
                $(this).next('ul').slideToggle();
            });
        });
        function validateForm() {
            var startTimeStr = document.getElementById('startTime').value;
            var endTimeStr = document.getElementById('endTime').value;
            if (startTimeStr === "" || endTimeStr === "") {
                alert("Please fill in both start time and end time.");
                return false; // 阻止表单提交
            }
            var startTime = new Date(startTimeStr.value);
            var endTime = new Date(endTimeStr.value);
            // 检查结束时间是否早于开始时间
            if (endTime < startTime) {
                alert('The start time： ' + startTime + 'cannot be greater than the end time:' + endTime + '!');
                return false;
            }
            return true; // 允许表单提交
        }
    </script>
</head>
<body>

<#include "header.ftl"/>

<h1>meeting management</h1>

<form action="/speech/search" method="post" id="form" onsubmit="return validateForm()">


    <div class="col-sm-4 input-group flex-box" id="date-picker">
        <div class="date-box" style="margin-right: 20px;">
            <label for="startTime">startTime</label>
            <div>
                <input type="datetime-local" id="startTime" name="startTime" value="${startDate!""}">
                <i id="calendar-icon" class="fa fa-calendar"></i>
            </div>
        </div>
        <div class="date-box">
            <label for="endTime">endTime</label>
            <div>
                <input type="datetime-local" id="endTime" name="endTime" value="${endDate!""}">
                <i id="calendar-icon" class="fa fa-calendar"></i>
            </div>
        </div>
    </div>
    <div class="input-group">
        <input type="submit" value="Search"/>
        <button type="reset" onclick="window.open('/speech', '_self')">clear</button>
    </div>
</form>

<div class="queryLine">
    <input id="queryInput" type="text" required placeholder="Please enter keywords for global search" />
    <button onclick="window.open('/speech/query?keyword=' + $('#queryInput').val(), '_self')">
        <i class="fa fa-search"></i>
    </button>
</div>

<#if errorMsg??>
    <h3>${errorMsg}</h3>
</#if>
<div class="header">
    <span class="speaker pdl20">speaker</span>
    <span class="title pdl20">title</span>
    <span class="startTime pdl20">startTime</span>
    <span class="endTime">endTime</span>
    <span class="place">place</span>
    <span class="link">speech detail</span>
    <span class="link">speaker detail</span>
</div>
<div id="directory" style="display: none">
    <#if speechMap?exists && speechMap?size != 0>
        <ul>
            <#list speechMap?keys as key>
                <#assign parts = key?split("&")>
                <li>
                    <span class="folder speaker">${parts[1]}</span>
                    <ul>
                        <#assign titleMap = speechMap[key]>
                        <#list titleMap?keys as subKey>
                            <li>
                                <span class="folder title">${subKey}</span>
                                <ul>
                                    <#list titleMap[subKey] as speech>
                                        <li class="file">
                                            <span class="startTime">${speech["starttime"]?number_to_date!""}</span>
                                            <span class="endTime">${speech["endtime"]?number_to_date!""}</span>
                                            <span class="place">${speech["place"]!""}</span>
                                            <a class="link" href="/speechDetail/${speech["_id"]}">speech detail</a>
                                            <a class="link" href="/speakerDetail/${speech["speakerId"]}">speaker detail</a>
                                        </li>
                                    </#list>
                                </ul>
                            </li>
                        </#list>
                    </ul>
                </li>
            </#list>
        </ul>
    </#if>
</div>

</body>
</html>

