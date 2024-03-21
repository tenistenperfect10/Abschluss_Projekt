<!-- Authors: He Liu, Yu Ming -->
<!DOCTYPE html>
<html>
<head>
    <title>metting management</title>
    <#include "common.ftl"/>
    <style>
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        .modal-title {
            text-align: center;
        }

        .modal-content {
            background-color: #fefefe;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            border-radius: 4px;
            max-height: 600px;
            overflow: auto;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
        form {
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        /* Setting the style of form items */
        .form-group {
            width: 80%;
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        .form-group label {
            width: 150px;
        }
        .form-group input[type="text"],
        .form-group input[type="date"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .form-group textarea {
            height: 100px;
        }
        .button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 8px;
        }
        .button:hover {
            background-color: #45a049; /* Background colour change on mouse hover */
        }
    </style>
    <script>
        $(document).ready(function () {
            $("#form").on("submit", function (e) {
                e.preventDefault();

                var speech = {
                    "_id": $("#_id").val(),
                    "text": $("#text").val(),
                    "length": $("#text").val().length,
                    "protocol.starttime": (new Date($("#starttime").val())).getTime(),
                    "protocol.endtime": (new Date($("#endtime").val())).getTime(),
                    "title": $("#title").val(),
                    "protocol.place": $("#place").val()
                }
                $.ajax({
                    url: "/api/speech/update",
                    method: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(speech),
                    success: function(response) {
                        // After the successful operation, process the response returned by the backend as needed
                        if (response && response.code === 0) {
                            messageBox.showSuccess("Update succeed");
                            location.reload();
                        } else {
                            messageBox.showError(response.message);
                        }
                    },
                    error: function(error) {
                        // process error
                        console.log(error);
                    }
                })
            })
        })
    </script>
</head>
<body>

<#include "header.ftl"/>

<h1>metting management</h1>

<#if canEdit?exists && canEdit == 1><button class="button" onclick="$('#myModal').show();">edit speech</button></#if>

<table border="1">
    <tr>
        <td>key</td>
        <td>value</td>
    </tr>
    <tr>
        <td>text</td>
        <td>${speech["text"]}</td>
    </tr>
    <tr>
        <td>length</td>
        <td>${speech["length"]!""}</td>
    </tr>

    <tr>
        <td>name</td>
        <td>${speaker["name"]!""}</td>
    </tr>

    <tr>
        <td>starttime</td>
        <td>${speech["starttime"]?number_to_date!""}</td>
    </tr>

    <tr>
        <td>endtime</td>
        <td>${speech["endtime"]?number_to_date!""}</td>
    </tr>

    <tr>
        <td>title</td>
        <td>${speech["title"]!""}</td>
    </tr>

    <tr>
        <td>place</td>
        <td>${speech["place"]!""}</td>
    </tr>
</table>

<h3>comment</h3>
<table >
    <tr>
        <td>speaker</td>
        <td>text</td>
    </tr>

    <#list commentList as comment>
        <tr>
            <td><a href="/speakerDetail/${comment["speakerId"]}">${comment["speakerName"]}</a></td>
            <td>${comment["text"]!""}</td>
        </tr>
    </#list>
</table>

<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="$('#myModal').hide();">&times;</span>
        <h2 class="modal-title">Edit speech</h2>
        <form id="form">
            <input type="hidden" id="_id" value="${speech["_id"]}" />

            <div class="form-group">
                <label for="text">Text:</label>
                <textarea type="text" id="text" name="text">${speech['text']}</textarea>
            </div>

            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" value="${speaker['name']!''}" disabled/>
            </div>

            <div class="form-group">
                <label for="starttime">Start Time:</label>
                <input type="datetime-local" id="starttime" name="starttime" value="${startDate!""}" />
            </div>

            <div class="form-group">
                <label for="endtime">End Time:</label>
                <input type="datetime-local" id="endtime" name="endtime" value="${endDate!""}" />
            </div>

            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title" value="${speech['title']!''}" />
            </div>

            <div class="form-group">
                <label for="place">Place:</label>
                <input type="text" id="place" name="place" value="${speech['place']!''}" />
            </div>

            <input type="submit" value="Update" />
        </form>
    </div>
</div>

</body>
</html>

