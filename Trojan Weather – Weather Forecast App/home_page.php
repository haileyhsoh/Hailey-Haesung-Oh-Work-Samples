<?php
// Establish DB Connection

$host = "303.itpwebdev.com";
$user = "haileyoh_db_user";
$pass = "uscitp2024";
$db = "haileyoh_fp_db";

$mysqli = new mysqli($host, $user, $pass, $db);

// Check for connection errors
if ($mysqli->connect_errno) {
    echo $mysqli->connect_error;
    exit();
}

// Close DB Connection
$mysqli->close();
?>

<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <title>Trojan Weather - Home</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <style>
        .container {
            border-radius: 10px;
        }

        #menu-bar {
            margin-top: 20px;
            margin-bottom: 20px;

        }

        #header {
            /* margin-top: 50px; */
            background-color: #A0D398;
            padding: 10px;
            margin-bottom: 20px;
        }

        #city {
            background-color: #c9e5bc;
            padding: 10px;
            padding-bottom: 1px;
            margin-bottom: 20px;
            font-size: x-large;
            padding-left: 30px;
            padding-right: 30px;
        }

        #main-content {
            background-color: #fcf9f2;
            padding: 30px;
            margin-bottom: 20px;
        }

        #icon {
            /* height: auto; */
            margin-top: 20px;
            margin-right: 20px;
            height: 300px;
            width: auto;

        }

        #degrees {
            font-size: xx-large;
        }

        #weather-name {
            font-size: x-large;
        }

        #forecast {
            background-color: #f9efb1;
            padding: 30px;
        }

        #change-location {
            /* margin-top: 8px; */
            width: auto;
            margin-bottom: 8px;
            font-size: small;
        }

        .weather-stats {
            font-size: large;
            font-weight: bold;
        }

        .img-table {
            height: 50px;
            width: auto;
        }


        #main-content,
        #forecast {
            display: none;
        }
    </style>
</head>

<body onload="showWeather()">

    <div id="menu-bar" class="container">
        <div class="row">
            <div class="col-6">
                <a class="btn btn-outline-dark" href="home_page.php" role="button">Home</a>
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

    <div id="city" class="container">
        <div class="row">
            <p id="city-name" class="col-6 font-weight-bold">Los Angeles</p>
            <div id="change-location" class="btn-group ms-auto">
                <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown"
                    data-bs-auto-close="true" aria-expanded="false">
                    Change Location
                </button>
                <ul class="dropdown-menu dropdown-menu-right">
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Los Angeles')">Los Angeles</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('London')">London</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Paris')">Paris</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Seoul')">Seoul</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Bangkok')">Bangkok</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('New York')">New York</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('San Francisco')">San Francisco</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Tokyo')">Tokyo</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Rome')">Rome</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Budapest')">Budapest</a></li>
                    <li><a class="dropdown-item" href="#" onclick="changeCity('Cape Town')">Cape Town</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div id="main-content" class="container">
        <div class="row">

            <p class="col-12">Current Weather:</p>
        </div>

        <div class="row">
            <img class="col-6" id="icon" src="img/sunny.png" class="rounded float-left" alt="Sunny Image">
            <div class="col">
                <p id="weather-name" class="font-weight-bold">Sunny</p>
                <p id="degrees" class="font-weight-bold">68 °F</p>
                <p id="humidity" class="weather-stats">Humidity: 63%</p>
                <p id="wind-speed" class="weather-stats">Wind Speed: 4 mph</p>
                <p id="uvi" class="weather-stats">UV Index: 5 of 11</p>
                <p id="visibility" class="weather-stats">Visibility: 10 mi</p>
                <p id="pressure" class="weather-stats">Pressure: 29.97 in</p>

            </div>
        </div>

    </div>

    <div id="forecast" class="container">
        <p class="col-12">Hourly Forecast: </p>

        <table id="table" class="table text-center">
            <thead>
                <tr>
                    <th scope="col">(+ hours)</th>
                    <th scope="col">Temperature</th>
                    <th scope="col">Condition</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th scope="row">1</th>
                    <td id="temp-0">68 °F</td>
                    <td id="desc-0">Sunny</td>
                    <td><img id="img-table-0" class="col-6 img-table" src="img/sunny.png" class="rounded float-left"
                            alt="Sunny Image"></td>
                </tr>
                <tr>
                    <th scope="row">2</th>
                    <td id="temp-1">69 °F</td>
                    <td id="desc-1">Sunny</td>
                    <td><img id="img-table-1" class="col-6 img-table" src="img/sunny.png" class="rounded float-left"
                            alt="Sunny Image"></td>
                </tr>
                <tr>
                    <th scope="row">3</th>
                    <td id="temp-2">71 °F</td>
                    <td id="desc-2">Sunny</td>
                    <td><img id="img-table-2" class="col-6 img-table" src="img/sunny.png" class="rounded float-left"
                            alt="Sunny Image"></td>
                </tr>
                <tr>
                    <th scope="row">4</th>
                    <td id="temp-3">73 °F</td>
                    <td id="desc-3">Partly Cloudy</td>
                    <td><img id="img-table-3" class="col-6 img-table" src="img/partly-cloudy.png"
                            class="rounded float-left" alt="Partly Cloudy Image"></td>
                </tr>
                <tr>
                    <th scope="row">5</th>
                    <td id="temp-4">75 °F</td>
                    <td id="desc-4">Partly Cloudy</td>
                    <td><img id="img-table-4" class="col-6 img-table" src="img/partly-cloudy.png"
                            class="rounded float-left" alt="Partly Cloudy Image"></td>
                </tr>
                <tr>
                    <th scope="row">6</th>
                    <td id="temp-5">70 °F</td>
                    <td id="desc-5">Rainy</td>
                    <td><img id="img-table-5" class="col-6 img-table" src="img/rainy.png" class="rounded float-left"
                            alt="Rainy Image"></td>
                </tr>
                <tr>
                    <th scope="row">7</th>
                    <td id="temp-6">70 °F</td>
                    <td id="desc-6">Rainy</td>
                    <td><img id="img-table-6" class="col-6 img-table" src="img/rainy.png" class="rounded float-left"
                            alt="Rainy Image"></td>
                </tr>
                <tr>
                    <th scope="row">8</th>
                    <td id="temp-7">65 °F</td>
                    <td id="desc-7">Rainy</td>
                    <td><img id="img-table-7" class="col-6 img-table" src="img/rainy.png" class="rounded float-left"
                            alt="Rainy Image"></td>
                </tr>
                <tr>
                    <th scope="row">9</th>
                    <td id="temp-8">64 °F</td>
                    <td id="desc-8">Rainy</td>
                    <td><img id="img-table-8" class="col-6 img-table" src="img/rainy.png" class="rounded float-left"
                            alt="Rainy Image"></td>
                </tr>
                <tr>
                    <th scope="row">10</th>
                    <td id="temp-9">63 °F</td>
                    <td id="desc-9">Cloudy</td>
                    <td><img id="img-table-9" class="col-6 img-table" src="img/cloudy.png" class="rounded float-left"
                            alt="Cloudy Image"></td>
                </tr>
                <tr>
                    <th scope="row">11</th>
                    <td id="temp-10">63 °F</td>
                    <td id="desc-10">Cloudy</td>
                    <td><img id="img-table-10" class="col-6 img-table" src="img/cloudy.png" class="rounded float-left"
                            alt="Cloudy Image"></td>
                </tr>
                <tr>
                    <th scope="row">12</th>
                    <td id="temp-11">61 °F</td>
                    <td id="desc-11">Cloudy</td>
                    <td><img id="img-table-11" class="col-6 img-table" src="img/cloudy.png" class="rounded float-left"
                            alt="Cloudy Image"></td>
                </tr>
            </tbody>

        </table>

    </div>



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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>

        function showWeather() {

            var selectedCity = localStorage.getItem('selectedCity');

            if (selectedCity) {
                console.log('Selected city is: ' + selectedCity);

                changeCity(selectedCity);

            } else {
                console.log('Selected city is not set in local storage');

                changeCity('Los Angeles');
            }


        }



        function changeCity(city) {
            localStorage.setItem('selectedCity', city);
            document.getElementById('city-name').innerHTML = city;

            var lat = '';
            var long = '';

            if (city === 'London') {
                lat = '51.51';
                long = '-0.12';
            } else if (city === 'Los Angeles') {
                lat = '34.05';
                long = '-118.24';
            } else if (city === 'Paris') {
                lat = '48.86';
                long = '2.35';
            } else if (city === 'Seoul') {
                lat = '37.57';
                long = '126.98';
            } else if (city === 'Bangkok') {
                lat = '13.76';
                long = '100.50';
            } else if (city === 'New York') {
                lat = '40.71';
                long = '-74.01';
            } else if (city === 'San Francisco') {
                lat = '37.77';
                long = '-122.42';
            } else if (city === 'Tokyo') {
                lat = '35.69';
                long = '139.69';
            } else if (city === 'Rome') {
                lat = '41.90';
                long = '12.50';
            } else if (city === 'Budapest') {
                lat = '47.50';
                long = '19.04';
            } else if (city === 'Cape Town') {
                lat = '-33.92';
                long = '18.42';
            }

            const apiUrl = `https://api.openweathermap.org/data/3.0/onecall?lat=${lat}&lon=${long}&units=imperial&appid=7a01b754f52b5e166d7abbd47d20ee7c`;


            fetch(apiUrl)
                .then(response => response.json())
                .then(data => {
                    console.log(data);

                    const weatherIcon = data.current.weather[0].icon;
                    const weatherDescription = data.current.weather[0].description.split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
                    const temperature = Math.round(data.current.temp);
                    const humidity = data.current.humidity;
                    const windSpeed = data.current.wind_speed;
                    const uvIndex = data.current.uvi;
                    const visibility = data.current.visibility;
                    const pressure = data.current.pressure;

                    // Update HTML content with fetched data
                    document.getElementById('weather-name').textContent = weatherDescription;
                    document.getElementById('degrees').textContent = temperature + ' °F';
                    document.getElementById('humidity').textContent = 'Humidity: ' + humidity + '%';
                    document.getElementById('wind-speed').textContent = 'Wind Speed: ' + windSpeed + ' mph';
                    document.getElementById('uvi').textContent = 'UV Index: ' + uvIndex;
                    document.getElementById('visibility').textContent = 'Visibility: ' + visibility + ' mi';
                    document.getElementById('pressure').textContent = 'Pressure: ' + pressure + ' in';

                    document.getElementById('icon').src = `http://openweathermap.org/img/wn/${weatherIcon}@2x.png`;

                    const hourlyForecast = data.hourly.slice(0, 12); // Get the next 12 hours
                    hourlyForecast.forEach((hour, index) => {
                        const forecastTemperature = Math.round(hour.temp);
                        const forecastWeatherDescription = hour.weather[0].description.split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');

                        document.getElementById(`temp-${index}`).textContent = forecastTemperature + ' °F';
                        document.getElementById(`desc-${index}`).textContent = forecastWeatherDescription;
                        document.getElementById(`img-table-${index}`).src = `http://openweathermap.org/img/wn/${hour.weather[0].icon}@2x.png`;
                    });

                    document.getElementById('main-content').style.display = 'block';
                    document.getElementById('forecast').style.display = 'block';
                })
                .catch(error => console.error('Error fetching weather:', error));


        }
    </script>



</body>

</html>