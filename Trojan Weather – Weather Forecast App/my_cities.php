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

$query = "
    SELECT sc.city_name, i.url AS image_url, w.name AS weather_name, sc.temperature
    FROM saved_cities AS sc
    INNER JOIN images AS i ON sc.image_id = i.image_id
    INNER JOIN weathers AS w ON sc.weather_id = w.weather_id
";

$result = $mysqli->query($query);
if (!$result) {
    echo "Error fetching data: " . $mysqli->error;
    exit();
}
?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <title>Trojan Weather - My Saved Cities</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        .container { border-radius: 10px; }
        #menu-bar { margin-top: 20px; margin-bottom: 20px; }
        #header { background-color: #A0D398; padding: 10px; margin-bottom: 20px; }
        #saved-cities-title { background-color: #c9e5bc; padding: 10px; margin-bottom: 20px; }
        #city-row { background-color: #fcf9f2; padding: 30px; margin-bottom: 20px; }
        #icon { height: 100px; width: auto; }
        #city-name { font-size: xx-large; }
        #degrees { font-size: x-large; }
    </style>
</head>
<body>
    <div id="menu-bar" class="container">
        <div class="row">
            <div class="col-6">
                <a class="btn btn-outline-dark" href="home_page.php" role="button">Back to Home</a>
            </div>
            <div class="col-6 text-end">
                <a class="btn btn-outline-dark" href="edit_page.php" role="button">Edit List</a>
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
            <h2 class="col-12 text-center">My Saved Cities</h2>
        </div>
    </div>
    <?php while ($row = $result->fetch_assoc()): ?>
        <?php
        switch ($row['city_name']) {
            case 'London': $lat = '51.51'; $long = '-0.12'; break;
            case 'Los Angeles': $lat = '34.05'; $long = '-118.24'; break;
            case 'Paris': $lat = '48.86'; $long = '2.35'; break;
            case 'Seoul': $lat = '37.57'; $long = '126.98'; break;
            case 'Bangkok': $lat = '13.76'; $long = '100.50'; break;
            case 'New York': $lat = '40.71'; $long = '-74.01'; break;
            case 'San Francisco': $lat = '37.77'; $long = '-122.42'; break;
            case 'Tokyo': $lat = '35.69'; $long = '139.69'; break;
            case 'Rome': $lat = '41.90'; $long = '12.50'; break;
            case 'Budapest': $lat = '47.50'; $long = '19.04'; break;
            case 'Cape Town': $lat = '-33.92'; $long = '18.42'; break;
            default: $lat = '34.05'; $long = '-118.24'; break;
        }
        $apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat={$lat}&lon={$long}&units=imperial&appid=7a01b754f52b5e166d7abbd47d20ee7c";
        $weatherData = file_get_contents($apiUrl);
        $weatherData = json_decode($weatherData, true);

        $temperature = $weatherData['current']['temp'];
        $weatherDescription = ucwords($weatherData['current']['weather'][0]['description']);
        $weatherIcon = $weatherData['current']['weather'][0]['icon'];

        $imageQuery = "SELECT image_id FROM images WHERE api_id = ?";

        $stmt = $mysqli->prepare($imageQuery);
        $stmt->bind_param("s", $weatherIcon);
        $stmt->execute();
        $imageResult = $stmt->get_result();
        if ($imageResult->num_rows == 0) {
            // echo "No image found for API ID: $weatherIcon";
            continue;
        }
        $imageRow = $imageResult->fetch_assoc();
        $imageId = $imageRow['image_id'];
        $weatherIdQuery = "SELECT weather_id FROM weathers WHERE name = ?";
        $stmt = $mysqli->prepare($weatherIdQuery);
        $stmt->bind_param("s", $weatherDescription);
        $stmt->execute();
        $weatherIdResult = $stmt->get_result();
        if ($weatherIdResult->num_rows == 0) {
            // echo "No weather data found for description: $weatherDescription";

            continue;
        }
        $weatherIdRow = $weatherIdResult->fetch_assoc();
        $weatherId = $weatherIdRow['weather_id'];
        $updateQuery = "UPDATE saved_cities SET temperature = ?, image_id = ?, weather_id = ? WHERE city_name = ?";
        $stmt = $mysqli->prepare($updateQuery);
        $stmt->bind_param("ddis", $temperature, $imageId, $weatherId, $row['city_name']);
        $stmt->execute();
        if (!$stmt->execute()) {
            echo "Error updating data: " . $stmt->error;
            continue;
        }
        ?>
        <div id="city-row" class="container">
            <div class="row">
                <div class="col-2">
                    <p id="city-name" class="font-weight-bold"><?php echo $row['city_name']; ?></p>
                </div>
                <div class="col">
                    <img id="icon" src="<?php echo $row['image_url']; ?>" class="rounded float-left" alt="Weather Image">
                </div>
                <div class="col-5">
                    <p id="weather-name" class="font-weight-bold"><?php echo $row['weather_name']; ?></p>
                    <p id="degrees" class="font-weight-bold"><?php echo $row['temperature']; ?> °F</p>
                </div>
                <div class="col-2">
                    <a id="see-more-<?php echo $row['city_name']; ?>" class="btn btn-outline-dark see-more-button" href="home_page.php" role="button">See More</a>
                </div>
            </div>
        </div>
    <?php endwhile; ?>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
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
        $(document).ready(function() {
            $(document).on('click', '.see-more-button', function(e) {
                e.preventDefault();
                var cityName = $(this).closest('.container').find('.font-weight-bold#city-name').text();
                localStorage.setItem('selectedCity', cityName.trim());
                console.log("Saved city:", cityName.trim());
                window.location.href = "home_page.php";
            });
        });
    </script>
</body>
</html>
