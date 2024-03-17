<!DOCTYPE html>
<html>
<head>
    <title>metting management</title>
    <#include "common.ftl" />
    <style>
        .photoList {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: center;
            list-style: none;
            padding: 0;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
            background-color: #fff;
            padding: 10px;
            margin: 10px;
            cursor: pointer;
            transition: transform 0.3s;
        }
        .card:hover {
            transform: scale(1.05);
        }
        .card img {
            width: 100%;
            height: auto;
            border-radius: 10px 10px 0 0;
        }
        .desc {
            text-align: center;
            padding: 10px;
            background-color: #f4f4f4;
            border-radius: 0 0 10px 10px;
        }
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(to top, rgba(0, 0, 0, 0.9) 0%, rgba(0, 0, 0, 0.7) 50%, rgba(0, 0, 0, 0.5) 100%);
            z-index: 999;
            display: none;
        }
        .overlayImg {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            display: none;
            max-width: 80%;
            max-height: 80%;
            border: 5px solid #fff;
            border-radius: 10px;
        }
        .overlayDesc {
            position: absolute;
            left: 50%;
            bottom: 10px;
            transform: translateX(-50%);
            color: #fff;
            text-align: center;
        }

    </style>
    <script>
        $(document).ready(function () {
            // 点击蒙层消失
            $(".overlay").on("click", function () {
                $(".overlay").hide();
            })

            // 点击卡片展示蒙层
            $(".card").on("click", function () {
                var url = $(this).find("img").attr("src");
                var subHtml = $(this).find(".desc").attr("data-html");
                $(".overlayImg").attr("src", url).show();
                $(".overlayDesc").html(subHtml);
                $(".overlayDesc").find("button").remove();
                $(".overlay").show();
            });
        })
    </script>
</head>
<body>

<#include "header.ftl"/>

<h1>metting management</h1>

<table  border="1">
    <tr>
        <td>key</td>
        <td>value</td>
    </tr>
    <tr>
        <td>name</td>
        <td>${speaker["name"]}</td>
    </tr>
    <tr>
        <td>title</td>
        <td>${speaker["title"]!""}</td>
    </tr>

    <tr>
        <td>firstName</td>
        <td>${speaker["firstName"]!""}</td>
    </tr>

    <tr>
        <td>geburtsdatum</td>
        <td>${speaker["geburtsdatum"]?string("yyyy-MM-dd")}</td>
    </tr>

    <tr>
        <td>geburtsort</td>
        <td>${speaker["geburtsort"]!""}</td>
    </tr>
    <tr>
        <td>photos</td>
        <td>
            <#if speaker.memberImgList?has_content>
                <ul class="photoList">
                    <#list speaker.memberImgList as img>
                        <li class="card">
                            <img src="${img.imgLink}" alt="member imgs">
                            <div class="desc" data-html="${img.description}">click photo to see description</div>
                        </li>
                    </#list>
                </ul>
            <#else>
                <p>There are no member images to display.</p>
            </#if>
        </td>
    </tr>

    <tr>
        <td>sterbedatum</td>
        <td>${speaker["sterbedatum"]!""}</td>
    </tr>

    <tr>
        <td>geschlecht</td>
        <td>${speaker["geschlecht"]!""}</td>
    </tr>

    <tr>
        <td>beruf</td>
        <td>${speaker["beruf"]!""}</td>
    </tr>

    <tr>
        <td>akademischertitel</td>
        <td>${speaker["akademischertitel"]!""}</td>
    </tr>

    <tr>
        <td>familienstand</td>
        <td>${speaker["familienstand"]!""}</td>
    </tr>

    <tr>
        <td>religion</td>
        <td>${speaker["religion"]!""}</td>
    </tr>
    <tr>
        <td>vita</td>
        <td>${speaker["vita"]!""}</td>
    </tr>
    <tr>
        <td>adressing</td>
        <td>${speaker["adressing"]!""}</td>
    </tr>
    <tr>
        <td>party</td>
        <td>${speaker["party"]!""}</td>
    </tr>
    <tr>
        <td>role</td>
        <td>${speaker["role"]!""}</td>
    </tr>
    <tr>
        <td>fraction</td>
        <td>${speaker["fraction"]!""}</td>
    </tr>
</table>
<div class="overlay">
    <img src="#" class="overlayImg" style="display: none">
    <div class="overlayDesc"></div>
</div>
</body>
</html>






