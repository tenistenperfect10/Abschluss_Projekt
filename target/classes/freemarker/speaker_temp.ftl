<!DOCTYPE html>
<html>
<head>
    <title>metting management</title>
    <style>
        .photoList {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2); /* 阴影效果 */
            background-color: #ffffff; /* 背景色 */
            padding: 20px;
            margin: 10px;
        }
        .photoList li {
            width: 250px;
            height: 200px;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            cursor: pointer;
        }
        .photoList li img {
            width: 200px;
            height: 150px;
        }
        .desc {
            height: 30px;
            text-align: center;
            margin-top: 20px;
        }
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(to top, rgba(0, 0, 0, 0.9) 0%, rgba(0, 0, 0, 0.7) 50%, rgba(0, 0, 0, 0.5) 100%);
            z-index: 999; /* Ensure that it is displayed at the top */
            display: none; /* Not displayed in the initial state */
        }

        .overlayImg {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            display: none;
        }

        .overlayDesc {
            position: absolute;
            left: 0;
            bottom: 0;
            color: #dddddd;
        }

    </style>
    <script src="/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            // Click on the mask to disappear
            $(".overlay").on("click", function () {
                $(".overlay").hide();
            })

            // Click on the card to show the mask
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

