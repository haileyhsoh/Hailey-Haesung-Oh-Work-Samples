<?php

$host = "303.itpwebdev.com";
$user = "haileyoh_db_user";
$pass = "uscitp2024";
$db = "haileyoh_fp_db";

// DB Connection.
$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_errno) {
    echo $mysqli->connect_error;
    exit();
}

$mysqli->set_charset('utf8');
?>

<!DOCTYPE html>
<html>

<head>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <title>Trojan Weather - Edit List</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <link href="lib/font/css/open-iconic-bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        .container {
            border-radius: 10px;
        }

        #menu-bar {
            margin-top: 20px;
            margin-bottom: 20px;

        }

        #header {
            background-color: #A0D398;
            padding: 10px;
            margin-bottom: 20px;
        }

        #saved-cities-title {
            background-color: #c6e5f7;
            padding: 10px;
            margin-bottom: 20px;
        }

        #delete-all {
            margin-bottom: 10px;
            margin-right: 20px;
        }

        #add-button {
            margin-left: 20px;
        }
    </style>

</head>

<body>

    <div id="menu-bar" class="container">
        <div class="row">
            <div class="col-6">
                <a class="btn btn-outline-dark" href="home_page.php" role="button">Back to Home</a>
            </div>
            <div class="col-6 text-end">
                <a class="btn btn-outline-dark" href="my_cities.php" role="button">My Saved Cities</a>
            </div>
        </div>


    </div>

    <div id="header" class="container">
        <div class="row">
            <h1 class="col-12 text-center">Trojan Weather</h1>
        </div>
    </div>

    <div id="saved-cities-title" class="container">
        <div class="row">
            <h3 class="col-12 text-center">Add or Remove Cities</h3>
        </div>
    </div>


    <div id="list" class="container">

        <form id="list-form">
            <div class="form-row justify-content-end">

                <button id="delete-all" class="btn btn-danger">Delete All</button>
                <select id="cities" class="col-5 form-control">
                    <option value="Los Angeles">Los Angeles</option>
                    <option value="London">London</option>
                    <option value="Paris">Paris</option>
                    <option value="Seoul">Seoul</option>
                    <option value="Bangkok">Bangkok</option>
                    <option value="New York">New York</option>
                    <option value="San Francisco">San Francisco</option>
                    <option value="Tokyo">Tokyo</option>
                    <option value="Rome">Rome</option>
                    <option value="Budapest">Budapest</option>
                    <option value="Cape Town">Cape Town</option>
                </select>
                <div class="col">
                    <button id="add-button" role="button" type="submit" class="btn btn-primary">Add</button>
                </div> <!-- .col -->
            </div> <!-- .form-row -->
        </form>

        <ul id="prio-list" class="list-group">

            <li id="test" class="list-group-item d-flex justify-content-between">
                <div class="todo-item">London</div>
                <div class="todo-info">
                    <span class="todo-remove oi oi-circle-x" title="Remove"></span>
                </div>
            </li>

            <li class="list-group-item d-flex justify-content-between">
                <div class="todo-item">Paris</div>
                <div class="todo-info">
                    <span class="todo-remove oi oi-circle-x" title="Remove"></span>
                </div>
            </li>

            <li class="list-group-item d-flex justify-content-between">
                <div class="todo-item">New York</div>
                <div class="todo-info">
                    <span class="todo-remove oi oi-circle-x" title="Remove"></span>
                </div>
            </li>
        </ul>

    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            document.getElementById('delete-all').addEventListener('click', function (e) {
                e.preventDefault();
                var list = document.getElementById('prio-list');
                list.innerHTML = '';
            });

            document.getElementById('list-form').addEventListener('submit', function (e) {
                e.preventDefault();

                var selectedCity = document.getElementById('cities').value;

                var newItem = document.createElement('li');
                newItem.classList.add('list-group-item', 'd-flex', 'justify-content-between');

                var todoItem = document.createElement('div');
                todoItem.classList.add('todo-item');
                todoItem.textContent = selectedCity;
                newItem.appendChild(todoItem);

                var todoInfo = document.createElement('div');
                todoInfo.classList.add('todo-info');

                var removeSpan = document.createElement('span');
                removeSpan.classList.add('todo-remove', 'oi', 'oi-circle-x');
                removeSpan.setAttribute('title', 'Remove');
                removeSpan.addEventListener('click', function () {
                    newItem.remove();
                });

                todoInfo.appendChild(removeSpan);
                newItem.appendChild(todoInfo);

                document.getElementById('prio-list').appendChild(newItem);
            });
        });
    </script>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0-alpha1/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>

    <script>
        var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'))
        var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
            return new bootstrap.Dropdown(dropdownToggleEl)
        });
    </script>


</body>

</html>