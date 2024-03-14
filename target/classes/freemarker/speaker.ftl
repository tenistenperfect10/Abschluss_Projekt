<!DOCTYPE html>
<html>
<head>
    <title>Insight Bundestag</title>
    <#include "common.ftl" />
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
            max-width: 100px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        h1 {
            text-align: center;
        }

        form {
            margin-bottom: 20px;
        }

        input[type="text"] {
            margin-right: 10px;
            padding: 5px;
        }

        input[type="submit"] {
            padding: 7px 10px;
            border-radius: 4px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

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
        .modal-title {
            text-align: center;
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
        /* 设置表单项的样式 */
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
            background-color: #45a049; /* Background color change on mouse hover */
        }
    </style>
    <script>
        function showModal(title) {
            $("myModal").find(".modal-title").text(title);
            $("#myModal").show();
        }

        $(document).ready(function () {
            var speaker = {};
            var speakerId = null;

            $("#speakerDetailForm").on("submit", function (e) {
                e.preventDefault();
                var geburtsdatum = null;
                if ($("#geburtsdatum").val().length > 0) {
                    console.log($("#geburtsdatum").val())
                    geburtsdatum = new Date($("#geburtsdatum").val())
                }
                var sterbedatum = null;
                if ($("#sterbedatum").val().length > 0) {
                    sterbedatum = new Date($("#sterbedatum").val());
                }
                var newSpeaker = {
                    id: speakerId,
                    name: $("#speakerName").val(),
                    firstName: $("#speakerFirstName").val(),
                    title: $("#title").val(),
                    geburtsdatum: geburtsdatum,
                    geburtsort: $("#geburtsort").val(),
                    sterbedatum: sterbedatum,
                    geschlecht: $("#geschlecht").val(),
                    beruf: $("#beruf").val(),
                    akademischertitel: $("#akademischertitel").val(),
                    familienstand: $("#familienstand").val(),
                    religion: $("#religion").val(),
                    vita: $("#vita").val(),
                    adressing: $("#adressing").val(),
                    party: $("#speakerParty").val(),
                    fraction: $("#speakerFraction").val(),
                    role: $("#role").val(),
                }
                $.ajax({
                    url: "/api/speaker/save",
                    method: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(newSpeaker),
                    success: function(response) {
                        // Act on success, handling the response returned by the backend as needed
                        if (response && response.code === 0) {
                            messageBox.showSuccess("Update succeed");
                            location.reload();
                        } else {
                            messageBox.showError(response.message);
                        }
                    },
                    error: function(error) {
                        // 处理错误
                        console.log(error);
                    }
                })
            })

            $(".editBtn").on("click", function () {
                var tr = $(this).closest("tr");
                speakerId = tr.attr("data-id");
                // Get speaker object
                speaker.name = tr.find(".speaker").find("a").text(); // Retrieve the 'name' field
                speaker.firstName = tr.find(".firstName").text(); // Retrieve the 'firstName' field
                speaker.title = tr.find(".title").text(); // Retrieve the 'title' field
                speaker.geburtsdatum = tr.find(".geburtsdatum").text(); // Retrieve the 'geburtsdatum' field (birth date)
                speaker.geburtsort = tr.find(".geburtsort").text(); // Retrieve the 'geburtsort' field (place of birth)
                speaker.sterbedatum = tr.find(".sterbedatum").text(); // Retrieve the 'sterbedatum' field (date of death)
                speaker.geschlecht = tr.find(".geschlecht").text(); // Retrieve the 'geschlecht' field (gender)
                speaker.beruf = tr.find(".beruf").text(); // Retrieve the 'beruf' field (profession)
                speaker.akademischertitel = tr.find(".akademischertitel").text(); // Retrieve the 'akademischertitel' field (academic title)
                speaker.familienstand = tr.find(".familienstand").text(); // Retrieve the 'familienstand' field (marital status)
                speaker.religion = tr.find(".religion").text(); // Retrieve the 'religion' field
                speaker.vita = tr.find(".vita").text(); // Retrieve the 'vita' field (biography)
                speaker.adressing = tr.find(".adressing").text(); // Retrieve the 'adressing' field
                speaker.party = tr.find(".party").text(); // Retrieve the 'party' field
                speaker.fraction = tr.find(".fraction").text(); // Retrieve the 'fraction' field
                speaker.role = tr.find(".role").text(); // Retrieve the 'role' field
                console.log(speaker);


                $("#speakerName").val(tr.find(".speaker").find("a").text());
                $("#speakerFirstName").val(tr.find(".firstName").text());
                $("#title").val(tr.find(".title").text());
                $("#geburtsdatum").val(tr.find(".geburtsdatum").text());
                $("#geburtsort").val(tr.find(".geburtsort").text());
                $("#sterbedatum").val(tr.find(".sterbedatum").text());
                $("#geschlecht").val(tr.find(".geschlecht").text());
                $("#beruf").val(tr.find(".beruf").text());
                $("#akademischertitel").val(tr.find(".akademischertitel").text());
                $("#familienstand").val(tr.find(".familienstand").text());
                $("#religion").val(tr.find(".religion").text());
                $("#vita").val(tr.find(".vita").text());
                $("#adressing").val(tr.find(".adressing").text());
                $("#speakerParty").val(tr.find(".party").text());
                $("#speakerFraction").val(tr.find(".fraction").text());
                $("#role").val(tr.find(".role").text());

                $("#myModal").show();

            })
        })
    </script>
</head>
<body>

<a href="/speaker">speaker</a> | <a href="/speech">speech</a> | <a href="/nlpAnalyse">nlpAnalyse</a>

<h1 style="margin: 20px 0;">Insight Bundestag</h1>

<form action="/speaker/search" method="post">
    <input type="text" name="name" id="name" value="" placeholder="Search by name" />
    <input type="text" name="firstName" id="firstName" value="" placeholder="Search by firstName" />
    <input type="text" name="id" id="id" value="" placeholder="Search by ID" />
    <input type="text" name="fraction" id="fraction" value="" placeholder="Search by faction" />
    <input type="text" name="party" id="party" value="" placeholder="Search by Party" />
    <input type="submit" value="Search" />
</form>
<#if canEdit?exists && canEdit == 1><button class="button" onclick="showModal('add userImpl')">add speaker</button></#if>

<table>
    <tr>
        <th>name</th>
        <th>firstName</th>
        <th>title</th>
        <th>geburtsdatum</th>
        <th>geburtsort</th>
        <th>sterbedatum</th>
        <th>geschlecht</th>
        <th>beruf</th>
        <th>akademischertitel</th>
        <th>familienstand</th>
        <th>religion</th>
        <th>vita</th>
        <th>adressing</th>
        <th>party</th>
        <th>fraction</th>
        <th>role</th>
        <#if canEdit?exists && canEdit == 1>
            <th>operation</th>
        </#if>
    </tr>
    <#list speakerList as speaker>
        <tr data-id="${speaker["_id"]}">
            <td class="speaker"><h2><a href="/speakerDetail/${speaker["_id"]}">${speaker["name"]}</a></h2></td>
            <td class="firstName">${speaker["firstName"]!""}</td>
            <td class="title">${speaker["title"]!""}</td>
            <td class="geburtsdatum"><#if speaker.geburtsdatum??>${speaker.geburtsdatum?datetime?string("yyyy-MM-dd")}</#if></td>
            <td class="geburtsort">${speaker["geburtsort"]!""}</td>
            <td class="sterbedatum"><#if speaker.sterbedatum??>${speaker.sterbedatum?datetime?string("yyyy-MM-dd")}</#if></td>
            <td class="geschlecht">${speaker["geschlecht"]!""}</td>
            <td class="beruf">${speaker["beruf"]!""}</td>
            <td class="akademischertitel">${speaker["akademischertitel"]!""}</td>
            <td class="familienstand">${speaker["familienstand"]!""}</td>
            <td class="religion">${speaker["religion"]!""}</td>
            <td class="vita">${speaker["vita"]!""}</td>
            <td class="adressing">${speaker["adressing"]!""}</td>
            <td class="party">${speaker["party"]!""}</td>
            <td class="fraction">${speaker["fraction"]!""}</td>
            <td class="role">${speaker["role"]!""}</td>
            <#if canEdit?exists && canEdit == 1><td><button class="editBtn button">edit</button></td></#if>
        </tr>
    </#list>
</table>
<!-- modal box -->
<div id="myModal" class="modal">

    <div class="modal-content">
        <span class="close" onclick="$('#myModal').hide();$('#speakerDetailForm')[0].reset();">&times;</span>
        <h2 class="modal-title"></h2>
        <!-- Forms for displaying and modifying data -->
        <form id="speakerDetailForm">
            <input type="hidden" id="_id" name="_id" value="" />

            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="speakerName" name="name" value="" required />
            </div>

            <div class="form-group">
                <label for="firstName">FirstName:</label>
                <input type="text" id="speakerFirstName" name="firstName" value="" required />
            </div>

            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title" value="" />
            </div>

            <div class="form-group">
                <label for="geburtsdatum">Geburtsdatum:</label>
                <input type="date" id="geburtsdatum" name="geburtsdatum" value="" required />
            </div>

            <div class="form-group">
                <label for="geburtsort">Geburtsort:</label>
                <input type="text" id="geburtsort" name="geburtsort" value="" />
            </div>

            <div class="form-group">
                <label for="sterbedatum">Sterbedatum:</label>
                <input type="date" id="sterbedatum" name="sterbedatum" value="" />
            </div>

            <div class="form-group">
                <label for="geschlecht">Geschlecht:</label>
                <select id="geschlecht" name="geschlecht">
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>
                </select>
            </div>

            <div class="form-group">
                <label for="beruf">Beruf:</label>
                <input type="text" id="beruf" name="beruf" value="" />
            </div>

            <div class="form-group">
                <label for="akademischertitel">Akademischertitel:</label>
                <input type="text" id="akademischertitel" name="akademischertitel" value="" />
            </div>

            <div class="form-group">
                <label for="familienstand">Familienstand:</label>
                <select id="familienstand" name="familienstand">
                    <option value="single">Single</option>
                    <option value="married">Married</option>
                    <option value="divorced">Divorced</option>
                </select>
            </div>

            <div class="form-group">
                <label for="religion">Religion:</label>
                <input type="text" id="religion" name="religion" value="" />
            </div>

            <div class="form-group">
                <label for="vita">Vita:</label>
                <textarea id="vita" name="vita"></textarea>
            </div>

            <div class="form-group">
                <label for="adressing">Adressing:</label>
                <input type="text" id="adressing" name="adressing" value="" />
            </div>

            <div class="form-group">
                <label for="party">Party:</label>
                <input type="text" id="speakerParty" name="party" value="" />
            </div>

            <div class="form-group">
                <label for="fraction">Fraction:</label>
                <input type="text" id="speakerFraction" name="fraction" value="" />
            </div>

            <div class="form-group">
                <label for="role">Role:</label>
                <input type="text" id="role" name="role" value="" />
            </div>

            <input type="submit" value="Update">
        </form>

    </div>
</div>
</body>
</html>
